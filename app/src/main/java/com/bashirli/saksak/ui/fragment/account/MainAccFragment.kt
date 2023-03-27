package com.bashirli.saksak.ui.fragment.account

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentMainAccBinding
import com.bashirli.saksak.util.OnBackPressedFragment
import com.bashirli.saksak.viewmodel.acc.MainAccMVVM

class MainAccFragment : BaseFragment<FragmentMainAccBinding>
    (
    FragmentMainAccBinding::inflate
            ),OnBackPressedFragment {


    private lateinit var viewModel: MainAccMVVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(MainAccMVVM::class.java)

        onBackPressed()
        setup()

    }

    private fun setup() {
        val state=viewModel.isLoggedIn()
        if(state){
            findNavController().navigate(MainAccFragmentDirections.actionMainAccFragmentToProfileFragment())
        }
        binding.signIn.setOnClickListener {
            val extras= FragmentNavigatorExtras(binding.signIn to "signInButton")
            findNavController()
                .navigate(R.id.action_mainAccFragment_to_loginFragment,
            null,null,extras)
        }

        binding.contAsGuest.setOnClickListener {
            observeData()
        }


        binding.signUp.setOnClickListener {
            val extras= FragmentNavigatorExtras(binding.signIn to "signUpButton")
            findNavController()
                .navigate(R.id.action_mainAccFragment_to_registerFragment,
                    null,null,extras)
        }
    }

    private fun observeData(){
        viewModel.signInAsAGuest()
        val progressDialog= ProgressDialog(requireContext())
        progressDialog.setTitle(R.string.attention)
        progressDialog.setMessage("Please wait")
        progressDialog.setCancelable(false)
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                    progressDialog.show()
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
            Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
           progressDialog.cancel()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                progressDialog.cancel()
                requireActivity().finish()
            }
        }
    }

    override fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object:OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    Toast.makeText(requireContext(),R.string.infoacc,Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}

