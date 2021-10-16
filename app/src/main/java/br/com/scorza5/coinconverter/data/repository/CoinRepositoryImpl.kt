package br.com.scorza5.coinconverter.data.repository

import br.com.scorza5.coinconverter.core.exceptions.RemoteException
import br.com.scorza5.coinconverter.data.database.AppDatabase
import br.com.scorza5.coinconverter.data.model.ErrorResponse
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import br.com.scorza5.coinconverter.data.services.AwesomeService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class CoinRepositoryImpl(
    appDatabase: AppDatabase,
    private val service: AwesomeService
): CoinRepository {

    private val dao = appDatabase.exchangeDao()

    override suspend fun getExchangeValue(coins: String) = flow {
        //{"status":404,"code":"CoinNotExists","message":"moeda nao encontrada USD-USD"}
        try {
            val exchangeValue = service.exchangeValue(coins)
            val exchange = exchangeValue.values.first()
            emit(exchange)
        } catch (e: HttpException){
            val json = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(json, ErrorResponse::class.java)
            throw RemoteException(errorResponse.message)
        }
    }

    override suspend fun save(exchangeResponseValue: ExchangeResponseValue) {
        dao.save(exchangeResponseValue)
    }

    override suspend fun deleteAll(list: List<ExchangeResponseValue>) {
        dao.deleteAll(list)
    }

    override suspend fun deleteLast(erv: ExchangeResponseValue) {
        dao.deleteLast(erv)
    }

    override fun list(): Flow<List<ExchangeResponseValue>> {
        return dao.findAll()
    }
}