package com.meghdut.nimie.ui.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meghdut.nimie.R
import com.meghdut.nimie.databinding.ItemOnboardingContainerBinding

class OnBoardingAdapter(private val mOnBoardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    private var mContext: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        mContext = parent.context
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bindOnBoardingData(mOnBoardingItems[position])
    }

    override fun getItemCount(): Int {
        return mOnBoardingItems.size
    }

    class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemOnboardingContainerBinding.bind(itemView)
        private val resources: Context get() = binding.root.context
        fun bindOnBoardingData(item: OnBoardingItem) {
            binding.onBoardingIv.setImageResource(item.imageSrcId)
            binding.title.text = resources.getText(item.titleSrcId)
            binding.description.text = resources.getText(item.descriptionSrcId)
        }
    }
}