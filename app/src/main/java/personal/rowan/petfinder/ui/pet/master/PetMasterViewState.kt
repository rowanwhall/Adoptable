package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import io.realm.Realm
import personal.rowan.petfinder.network.AnimalsResponse
import personal.rowan.petfinder.ui.pet.detail.PetDetailViewState
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager

/**
 * Created by Rowan Hall
 */
class PetMasterViewState(val petData: MutableList<PetDetailViewState>, val currentPage: Int, val totalPages: Int) {

    companion object {
        fun fromAnimalsResponse(initialState: PetMasterViewState?, animalsResponse: AnimalsResponse, clear: Boolean, context: Context): PetMasterViewState {
            val petData = if (!clear && initialState?.petData != null) initialState.petData else ArrayList()
            petData.addAll(PetDetailViewState.fromAnimalList(context, animalsResponse.animals, RealmFavoritesManager(Realm.getDefaultInstance()))) // Realm must be created on the thread it is being accessed from
            val pagination = animalsResponse.pagination
            return PetMasterViewState(petData, pagination.currentPage, pagination.totalPages)
        }
    }

}