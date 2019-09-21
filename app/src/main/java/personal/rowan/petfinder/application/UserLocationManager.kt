package personal.rowan.petfinder.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.*
import android.text.TextUtils
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import java.util.*
import javax.inject.Singleton

/**
 * Created by Rowan Hall
 */

@Singleton
class UserLocationManager private constructor() {

    private object Holder {
        val INSTANCE = UserLocationManager()
    }

    companion object {
        val INSTANCE: UserLocationManager by lazy { Holder.INSTANCE }
        const val ERROR = "ERROR"
        const val PREFS_NAME = "PREFS_NAME"
        const val PREFS_KEY_ZIPCODE = "ZIPCODE"
    }

    private var mSharedPrefs: SharedPreferences? = null
    private val mPermissionSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun permissionEvent(granted: Boolean) {
        mPermissionSubject.onNext(granted)
    }

    fun permissionObservable(): Observable<Boolean> {
        return mPermissionSubject
    }

    @SuppressLint("MissingPermission")
    fun zipcodeObservable(context: Context): Observable<String> {
        return Observable.fromCallable {
            val zipcode = try {
                val locationManager = context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                addresses[0].postalCode
            } catch (e: Exception) {
                val savedZipcode = loadZipcode(context)
                if (!TextUtils.isEmpty(savedZipcode)) savedZipcode else ERROR
            }
            zipcode
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun loadZipcode(context: Context): String {
        initializePrefs(context)
        return mSharedPrefs!!.getString(PREFS_KEY_ZIPCODE, "") ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun saveZipcode(context: Context, zipcode: String) {
        initializePrefs(context)
        mSharedPrefs?.edit()?.putString(PREFS_KEY_ZIPCODE, zipcode)?.commit()
    }

    private fun initializePrefs(context: Context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

}
