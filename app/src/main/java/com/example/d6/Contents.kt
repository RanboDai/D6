package com.example.d6

import com.google.gson.annotations.SerializedName

data class Contents(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val ibge: String,
    val gia: String,
    val ddd: Int,
    val siafi: Int
)
