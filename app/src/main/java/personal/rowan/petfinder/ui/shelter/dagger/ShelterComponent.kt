package personal.rowan.petfinder.ui.shelter.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.UserLocationModule
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.ui.shelter.ShelterFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */
@ShelterScope
@Component(modules = [PetfinderApiModule::class, UserLocationModule::class], dependencies = [AppComponent::class])
interface ShelterComponent {

    fun inject(shelterFragment: ShelterFragment)

    companion object {
        val injector: Action1<ShelterFragment> = Action1 {
            DaggerShelterComponent.builder()
                    .appComponent(App.applicationComponent(it.context!!))
                    .build()
                    .inject(it)
        }
    }

}