package com.example.linguae.data.remote.api

import com.example.linguae.data.remote.entity.AuthResponse
import com.example.linguae.data.remote.entity.TranslationRequest
import com.example.linguae.data.remote.entity.TranslationResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Interface for translation requests
 */
interface ApiService {
    @POST
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Url url: String = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth",
        @Header("RqUID") rqUid: String,
        @Header("Authorization") auth: String,
        @Field("scope") scope: String = "GIGACHAT_API_PERS"
    ): AuthResponse

    @POST
    suspend fun translateText(
        @Url url: String = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions",
        @Header("Authorization") token: String,
        @Body request: TranslationRequest
    ): TranslationResponse
}