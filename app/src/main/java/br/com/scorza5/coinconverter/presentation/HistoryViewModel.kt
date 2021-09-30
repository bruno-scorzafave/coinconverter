package br.com.scorza5.coinconverter.presentation

import androidx.lifecycle.*
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import br.com.scorza5.coinconverter.domain.ListExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val listExchangeUseCase: ListExchangeUseCase
): ViewModel(), LifecycleObserver{

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun getExchanges(){
        viewModelScope.launch {
            listExchangeUseCase()
                .flowOn(Dispatchers.Main)
                .onStart {
                    //mostra dialog de progress
                    _state.value = State.Loading
                }
                .catch {
                    // usa pra mostrar um erro
                    _state.value = State.Error(it)
                }
                .collect {
                    // usar pra mostrar o resultado
                    _state.value = State.Success(it)
                }
        }
    }
    sealed class State{
        object Loading: State()
        data class Success(val list: List<ExchangeResponseValue>): State()
        data class Error(val error: Throwable): State()
    }
}