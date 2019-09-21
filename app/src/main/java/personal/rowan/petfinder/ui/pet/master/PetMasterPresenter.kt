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

    private var mPetResource: Resource<PetMasterViewState> = Resource.starting()

    fun initialLoad(context: Context, type: Int?, arguments: PetMasterArguments) {
        mType = type
        mArguments = arguments
        if (!mPetResource.hasData()) {
            loadData(context, true)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if (mPetResource.progress) return

        mNetworkSubscription = mRepository.getPets(mType!!, mArguments, nextPage(), if (mPetResource.hasData()) mPetResource.data() else null, clear, context)
                .map { Resource.success(it) }
                .startWith(Resource.progress(mPetResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mPetResource = it
                    publish()
                }, {
                    mPetResource = Resource.failure(mPetResource, it)
                    publish()
                })
    }

    fun paginate(context: Context) {
        if (mType != PetMasterFragment.TYPE_FAVORITE
                && !mPetResource.progress
                && mPetResource.hasData()) {
            val data = mPetResource.data()
            if (data.currentPage < data.totalPages) {
                loadData(context, false)
            }
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

    private fun nextPage(): Int {
        return if (mPetResource.hasData()) mPetResource.data().currentPage + 1 else 1
    }

    override fun publish() {
        if (mView == null) {
            return
        }

        val view = mView!!
        view.showProgress(mPetResource.progress)
        if (mPetResource.hasData()) {
            view.displayPets(mPetResource.data().petData, mType != PetMasterFragment.TYPE_FAVORITE)
        }
        if (mPetResource.hasError()) {
            view.showError(mPetResource.error().toString())
        }
    }

    override fun onDestroyed() {
        mNetworkSubscription?.unsubscribe()
        mRepository.closeRealm()
    }

}
