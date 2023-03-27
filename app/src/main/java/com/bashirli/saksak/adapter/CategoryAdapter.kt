package com.bashirli.saksak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclercategoryBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.util.setImageURL

class CategoryAdapter(var arrayList: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    class CategoryHolder(var binding:RecyclercategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding=RecyclercategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        with(holder.binding){
            recyclerImage.setImageURL(holder.itemView.context,arrayList.get(position).image)
            recyclerText.setText(arrayList.get(position).name)
        }
        }

}