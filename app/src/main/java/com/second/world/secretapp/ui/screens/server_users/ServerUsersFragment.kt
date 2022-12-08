package com.second.world.secretapp.ui.screens.server_users

import android.text.TextUtils
import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import com.second.world.secretapp.databinding.FragmentServerUsersBinding
import com.second.world.secretapp.ui.screens.main_screen.MainFragment
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerUsersFragment :
    BaseFragment<FragmentServerUsersBinding, ServerUsersViewModel>(FragmentServerUsersBinding::inflate) {
    override val viewModel: ServerUsersViewModel by viewModels()

    private val adapter = ServerUsersAdapter()

    override fun initView() = with(binding) {
        showTitle(true)

        updateTitle(title = "Пользователи")

        recyclerViewServerUsers.adapter = adapter
    }

    override fun initObservers() = with(viewModel) {

        serverUsersState.observe { state ->
            when (state) {
                ServerUsersState.EmptyState -> {
                    updateUi(progress = true)
                }
                is ServerUsersState.Error -> {
                    log(tag = "state", message = "пришло с ошибкой во фрагмент")
                    updateUi()
                }
                ServerUsersState.Loading -> {
                    updateUi(progress = true)
                }
                is ServerUsersState.NoInternet -> {
                    updateUi()
                }
                is ServerUsersState.Success -> {

                    log(tag = "state", message = "успешно пришло во фрагмент")
                    updateUi(content = true, data = state.data)

                }
                is ServerUsersState.Test -> {
                    updateUi()
                }
            }
        }
    }

    override fun initCallbacks() {

        adapter.callBackServerUserRemove = {
            alertDialog(
                positiveBtnLogic = {

                    // TODO: реализовать запрос на отключение пользователя

                },
                bodyText = "Вы хотите отключить пользователя?"
            )
        }

        adapter.callBackServerUserSendMess = {
            alertDialog(
                positiveBtnLogic = { editText ->
                    if (!TextUtils.isEmpty(editText)) {

                        // TODO: реализовать отправку сообщения пользователю на сервер

                        showSnackbar("текст такой : $editText")
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
        data: ResponseServerUsers? = null
    ) = with(binding) {
        if (progress) progressBar.show()
        else progressBar.hide()

        if (content) recyclerViewServerUsers.show()
        else recyclerViewServerUsers.hide()

        if (data != null) {
            adapter.submitList(data.data?.users)
        }
    }

    override fun listenerBundleArguments() {

        readArguments<NextScreenConnUI>(MainFragment.NEXT_SCREEN_CONN_KEY,
            ifExist = { connItem ->
                viewModel.getSaveClientAndGetUsers(connItem)
            })

    }
}
