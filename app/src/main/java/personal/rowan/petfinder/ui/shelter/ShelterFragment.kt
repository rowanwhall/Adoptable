package personal.rowan.petfinder.ui.shelter

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.application.UserLocationManager
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.location.LocationActivity
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterContainerActivity
import personal.rowan.petfinder.ui.shelter.dagger.ShelterComponent
import personal.rowan.petfinder.ui.shelter.recycler.ShelterAdapter
import personal.rowan.petfinder.util.IntentUtils
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class ShelterFragment : BasePresenterFragment<ShelterPresenter, ShelterView>(), ShelterView {

    @Inject
    lateinit var mPresenterFactory: ShelterPresenterFactory
    @Inject
    lateinit var mUserLocationManager: UserLocationManager

    companion object {

        fun newInstance(): ShelterFragment {
            return ShelterFragment()
        }
    }

    private lateinit var toolbar: Toolbar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var shelterList: RecyclerView
    private lateinit var emptyView: TextView

    private lateinit var mPresenter: ShelterPresenter
    private val mAdapter = ShelterAdapter(ArrayList())
    private val mViewSubscriptions = CompositeSubscription()

    override fun beforePresenterPrepared() {
        ShelterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shelter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.shelter_toolbar)
        swipeRefresh = view.findViewById(R.id.shelter_swipe_refresh)
        shelterList = view.findViewById(R.id.shelter_recycler)
        emptyView = view.findViewById(R.id.shelter_empty_message)

        setToolbar(toolbar, getString(R.string.shelter_title))
        val layoutManager = LinearLayoutManager(context!!)
        shelterList.layoutManager = layoutManager
        shelterList.adapter = mAdapter
        mViewSubscriptions.add(mAdapter.petsButtonObservable().subscribe { mPresenter.onPetsClicked(it) })
        mViewSubscriptions.add(mAdapter.directionsButtonObservable().subscribe { mPresenter.onDirectionsClicked(it) })
        mViewSubscriptions.add(RxRecyclerView.scrollEvents(shelterList)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (layoutManager.findLastVisibleItemPosition() >= mAdapter.itemCount - 1) {
                        mPresenter.paginate(context!!)
                    }
                })
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData(context!!) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(!mViewSubscriptions.isUnsubscribed) {
            mViewSubscriptions.unsubscribe()
        }
    }

    override fun onPresenterPrepared(presenter: ShelterPresenter) {
        mPresenter = presenter
        setupRecyclerWithLocation(mUserLocationManager.loadSavedLocation(context!!))
    }

    private fun setupRecyclerWithLocation(location: String) {
        dismissProgressDialog()
        if (TextUtils.isEmpty(location) || location == UserLocationManager.ERROR) {
            startActivity(LocationActivity.newIntent(context!!).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            return
        }

        val context = context
        if (context != null) {
            mPresenter.initialLoad(context, location)
        }
    }

    override fun presenterFactory(): PresenterFactory<ShelterPresenter> {
        return mPresenterFactory
    }

    override fun displayShelters(shelters: List<ShelterListViewState>) {
        if(shelters.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            mAdapter.paginateData(shelters)
        }
    }

    override fun onPetsButtonClicked(pair: Pair<String?, String?>) {
        startActivity(PetMasterShelterContainerActivity.newIntent(context!!, pair.first, pair.second))
    }

    override fun onDirectionsButtonClicked(address: String) {
        startActivity(IntentUtils.addressIntent(address))
    }

    override fun showError(error: String) {
        Log.e("sheltermastererror", error)
        showToastMessage(error)
    }

    override fun showProgress(progress: Boolean) {
        swipeRefresh.isRefreshing = progress
    }

}