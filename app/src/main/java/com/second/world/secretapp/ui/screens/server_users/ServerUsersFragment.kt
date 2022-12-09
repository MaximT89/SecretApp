package com.second.world.secretapp.ui.screens.server_users

import android.text.TextUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ServerUsersItem
import com.second.world.secretapp.databinding.FragmentServerUsersBinding
import com.second.world.secretapp.ui.screens.main_screen.MainFragment
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerUsersFragment :
    BaseFragment<FragmentServerUsersBinding, ServerUsersViewModel>(FragmentServerUsersBinding::inflate) {
    override val viewModel: ServerUsersViewModel by viewModels()

    private val adapter = ServerUsersAdapter()

    override fun initView() {
        showTitle(true)

        updateTitle(title = "Пользователи")

        binding.recyclerViewServerUsers.adapter = adapter

        binding.serverUserSearch.addTextChangedListener {
            viewModel.getSearchResult(it.toString())
        }
    }

    override fun initObservers() = with(viewModel) {

        // TODO: обработать все состояния
        serverUsersState.observe { state ->
            when (state) {
                ServerUsersState.EmptyState -> updateUi(progress = true)

                is ServerUsersState.Error -> updateUi()

                ServerUsersState.Loading -> updateUi(progress = true)

                is ServerUsersState.NoInternet -> updateUi()

                is ServerUsersState.Success -> updateUi(content = true, data = state.data)

                is ServerUsersState.Test -> updateUi()

                is ServerUsersState.SuccessSearch -> updateUi(content = true, listData = state.data)

                is ServerUsersState.ResultSendMessage -> showSnackbar(state.resultSendMessage)

                is ServerUsersState.ResultBlockUser -> showSnackbar(state.resultBlockUser)
            }
        }
    }

    override fun initCallbacks() {

        adapter.callBackServerUserRemove = { userId, userName ->
            alertDialog(
                positiveBtnLogic = {
                    viewModel.blockServerUser(userId, userName)
                },
                bodyText = "Вы хотите отключить пользователя?"
            )
        }

        adapter.callBackServerUserSendMess = { userId ->
            alertDialog(
                positiveBtnLogic = { editText ->
                    if (!TextUtils.isEmpty(editText)) {
                        viewModel.sendServerUserMessage(userId = userId, message = editText)
                    } else showSnackbar("вы не ввели текст")
                },
                needEditText = true,
                needBodyText = false,
                textPositiveBtn = "Отправить",
                textNegativeBtn = "Отмена"
            )
        }
    }

    private fun updateUi(
        progress: Boolean = false,
        content: Boolean = false,
        data: ResponseServerUsers? = null,
        listData: List<ServerUsersItem?>? = null
    ) = with(binding) {
        if (progress) progressBar.show()
        else progressBar.hide()

        if (content) recyclerViewServerUsers.show()
        else recyclerViewServerUsers.hide()

        data?.let { adapter.submitList(it.data?.users) }

        listData?.let { adapter.submitList(it) }
    }

    override fun listenerBundleArguments() {

        readArguments<NextScreenConnUI>(MainFragment.NEXT_SCREEN_CONN_KEY,
            ifExist = { connItem -> viewModel.getSaveClientAndGetUsers(connItem) })
    }
}
