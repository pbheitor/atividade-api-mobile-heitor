# Buscador de Bandas 

## Descrição 
Aplicativo Android capaz de consultar álbuns de artistas musicais. O usuário informa o nome de uma banda e o app retorna os principais lançamentos organizados por ano e gênero musical.

## API utilizada 
- Nome da API: iTunes Search API 
- Endpoint utilizado: /search 
- Exemplo de URL consultada: https://itunes.apple.com/search?term=Deftones&entity=album&limit=3 
-  Principais dados retornados: Nome do álbum, gênero musical e ano de lançamento.

## Funcionalidades 
- Entrada de dados pelo usuário 
- Validação de campo vazio 
- Consulta a uma API pública 
- Exibição dos dados retornados 
- Tratamento básico de erro 

## Tecnologias utilizadas 
- Kotlin 
- Android Studio 
- XML 
- Biblioteca de requisição utilizada: Volley 
   API pública escolhida: iTunes Search 

## Permissões utilizadas 
O aplicativo utiliza a permissão INTERNET para realizar requisições à API pública.

## Prints do aplicativo
<img width="396" height="845" alt="banda_2" src="https://github.com/user-attachments/assets/b120e7ef-de7b-41d3-9bf3-6a774e603245" />
<img width="412" height="862" alt="banda_1" src="https://github.com/user-attachments/assets/7c22ea95-a759-40ef-8434-184e5403f55f" />



<uses-permission android:name="android.permission.INTERNET" />
```xml


