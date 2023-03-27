package com.bashirli.saksak.ui.fragment.account

import android.app.ProgressDialog
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentRegisterBinding
import com.bashirli.saksak.util.OnBackPressedFragment
import com.bashirli.saksak.util.errorFinderRegister
import com.bashirli.saksak.viewmodel.acc.RegisterMVVM

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(
    FragmentRegisterBinding::inflate
),OnBackPressedFragment {


    private lateinit var viewModel: RegisterMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation=TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition=animation
        sharedElementReturnTransition=animation

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(RegisterMVVM::class.java)
        animButton()
        onBackPressed()
        onClick()

    }

    private fun onClick(){
        binding.signUpButton.setOnClickListener {
           with(binding){
               if(!errorFinderRegister(email.text.toString(),
                       pass.text.toString(),
                       repass.text.toString(),
                       nickname.text.toString(),
                       requireContext())){
                   register(email.text.toString(), pass.text.toString(),nickname.text.toString())
               }
           }
        }
        binding.goBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun register(email:String,password:String,nickname:String){
        viewModel.register(email, password,nickname)
        val progressDialog=ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setTitle(R.string.pleasewait)
        progressDialog.setMessage("Loading")
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                progressDialog.show()
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                progressDialog.cancel()
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                progressDialog.cancel()
                Toast.makeText(requireContext(),R.string.accsuc,Toast.LENGTH_SHORT).show()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToProfileFragment())
            }
        }


    }

    override fun onBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }


    private fun animButton(){
        val anim=AnimationUtils.loadAnimation(requireContext(),R.anim.buttonmoveanim)
        binding.goBack.animation=anim
    }
}