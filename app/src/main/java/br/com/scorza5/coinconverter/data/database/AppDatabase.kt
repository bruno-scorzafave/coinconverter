package br.com.scorza5.coinconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.scorza5.coinconverter.data.database.dao.ExchangeDao
import br.com.scorza5.coinconverter.data.model.ExchangeResponseValue

@Database(entities = [ExchangeResponseValue::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao

    companion object{
        fun getInstance(context: Context): AppDatabase{
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database"
            ).build()
        }
    }
}