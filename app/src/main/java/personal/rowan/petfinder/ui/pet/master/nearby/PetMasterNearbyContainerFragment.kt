package personal.rowan.petfinder.ui.pet.master.nearby

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.BaseFragment
import personal.rowan.petfinder.ui.location.LocationActivity
import personal.rowan.petfinder.ui.pet.master.nearby.dagger.PetMasterNearbyContainerComponent
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class PetMasterNearbyContainerFragment : BaseFragment() {

    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    companion object {
        fun newInstance(): PetMasterNearbyContainerFragment {
            return PetMasterNearbyContainerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_master_nearby_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.pet_master_nearby_container_toolbar)
        tabLayout = view.findViewById(R.id.pet_master_nearby_container_tabs)
        viewPager = view.findViewById(R.id.pet_master_nearby_container_pager)

        PetMasterNearbyContainerComponent.injector.call(this)
        setToolbar(toolbar, getString(R.string.pet_master_nearby_container_title))
        setupViewPagerWithLocation(mUserLocationManager.loadSavedLocation(context!!))
    }

    private fun setupViewPagerWithLocation(location: String) {
        if (TextUtils.isEmpty(location) || location == UserLocationManager.ERROR) {
            startActivity(LocationActivity.newIntent(context!!).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            return
        }
        if (viewPager.adapter == null) {
            viewPager.adapter = PetMasterNearbyContainerAdapter(childFragmentManager, context!!, location)
        }
        viewPager.offscreenPageLimit = PetMasterNearbyContainerAdapter.NUM_PAGES
        tabLayout.setupWithViewPager(viewPager)
    }

}