# Buscador de Bandas — Versão com Permissão Android

## Descrição
Aplicativo Android capaz de consultar álbuns de artistas musicais. O usuário informa o nome de uma banda e o app retorna os principais lançamentos organizados por ano e gênero musical. Nesta versão atualizada, o aplicativo também conta com um sistema de alertas integrado às notificações do sistema operacional.

## Relação com a atividade anterior
O miniapp original tinha como foco central o consumo da API do iTunes e a exibição de dados em tela. Nesta evolução, mantivemos o consumo da API pública inalterado e adicionamos um botão para "Ativar Alertas de Lançamentos", introduzindo um caso de uso real de permissão de sistema (Runtime Permission) focada no envio de notificações.

## API utilizada
- **Nome da API:** iTunes Search API
- **Endpoint utilizado:** `/search`
- **Exemplo de URL consultada:** `https://itunes.apple.com/search?term=Deftones&entity=album&limit=3`
- **Principais dados retornados:** Nome do álbum, gênero musical e ano de lançamento.

## Funcionalidades
- Entrada de dados pelo usuário
- Validação de campo vazio
- Consulta a uma API pública
- Exibição dos dados retornados
- Tratamento básico de erro
- **Nova:** Funcionalidade com solicitação de permissão Android (Envio de Notificações)
- **Nova:** Tratamento de permissão concedida (Disparo da notificação) e negada (Feedback em Toast)

## Tecnologias utilizadas
- Kotlin
- Android Studio
- XML
- Biblioteca de requisição utilizada: Volley
- API pública escolhida: iTunes Search
- **Permissão Android adicionada:** `POST_NOTIFICATIONS`

## Permissões utilizadas e Fluxo
O aplicativo utiliza a permissão `INTERNET` para realizar as requisições à API pública, e agora implementa a permissão restrita `POST_NOTIFICATIONS`.

- **Onde foi declarada:** Dentro da tag `<manifest>` no arquivo `AndroidManifest.xml`.
- **Por que é necessária:** Para avisar o usuário proativamente sobre novos álbuns e lançamentos da banda pesquisada, melhorando a utilidade do app.
- **Momento da solicitação:** O pop-up nativo do sistema é exibido no exato momento em que o usuário clica no botão "Ativar Alertas de Lançamentos" na tela principal.

**Fluxo da permissão:**
1. **Permissão já concedida:** O aplicativo ignora o pedido e dispara a notificação de sucesso diretamente na barra superior.
2. **Usuário concede a permissão:** O aplicativo registra a autorização e imediatamente dispara a notificação avisando que o alerta foi ativado.
3. **Usuário nega a permissão:** O app exibe um aviso em formato de Toast (alertando que os alertas estão desativados), permitindo que o usuário continue usando a busca de bandas normalmente sem que o aplicativo feche ou trave.


## Prints do aplicativo
<img width="396" height="845" alt="banda_2" src="https://github.com/user-attachments/assets/b120e7ef-de7b-41d3-9bf3-6a774e603245" />
<img width="412" height="862" alt="banda_1" src="https://github.com/user-attachments/assets/7c22ea95-a759-40ef-8434-184e5403f55f" />
<img width="401" height="867" alt="banda_4" src="https://github.com/user-attachments/assets/b2b7bafb-58a7-4b9a-a3e7-7a9c659f63c8" />
<img width="404" height="860" alt="banda_3" src="https://github.com/user-attachments/assets/93629a05-d514-45fa-9959-747ec698af8a" />


*(Arraste e solte aqui os prints novos mostrando o pop-up de permissão e a notificação)*

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```xml
