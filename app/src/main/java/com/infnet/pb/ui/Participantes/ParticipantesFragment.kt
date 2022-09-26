package com.infnet.pb.ui.Participantes


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.infnet.pb.Adapter.participantesRV
import com.infnet.pb.R
import com.infnet.pb.databinding.FragmentParticipantesBinding
import com.infnet.pb.model.participante

class ParticipantesFragment : Fragment(){

    private lateinit var recyclerView : RecyclerView
    private lateinit var participanteArrayList : ArrayList<participante>
    private lateinit var myAdapter: participantesRV
    private lateinit var db : FirebaseFirestore


    private var _binding: FragmentParticipantesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ParticipantesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParticipantesBinding.inflate(inflater, container, false)
        val documentId = arguments?.getString("documentId")
        val factory = participanteViewModelFactory(documentId!!)
        viewModel = ViewModelProvider(this, factory).get(ParticipantesViewModel::class.java)
        val view = binding.root

        recyclerView = binding.participantesRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        participanteArrayList = arrayListOf()
        myAdapter = participantesRV(participanteArrayList)

        recyclerView.adapter = myAdapter

        viewModel.participante.observe(viewLifecycleOwner, { list -> list?.let {myAdapter.updateList(it)}} )

        viewModel.reuniao.observe(viewLifecycleOwner){
            if (it != null){
                //view.findViewById<TextView>(R.id.input_categoria).text = it.categoria
                view.findViewById<TextView>(R.id.tvNome).text = it.titulo
                view.findViewById<TextView>(R.id.tvNomeCriador).text = it.nome

            }
        }

        binding.btnSair.visibility = GONE

        //Toast.makeText(requireContext(), "${documentId}", Toast.LENGTH_LONG).show()

        binding.imageBackstack.setOnClickListener{
            findNavController().popBackStack()
        }



        viewModel.checkParticipante()

        viewModel.msg
            .observe(viewLifecycleOwner){
                if ( it.isNotBlank()) {
                    showSnackbar(view, it)
                }
            }

        viewModel.status.observe(viewLifecycleOwner){
            if (it){
                findNavController().popBackStack(R.id.homeFragment, true)
                findNavController().navigate(R.id.homeFragment)
            }
        }
        viewModel.ingressoStatus.observe(viewLifecycleOwner){
            if (it){
                binding.btnSair.visibility = View.VISIBLE
                binding.btnSair.setOnClickListener {
                    viewModel.sair()
                }
            }
            else{


            }
        }
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ParticipantesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun showSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }


}