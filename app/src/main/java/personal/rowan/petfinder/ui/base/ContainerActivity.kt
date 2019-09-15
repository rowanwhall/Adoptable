package personal.rowan.petfinder.ui.base

import android.os.Bundle
import android.widget.FrameLayout

import personal.rowan.petfinder.R

/**
 * Created by Rowan Hall
 */
abstract class ContainerActivity : BaseActivity() {

    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        container = findViewById(R.id.container)

        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, getFragment())
                    .commit()
        }
    }

    abstract protected fun getFragment(): BaseFragment

}