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
import androidx.navigation.fragment.findNavController
import com.bashirli.saksak.R
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentProfileBinding
import com.bashirli.saksak.model.UserData
import com.bashirli.saksak.util.OnBackPressedFragment
import com.bashirli.saksak.util.setImageProfile
import com.bashirli.saksak.util.setImageURL
import com.bashirli.saksak.viewmodel.acc.ProfileMVVM

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
    ),OnBackPressedFragment {


    private lateinit var loadedUserData: UserData
    private lateinit var viewModel: ProfileMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(ProfileMVVM::class.java)
        onBackPressed()
        onClick()
        setup()
    }

    private fun setup(){
        viewModel.loadData()
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                binding.cardView3.visibility=View.GONE
                binding.userName.visibility=View.GONE
                binding.progressBar.visibility=View.VISIBLE
            }
        }

        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                binding.cardView3.visibility=View.INVISIBLE
                binding.userName.visibility=View.INVISIBLE
                binding.progressBar.visibility=View.INVISIBLE
            }
        }

        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                val data=viewModel.userData.value
                binding.profileImage.setImageProfile(requireContext(),data!!.profilePicture)
                binding.userName.setText(data.nickname)
                loadedUserData=viewModel.userData.value!!
                binding.cardView3.visibility=View.VISIBLE
                binding.userName.visibility=View.VISIBLE
                binding.progressBar.visibility=View.GONE
            }
        }

    }

    private fun onClick(){
        binding.goBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener {
      if(this::loadedUserData.isInitialized){
          findNavController()
              .navigate(ProfileFragmentDirections
                  .actionProfileFragmentToEditProfileFragment(loadedUserData))
      }
        }

        binding.signOut.setOnClickListener {
            signOut()
        }

    }

    private fun signOut(){
        viewModel.signOut()
        val progressDialog=ProgressDialog(requireContext())
        progressDialog.setTitle(R.string.attention)
        progressDialog.setMessage("Please wait")
        progressDialog.setCancelable(false)

        viewModel.loadingSignOut.observe(viewLifecycleOwner){
            if(it){
                progressDialog.show()
            }
        }

        viewModel.errorDataSignOut.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
        }

        viewModel.successSignOut.observe(viewLifecycleOwner){
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
                    requireActivity().finish()
                }
            }
        )
    }

}