package personal.rowan.petfinder.ui.shelter

import android.content.Context
import personal.rowan.petfinder.application.Resource
import personal.rowan.petfinder.ui.base.presenter.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Rowan Hall
 */
class ShelterPresenter(private var mRepository: ShelterRepository) : BasePresenter<ShelterView>(ShelterView::class.java) {

    private var mNetworkSubscription: Subscription? = null
    private lateinit var mLocation: String
    private var mShelterResource: Resource<ShelterViewState> = Resource.starting()

    fun initialLoad(context: Context, location: String) {
        mLocation = location
        if (!mShelterResource.hasData()) {
            loadData(context, true)
        }
    }

    fun refreshData(context: Context) {
        loadData(context, true)
    }

    private fun loadData(context: Context, clear: Boolean) {
        if (mShelterResource.progress) return

        mNetworkSubscription = mRepository.getShelters(mLocation, nextPage(), if (mShelterResource.hasData()) mShelterResource.data() else null, clear, context)
                .map { Resource.success(it) }
                .startWith(Resource.progress(mShelterResource))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                            mShelterResource = it
                            publish()
                        }, {
                            mShelterResource = Resource.failure(mShelterResource, it)
                            publish()
                        })

    }

    fun paginate(context: Context) {
        if (!mShelterResource.progress
                && mShelterResource.hasData()) {
            val data = mShelterResource.data()
            if (data.currentPage < data.totalPages) {
                loadData(context, false)
            }
        }
    }

    fun onPetsClicked(pair: Pair<String?, String?>) {
        mView?.onPetsButtonClicked(pair)
    }

    fun onDirectionsClicked(address: String) {
        mView?.onDirectionsButtonClicked(address)
    }

    private fun nextPage(): Int {
        return if (mShelterResource.hasData()) mShelterResource.data().currentPage + 1 else 1
    }

    override fun publish() {
        if (mView == null) {
            return
        }

        val view = mView!!
        view.showProgress(mShelterResource.progress)
        if (mShelterResource.hasData()) {
            view.displayShelters(mShelterResource.data().shelterData)
        }
        if (mShelterResource.hasError()) {
            view.showError(mShelterResource.error().toString())
            mShelterResource = Resource.starting()
        }
    }

    override fun onDestroyed() {
        mNetworkSubscription?.unsubscribe()
    }

}