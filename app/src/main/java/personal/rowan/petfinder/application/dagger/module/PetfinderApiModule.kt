package personal.rowan.petfinder.application.dagger.module

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import personal.rowan.petfinder.network.Petfinder2Service
import personal.rowan.petfinder.util.RetrofitServiceFactory

/**
 * Created by Rowan Hall
 */

@Module
class PetfinderApiModule(private val context: Context) {

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
    fun providePetfinder2Service(): Petfinder2Service {
        return RetrofitServiceFactory.createRetrofitService(Petfinder2Service::class.java, Petfinder2Service.BASE_URL, petfinder2HttpClient, petfinder2Gson)
    }

    @SuppressLint("ApplySharedPref")
    private fun refreshOAuthToken(): String? {
        val authService = RetrofitServiceFactory.createRetrofitService(Petfinder2Service::class.java, Petfinder2Service.BASE_URL, OkHttpClient(), Gson())
        val accessToken = authService.getOAuthToken().execute().body()?.accessToken ?: return null
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFS_KEY_OAUTH_TOKEN, accessToken).commit()
        return accessToken
    }

    companion object {
        private const val PREFS_KEY_OAUTH_TOKEN = "OAUTH_TOKEN"
        private const val HEADER_KEY_OAUTH = "AUTHORIZATION"
        private const val HEADER_VALUE_OAUTH_PREFIX = "Bearer "
    }

}
