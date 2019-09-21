package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import personal.rowan.petfinder.model.AnimalsResponse
import personal.rowan.petfinder.network.Petfinder2Service
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
class PetMasterRepository @Inject constructor(private val mPetfinder2Service: Petfinder2Service,
                                              private val mRealmManager: RealmFavoritesManager) {

    fun getPets(type: Int, args: PetMasterArguments, page: Int, initialState: PetMasterViewState?, clear: Boolean, context: Context): Observable<PetMasterViewState> {
        val resultObservable: Observable<AnimalsResponse>
        resultObservable = when (type) {
            PetMasterFragment.TYPE_FIND -> {
                val searchArgs: PetMasterSearchArguments = args as PetMasterSearchArguments
                mPetfinder2Service.getAnimals(searchArgs.location(), searchArgs.animal(), searchArgs.size(), searchArgs.age(), searchArgs.sex(), searchArgs.breed(), page)
            }
            PetMasterFragment.TYPE_SHELTER -> {
                val shelterArgs: PetMasterShelterArguments = args as PetMasterShelterArguments
                mPetfinder2Service.getAnimalsForShelter(shelterArgs.shelterId(), shelterArgs.status(), page)
            }
            PetMasterFragment.TYPE_FAVORITE -> {
                return Observable.just(PetMasterViewState(mRealmManager.loadFavorites(), 1, 1))
            }
            else -> throw RuntimeException("invalid pet master type")
        }
        return resultObservable.map { PetMasterViewState.fromAnimalsResponse(initialState, it, clear, context) }
    }

    fun closeRealm() {
        mRealmManager.close()
    }

}