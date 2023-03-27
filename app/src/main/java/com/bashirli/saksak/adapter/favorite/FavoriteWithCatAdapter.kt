package com.bashirli.saksak.adapter.favorite

import android.app.Activity
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bashirli.saksak.R
import com.bashirli.saksak.databinding.RecyclerfavwithcatBinding
import com.bashirli.saksak.model.CategoryModel
import com.bashirli.saksak.model.ProductModel
import com.bashirli.saksak.model.db.ItemModel
import com.bashirli.saksak.viewmodel.main.FavoritesMVVM
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class FavoriteWithCatAdapter
    (var arrayList: ArrayList<ProductModel>,
     var catList:ArrayList<CategoryModel>,
     var viewModel: FavoritesMVVM,
     val activity: Activity) : RecyclerView.Adapter<FavoriteWithCatAdapter.FavoriteWithCatHolder>() {


     var categoryAndProductList=ArrayList<ProductModel>()
    private set


    class FavoriteWithCatHolder(var binding: RecyclerfavwithcatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteWithCatHolder {
        val binding =
            RecyclerfavwithcatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteWithCatHolder(binding)
    }


    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: FavoriteWithCatHolder, position: Int) {
        categoryAndProductList.clear()
    holder.binding.category.setText(catList.get(position).name)
        for(data in arrayList){
            if(data.category.id==catList.get(position).id){
                categoryAndProductList.add(data)
            }
        }

        val adapter=FavoriteAdapter(categoryAndProductList,activity)
        recyclerEdit(holder.binding.recyclerFav,adapter)
        holder.binding.recyclerFav.adapter=adapter
        holder.binding.recyclerFav.layoutManager=LinearLayoutManager(holder.itemView.context)
    }

    private fun recyclerEdit(myRecyclerView: RecyclerView,adapter: FavoriteAdapter){
        var callback=object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val myColor="#F1F1F1"
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(myColor.toColorInt())
                    .addSwipeLeftActionIcon(R.drawable.deleteico)
                    .addCornerRadius(1,20)
                    .create().decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.bindingAdapterPosition
                val deletedData=adapter.arrayList.get(position)
                val deletedItemModel= ItemModel(deletedData.id,deletedData.title)
                viewModel.deleteItem(deletedData.id)
                adapter.arrayList.removeAt(position)
                adapter.notifyItemRemoved(position)
                Snackbar.make(viewHolder.itemView,"Product deleted from cart.", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){
                        viewModel.insertFav(deletedItemModel)
                        adapter.arrayList.add(position,deletedData)
                        adapter.notifyItemInserted(position)
                    }.show()
            }

        }

        var itemTouchHelper= ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(myRecyclerView)


    }

}
