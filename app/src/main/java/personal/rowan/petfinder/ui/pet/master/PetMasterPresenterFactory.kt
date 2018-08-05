package personal.rowan.petfinder.ui.pet.master

import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import personal.rowan.petfinder.network.PetfinderService
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterPresenterFactory @Inject constructor(private val mPetMasterRepository: PetMasterRepository) : PresenterFactory<PetMasterPresenter> {

    override fun create(): PetMasterPresenter {
        return PetMasterPresenter(mPetMasterRepository)
    }

}