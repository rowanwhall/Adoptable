package personal.rowan.petfinder.ui.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.location.dagger.LocationComponent
import personal.rowan.petfinder.ui.main.MainActivity
import personal.rowan.petfinder.util.AndroidUtils
import personal.rowan.petfinder.util.PermissionUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class LocationFragment : BaseFragment() {

    companion object {

        fun newInstance(): LocationFragment {
            return LocationFragment()
        }
    }

    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    private lateinit var locationFailureContainer: LinearLayout
    private lateinit var locationFailureButton: Button
    private lateinit var zipcodeEntry: EditText
    private lateinit var zipcodeEntryButton: Button
    private lateinit var locationPermissionContainer: LinearLayout
    private lateinit var locationPermissionButton: Button

    private val mCompositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationComponent.injector.call(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationFailureContainer = view.findViewById(R.id.location_failure_container)
        locationFailureButton = view.findViewById(R.id.location_failure_button)
        zipcodeEntry = view.findViewById(R.id.zipcode_entry)
        zipcodeEntryButton = view.findViewById(R.id.zipcode_entry_button)
        locationPermissionContainer = view.findViewById(R.id.location_permission_container)
        locationPermissionButton = view.findViewById(R.id.location_permission_button)

        locationPermissionButton.setOnClickListener { requestLocationPermission() }
        locationFailureButton.setOnClickListener { findLocation() }
        zipcodeEntry.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                zipcodeEntryButton.isEnabled = !TextUtils.isEmpty(p0)
            }
        })
        zipcodeEntryButton.isEnabled = !TextUtils.isEmpty(zipcodeEntry.text)
        zipcodeEntryButton.setOnClickListener {
            proceedToMainActivity(zipcodeEntry.text.toString())
            AndroidUtils.hideKeyboard(context!!, zipcodeEntry)
        }

        showLocationPermissionOrFindLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCompositeSubscription.unsubscribe()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionUtils.PERMISSION_CODE_LOCATION ->
                mUserLocationManager.permissionEvent(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun showLocationPermissionOrFindLocation() {
        mCompositeSubscription.add(mUserLocationManager.permissionObservable()
                .subscribe ({ if(it) findLocation() else showLocationPermissionButton() }, { showLocationFailure() }))

        if(!PermissionUtils.hasPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestLocationPermission()
            return
        }

        findLocation()
    }

    private fun showLocationPermissionButton() {
        locationPermissionContainer.visibility = View.VISIBLE
        locationFailureContainer.visibility = View.GONE
    }

    private fun showLocationFailure() {
        locationPermissionContainer.visibility = View.GONE
        locationFailureContainer.visibility = View.VISIBLE
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PermissionUtils.PERMISSION_CODE_LOCATION)
    }

    private fun findLocation() {
        mUserLocationManager.getLocation(context!!, { proceedToMainActivity(it) }, { showLocationFailure() })
    }

    private fun proceedToMainActivity(zipcode: String) {
        if (TextUtils.isEmpty(zipcode) || zipcode == UserLocationManager.ERROR) {
            showLocationFailure()
            return
        }
        mUserLocationManager.saveLocation(context!!, zipcode)
        startActivity(MainActivity.createIntent(context!!).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        activity?.finish()
    }

}