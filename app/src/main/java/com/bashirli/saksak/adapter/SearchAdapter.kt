package com.bashirli.saksak.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclerproductsBinding
import com.bashirli.saksak.databinding.RecyclersearchproductBinding
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.ui.activity.ProductActivity
import com.bashirli.saksak.util.setImageURL

class SearchAdapter(var arrayList: ArrayList<ProductModel>, val activity: Activity) : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

    class SearchHolder(var binding: RecyclersearchproductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val binding =
            RecyclersearchproductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHolder(binding)
    }

    override fun getItemCount(): Int {
        if(arrayList.size>100){
            return 100
        }else{
            return arrayList.size
        }
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
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

    fun updateList(newArrayList: ArrayList<ProductModel>) {
        arrayList.clear()
        arrayList.addAll(newArrayList)
        notifyDataSetChanged()
    }
}
