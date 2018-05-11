package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import io.realm.Realm
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewState
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager

/**
 * Created by Rowan Hall
 */
class PetMasterViewState(val petData: MutableList<PetDetailViewState>, val offset: String, val allLoaded: Boolean) {

    companion object {
        fun fromPetResult(initialState: PetMasterViewState?, petResult: PetResult?, clear: Boolean, context: Context): PetMasterViewState {
            val petData = if (!clear && initialState?.petData != null) initialState.petData else ArrayList()
            val pets: List<Pet>? = petResult?.petfinder?.pets?.pet
            var allLoaded = false
            if(pets != null) {
                petData.addAll(PetDetailViewState.fromPetList(context, pets, RealmFavoritesManager(Realm.getDefaultInstance()))) // Realm must be created on the thread it is being accessed from
                allLoaded = pets.size < 25 // Petfinder API supplies no total count in the response, making this the only way to handle pagination. Not a perfect solution as it will loop on datasets of size divisible by 25
            }
            var offset = petResult?.petfinder?.lastOffset?.`$t`
            offset = if (offset == null) "0" else offset
            return PetMasterViewState(petData, offset, allLoaded)
        }
    }

}