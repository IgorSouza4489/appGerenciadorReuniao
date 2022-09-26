package com.infnet.pb.ui.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.infnet.pb.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)


        binding.tvBack.setOnClickListener {

            findNavController().popBackStack()
        }

        val email = binding.tvEmailRecuperar.text.toString().trim{it <= ' '}

        binding.btnEmailRecuperar.setOnClickListener {
            recuperarSenha(email)

        }


        return view







    }


    fun recuperarSenha(email: String){

            val email = binding.tvEmailRecuperar.text.toString().trim{it <= ' '}

            if (email.isNotEmpty()){
                val task = FirebaseAuth.getInstance().sendPasswordResetEmail(email)

                task.addOnSuccessListener {
                    Toast.makeText(requireContext(), "Email enviado", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), "Email não enviado", Toast.LENGTH_LONG).show()
                }

            }
            else{
                    Toast.makeText(requireContext(), "Insira o Email", Toast.LENGTH_LONG).show()
                }


            //findNavController().popBackStack()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

}