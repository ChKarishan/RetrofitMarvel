package com.example.Model

import kotlinx.serialization.Serializable


//@kotlinx.serialization.Serializable
//data class Results(val name: String)


@Serializable
data class Results(
    val title: String,
    val description: String,
    val image: String
)