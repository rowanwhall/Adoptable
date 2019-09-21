package personal.rowan.petfinder.model

import personal.rowan.petfinder.network.Petfinder2Service

/**
 * Created by Rowan Hall
 */

data class OAuthTokenBody(val grant_type: String = "client_credentials",
                          val client_id: String = Petfinder2Service.API_KEY,
                          val client_secret: String = Petfinder2Service.SECRET)

data class OAuthTokenResponse(val token_type: String,
                              val expires_in: Long,
                              val access_token: String)