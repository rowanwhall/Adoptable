package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.network.OrganizationsResponse

/**
 * Created by Rowan Hall
 */
class ShelterViewState(val shelterData: MutableList<ShelterListViewState>, val currentPage: Int, val totalPages: Int) {

    companion object {
        fun fromOrganizationsResponse(initialState: ShelterViewState?, organizationsResponse: OrganizationsResponse, clear: Boolean, context: Context): ShelterViewState {
            val shelterData = if (!clear && initialState?.shelterData != null) initialState.shelterData else ArrayList()
            val listViewStates = mutableListOf<ShelterListViewState>()
            for (shelter in organizationsResponse.organizations) {
                listViewStates.add(ShelterListViewState(context, shelter))
            }
            shelterData.addAll(listViewStates)
            val pagination = organizationsResponse.pagination
            return ShelterViewState(shelterData, pagination.currentPage, pagination.totalPages)
        }
    }

}