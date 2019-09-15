package personal.rowan.petfinder.ui.pet.master.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout

import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterSearchContainerActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var container: FrameLayout

    companion object {

        private const val ARG_LOCATION = "PetMasterSearchContainerActivity.Location"
        private const val ARG_ANIMAL = "PetMasterSearchContainerActivity.Animal"
        private const val ARG_SIZE = "PetMasterSearchContainerActivity.Size"
        private const val ARG_AGE = "PetMasterSearchContainerActivity.Age"
        private const val ARG_SEX = "PetMasterSearchContainerActivity.Sex"
        private const val ARG_BREED = "PetMasterSearchContainerActivity.Breed"

        @JvmOverloads
        fun newIntent(context: Context,
                      location: String,
                      animal: String? = null,
                      size: String? = null,
                      age: String? = null,
                      sex: String? = null,
                      breed: String? = null): Intent {
            return Intent(context, PetMasterSearchContainerActivity::class.java)
                    .putExtra(ARG_LOCATION, location)
                    .putExtra(ARG_ANIMAL, animal)
                    .putExtra(ARG_SIZE, size)
                    .putExtra(ARG_AGE, age)
                    .putExtra(ARG_SEX, sex)
                    .putExtra(ARG_BREED, breed)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_master_search_container)
        toolbar = findViewById(R.id.pet_master_search_container_toolbar)
        container = findViewById(R.id.pet_master_search_container)

        setToolbar(toolbar, getString(R.string.pet_master_search_title), true)
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val intent = intent
            fragmentTransaction.replace(container.id,
                    PetMasterFragment.newSearchInstance(intent.getStringExtra(ARG_LOCATION),
                            intent.getStringExtra(ARG_ANIMAL),
                            intent.getStringExtra(ARG_SIZE),
                            intent.getStringExtra(ARG_AGE),
                            intent.getStringExtra(ARG_SEX),
                            intent.getStringExtra(ARG_BREED)))
            fragmentTransaction.commit()
        }
    }

}
