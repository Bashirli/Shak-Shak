package com.bashirli.saksak.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.GridCategoryAdapter
import com.bashirli.saksak.adapter.MagazineAdapter
import com.bashirli.saksak.adapter.ProductAdapter
import com.bashirli.saksak.adapter.TopCategoryAdapter
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentCategoryBinding
import com.bashirli.saksak.viewmodel.main.CategoryMVVM


class CategoryFragment : BaseFragment<FragmentCategoryBinding>(
    FragmentCategoryBinding::inflate
) {

    private lateinit var viewModel: CategoryMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(CategoryMVVM::class.java)
        loadData()
        setup()
    }


    private fun loadData(){
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
              binding.recyclerCategory.visibility=View.GONE
              binding.progressBar.visibility=View.VISIBLE
              binding.recyclerShop.visibility=View.GONE
              binding.recyclerItem.visibility=View.GONE
            }
        }

        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                binding.recyclerCategory.visibility=View.GONE
                binding.progressBar.visibility=View.GONE
                binding.recyclerShop.visibility=View.GONE
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
                binding.recyclerItem.visibility=View.GONE
            }
        }

        viewModel.success.observe(viewLifecycleOwner){
            if(it) {
                binding.recyclerCategory.layoutManager=LinearLayoutManager(
                    requireContext(),LinearLayoutManager.HORIZONTAL,false
                )
                binding.recyclerCategory.adapter=TopCategoryAdapter(ArrayList(viewModel.categoryList.value))
            binding.recyclerShop.layoutManager=GridLayoutManager(
                requireContext(),3)
        binding.recyclerShop.adapter=GridCategoryAdapter(ArrayList(viewModel.categoryList.value))
                binding.recyclerCategory.visibility=View.VISIBLE
                binding.progressBar.visibility=View.GONE
                binding.recyclerShop.visibility=View.VISIBLE
                binding.recyclerItem.visibility=View.VISIBLE

            }
        }

        viewModel.loadingProduct.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.visibility=View.VISIBLE
                binding.recyclerHighlySalable.visibility=View.GONE
            }
        }

        viewModel.errorDataProduct.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar.visibility=View.GONE
                binding.recyclerHighlySalable.visibility=View.GONE
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.successProduct.observe(viewLifecycleOwner){
            if(it) {
                binding.progressBar.visibility=View.GONE
                binding.recyclerHighlySalable.visibility=View.VISIBLE
                binding.recyclerItem.layoutManager=object:LinearLayoutManager(requireContext()){
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                binding.recyclerItem.adapter=MagazineAdapter(ArrayList(viewModel.productList.value))
                binding.recyclerHighlySalable.layoutManager=LinearLayoutManager(
                    requireContext()
                    ,LinearLayoutManager.HORIZONTAL,
                    false)
                binding.recyclerHighlySalable.adapter=ProductAdapter(ArrayList(viewModel.productList.value),requireActivity())
            }
        }




    }

    private fun setup(){
        binding.constraintLayout2.setOnClickListener {
            findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToSearchFragment())
        }
    }

}