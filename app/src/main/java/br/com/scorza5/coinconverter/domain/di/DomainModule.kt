package br.com.scorza5.coinconverter.domain.di

import br.com.scorza5.coinconverter.domain.*
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {
    fun load(){
        loadKoinModules(useCaseModules())
    }

    private fun useCaseModules(): Module {
        return module {
            factory { ListExchangeUseCase(get()) }
            factory { SaveExchangeUseCase(get()) }
            factory { GetExchangeValueUseCase(get()) }
            factory { DeleteLisExchangeUseCase(get()) }
            factory { DeleteLastExchangeUseCase(get()) }
        }
    }
}