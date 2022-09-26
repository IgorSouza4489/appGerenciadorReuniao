package com.infnet.pb.ui.signin


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.firebase.ui.auth.AuthUI
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import com.google.firebase.auth.FirebaseAuth
import com.infnet.pb.R
import com.infnet.pb.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignInViewModel
    lateinit var mAdView: AdView

    private lateinit var auth: FirebaseAuth

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //signin

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

//        val signInIntent =
//            AuthUI.getInstance().createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build()
//
//        signInLauncher.launch(signInIntent)

        MobileAds.initialize(requireContext())
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        auth = FirebaseAuth.getInstance()
        setup()
        return view
    }


    fun setup(){
        checkiflogged()
        setupClickListeners()
    }

    fun setupClickListeners(){
        binding.btnRegistrar.setOnClickListener{
            login()
        }

        binding.tvRegister.setOnClickListener{
            findNavController().navigate(R.id.signUpFragment)
        }

//        binding.imageRegister.setOnClickListener{
//            findNavController().navigate(R.id.signUpFragment)
//        }

        binding.tvEsquecerSenha.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
    }

    fun checkiflogged(){
        val current_user = auth.currentUser

        if (current_user != null){
            findNavController().navigate(R.id.homeFragment)
        }
    }

    fun login(){
        val email = binding.inputEmail1.text.toString()
        val password = binding.inputSenha1.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            val task = auth.signInWithEmailAndPassword(email, password)

            task.addOnCompleteListener{
                if (task.isSuccessful){
                    val gkc = auth.currentUser?.email.toString()
                    Toast.makeText(requireContext(), "O email do usuário logado é ${gkc}", Toast.LENGTH_LONG).show()

                    findNavController().navigate(R.id.homeFragment)
                }
            }

            task.addOnFailureListener{
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

        else{
            Toast.makeText(requireContext(), "Insira os campos", Toast.LENGTH_LONG).show()
        }




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

//    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
//        val response = result.idpResponse
//        if (result.resultCode == RESULT_OK) {
//            // Successfully signed in
//            val user = FirebaseAuth.getInstance().currentUser
//            // ...
//        } else {
//            // Sign in failed. If response is null the user canceled the
//            // sign-in flow using the back button. Otherwise check
//            // response.getError().getErrorCode() and handle the error.
//            // ...
//        }

}