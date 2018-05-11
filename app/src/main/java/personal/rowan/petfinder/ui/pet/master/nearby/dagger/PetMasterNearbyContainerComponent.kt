package personal.rowan.petfinder.ui.pet.master.nearby.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.UserLocationModule
import personal.rowan.petfinder.ui.pet.master.nearby.PetMasterNearbyContainerFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */
@PetMasterNearbyContainerScope
@Component(modules = arrayOf(UserLocationModule::class), dependencies = arrayOf(AppComponent::class))
interface PetMasterNearbyContainerComponent {

    fun inject(petMasterNearbyContainerFragment: PetMasterNearbyContainerFragment)

    companion object {
        val injector: Action1<PetMasterNearbyContainerFragment> = Action1 { petMasterNearbyContainerFragment ->
            DaggerPetMasterNearbyContainerComponent.builder()
                    .appComponent(App.applicationComponent(petMasterNearbyContainerFragment.context!!))
                    .build()
                    .inject(petMasterNearbyContainerFragment)
        }
    }

}