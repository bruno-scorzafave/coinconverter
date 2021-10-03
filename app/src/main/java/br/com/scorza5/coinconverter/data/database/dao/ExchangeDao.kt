package br.com.scorza5.coinconverter.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM tb_exchange")
    fun findAll(): Flow<List<ExchangeResponseValue>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(entity: ExchangeResponseValue)

}