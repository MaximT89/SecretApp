package com.second.world.secretapp.ui.screens.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.data.main_screen.common.Indicators
import com.second.world.secretapp.databinding.MainHolderBinding
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi

class MainAdapter : ListAdapter<SrvItemUi, MainAdapter.MainViewHolder>(ItemComparator()) {

    var callBackBtnStopServer: ((serverData: SrvItemUi) -> Unit)? = null

    class ItemComparator : DiffUtil.ItemCallback<SrvItemUi>() {
        override fun areItemsTheSame(oldItem: SrvItemUi, newItem: SrvItemUi) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: SrvItemUi, newItem: SrvItemUi) = oldItem == newItem
    }

    inner class MainViewHolder(private val binding: MainHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SrvItemUi) = with(binding) {
            titleElement.text = item.name

            btnStopServer.click { callBackBtnStopServer?.invoke(item) }

            item.workStatus?.let {
                if (it) {
                    statusServerWorkText.text = item.textStatusOn
                    indicator.setBackgroundResource(Indicators.GREEN.image)
                } else {
                    statusServerWorkText.text = item.textStatusOff
                    indicator.setBackgroundResource(Indicators.RED.image)
                }
            }
        }
    }

    fun convertUrl(item: SrvItemUi): String {
        return "${item.protocol}://${item.ip}:${item.port}/"
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