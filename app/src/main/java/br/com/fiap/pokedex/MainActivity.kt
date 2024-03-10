package br.com.fiap.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.fiap.pokedex.model.Pokemon
import br.com.fiap.pokedex.service.PokeApi
import br.com.fiap.pokedex.ui.theme.PokedexTheme
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenPokedex()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPokedex() {
    val context = LocalContext.current
    var idName by remember {
        mutableStateOf("")
    }
    var currentPokemonId by remember {
        mutableStateOf(1)
    }
    var pokemon: Pokemon? by remember {
        mutableStateOf(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()


    // Função para buscar um Pokémon por ID
    fun fetchPokemonById(id: String) {
        coroutineScope.launch {
            isLoading = true
            val call = PokeApi.service.getPokemonById(id)
            call.enqueue(object : Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if (response.isSuccessful) {
                        pokemon = response.body()
                        currentPokemonId = pokemon?.id!!
                        Log.i("Poke", "Response: ${pokemon}")
                    } else {
                        Log.e(
                            "Poke",
                            "Response not successful ${response.code()} ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    Log.i("Poke", "onResponse ${t.message}")
                }

            })
            isLoading = false
        }
    }

    // Função para buscar o próximo Pokémon
    fun nextPokemon() {
        currentPokemonId++
        fetchPokemonById(currentPokemonId.toString())
    }

    // Função para buscar o Pokémon anterior
    fun previousPokemon() {
        if (currentPokemonId > 1) {
            currentPokemonId--
            fetchPokemonById(currentPokemonId.toString())
        }
    }

    LaunchedEffect(Unit) {
        fetchPokemonById("1") // Buscar o Pokémon com ID 1
    }

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = idName,
            onValueChange = {
                idName = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = "Digite o id ou nome do Pokemon"
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (idName != null) {
                    fetchPokemonById(idName)
                }
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        pokemon?.id?.let { Text(text = "# $it") }
                    }
                    Image(
                        painter = rememberImagePainter(pokemon?.sprites?.front_default),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    pokemon?.name?.let { Text(text = it) }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        pokemon?.types?.forEach { type ->
                            Text(text = type.type.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(
                            onClick = { previousPokemon() },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                        IconButton(onClick = { nextPokemon() }, modifier = Modifier.size(30.dp)) {
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Voltar")
                        }
                    }
                }
            }

        }
    }
}



