package com.meghdut.nimie.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

class XPagedAdapter<T : Any>(
    @LayoutRes val layout: Int,
    val bindData: View.(T, Int) -> Unit
) : PagingDataAdapter<T, GenericViewModel>(diffUtil()) {
    override fun onBindViewHolder(holder: GenericViewModel, position: Int) {
        holder.itemView.bindData(getItem(position)!!, position)
    }

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
}