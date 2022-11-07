package com.second.world.secretapp.ui.screens.calculator_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.databinding.FragmentCalculatorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculatorFragment  : BaseFragment<FragmentCalculatorBinding,
        CalculatorViewModel>(FragmentCalculatorBinding::inflate) {

    override val viewModel: CalculatorViewModel by viewModels()

    override fun initView() = with(binding){

        clear.click {  }

        backspace.click {  }

        percent.click {  }

        division.click {  }

        multiply.click {  }

        minus.click {  }

        plus.click {  }

        equal.click {  }

        comma.click {  }

        btn0.click {  }

        btn1.click {  }

        btn2.click {  }

        btn3.click {  }

        btn4.click {  }

        btn5.click {  }

        btn6.click {  }

        btn7.click {  }

        btn8.click {  }

        btn9.click {  }

    }

    override fun initObservers() {
        viewModel.destination.observe { destination -> navigateTo(destination) }
    }
}