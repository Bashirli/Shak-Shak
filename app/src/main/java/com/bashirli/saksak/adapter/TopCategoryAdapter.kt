package com.bashirli.saksak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclertopcategoryBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.ui.fragment.main.CategoryFragmentDirections

class TopCategoryAdapter(val arrayList: ArrayList<CategoryModel>) : RecyclerView.Adapter<TopCategoryAdapter.TopCategoryHolder>() {
    class TopCategoryHolder(val binding:RecyclertopcategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCategoryHolder {
       val binding=RecyclertopcategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TopCategoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: TopCategoryHolder, position: Int) {
        holder.binding.categoryName.setText(arrayList.get(position).name)
        holder.itemView.setOnClickListener {
            val navController=
                CategoryFragmentDirections.actionCategoryFragmentToSelectedCategoryFragment(

                    arrayList.get(position).id,
                    arrayList.get(position).name
                )


            Navigation.findNavController(it).navigate(navController)

        }
    }
    


}