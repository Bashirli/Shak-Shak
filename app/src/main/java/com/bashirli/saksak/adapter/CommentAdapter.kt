package com.bashirli.saksak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bashirli.saksak.databinding.RecyclercommentBinding
import com.bashirli.saksak.model.CommentModel

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    val arrayList= arrayListOf<CommentModel>()
    inner class CommentHolder(var binding:RecyclercommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
    val binding=RecyclercommentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommentHolder(binding)
    }

    override fun getItemCount(): Int {
     return arrayList.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.binding.comData=arrayList.get(position)
    }

    fun updateList(list:List<CommentModel>){
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

}