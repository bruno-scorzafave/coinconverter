package br.com.scorza5.coinconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias ExchangeResponse = HashMap<String, ExchangeResponseValue>

@Entity(tableName = "tb_exchange")
data class ExchangeResponseValue (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val code: String,
    val codein: String,
    val name: String,
    val bid: Double,
    var result:Double = 1.00,
    var exchangeValue: Double = 1.00
)