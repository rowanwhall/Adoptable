package personal.rowan.petfinder.ui.search

import android.os.Bundle
import android.support.annotation.ArrayRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton

import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.pet.Breeds
import personal.rowan.petfinder.ui.base.presenter.BasePresenterFragment
import personal.rowan.petfinder.ui.base.presenter.PresenterFactory
import personal.rowan.petfinder.ui.pet.master.search.PetMasterSearchContainerActivity
import personal.rowan.petfinder.ui.search.dagger.SearchComponent
import personal.rowan.petfinder.util.PetUtils
import rx.Subscription
import javax.inject.Inject

/**
 * Created by Rowan Hall
 */
class SearchFragment : BasePresenterFragment<SearchPresenter, SearchView>(), SearchView {

    @Inject
    lateinit var mPresenterFactory: SearchPresenterFactory

    companion object {

        private const val TAG_BREED_DIALOG = "SearchBreedsDialog"

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private lateinit var toolbar: Toolbar
    private lateinit var locationView: TextInputEditText
    private lateinit var animalView: AppCompatSpinner
    private lateinit var sizeView: AppCompatSpinner
    private lateinit var ageView: AppCompatSpinner
    private lateinit var maleSexView: RadioButton
    private lateinit var femaleSexView: RadioButton
    private lateinit var breedButton: Button
    private lateinit var searchFab: FloatingActionButton

    private lateinit var mPresenter: SearchPresenter
    private var mDialogSubscription: Subscription? = null

    override fun beforePresenterPrepared() {
        SearchComponent.injector.call(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.search_toolbar)
        locationView = view.findViewById(R.id.search_location)
        animalView = view.findViewById(R.id.search_animal_spinner)
        sizeView = view.findViewById(R.id.search_size_spinner)
        ageView = view.findViewById(R.id.search_age_spinner)
        maleSexView = view.findViewById(R.id.search_male_radio)
        femaleSexView = view.findViewById(R.id.search_female_radio)
        breedButton = view.findViewById(R.id.search_breed_button)
        searchFab = view.findViewById(R.id.search_fab)

        setToolbar(toolbar, getString(R.string.search_title))
        animalView.adapter = generateSpinnerAdapter(R.array.search_animal_options)
        animalView.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                breedButton.text = getString(R.string.search_breed_defaut)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                breedButton.text = getString(R.string.search_breed_defaut)
            }
        }
        sizeView.adapter = generateSpinnerAdapter(R.array.search_size_options)
        ageView.adapter = generateSpinnerAdapter(R.array.search_age_options)
        RxView.clicks(breedButton).subscribe { mPresenter.loadBreeds(PetUtils.searchAnimalByIndex(animalView.selectedItemPosition)) }
        RxTextView.textChanges(locationView).subscribe { s -> enableFab(s.isNotEmpty())}
        enableFab(false)

        RxView.clicks(searchFab).subscribe { performSearch() }
        subscribeBreedsDialog()
    }

    override fun onPresenterPrepared(presenter: SearchPresenter) {
        mPresenter = presenter
    }

    override fun displayBreedsProgress() {
        showProgressDialog(getString(R.string.search_breed_progress_title), getString(R.string.search_breed_progress_detail))
    }

    override fun hideBreedsProgress() {
        dismissProgressDialog()
    }

    override fun displayBreedsEmptyAnimalError() {
        showToastMessage(getString(R.string.search_breed_empty_animal_error))
    }

    override fun displayBreeds(breeds: Breeds) {
        val breedsDialog = SearchBreedsDialogFragment.newInstance(breeds)
        breedsDialog.show(childFragmentManager, TAG_BREED_DIALOG)
        mDialogSubscription?.unsubscribe()
        mDialogSubscription = breedsDialog.breedsObservable().subscribe { s -> onBreedSelected(s) }
    }

    override fun displayBreedsLoadingError() {
        showToastMessage(getString(R.string.search_breed_error))
    }

    private fun performSearch() {
        val location = locationView.text.toString()
        val animal = PetUtils.searchAnimalByIndex(animalView.selectedItemPosition)
        val size = PetUtils.searchSizeByIndex(sizeView.selectedItemPosition)
        val age = PetUtils.searchAgeByIndex(ageView.selectedItemPosition)
        val sex = if(maleSexView.isChecked) "M" else if(femaleSexView.isChecked) "F" else null
        val breedText = breedButton.text.toString()
        val breed = if(breedText == getString(R.string.search_breed_defaut)) null else breedText
        startActivity(PetMasterSearchContainerActivity.newIntent(context!!, location, animal, size, age, sex, breed))
    }

    private fun subscribeBreedsDialog() {
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(TAG_BREED_DIALOG)
        if (fragment != null && fragment is SearchBreedsDialogFragment) {
            mDialogSubscription?.unsubscribe()
            mDialogSubscription = fragment.breedsObservable().subscribe { s -> onBreedSelected(s) }
        }
    }

    private fun onBreedSelected(breed: String) {
        breedButton.text = breed
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(TAG_BREED_DIALOG)
        if (fragment != null && fragment is DialogFragment) {
            fragment.dismiss()
        }
    }

    private fun enableFab(enabled: Boolean) {
        searchFab.isEnabled = enabled
        searchFab.alpha = if(enabled) 1f else .75f
    }

    private fun generateSpinnerAdapter(@ArrayRes options: Int): ArrayAdapter<String> {
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, resources.getStringArray(options))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    override fun presenterFactory(): PresenterFactory<SearchPresenter> {
        return mPresenterFactory
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDialogSubscription?.unsubscribe()
    }

}