package personal.rowan.petfinder.model

import com.google.gson.annotations.SerializedName
import personal.rowan.petfinder.network.Petfinder2Service

/**
 * Created by Rowan Hall
 */

data class OAuthTokenBody(@SerializedName("grant_type") val grantType: String = "client_credentials",
                          @SerializedName("client_id") val clientId: String = Petfinder2Service.API_KEY,
                          @SerializedName("client_secret") val clientSecret: String = Petfinder2Service.SECRET)

data class OAuthTokenResponse(@SerializedName("token_type") val tokenType: String,
                              @SerializedName("expires_in") val expiresIn: Long,
                              @SerializedName("access_token") val accessToken: String)