package personal.rowan.petfinder.ui.pet.master

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.detail.PetDetailActivity
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterComponent
import personal.rowan.petfinder.ui.pet.master.dagger.PetMasterScope
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterAdapter
import java.util.*
import javax.inject.Inject
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.Window
import personal.rowan.petfinder.ui.pet.master.favorite.PetMasterFavoriteArguments
import personal.rowan.petfinder.ui.pet.master.recycler.PetMasterViewHolder
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchArguments
import personal.rowan.petfinder.ui.pet.master.shelter.PetMasterShelterArguments
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Rowan Hall
 */

@PetMasterScope
class PetMasterFragment : BasePresenterFragment<PetMasterPresenter, PetMasterView>(), PetMasterView {

    @Inject
    lateinit var mPresenterFactory: PetMasterPresenterFactory

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var petList: RecyclerView
    private lateinit var emptyView: TextView

    private lateinit var mPresenter: PetMasterPresenter
    private val mAdapter = PetMasterAdapter(ArrayList())
    private val mViewSubscriptions: CompositeSubscription = CompositeSubscription()

    companion object {

        private const val ARG_PET_MASTER_TYPE = "PetMasterFragment.Arg.Type"
        const val TYPE_FIND = 0
        const val TYPE_SHELTER = 1
        const val TYPE_FAVORITE = 2

        private const val ARG_PET_MASTER_ARGUMENTS = "PetMasterFragment.Arg.Arguments"

        fun newSearchInstance(location: String,
                              animal: String? = "",
                              size: String? = "",
                              age: String? = "",
                              sex: String? = "",
                              breed: String? = ""): PetMasterFragment {
            val fragment = PetMasterFragment()
            val args = Bundle()
            args.putInt(ARG_PET_MASTER_TYPE, TYPE_FIND)
            args.putParcelable(ARG_PET_MASTER_ARGUMENTS, PetMasterSearchArguments(location, animal, size, age, sex, breed))
            fragment.arguments = args
            return fragment
        }

        fun newShelterInstance(shelterId: String, status: Char): PetMasterFragment {
            val fragment = PetMasterFragment()
            val args = Bundle()
            args.putInt(ARG_PET_MASTER_TYPE, TYPE_SHELTER)
            args.putParcelable(ARG_PET_MASTER_ARGUMENTS, PetMasterShelterArguments(shelterId, status))
            fragment.arguments = args
            return fragment
        }

        fun newFavoriteInstance(): PetMasterFragment {
            val fragment = PetMasterFragment()
            val args = Bundle()
            args.putInt(ARG_PET_MASTER_TYPE, TYPE_FAVORITE)
            args.putParcelable(ARG_PET_MASTER_ARGUMENTS, PetMasterFavoriteArguments())
            fragment.arguments = args
            return fragment
        }
    }

    override fun beforePresenterPrepared() {
        PetMasterComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh = view.findViewById(R.id.pet_master_swipe_refresh)
        petList = view.findViewById(R.id.pet_master_recycler)
        emptyView = view.findViewById(R.id.pet_master_empty_message)
        val context = context!!
        val layoutManager = LinearLayoutManager(context)
        petList.layoutManager = layoutManager
        petList.adapter = mAdapter
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh)
        swipeRefresh.setOnRefreshListener { mPresenter.refreshData(context) }
        mViewSubscriptions.add(RxRecyclerView.scrollEvents(petList)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (layoutManager.findLastVisibleItemPosition() >= mAdapter.itemCount - 1) {
                        mPresenter.paginate(this.context!!)
                    }
                })
        mViewSubscriptions.add(mAdapter.itemClickObservable().subscribe { clickData -> mPresenter.onPetClicked(clickData) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewSubscriptions.unsubscribe()
    }

    override fun onStart() {
        super.onStart()
        if (::mPresenter.isInitialized) mPresenter.onStart(context!!)
    }

    override fun onPresenterPrepared(presenter: PetMasterPresenter) {
        mPresenter = presenter
        val context = context!!
        val args = arguments!!
        mPresenter.initialLoad(context, args.getInt(ARG_PET_MASTER_TYPE), args.getParcelable(ARG_PET_MASTER_ARGUMENTS))
    }

    override fun presenterFactory(): PresenterFactory<PetMasterPresenter> {
        return mPresenterFactory
    }

    override fun displayPets(listViewStates: List<PetMasterListViewState>, paginate: Boolean) {
        if (listViewStates.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            mAdapter.setData(listViewStates, paginate)
        }
    }

    override fun onPetClicked(petMasterClickData: PetMasterViewHolder.PetMasterClickData) {
        // Add status and navigation bars to shared element transition to prevent overlap
        val activity = activity
        val navigationBar = activity?.findViewById<View>(android.R.id.navigationBarBackground)
        // Devices with physical buttons (i.e. Samsung) will return a null navigation bar
        val systemTransitionViews =
                if (navigationBar != null)
                    arrayOf(Pair.create(activity.findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                            Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))
                else
                    arrayOf(Pair.create(activity?.findViewById(android.R.id.statusBarBackground), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME))
        startActivity(PetDetailActivity.createIntent(context!!, petMasterClickData.viewModel()),
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, *petMasterClickData.transitionViews(), *systemTransitionViews).toBundle())
    }

    override fun showError(error: String) {
        Log.e("petmastererror", error)
        showToastMessage(error)
    }

    override fun showProgress(progress: Boolean) {
        swipeRefresh.isRefreshing = progress
    }

}