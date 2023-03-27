package com.bashirli.saksak.ui.fragment.main

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.SearchAdapter
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentSelectedCategoryBinding
import com.bashirli.saksak.viewmodel.main.SelectedCatMVVM

class SelectedCategoryFragment :BaseFragment<FragmentSelectedCategoryBinding>(
    FragmentSelectedCategoryBinding::inflate
) {


    private lateinit var viewModel: SelectedCatMVVM
    private var categoryId=0
    private var categoryName:String=""
    private lateinit var adapter:SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(SelectedCatMVVM::class.java)
        setup()
        observeData()

    }

    private fun setup(){
        categoryId= SelectedCategoryFragmentArgs.fromBundle(requireArguments()).categoryId
        categoryName= SelectedCategoryFragmentArgs.fromBundle(requireArguments()).categoryName
        viewModel.loadData(categoryId)
        with(binding){
            textView5.setText(categoryName)
            goBack.setOnClickListener {
                findNavController().popBackStack()
            }
            adapter=SearchAdapter(
                ArrayList()
                ,requireActivity())
            recyclerSelectedCat.adapter=adapter
            recyclerSelectedCat.layoutManager=GridLayoutManager(requireContext(),2)
            filter.setOnClickListener {
                drawerLayout.openDrawer(Gravity.RIGHT)
            }
            applyButton.setOnClickListener {
                sendToFilter()
                drawerLayout.closeDrawer(Gravity.RIGHT)
            }
            priceText.setOnClickListener {
                if(priceList.visibility==View.GONE){
                    priceList.visibility=View.VISIBLE
                }else{
                    priceList.visibility=View.GONE
                }
            }

        }

    }
    private fun sendToFilter(){
        if(binding.minPrice.text!!.trim()!!.isEmpty()
            &&binding.maxPrice.text!!.trim().isNotEmpty()){
            viewModel.filterCategories(null,binding.maxPrice.text.toString().toInt(),categoryId)

        }else if(binding.maxPrice.text!!.trim().isEmpty()
            &&binding.minPrice.text!!.trim().isNotEmpty()){
            viewModel.filterCategories(binding.minPrice.text.toString().toInt(),null,categoryId)
        }else if(binding.maxPrice.text!!.trim().isNotEmpty()
            &&binding.minPrice.text!!.trim().isNotEmpty()){
            viewModel.filterCategories(binding.minPrice.text.toString().toInt(),binding.maxPrice.text.toString().toInt(),categoryId)
        }else{
            viewModel.loadData(categoryId)
        }
        observeData()
    }

    private fun observeData(){
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
        binding.recyclerSelectedCat.visibility=View.GONE
            binding.progressSelectedCat.visibility=View.VISIBLE
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                binding.recyclerSelectedCat.visibility=View.GONE
                binding.progressSelectedCat.visibility=View.GONE
            }
        }

        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                adapter.updateList(ArrayList(viewModel.productList.value))
                binding.recyclerSelectedCat.visibility=View.VISIBLE
                binding.progressSelectedCat.visibility=View.GONE
            }
        }
    }

}