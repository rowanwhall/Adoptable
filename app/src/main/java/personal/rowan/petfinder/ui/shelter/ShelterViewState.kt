package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.model.shelter.Shelter
import personal.rowan.petfinder.model.shelter.ShelterResult

/**
 * Created by Rowan Hall
 */
class ShelterViewState(val shelterData: MutableList<ShelterListViewState>, val offset: String, val allLoaded: Boolean) {

    companion object {
        fun fromShelterResult(initialState: ShelterViewState?, shelterResult: ShelterResult?, clear: Boolean, context: Context): ShelterViewState {
            val shelterData = if (!clear && initialState?.shelterData != null) initialState.shelterData else ArrayList()
            val shelters: List<Shelter>? = shelterResult?.petfinder?.shelters?.shelter
            var allLoaded = false
            if (shelters != null) {
                val listViewStates: MutableList<ShelterListViewState> = ArrayList()
                for (shelter in shelters) {
                    listViewStates.add(ShelterListViewState(context, shelter))
                }
                shelterData.addAll(listViewStates)
                allLoaded = shelters.size < 25 // Petfinder API supplies no total count in the response, making this the only way to handle pagination. Not a perfect solution as it will loop on datasets of size divisible by 25
            }
            var offset = shelterResult?.petfinder?.lastOffset?.`$t`
            offset = if (offset == null) "0" else offset
            return ShelterViewState(shelterData, offset, allLoaded)
        }
    }

}