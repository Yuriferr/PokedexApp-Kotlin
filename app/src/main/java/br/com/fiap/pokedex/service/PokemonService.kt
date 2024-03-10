package br.com.fiap.pokedex.service

import br.com.fiap.pokedex.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: String): Call<Pokemon>
}