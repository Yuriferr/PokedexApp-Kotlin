package br.com.fiap.pokedex.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val sprites: Sprites,
)

data class Type(
    val type: TypeName
)

data class TypeName(
    val name: String
)

data class Sprites(
    val front_default: String,
    val back_default: String,
)

