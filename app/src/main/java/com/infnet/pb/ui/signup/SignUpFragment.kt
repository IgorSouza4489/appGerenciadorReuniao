package com.infnet.pb.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.infnet.pb.R
import com.infnet.pb.databinding.FragmentSignUpBinding
import com.infnet.pb.model.profile

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val dbFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        setup()

        return view
    }

    fun setup(){
       setupClickListener()
    }




    fun setupClickListener(){
        binding.btnRegistrar.setOnClickListener{
            register()
        }

        binding.imageBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }


    fun profilecreateFirestore(email: String, nome: String){



        val user = profile()
        user.email = email
        user.nome = nome

        insertProfile(user)
    }

    fun insertProfile(user: profile){
        val currentUser = auth.currentUser
        val userRef = dbFirestore.collection("profile")

        userRef.document(currentUser?.uid!!).set(user).addOnSuccessListener {
            Toast.makeText(requireContext(), "Usuário Criado", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Erro ao criar usuário", Toast.LENGTH_LONG).show()
        }

    }


    fun register(){
        auth = FirebaseAuth.getInstance()
        val nome = binding.inputNome.text.toString()
        val email = binding.inputEmail1.text.toString()
        val password = binding.inputSenha1.text.toString()
        val confirm = binding.inputConfirmar1.text.toString()



        if (email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()){
            if (confirm == password) {
                val task = auth.createUserWithEmailAndPassword(email, password)
                //val uid
                task.addOnCompleteListener{
                    if (task.isSuccessful){
                        profilecreateFirestore(email, nome) //tentar passar pra viewmodel este create de user
                        findNavController().navigate(R.id.homeFragment)
                    }
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(requireContext(), "As senhas devem coincidir", Toast.LENGTH_LONG).show()

            }

        }
        else{
            Toast.makeText(requireContext(), "Insira os campos", Toast.LENGTH_LONG).show()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}