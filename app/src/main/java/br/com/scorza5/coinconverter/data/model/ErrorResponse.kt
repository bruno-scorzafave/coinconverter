package br.com.scorza5.coinconverter.data.model

data class ErrorResponse(
    val status: Long,
    val code: String,
    val message: String
)
