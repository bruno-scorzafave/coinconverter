package br.com.scorza5.coinconverter.data.repository

import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getExchangeValue(coins: String): Flow<ExchangeResponseValue>
    suspend fun save(exchangeResponseValue: ExchangeResponseValue)
    suspend fun deleteAll(list: List<ExchangeResponseValue>)
    suspend fun deleteLast(erv: ExchangeResponseValue)

    fun list():Flow<List<ExchangeResponseValue>>
}