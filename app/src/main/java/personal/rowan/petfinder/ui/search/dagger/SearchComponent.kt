package personal.rowan.petfinder.ui.search.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.ui.search.SearchFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */
@SearchScope
@Component(modules = [PetfinderApiModule::class], dependencies = [AppComponent::class])
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)

    companion object {
        val injector: Action1<SearchFragment> = Action1 {
            DaggerSearchComponent.builder()
                    .appComponent(App.applicationComponent(it.context!!))
                    .petfinderApiModule(PetfinderApiModule(it.context!!.applicationContext))
                    .build()
                    .inject(it)
        }
    }

}