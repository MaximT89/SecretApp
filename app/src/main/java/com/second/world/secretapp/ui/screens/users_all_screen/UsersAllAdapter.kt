package com.second.world.secretapp.ui.screens.users_all_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.second.world.secretapp.R
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.data.users_feature.remote.model.response.UsersItem
import com.second.world.secretapp.databinding.UsersAllHolderBinding

@SuppressLint("NotifyDataSetChanged, SetTextI18n")
class UsersAllAdapter : RecyclerView.Adapter<UsersAllAdapter.UsersAllHolder>() {

    var items: List<UsersItem?>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var callBackUserAdapter: ((item: UsersItem?) -> Unit)? = null

    inner class UsersAllHolder(private val binding: UsersAllHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UsersItem?) = with(binding) {
            userName.text = "Имя : ${item?.name}"
            userPhone.text = "Тел. : +7${item?.phone}"

            if (item?.active == 1) {
                userActive.text = "Активность : Да "
                imgActive.setBackgroundResource(R.drawable.green_ind)
            }
            else {
                userActive.text = "Активность. : Нет"
                imgActive.setBackgroundResource(R.drawable.red_ind)
            }

            content.click { callBackUserAdapter?.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAllHolder {
        return UsersAllHolder(
            UsersAllHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersAllHolder, position: Int) {
        holder.bind(items?.get(position))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }
}