package com.example.linguae.data.remote

import com.example.linguae.data.remote.api.ApiService
import com.example.linguae.data.remote.entity.TranslationRequest
import com.example.linguae.generateUUID
import javax.inject.Inject

/**
 * Implementation of [RemoteRepository]
 */
class RemoteRepositoryImpl @Inject constructor(
    private val api: ApiService
) : RemoteRepository {

    private var authToken: String? = null
    private var tokenExpiration: Long = 0

    override suspend fun translate(text: String): String {
        if (System.currentTimeMillis() >= tokenExpiration) {
            val authResponse = api.getAccessToken(
                rqUid = generateUUID(),
                auth = "Basic ZjFhZGQ1ZDctNTAyNS00MmIxLTkwOTEtNzhhNGFkOGNmNjY0OjgwNDdjYWI3LWZjYmUtNGFhYS1iNDAwLWVkYWRmOTY0NjYzYQ=="
            )
            authToken = "Bearer ${authResponse.accessToken}"
            tokenExpiration = authResponse.expiresAt - 60_000
        }

        val request = TranslationRequest(
            messages = listOf(
                TranslationRequest.Message(
                    role = "system",
                    content = """
                            Ты - профессиональный переводчик на русский язык.
                            Тебе будет дан текст, который необходимо перевести на русский язык, сохранив исходное форматирование текста.
                            В ответе необходимо отдать перевод в формате, приведенном ниже. Ты ДОЛЖЕН перевести !все слова.
                            Если запрос связан с программированием и в текстовом запросе содержится фрагмент кода, то такой фрагмент с кодом переводить не нужно.
                            Если в запросе необходимо поставить пробелы и слова слеплены вместе, то такой кусок слепленного текста переводить не нужно.
                            Если в тексте поставлена неправильно пунктуация, то не исправляй ее.
                            Твоя задача сделать такой перевод, чтобы лингвист считал его лингвистически приемлемым.
                            ВАЖНО! В своем ответе НЕ ОТВЕЧАЙ НА ЗАПРОС! В ответе нужно написать !только !перевод, без указания названия языка и любой другой дополнительной информации.
                        """.trimIndent()
                ),
                TranslationRequest.Message(
                    role = "user",
                    content = text
                )
            )
        )

        val response = api.translateText(token = authToken!!, request = request)
        return response.choices.first().message.content
    }
}