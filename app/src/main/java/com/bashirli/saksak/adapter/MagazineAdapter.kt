package com.bashirli.saksak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclercategoryBinding
import com.bashirli.saksak.databinding.RecyclermagazineBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.util.setImageURL

class MagazineAdapter(var arrayList: ArrayList<ProductModel>) : RecyclerView.Adapter<MagazineAdapter.MagazineHolder>() {

    class MagazineHolder(var binding: RecyclermagazineBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagazineHolder {
        val binding =
            RecyclermagazineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MagazineHolder(binding)
    }

    override fun getItemCount(): Int {
        if(arrayList.size>5){
            return 5
        }else{
            return arrayList.size
        }
    }

    override fun onBindViewHolder(holder: MagazineHolder, position: Int) {
        holder.binding.magazineImage.setImageURL(
            holder.itemView.context,
            arrayList.get(position).images.get(
                arrayList.get(position).images.lastIndex
            )
        )

    }
}