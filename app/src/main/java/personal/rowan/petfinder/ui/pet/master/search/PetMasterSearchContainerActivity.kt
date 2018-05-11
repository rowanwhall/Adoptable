package personal.rowan.petfinder.ui.pet.master.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import kotterknife.bindView
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.base.BaseActivity
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment

/**
 * Created by Rowan Hall
 */
class PetMasterSearchContainerActivity : BaseActivity() {

    private val toolbar: Toolbar by bindView(R.id.pet_master_search_container_toolbar)
    private val container: FrameLayout by bindView(R.id.pet_master_search_container)

    companion object {

        private val ARG_LOCATION = "PetMasterSearchContainerActivity.Location"
        private val ARG_ANIMAL = "PetMasterSearchContainerActivity.Animal"
        private val ARG_SIZE = "PetMasterSearchContainerActivity.Size"
        private val ARG_AGE = "PetMasterSearchContainerActivity.Age"
        private val ARG_SEX = "PetMasterSearchContainerActivity.Sex"
        private val ARG_BREED = "PetMasterSearchContainerActivity.Breed"

        @JvmOverloads
        fun getIntent(context: Context,
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

        setToolbar(toolbar, getString(R.string.pet_master_search_title), true)
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val intent = intent
            fragmentTransaction.replace(container.id,
                    PetMasterFragment.getInstance(intent.getStringExtra(ARG_LOCATION),
                            intent.getStringExtra(ARG_ANIMAL),
                            intent.getStringExtra(ARG_SIZE),
                            intent.getStringExtra(ARG_AGE),
                            intent.getStringExtra(ARG_SEX),
                            intent.getStringExtra(ARG_BREED)))
            fragmentTransaction.commit()
        }
    }

}
