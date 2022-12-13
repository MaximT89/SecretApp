package com.second.world.secretapp.ui.screens.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.server_feature.common.Indicators
import com.second.world.secretapp.data.server_feature.remote.conn_elements.client.ConnectionItemClient
import com.second.world.secretapp.databinding.MainHolderBinding
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi

class MainAdapter : ListAdapter<ConnectionItemClient, MainAdapter.MainViewHolder>(ItemComparator()) {

    var callBackBtnGoServerUsers: ((nextScreenConn : NextScreenConnUI?) -> Unit)? = null
    var callBackBtnStopServer: ((serverId : Int) -> Unit)? = null

    class ItemComparator : DiffUtil.ItemCallback<ConnectionItemClient>() {
        override fun areItemsTheSame(oldItem: ConnectionItemClient, newItem: ConnectionItemClient) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ConnectionItemClient, newItem: ConnectionItemClient) = oldItem == newItem
    }

    inner class MainViewHolder(private val binding: MainHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConnectionItemClient) = with(binding) {

            titleElement.text = item.connUi?.name

            if(item.connUi?.nextScreenConn != null) {
                btnWatchUsers.show()
            } else btnWatchUsers.hide()

            btnWatchUsers.click { callBackBtnGoServerUsers?.invoke(item.connUi?.nextScreenConn) }

            btnStopServer.click { callBackBtnStopServer?.invoke(item.id!!) }

            item.serverWorkStatus?.let {
                if (it) {
                    statusServerWorkText.text = item.connUi?.textStatusOn
                    indicator.setImageResource(Indicators.GREEN.image)
                } else {
                    statusServerWorkText.text = item.connUi?.textStatusOff
                    indicator.setImageResource(Indicators.RED.image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            MainHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}