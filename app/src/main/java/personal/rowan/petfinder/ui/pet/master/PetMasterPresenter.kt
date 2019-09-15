package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterPresenter(private var mRepository: PetMasterRepository) : BasePresenter<PetMasterView>(PetMasterView::class.java) {

    private var mNetworkSubscription: Subscription? = null

    private var mType: Int? = null
    private lateinit var mArguments: PetMasterArguments

    private var petResource: Resource<PetMasterViewState> = Resource.starting()

    fun initialLoad(context: Context, type: Int?, arguments: PetMasterArguments) {
        mType = type
        mArguments = arguments
        if (!petResource.hasData()) {
            loadData(context, true)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if (petResource.progress) return

        mNetworkSubscription = mRepository.getPets(mType!!, mArguments, offset(), if (petResource.hasData()) petResource.data() else null, clear, context)
                .map { Resource.success(it) }
                .startWith(Resource.progress(petResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    petResource = it
                    publish()
                }, {
                    petResource = Resource.failure(petResource, it)
                    publish()
                })
    }

    fun paginate(context: Context) {
        if (mType != PetMasterFragment.TYPE_FAVORITE
                && !petResource.progress
                && petResource.hasData()
                && !petResource.data().allLoaded) {
            loadData(context, false)
        }
    }

    fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        mView?.onPetClicked(petMasterClickData)
    }

    fun onStart(context: Context) {
        if (mType == PetMasterFragment.TYPE_FAVORITE) {
            refreshData(context)
        }
    }

    private fun offset(): String {
        return if (petResource.hasData()) petResource.data().offset else "0"
    }

    override fun publish() {
        if (mView == null) {
            return
        }

        val view = mView!!
        view.showProgress(petResource.progress)
        if (petResource.hasData()) {
            view.displayPets(petResource.data().petData, mType != PetMasterFragment.TYPE_FAVORITE)
        }
        if (petResource.hasError()) {
            view.showError(petResource.error().toString())
        }
    }

    override fun onDestroyed() {
        mNetworkSubscription?.unsubscribe()
        mRepository.closeRealm()
    }

}
