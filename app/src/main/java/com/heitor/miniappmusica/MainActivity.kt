import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mapeando os elementos do XML
        val etBanda = findViewById<EditText>(R.id.etBanda)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Configurando a ação do clique no botão
        btnBuscar.setOnClickListener {
            val banda = etBanda.text.toString().trim()

            // Validação de campo vazio antes de realizar a consulta
            if (banda.isEmpty()) {
                Toast.makeText(this, "Por favor, digite o nome de uma banda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tvResultado.text = "Buscando na API..."
            buscarBandaNaAPI(banda, tvResultado)
        }
    }

    private fun buscarBandaNaAPI(banda: String, tvResultado: TextView) {
        // Encodifica o texto para URL (ex: "Novos Baianos" vira "Novos+Baianos")
        val termoBusca = URLEncoder.encode(banda, "UTF-8")

        // Montando a URL da API do iTunes limitando a 3 resultados
        val url = "https://itunes.apple.com/search?term=$termoBusca&entity=album&limit=3"

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val results = response.getJSONArray("results")

                    if (results.length() > 0) {
                        var textoExibicao = "Principais Resultados:\n\n"

                        for (i in 0 until results.length()) {
                            val album = results.getJSONObject(i)

                            // Extraindo 3 informações úteis do JSON
                            val nomeAlbum = album.getString("collectionName")
                            val genero = album.getString("primaryGenreName")
                            val dataLancamento = album.getString("releaseDate").substring(0, 4) // Pegando apenas o ano

                            textoExibicao += "- $nomeAlbum ($dataLancamento) | Gênero: $genero\n\n"
                        }
                        tvResultado.text = textoExibicao
                    } else {
                        tvResultado.text = "Nenhum resultado encontrado para esta pesquisa."
                    }
                } catch (e: Exception) {
                    // Trata erro caso o JSON venha em um formato inesperado
                    tvResultado.text = "Erro ao processar os dados da API."
                }
            },
            { error ->
                // Trata erro de requisição (ex: sem internet)
                tvResultado.text = "Erro na requisição. Verifique sua conexão com a internet."
            }
        )

        queue.add(jsonObjectRequest)
    }
}