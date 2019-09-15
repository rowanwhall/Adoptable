package personal.rowan.petfinder.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.pet.master.favorite.PetMasterFavoritesContainerFragment
import personal.rowan.petfinder.ui.pet.master.nearby.PetMasterNearbyContainerFragment
import personal.rowan.petfinder.ui.search.SearchFragment
import personal.rowan.petfinder.ui.shelter.ShelterFragment

class MainActivity : BaseActivity() {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var pager: ViewPager
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager = findViewById(R.id.main_pager)
        bottomNavigation = findViewById(R.id.main_bottom_navigation)

        pager.adapter = MainPagerAdapter(supportFragmentManager)
        pager.offscreenPageLimit = MainPagerAdapter.NUM_PAGES

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_nearby_animals -> pager.setCurrentItem(MainPagerAdapter.POSITION_NEARBY_ANIMALS, false)
                R.id.action_nearby_shelters -> pager.setCurrentItem(MainPagerAdapter.POSITION_NEARBY_SHELTERS, false)
                R.id.action_search -> pager.setCurrentItem(MainPagerAdapter.POSITION_SEARCH, false)
                R.id.action_favorites -> pager.setCurrentItem(MainPagerAdapter.POSITION_FAVORITES, false)
            }
            true
        }
    }

    private class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        companion object {
            const val NUM_PAGES = 4

            const val POSITION_NEARBY_ANIMALS = 0
            const val POSITION_NEARBY_SHELTERS = 1
            const val POSITION_SEARCH = 2
            const val POSITION_FAVORITES = 3
        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                POSITION_NEARBY_ANIMALS -> PetMasterNearbyContainerFragment.newInstance()
                POSITION_NEARBY_SHELTERS -> ShelterFragment.newInstance()
                POSITION_SEARCH -> SearchFragment.newInstance()
                POSITION_FAVORITES -> PetMasterFavoritesContainerFragment.newInstance()
                else -> throw RuntimeException("Invalid viewpager position")
            }
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }

    }

}
