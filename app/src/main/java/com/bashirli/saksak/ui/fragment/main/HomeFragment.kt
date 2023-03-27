package com.bashirli.saksak.ui.fragment.main

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bashirli.saksak.R
import com.bashirli.saksak.adapter.CategoryAdapter
import com.bashirli.saksak.adapter.ProductAdapter
import com.bashirli.saksak.base.BaseFragment
import com.bashirli.saksak.databinding.FragmentHomeBinding
import com.bashirli.saksak.viewmodel.main.HomeMVVM
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private var oldMarginTop=0f
    private var isStopped=false
    private lateinit var viewModel: HomeMVVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(HomeMVVM::class.java)
        movingEdit()
        observeData()
        loadImages()

    }


    private fun observeData(){
        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerViewCategories.visibility=View.GONE
                binding.progressCategory.visibility=View.VISIBLE
            }
        }
        viewModel.errorData.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerViewCategories.visibility=View.GONE
                binding.progressCategory.visibility=View.GONE
                Toast.makeText(
                    requireContext()
                    ,viewModel.errorMessage
                    ,Toast.LENGTH_SHORT
                ).show()

            }
        }
        viewModel.success.observe(viewLifecycleOwner){
            if(it){
                viewModel.categoryList.observe(viewLifecycleOwner){myList->
                    binding.recyclerViewCategories.layoutManager=
                        LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                        )
                    binding.recyclerViewCategories.adapter=CategoryAdapter(ArrayList(myList))
                    binding.recyclerViewCategories.visibility=View.VISIBLE
                    binding.progressCategory.visibility=View.GONE

                }
            }
        }


        viewModel.loadingProduct.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerProduct.visibility=View.GONE
            }
        }
        viewModel.errorDataProduct.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerProduct.visibility=View.GONE

                Toast.makeText(
                    requireContext()
                    ,viewModel.errorMessage
                    ,Toast.LENGTH_SHORT
                ).show()

            }
        }
        viewModel.successProduct.observe(viewLifecycleOwner){
            if(it){
                viewModel.categoryListProduct.observe(viewLifecycleOwner){myList->
                    binding.recyclerProduct.layoutManager=
                       object: GridLayoutManager(
                            requireContext(),2
                        ){
                           override fun canScrollVertically(): Boolean {
                               return false
                           }
                       }
                    binding.recyclerProduct.adapter=ProductAdapter(ArrayList(myList),requireActivity())
                    binding.recyclerProduct.visibility=View.VISIBLE


                }
            }
        }




    }

    private fun loadImages(){
        viewModel.categoryListProduct.observe(viewLifecycleOwner){
            if(it!=null){
                val imageList=ArrayList<SlideModel>()
                for(model in it){
                        for (image in model.images) {
                            if (imageList.size < 15) {
                                imageList.add(SlideModel(image))
                            }else{
                                break
                            }
                        }
                    if(imageList.size>15){
                        break
                    }
                }
                binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
            }
        }
    }

    private fun movingEdit(){
        val displayMetrics = DisplayMetrics()
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val x=width-binding.cardView.layoutParams.width
        val oldMarginRight=binding.cardView.marginRight.toFloat()
        oldMarginTop=binding.cardView.marginTop.toFloat()
        val myX=binding.cardView.layoutParams.width


        binding.scrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int,
            ) {
                if(scrollY>0){
                    binding.cardView.y=scrollY.toFloat()+oldMarginTop
                }else{
                    binding.cardView.y=0f+oldMarginTop
                }
                 if(scrollY>myX&&scrollY<width-oldMarginRight){
                        binding.cardView.x=oldMarginRight

                        binding.cardView.layoutParams=
                            ViewGroup.LayoutParams(
                                scrollY-oldMarginRight.toInt(), binding.cardView.height
                            )

                }else if(scrollY<myX){
                    binding.cardView.x=x.toFloat()-oldMarginRight
                }

            }
        })

        binding.cardView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }

    }


    override fun onResume() {
        super.onResume()

          if(isStopped){
              binding.cardView.y=binding.scrollView.scrollY.toFloat()+oldMarginTop
          }else{
              binding.cardView.y=binding.scrollView.scrollY.toFloat()
          }
        isStopped=false

    }

    override fun onStop() {
        super.onStop()
       isStopped=true
    }

}