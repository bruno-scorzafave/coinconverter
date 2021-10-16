package br.com.scorza5.coinconverter.domain

import br.com.scorza5.coinconverter.core.UseCase
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import br.com.scorza5.coinconverter.data.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteLisExchangeUseCase(
    private val repository: CoinRepository
):UseCase.NoSource<List<ExchangeResponseValue>>() {
    override suspend fun execute(param: List<ExchangeResponseValue>): Flow<Unit> {
        return flow {
            repository.deleteAll(param)
            emit(Unit)
        }

    }


}