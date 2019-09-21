package personal.rowan.petfinder.application.dagger.module

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import personal.rowan.petfinder.model.pet.Breeds
import personal.rowan.petfinder.model.pet.Options
import personal.rowan.petfinder.network.BreedsTypeAdapter
import personal.rowan.petfinder.network.OptionsTypeAdapter
import personal.rowan.petfinder.network.Petfinder2Service
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.util.RetrofitServiceFactory

/**
 * Created by Rowan Hall
 */

@Module
class PetfinderApiModule(private val context: Context) {

    private val petfinderHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("key", PetfinderService.API_KEY)
                        .addQueryParameter("format", "json")
                        .build()

                val request = original.newBuilder().url(url).build()
                it.proceed(request)
            }
            .build()

    private val petfinderGson = GsonBuilder()
            //.registerTypeAdapter(Pets::class.java, PetsTypeAdapter())
            .registerTypeAdapter(Breeds::class.java, BreedsTypeAdapter())
            .registerTypeAdapter(Options::class.java, OptionsTypeAdapter())
            .create()

    private val petfinder2HttpClient = OkHttpClient.Builder()
            .authenticator { _, response ->
                if (response.code() in 400..499) {
                    val refreshedToken = refreshOAuthToken() ?: return@authenticator null
                    return@authenticator response.request().newBuilder()
                            .addHeader(HEADER_KEY_OAUTH, HEADER_VALUE_OAUTH_PREFIX + refreshedToken)
                            .build()
                }
                null
            }
            .addInterceptor {
                val token = PreferenceManager.getDefaultSharedPreferences(context).getString(PREFS_KEY_OAUTH_TOKEN, null)
                        ?: refreshOAuthToken()
                val request = it.request()
                token.let {
                    request.headers().newBuilder().add(HEADER_KEY_OAUTH, HEADER_VALUE_OAUTH_PREFIX + token)
                }
                it.proceed(request)
            }
            .build()

    private val petfinder2Gson = GsonBuilder().create()

    @Provides
    fun providePetfinderService(): PetfinderService {
        return RetrofitServiceFactory.createRetrofitService(PetfinderService::class.java, PetfinderService.BASE_URL, petfinderHttpClient, petfinderGson)
    }

    @Provides
    fun providePetfinder2Service(): Petfinder2Service {
        return RetrofitServiceFactory.createRetrofitService(Petfinder2Service::class.java, Petfinder2Service.BASE_URL, petfinder2HttpClient, petfinder2Gson)
    }

    @SuppressLint("ApplySharedPref")
    private fun refreshOAuthToken(): String? {
        val authService = RetrofitServiceFactory.createRetrofitService(Petfinder2Service::class.java, Petfinder2Service.BASE_URL, OkHttpClient(), Gson())
        val authResponse = authService.getOAuthToken().execute().body() ?: return null
        val accessToken = authResponse.access_token
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFS_KEY_OAUTH_TOKEN, accessToken).commit()
        return accessToken
    }

    companion object {
        private const val PREFS_KEY_OAUTH_TOKEN = "OAUTH_TOKEN"
        private const val HEADER_KEY_OAUTH = "AUTHORIZATION"
        private const val HEADER_VALUE_OAUTH_PREFIX = "Bearer "
    }

}
