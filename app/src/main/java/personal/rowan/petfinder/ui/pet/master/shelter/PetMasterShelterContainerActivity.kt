package personal.rowan.petfinder.ui.pet.master.shelter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity

/**
 * Created by Rowan Hall
 */
class PetMasterShelterContainerActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    companion object {

        private const val ARG_SHELTER_ID = "PetMasterShelterContainerActivity.ShelterId"
        private const val ARG_SHELTER_NAME = "PetMasterShelterContainerActivity.ShelterName"

        fun newIntent(context: Context, shelterId: String?, shelterName: String?): Intent {
            return Intent(context, PetMasterShelterContainerActivity::class.java)
                    .putExtra(ARG_SHELTER_ID, shelterId)
                    .putExtra(ARG_SHELTER_NAME, shelterName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_master_shelter_container)
        toolbar = findViewById(R.id.pet_master_shelter_container_toolbar)
        tabLayout = findViewById(R.id.pet_master_shelter_container_tabs)
        viewPager = findViewById(R.id.pet_master_shelter_container_pager)

        setToolbar(toolbar, intent.getStringExtra(ARG_SHELTER_NAME), true)
        viewPager.setAdapter(PetMasterShelterContainerAdapter(supportFragmentManager, this, intent.getStringExtra(ARG_SHELTER_ID)))
        viewPager.offscreenPageLimit = PetMasterShelterContainerAdapter.NUM_PAGES
        tabLayout.setupWithViewPager(viewPager)
    }

}