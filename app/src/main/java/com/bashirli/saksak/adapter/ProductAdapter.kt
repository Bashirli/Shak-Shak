package com.bashirli.saksak.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclerproductsBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.ui.activity.ProductActivity
import com.bashirli.saksak.util.setImageURL

class ProductAdapter(var arrayList: ArrayList<ProductModel>,val activity:Activity) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    class ProductHolder(var binding: RecyclerproductsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding=
            RecyclerproductsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        with(holder.binding){
         productData=arrayList.get(position)
            val imageView = imageView

            holder.itemView.setOnClickListener {

                val sharedElement = Intent(activity, ProductActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    Pair.create(imageView, "image")
                )
                sharedElement.putExtra("imageURL", arrayList.get(position).images[0])
                sharedElement.putExtra("productId", arrayList.get(position).id)
                holder.itemView.context.startActivity(sharedElement, options.toBundle())
            }


        }

    }

    fun updateList(newArrayList:ArrayList<ProductModel>){
        arrayList.clear()
        arrayList.addAll(newArrayList)
        notifyDataSetChanged()
    }


}