package com.infnet.pb.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.infnet.pb.Adapter.reuniaoRV
import com.infnet.pb.R
import com.infnet.pb.dao.authDao
import com.infnet.pb.dao.reuniaoDao
import com.infnet.pb.databinding.FragmentHomeBinding
import com.infnet.pb.model.reuniao

class HomeFragment : Fragment(), reuniaoRV.ReuniaoClickInterface {


    private lateinit var recyclerView : RecyclerView
    private lateinit var reuniaoArrayList : ArrayList<reuniao>
    private lateinit var myAdapter: reuniaoRV
    private lateinit var db : FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerView: NavigationView


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = binding.root
        drawerLayout = binding.drawerLayout
        drawerView = binding.navView


        recyclerView = binding.reuniaoRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        reuniaoArrayList = arrayListOf()
        myAdapter = reuniaoRV(reuniaoArrayList, this)

        recyclerView.adapter = myAdapter


        viewModel.reunioes.observe(viewLifecycleOwner, { list -> list?.let {myAdapter.updateList(it) }} )


        viewModel.todasReunioes()

        auth = FirebaseAuth.getInstance()
        binding.btnLogout.setOnClickListener{
            deslogar()
        }

        binding.floatingActionButton2.setOnClickListener{
            findNavController().navigate(R.id.reuniaoCreateFragment)
        }


        draweritem()
        checkiflogged()
        return view
    }

    fun deslogar(){
        auth.signOut()
        findNavController().popBackStack(R.id.signInFragment, true)
        findNavController().navigate(R.id.signInFragment)
    }

    fun draweritem(){
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.todasreunioes ->{
                    viewModel.todasReunioes()
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return@setNavigationItemSelectedListener true

                }

                R.id.minhasreunioes ->{
                    viewModel.minhasReunioes()


                    drawerLayout.closeDrawer(GravityCompat.START);
                    return@setNavigationItemSelectedListener true
                }

                R.id.reunioesingressadas ->{
                    Toast.makeText(requireContext(), "Feature não implementada ainda", Toast.LENGTH_LONG).show()


                    drawerLayout.closeDrawer(GravityCompat.START);
                    return@setNavigationItemSelectedListener true
                }


                R.id.sair -> {
                    deslogar()
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }
    //passar para vm
    fun mostrarNome(){


        reuniaoDao.test().addOnSuccessListener { documento ->
            if (documento.exists()){
                var nome = documento.getString("nome")
                Log.d("TAG", "Documento existe")

                binding.tvNomeUsuario.text = nome
            }
            else{
                Log.d("TAG", "Documento NAO existe")
            }
        }.addOnFailureListener{
            Log.d("ErroErroErroo", it.message.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onReuniaoClick(reuniao: reuniao) {
        //Toast.makeText(requireContext(), "item: ${reuniao.titulo}", Toast.LENGTH_LONG).show()
        findNavController()
            .navigate(
                R.id.reuniaoShowFragment, bundleOf(
                    "documentId" to reuniao.documentId
                )
            )
    }

    fun checkiflogged(){
        val current_user = auth.currentUser

        if (current_user != null){
            mostrarNome()

        }
    }


    override fun onStart() {
        super.onStart()
        val user = authDao.getCurrentUser()
        if (user == null) {
            findNavController()
                .navigate(R.id.signInFragment)
        }
    }

}


