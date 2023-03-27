package com.bashirli.saksak.ui.fragment.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentEditProfileBinding
import com.bashirli.saksak.model.UserData
import com.bashirli.saksak.ui.fragment.EditImageBottomSheet
import com.bashirli.saksak.util.CustomProgressBar
import com.bashirli.saksak.util.OnBackPressedFragment
import com.bashirli.saksak.util.setImageProfile
import com.bashirli.saksak.viewmodel.acc.EditProfileMVVM

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(
    FragmentEditProfileBinding::inflate
            )
    ,OnBackPressedFragment {


    private lateinit var viewModel:EditProfileMVVM
    private lateinit var userData:UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressed()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(EditProfileMVVM::class.java)
        setup()
        onClick()
    }

    private fun setup(){
    userData=EditProfileFragmentArgs.fromBundle(requireArguments()).loadedUserData
        with(binding){
           profileImage.setImageProfile(requireContext(),userData.profilePicture)
            email.setText(userData.email)
            nickname.setText(userData.nickname)
        }
        observeData()
    }



    private fun onClick(){
        binding.goBack.setOnClickListener {
                    findNavController()
                    .navigate(EditProfileFragmentDirections
                    .actionEditProfileFragmentToProfileFragment())
        }
        binding.editImage.setOnClickListener {
        val editImageFragment=EditImageBottomSheet()
            editImageFragment.show(requireActivity().supportFragmentManager,"editImage")
        }
        binding.update.setOnClickListener {
            updateData(it)
        }
    }

    private fun updateData(view: View){
        if(binding.nickname.text.toString().isEmpty()){
            Toast.makeText(requireContext(), R.string.fillTheGaps,Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.updateData(binding.nickname.text.toString())
        val customProgressBar=CustomProgressBar(requireContext())
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                customProgressBar.startBar()
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
            customProgressBar.stopBar()
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
            customProgressBar.stopBar()
                Toast.makeText(requireContext(),R.string.sucupdate,Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun observeData(){
        viewModel.loadData()
    viewModel.loadError.observe(viewLifecycleOwner){
        if(it){
            Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
        }
    }
        viewModel.loadSuccess.observe(viewLifecycleOwner){
            if(it){
                val data=viewModel.userData.value!!
                with(binding){
                    profileImage.setImageProfile(requireContext(),data.profilePicture)
                }
            }
        }
    }


    override fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
            }
        })
    }


}