package com.second.world.secretapp.ui.screens.server_users

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.data.server_feature.common.Indicators
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ServerUsersItem
import com.second.world.secretapp.databinding.ServerUserHolderBinding

class ServerUsersAdapter : ListAdapter<ServerUsersItem, ServerUsersAdapter.ServerUserHolder>(ItemComparator()){

    var callBackServerUserSendMess : ((id : Int) -> Unit)? = null
    var callBackServerUserRemove : ((id : Int) -> Unit)? = null

    class ItemComparator : DiffUtil.ItemCallback<ServerUsersItem>() {
        override fun areItemsTheSame(oldItem: ServerUsersItem, newItem: ServerUsersItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ServerUsersItem, newItem: ServerUsersItem) = oldItem == newItem
    }

    inner class ServerUserHolder(private val binding: ServerUserHolderBinding) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(item : ServerUsersItem) = with(binding){

            userName.text = "Имя : ${item.userName}"
            userActive.text = "Активность : ${item.status}"
            userTimeSleep.text = "Time_sleep : ${item.timeSleep}"
            userTimeIn.text = "Time_in : ${item.timeIn}"

            item.status?.let {
                if (it == "Активно") imgActive.setBackgroundResource(Indicators.GREEN.image)
                else imgActive.setBackgroundResource(Indicators.RED.image)
            }

            btnRemoveUser.click { callBackServerUserRemove?.invoke(item.id!!) }
            btnSendMessage.click { callBackServerUserSendMess?.invoke(item.id!!) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerUserHolder {
        return ServerUserHolder(ServerUserHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ServerUserHolder, position: Int) {
        holder.bind(getItem(position))
    }
}