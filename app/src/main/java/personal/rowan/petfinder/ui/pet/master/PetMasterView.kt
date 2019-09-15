package personal.rowan.petfinder.ui.pet.master

import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder

/**
 * Created by Rowan Hall
 */
interface PetMasterView {

    fun displayPets(listViewStates: List<PetMasterListViewState>, paginate: Boolean)

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData)

    fun showError(error: String)

    fun showProgress(progress: Boolean)

}