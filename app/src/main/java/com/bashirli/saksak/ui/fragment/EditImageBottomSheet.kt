package com.bashirli.saksak.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions.getExtensionVersion
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bashirli.saksak.R
import com.bashirli.saksak.databinding.FragmentEditImageBottomSheetBinding
import com.bashirli.saksak.util.CustomProgressBar
import com.bashirli.saksak.viewmodel.acc.EditImageMVVM
import com.bashirli.saksak.viewmodel.acc.EditProfileMVVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class EditImageBottomSheet : BottomSheetDialogFragment() {

    private var _binding:FragmentEditImageBottomSheetBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel:EditImageMVVM

    private lateinit var permissionLauncher:ActivityResultLauncher<String>
    private lateinit var activityLauncher:ActivityResultLauncher<Intent>
    private lateinit var imagePick:ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var selectedImage : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding= FragmentEditImageBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(EditImageMVVM::class.java)
        activateActivityLauncher()
        onClick()

    }

    private fun onClick(){
        binding.changePhoto.setOnClickListener {
            changePhoto(it)
        }
        binding.deletePhoto.setOnClickListener {
            deletePhoto(it)
        }
    }

    private fun deletePhoto(view:View){
        viewModel.deleteImage()
        observeData()
    }

    private fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }

    private fun changePhoto(view: View){

        // DEPRECATED FOR ABOVE v11 ->

        if(isPhotoPickerAvailable()){
            imagePick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }else{
            if(ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )!=PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }else{
                    val alert=AlertDialog.Builder(requireContext())
                    alert.setTitle(R.string.attention).setMessage(R.string.infoper)
                        .setCancelable(false)
                        .setPositiveButton(R.string.givePer){
                                dialog,which->
                            //permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }.setNegativeButton(R.string.no){dialog,which->
                            return@setNegativeButton
                        }.create().show()

                }

            }
            else{
                //action OK
                activityLauncher.launch(Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }
        }

    }

    private fun observeData(){
        var progressBar=CustomProgressBar(requireContext())
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                progressBar.startBar()
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                progressBar.stopBar()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                progressBar.stopBar()
                onDestroyView()
            }
        }

    }

    private fun activateActivityLauncher(){
        imagePick=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if(it!=null){
                selectedImage=it
                try{
                    viewModel.updateImage(selectedImage)
                    observeData()
                }catch (e:Exception){
                    Toast.makeText(requireContext(),e.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }else{
                return@registerForActivityResult
            }
        }

        activityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode==Activity.RESULT_OK){
            val data=it.data
            if(data!=null){
               data.data?.let {imageData->
                   selectedImage=imageData
                   try{
                       viewModel.updateImage(selectedImage)
                       observeData()
                   }catch (e:Exception){
                       Toast.makeText(requireContext(),e.localizedMessage,Toast.LENGTH_SHORT).show()
                   }
               }
            }
            }
        }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                activityLauncher.launch(Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }else{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri= Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }



    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}