package personal.rowan.petfinder.ui.search

import personal.rowan.petfinder.network.Petfinder2Service
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class SearchPresenterFactory @Inject constructor(private val mPetfinderService: Petfinder2Service) : PresenterFactory<SearchPresenter> {

    override fun create(): SearchPresenter {
        return SearchPresenter(mPetfinderService)
    }

}