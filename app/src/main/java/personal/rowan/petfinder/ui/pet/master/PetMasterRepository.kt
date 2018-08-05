package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchArguments
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterArguments
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterRepository @Inject constructor(private val mPetfinderService: PetfinderService, private val mRealmManager: RealmFavoritesManager) {

    fun getPets(type: Int, args: PetMasterArguments, offset: String, initialState: PetMasterViewState?, clear: Boolean, context: Context): Observable<PetMasterViewState> {
        val resultObservable: Observable<PetResult>
        when (type) {
            PetMasterFragment.TYPE_FIND -> {
                val searchArgs: PetMasterSearchArguments = args as PetMasterSearchArguments
                resultObservable = mPetfinderService.getNearbyPets(searchArgs.location(), searchArgs.animal(), searchArgs.size(), searchArgs.age(), searchArgs.size(), searchArgs.breed(), offset)
            }
            PetMasterFragment.TYPE_SHELTER -> {
                val shelterArgs: PetMasterShelterArguments = args as PetMasterShelterArguments
                resultObservable = mPetfinderService.getPetsForShelter(shelterArgs.shelterId(), shelterArgs.status(), offset)
            }
            PetMasterFragment.TYPE_FAVORITE -> {
                return Observable.just(PetMasterViewState(mRealmManager.loadFavorites(), "0", true))
            }
            else -> throw RuntimeException("invalid pet master type")
        }
        return resultObservable.map { PetMasterViewState.fromPetResult(initialState, it, clear, context) }
    }

    fun closeRealm() {
        mRealmManager.close()
    }

}