package personal.rowan.petfinder.ui.pet.master.nearby

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment
import personal.rowan.petfinder.util.PetUtils

/**
 * Created by Rowan Hall
 */
class PetMasterNearbyContainerAdapter(fm: FragmentManager, val mContext: Context, val mLocation: String) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val NUM_PAGES = 8

        private const val POSITION_DOG = 0
        private const val POSITION_CAT = 1
        private const val POSITION_BIRD = 2
        private const val POSITION_REPTILE = 3
        private const val POSITION_SMALL_FURRY = 4
        private const val POSITION_HORSE = 5
        private const val POSITION_RABBIT = 6
        private const val POSITION_BARNYARD = 7
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            POSITION_DOG -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_DOG)
            POSITION_CAT -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_CAT)
            POSITION_BIRD -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_BIRD)
            POSITION_REPTILE -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_REPTILE)
            POSITION_SMALL_FURRY -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_SMALL_FURRY)
            POSITION_HORSE -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_HORSE)
            POSITION_RABBIT -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_RABBIT)
            POSITION_BARNYARD -> return PetMasterFragment.newSearchInstance(mLocation, PetUtils.ANIMAL_OPTION_BARNYARD)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            POSITION_DOG -> return mContext.getString(R.string.pet_master_tab_dogs)
            POSITION_CAT -> return mContext.getString(R.string.pet_master_tab_cats)
            POSITION_BIRD -> return mContext.getString(R.string.pet_master_tab_birds)
            POSITION_REPTILE -> return mContext.getString(R.string.pet_master_tab_reptiles)
            POSITION_SMALL_FURRY -> return mContext.getString(R.string.pet_master_tab_small_furry)
            POSITION_HORSE -> return mContext.getString(R.string.pet_master_tab_horses)
            POSITION_RABBIT -> return mContext.getString(R.string.pet_master_tab_rabbits)
            POSITION_BARNYARD -> return mContext.getString(R.string.pet_master_tab_barnyard)
            else -> throw RuntimeException("Invalid viewpager position")
        }
    }

}