package com.bashirli.saksak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.GridcatrecyclerBinding
import com.bashirli.saksak.databinding.RecyclertopcategoryBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.util.setImageURL

class GridCategoryAdapter(var arrayList: ArrayList<CategoryModel>) : RecyclerView.Adapter<GridCategoryAdapter.GridCategoryHolder>() {
    class GridCategoryHolder(val binding: GridcatrecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridCategoryHolder {
        val binding=
            GridcatrecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GridCategoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: GridCategoryHolder, position: Int) {
        with(holder.binding){
            recyclerImage.setImageURL(
                holder.itemView.context,
                arrayList.get(position).image)

            recyclerText.setText(arrayList.get(position).name)
            holder.itemView.setOnClickListener {

            }
        }


    }
}