package personal.rowan.petfinder.network

import personal.rowan.petfinder.BuildConfig
import personal.rowan.petfinder.model.AnimalsResponse
import personal.rowan.petfinder.model.OAuthTokenBody
import personal.rowan.petfinder.model.OAuthTokenResponse
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

/**
 * Created by Rowan Hall
 */
interface Petfinder2Service {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getOAuthToken(@Body body: OAuthTokenBody = OAuthTokenBody()): Call<OAuthTokenResponse>

    @GET("animals")
    fun getAnimals(@Query("location") location: String,
                   @Query("type") type: String?,
                   @Query("size") size: String?,
                   @Query("age") age: String?,
                   @Query("gender") gender: String?,
                   @Query("breed") breed: String?,
                   @Query("page") page: Int?): Observable<AnimalsResponse>

    companion object {
        const val BASE_URL = "http://api.petfinder.com/v2/"
        const val API_KEY = BuildConfig.PET_FINDER_2_API_KEY
        const val SECRET = BuildConfig.PET_FINDER_2_SECRET
    }

}
