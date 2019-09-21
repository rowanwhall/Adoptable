package personal.rowan.petfinder.ui.pet.master.shelter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterShelterContainerAdapter(fm: FragmentManager,
                                       private val mContext: Context,
                                       private val mShelterId: String) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val NUM_PAGES = 3

        private const val POSITION_ADOPTABLE = 0
        private const val POSITION_ADOPTED = 1
        private const val POSITION_FOUND = 2

        private const val STATUS_OPTION_ADOPTABLE = "adoptable"
        private const val STATUS_OPTION_ADOPTED = "adopted"
        private const val STATUS_OPTION_FOUND = "found"
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            POSITION_ADOPTABLE -> PetMasterFragment.newShelterInstance(mShelterId, STATUS_OPTION_ADOPTABLE)
            POSITION_ADOPTED -> PetMasterFragment.newShelterInstance(mShelterId, STATUS_OPTION_ADOPTED)
            POSITION_FOUND -> PetMasterFragment.newShelterInstance(mShelterId, STATUS_OPTION_FOUND)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            POSITION_ADOPTABLE -> mContext.getString(R.string.pet_master_tab_adoptable)
            POSITION_ADOPTED -> mContext.getString(R.string.pet_master_tab_adopted)
            POSITION_FOUND -> mContext.getString(R.string.pet_master_tab_found)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

}