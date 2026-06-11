package com.heitor.miniappmusica

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.heitor.miniappmusica.R
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enviarNotificacao()
        } else {
            Toast.makeText(this, "Permissão negada. Os alertas estão desativados.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarNotificacao() {
        val channelId = "canal_bandas"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        // O Android 8.0+ exige a criação de um "Canal" de notificações
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Alertas de Bandas",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Monta a notificação visualmente
        val builder = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Usa o ícone padrão do seu app
            .setContentTitle("Buscador de Bandas")
            .setContentText("Alerta ativado! Avisaremos sobre novos álbuns.")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)

        // Dispara a notificação
        notificationManager.notify(1, builder.build())
    }

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

        val btnNotificacao = findViewById<Button>(R.id.btnNotificacao)

        btnNotificacao.setOnClickListener {
            // Verifica se o celular está rodando Android 13 ou superior
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when {
                    // Se já tem permissão, apenas envia a notificação
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                        enviarNotificacao()
                    }
                    // Se não tem, abre o pop-up pedindo a permissão
                    else -> {
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            } else {
                // Celulares com Android antigo não precisam desse pop-up, a permissão é automática
                enviarNotificacao()
            }
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