package com.bashirli.saksak.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bashirli.saksak.R
import com.bashirli.saksak.databinding.FragmentForgotPassBinding
import com.bashirli.saksak.viewmodel.acc.ForgotPassMVVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ForgotPassBottomFragment (var email:String) : BottomSheetDialogFragment() {

    private lateinit var viewModel: ForgotPassMVVM

    private lateinit var binding:FragmentForgotPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_pass,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentForgotPassBinding.bind(view)
        viewModel=ViewModelProvider(requireActivity()).get(ForgotPassMVVM::class.java)
        binding.emailText.setText(email)
        onClick()

    }

    private fun onClick(){
        binding.sendReset.setOnClickListener {
            if(binding.emailText.text.toString().trim().equals("")){
                Toast.makeText(requireContext(),R.string.fillTheGaps,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                observeData(binding.emailText.text.toString())
            }

        }
    }

    private fun observeData(dataEmail: String){
        viewModel.sendEmailLink(dataEmail)

        val progressDialog=ProgressDialog(requireContext())
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
            progressDialog.cancel()
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                val alertDialog=MaterialAlertDialogBuilder(requireContext())
                alertDialog.setCancelable(false)
                    .setTitle(R.string.attention)
                    .setMessage(R.string.resetInfo2)
                    .setPositiveButton(R.string.ok){
                        dialog,which->
                        onDestroyView()
                    }.create().show()
                progressDialog.cancel()
            }
        }

    }



    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}