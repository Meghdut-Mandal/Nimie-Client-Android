package com.meghdut.nimie.ui.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meghdut.nimie.R
import com.meghdut.nimie.data.model.ChatMessage
import com.meghdut.nimie.data.model.ContentType
import com.meghdut.nimie.databinding.ItemInMsgBinding
import com.meghdut.nimie.databinding.ItemOutMsgBinding

class ChatAdapter : PagingDataAdapter<ChatMessage, GenericViewModel>(diffUtil()) {

    private val VIEW_TYPE_OUTGOING = 0
    private val VIEW_TYPE_RECEIVED = 1


    override fun onBindViewHolder(holder: GenericViewModel, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_OUTGOING -> bindOutMsg(holder, position)
            VIEW_TYPE_RECEIVED -> bindInMsg(holder, position)
        }
    }

    private fun bindInMsg(holder: GenericViewModel, position: Int) {
        val bind = ItemInMsgBinding.bind(holder.itemView)
        val item = getItem(position)!!
        fun setItemLayoutBackground() {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            var prevMessage: ChatMessage? = null
            var isPreviousMessageReceivingType = false
            if (position > 0) {
                prevMessage = getItem(position - 1)
                isPreviousMessageReceivingType =
                    prevMessage!!.userId == item.userId
            }
            if (prevMessage != null && isPreviousMessageReceivingType) {
                params.setMargins(3, 5, 3, 8)
                bind.llMessageBody.layoutParams = params
                bind.llMessageBody.background = ResourcesCompat.getDrawable(
                    holder.itemView.resources,
                    R.drawable.receiving_second,
                    null
                )
            } else {
                // If some other user sent the message
                params.setMargins(3, 25, 3, 8)
                bind.llMessageBody.layoutParams = params
                bind.llMessageBody.background = ResourcesCompat.getDrawable(
                    holder.itemView.resources,
                    R.drawable.receiving_first,
                    null
                )
            }
        }

        fun handleMessageText(message: ChatMessage) {
            var messageText = ""
            if (message.contentType === com.meghdut.nimie.data.model.ContentType.TXT) {
                messageText = message.message
            }
            val textMinimumLong = "This is a short message"
            val messageLength = messageText.length
            if (messageLength <= textMinimumLong.length) {
                bind.tvMessage.visibility = View.GONE
                bind.tvSmallMessage.visibility = View.VISIBLE
                bind.tvSmallMessage.text = messageText
            } else {
                bind.tvMessage.visibility = View.VISIBLE
                bind.tvSmallMessage.visibility = View.GONE
                bind.tvMessage.text = messageText
            }
        }

        bind.tvTime.text =
            holder.itemView.context.getDisplayableDateOfGivenTimeStamp(item.createTime, true)

        setItemLayoutBackground()
        handleMessageText(item)
    }

    private fun bindOutMsg(holder: GenericViewModel, position: Int) {
        val bind = ItemOutMsgBinding.bind(holder.itemView)
        val item = getItem(position)!!

        fun setItemLayoutBackground() {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            var prevMessage: ChatMessage? = null
            var isPreviousMessageReceivingType = false
            if (position > 0) {
                prevMessage = getItem(position - 1)
                isPreviousMessageReceivingType =
                    prevMessage!!.userId == item.userId
            }
            if (prevMessage != null && !isPreviousMessageReceivingType) {
                params.setMargins(3, 5, 3, 8)
                bind.llMessageBody.setLayoutParams(params)
                bind.llMessageBody.setBackground(
                    ResourcesCompat.getDrawable(
                        holder.itemView.resources,
                        R.drawable.outgoing_second,
                        null
                    )
                )
            } else {
                params.setMargins(3, 25, 3, 8)
                bind.llMessageBody.setLayoutParams(params)
                bind.llMessageBody.setBackground(
                    ResourcesCompat.getDrawable(
                        holder.itemView.resources,
                        R.drawable.outgoing_first,
                        null
                    )
                )
            }
        }

        fun handleMessageText(message: ChatMessage) {

            var messageText = ""
            if (message.contentType === ContentType.TXT) {
                messageText = message.message
            }
            val textMinimumLong = "This is a short message"
            val messageLength = messageText.length
            if (messageLength <= textMinimumLong.length) {
                bind.tvMessage.setVisibility(View.GONE)
                bind.tvSmallMessage.setVisibility(View.VISIBLE)
                bind.tvSmallMessage.setText(messageText)
            } else {
                bind.tvMessage.setVisibility(View.VISIBLE)
                bind.tvSmallMessage.setVisibility(View.GONE)
                bind.tvMessage.setText(messageText)
            }
        }

        setItemLayoutBackground()
        handleMessageText(item)
        bind.tvTime.text =
            holder.itemView.context.getDisplayableDateOfGivenTimeStamp(item.createTime, true)

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.userId.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewModel {

        val layoutRes =
            if (viewType == VIEW_TYPE_OUTGOING) R.layout.item_out_msg else R.layout.item_in_msg

        val inflater = LayoutInflater.from(parent.context)
        val layout = inflater.inflate(layoutRes, null, false)
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layout.layoutParams = lp
        return GenericViewModel(layout)
    }
}





