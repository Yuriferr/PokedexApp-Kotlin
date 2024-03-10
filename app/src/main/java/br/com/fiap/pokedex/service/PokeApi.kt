package br.com.fiap.pokedex.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApi {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PokemonService = retrofit.create(PokemonService::class.java)
}