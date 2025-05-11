package com.example.linguae.data.remote.entity

import com.google.gson.annotations.SerializedName

/**
 * Model for getting access token
 */
data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_at") val expiresAt: Long
)
