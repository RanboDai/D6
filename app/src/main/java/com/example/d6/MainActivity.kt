import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.example.d6.databinding.ActivityMainBinding

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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.idButton.setOnClickListener {
            val cep = binding.idCep.text.toString().trim()
            if (cep.length == 8) {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = buscarEndereco("https://viacep.com.br/ws/$cep/json/", "GET")
                    withContext(Dispatchers.Main) {
                        if (result.isNotEmpty()) {
                            val gson = Gson()
                            val gsonResponse = gson.fromJson(result, Contents::class.java)
                            updateUI(gsonResponse)
                        } else {
                            binding.idView.text = "CEP Não Encontrado"
                        }
                    }
                }
            } else {
                binding.idView.text = "CEP inválido"
            }
        }
    }

    private fun buscarEndereco(urlString: String, requestMethod: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = requestMethod
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            response.append(line)
            line = reader.readLine()
        }
        reader.close()
        connection.disconnect()
        return response.toString()
    }

    private fun updateUI(contents: Contents) {
        binding.idView.text = "CEP: ${contents.cep}\nLogradouro: ${contents.logradouro}\nBairro: ${contents.bairro}\nCidade: ${contents.localidade}\nUF: ${contents.uf}"
    }
}

