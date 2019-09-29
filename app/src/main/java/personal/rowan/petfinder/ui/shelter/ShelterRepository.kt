package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.network.Petfinder2Service
import personal.rowan.petfinder.ui.shelter.dagger.ShelterScope
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@ShelterScope
class ShelterRepository @Inject constructor(private val mPetfinderService: Petfinder2Service) {

    fun getShelters(location: String, page: Int, initialState: ShelterViewState?, clear: Boolean, context: Context): Observable<ShelterViewState> {
        return mPetfinderService.getOrganizations(location, page)
                .map { ShelterViewState.fromOrganizationsResponse(initialState, it, clear, context) }
    }

}