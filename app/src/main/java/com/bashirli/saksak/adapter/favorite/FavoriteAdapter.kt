package com.bashirli.saksak.adapter.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.databinding.RecyclerfavBinding
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.ui.activity.ProductActivity
import com.bashirli.saksak.util.setImageURL

class FavoriteAdapter(var arrayList: ArrayList<ProductModel>,
                      val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {



    class FavoriteHolder(var binding: RecyclerfavBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val binding =
            RecyclerfavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        with(holder.binding){
           favoriteData=arrayList.get(position)
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



}
