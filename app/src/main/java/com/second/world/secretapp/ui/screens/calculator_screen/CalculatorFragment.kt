package com.second.world.secretapp.ui.screens.calculator_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.updateText
import com.second.world.secretapp.databinding.FragmentCalculatorBinding
import com.second.world.secretapp.ui.screens.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculatorFragment  : BaseFragment<FragmentCalculatorBinding,
        CalculatorViewModel>(FragmentCalculatorBinding::inflate) {

    override val viewModel: CalculatorViewModel by viewModels()

    override fun initView() = with(binding){

        clear.click { viewModel.clearCurrentNumber() }

        backspace.click { viewModel.backspace() }

        percent.click { viewModel.convertToPercent() }

        division.click { viewModel.setOperation(Operation.DIV) }

        multiply.click { viewModel.setOperation(Operation.TIMES) }

        minus.click { viewModel.setOperation(Operation.MINUS) }

        plus.click { viewModel.setOperation(Operation.PLUS) }

        equal.click { viewModel.setOperation(Operation.EQUAL) }

        comma.click { viewModel.addComma() }

        btn0.click { viewModel.addNewValue("0") }

        btn1.click { viewModel.addNewValue("1") }

        btn2.click { viewModel.addNewValue("2") }

        btn3.click { viewModel.addNewValue("3") }

        btn4.click { viewModel.addNewValue("4") }

        btn5.click { viewModel.addNewValue("5") }

        btn6.click { viewModel.addNewValue("6") }

        btn7.click { viewModel.addNewValue("7") }

        btn8.click { viewModel.addNewValue("8") }

        btn9.click { viewModel.addNewValue("9") }

    }

    override fun initObservers() {
        viewModel.destination.observe { destination -> navigateTo(destination) }

        viewModel.currentNumber.observe {
            updateText(binding.mainText, it)
        }

        viewModel.finalText.observe {
            updateText(binding.finalDataText, it)
        }

        viewModel.allUserInput.observe{ userInput ->
            viewModel.checkSecretPin(userInput)
        }

    }
}