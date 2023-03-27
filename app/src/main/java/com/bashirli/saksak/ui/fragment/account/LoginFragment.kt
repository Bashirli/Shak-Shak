package com.bashirli.saksak.ui.fragment.account

import android.app.ProgressDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentLoginBinding
import com.bashirli.saksak.ui.fragment.ForgotPassBottomFragment
import com.bashirli.saksak.util.OnBackPressedFragment
import com.bashirli.saksak.util.errorFinderLogin
import com.bashirli.saksak.viewmodel.acc.LoginMVVM


class LoginFragment :
    BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
),OnBackPressedFragment {

    private lateinit var viewModel: LoginMVVM

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
        viewModel=ViewModelProvider(requireActivity()).get(LoginMVVM::class.java)
        animButton()
        onClick()
        onBackPressed()
    }


    private fun onClick(){
        binding.goBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signin.setOnClickListener {
            if(!errorFinderLogin(binding.email.text.toString(),binding.pass.text.toString(),requireContext())){
                login(binding.email.text.toString(),binding.pass.text.toString())
            }
        }

        binding.forgotPass.setOnClickListener {
            val bottomSheetFragment=ForgotPassBottomFragment(binding.email.text.toString())
            bottomSheetFragment.show(requireActivity().supportFragmentManager,"forgotPass")
        }


    }

    private fun login(email:String,pass:String){
        viewModel.login(email, pass)
        val progressDialog= ProgressDialog(requireContext())
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
                Toast.makeText(requireContext(),R.string.welcome,Toast.LENGTH_SHORT).show()
                 findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToProfileFragment())
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