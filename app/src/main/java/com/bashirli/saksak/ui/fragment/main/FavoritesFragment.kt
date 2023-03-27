package com.bashirli.saksak.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.favorite.FavoriteWithCatAdapter
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentFavoritesBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.viewmodel.main.FavoritesMVVM

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {

    private lateinit var viewModel: FavoritesMVVM
    private lateinit var adapter:FavoriteWithCatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(FavoritesMVVM::class.java)
        loadData()
    }



    private fun loadData(){
        viewModel.loadData()
        viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar2.visibility=View.VISIBLE
                binding.recyclerFavorites.visibility=View.GONE
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if(it){
                binding.progressBar2.visibility=View.GONE
                binding.recyclerFavorites.visibility=View.GONE
                Toast.makeText(requireContext(),viewModel.errorMessage.value,Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                viewModel.favList.observe(viewLifecycleOwner){myList->
                  viewModel.catList.observe(viewLifecycleOwner){catList->
                      setupRecycler(myList, catList)
                      binding.progressBar2.visibility=View.GONE
                      binding.recyclerFavorites.visibility=View.VISIBLE
                  }
                }
            }
        }

    }


    private fun setupRecycler(myList:ArrayList<ProductModel>,catList:ArrayList<CategoryModel>){
        val newCatSet=HashSet<CategoryModel>()
        for(data in catList){
            for(secondData in myList){
                if(secondData.category.id==data.id){
                    newCatSet.add(data)
                }
            }
        }
        val newCatList=ArrayList<CategoryModel>()
        for(data in newCatSet){
            newCatList.add(data)
        }
        adapter=FavoriteWithCatAdapter(myList,newCatList,viewModel,requireActivity())
        binding.recyclerFavorites.adapter=adapter
        binding.recyclerFavorites.layoutManager=LinearLayoutManager(requireContext())

    }


    override fun onResume() {
        super.onResume()
        if(this::viewModel.isInitialized){
            viewModel.refreshData()
        }
    }

}