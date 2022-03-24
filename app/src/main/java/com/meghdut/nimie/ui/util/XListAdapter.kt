package com.meghdut.nimie.ui.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class XListAdapter<T : Any>(
    @LayoutRes val layout: Int,
    val bindData: View.(T, Int) -> Unit
) :
    ListAdapter<T, GenericViewModel>(diffUtil<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val layout = inflater.inflate(layout, null, false)
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layout.layoutParams = lp
        return GenericViewModel(layout)
    }

    override fun onBindViewHolder(holder: GenericViewModel, position: Int) {
        holder.itemView.bindData(getItem(position), position)
    }
}

open class GenericViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)



fun <T : Any> diffUtil(): DiffUtil.ItemCallback<T>{
    return object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            return oldItem === newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            return oldItem == newItem
        }
    }
}