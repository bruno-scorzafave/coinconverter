package br.com.scorza5.coinconverter.presentation.di

import br.com.scorza5.coinconverter.presentation.HistoryViewModel
import br.com.scorza5.coinconverter.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModules {

    fun load(){
        loadKoinModules(viewModelModules())
    }

    private fun viewModelModules(): Module {
        return module {
            viewModel { HistoryViewModel(get(), get(), get()) }
            viewModel { MainViewModel(get(), get()) }
        }
    }
}