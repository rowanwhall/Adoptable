package personal.rowan.petfinder.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.android.gms.location.LocationServices
import rx.Observable
import rx.subjects.BehaviorSubject
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
        const val PREFS_KEY_LOCATION = "ZIPCODE"
    }

    private var mSharedPrefs: SharedPreferences? = null
    private val mPermissionSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun permissionEvent(granted: Boolean) {
        mPermissionSubject.onNext(granted)
    }

    fun permissionObservable(): Observable<Boolean> {
        return mPermissionSubject
    }

    fun getLocation(context: Context, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {
            onSuccess(it.latitude.toString() + "," + it.longitude.toString())
        }.addOnFailureListener {
            val savedLocation = loadSavedLocation(context)
            if (!TextUtils.isEmpty(savedLocation)) onSuccess(savedLocation) else onFailure()
        }
    }

    fun loadSavedLocation(context: Context): String {
        initializePrefs(context)
        return mSharedPrefs!!.getString(PREFS_KEY_LOCATION, "") ?: ""
    }

    @SuppressLint("ApplySharedPref")
    fun saveLocation(context: Context, zipcode: String) {
        initializePrefs(context)
        mSharedPrefs?.edit()?.putString(PREFS_KEY_LOCATION, zipcode)?.commit()
    }

    private fun initializePrefs(context: Context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

}
