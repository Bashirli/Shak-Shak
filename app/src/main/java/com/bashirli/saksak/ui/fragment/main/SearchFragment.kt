package com.bashirli.saksak.ui.fragment.main

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.SearchAdapter
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentSearchBinding
import com.bashirli.saksak.util.isAnimEnabled
import com.bashirli.saksak.viewmodel.main.SearchMVVM
import com.google.gson.Gson
import kotlinx.coroutines.*


class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private lateinit var viewModel: SearchMVVM
    private  var job:Job?=null
    private lateinit var adapter:SearchAdapter
    private var isFiltered=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchMVVM::class.java)
        setup()

        productLoad()

    }


    private fun setup(){
            with(binding){
                filter.setOnClickListener {
                    drawerLayout.openDrawer(Gravity.RIGHT)
                }


            priceText.setOnClickListener {
                    if(priceList.visibility==View.VISIBLE){
                        priceList.visibility=View.GONE
                    }else{
                        priceList.visibility=View.VISIBLE
                    }
                }

                binding.applyButton.setOnClickListener{
                    drawerLayout.closeDrawer(Gravity.RIGHT)
                   if(binding.maxPrice.text!!.trim().isEmpty()
                       &&binding.minPrice.text!!.trim().isEmpty()){
                       return@setOnClickListener
                   }else{
                       job?.cancel()
                       job=lifecycleScope.launch{
                           sendToFilter(binding.searchText.text.toString())
                       }
                   }

                }

            }


        binding.searchText.requestFocus()
        val imm = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchText, InputMethodManager.SHOW_IMPLICIT)

        adapter=SearchAdapter(arrayListOf(),requireActivity())
        viewModel.loadData()

        binding.searchText.addTextChangedListener {myEditable->
            job?.cancel()
            job=lifecycleScope.launch {
                delay(500)
                if(myEditable.toString().isNotEmpty()){
                    resetFilter()
                    observeSearch(myEditable.toString())
                }
            }
        }

        binding.goBackImage.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerProducts.layoutManager=GridLayoutManager(requireContext(),2)
        binding.recyclerProducts.adapter=adapter

    }


    private fun productLoad(){

        viewModel.productLoading.observe(viewLifecycleOwner){
            if(it){
                binding.progressSearching.visibility=View.VISIBLE
                binding.recyclerProducts.visibility=View.GONE
                binding.motionSearch.isAnimEnabled(false)
            }
        }
        viewModel.productError.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                binding.progressSearching.visibility=View.GONE
                binding.recyclerProducts.visibility=View.GONE
            }
        }
        viewModel.productSuccess.observe(viewLifecycleOwner){
            if(it){
                viewModel.productList.value?.let {
                        myList->
                    adapter.updateList(ArrayList(myList))
                }
                binding.motionSearch.isAnimEnabled(true)
                binding.progressSearching.visibility=View.GONE
                binding.recyclerProducts.visibility=View.VISIBLE
            }
        }

    }

    private fun sendToFilter(searchText:String){
        isFiltered=true
        if(binding.minPrice.text!!.trim()!!.isEmpty()
            &&binding.maxPrice.text!!.trim().isNotEmpty()){
            observeSearch(searchText,null,binding.maxPrice.text.toString().toInt())

        }else if(binding.maxPrice.text!!.trim().isEmpty()
                &&binding.minPrice.text!!.trim().isNotEmpty()){
            observeSearch(searchText,binding.minPrice.text.toString().toInt())
        }else{
            observeSearch(searchText,binding.minPrice.text.toString().toInt(),binding.maxPrice.text.toString().toInt())
        }
    }

    private fun resetFilter(){
        with(binding){
            isFiltered=false
            maxPrice.setText("")
            minPrice.setText("")
        }
    }


    private fun observeSearch(searchText:String,minPrice:Int?=null,maxPrice:Int?=null){
        viewModel.searchProduct(searchText,isFiltered,minPrice, maxPrice)
        viewModel.searchLoading.observe(viewLifecycleOwner){
            if(it){
                binding.progressSearching.visibility=View.VISIBLE
                binding.recyclerProducts.visibility=View.GONE
                binding.motionSearch.isAnimEnabled(false)
            }
        }
        viewModel.searchError.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                binding.progressSearching.visibility=View.GONE
                binding.recyclerProducts.visibility=View.GONE
            }
        }
        viewModel.searchSuccess.observe(viewLifecycleOwner){
            if(it){
                viewModel.searchList.value?.let {
                    myList->
                    adapter.updateList(ArrayList(myList))
                }
                binding.motionSearch.isAnimEnabled(true)
                binding.progressSearching.visibility=View.GONE
                binding.recyclerProducts.visibility=View.VISIBLE
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        println("destroyed")
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        println("destroyed comp")
    }



}