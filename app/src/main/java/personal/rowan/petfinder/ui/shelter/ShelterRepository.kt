package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.shelter.dagger.ShelterScope
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@ShelterScope
class ShelterRepository @Inject constructor(private val mPetfinderService: PetfinderService) {

    fun getShelters(location: String, offset: String, initialState: ShelterViewState?, clear: Boolean, context: Context): Observable<ShelterViewState> {
        return mPetfinderService.getNearbyShelters(location, offset)
                .map { ShelterViewState.fromShelterResult(initialState, it, clear, context) }
    }

}