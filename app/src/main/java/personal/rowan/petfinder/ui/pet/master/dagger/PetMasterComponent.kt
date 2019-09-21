package personal.rowan.petfinder.ui.pet.master.dagger

import dagger.Component
import personal.rowan.petfinder.application.App
import personal.rowan.petfinder.application.dagger.component.AppComponent
import personal.rowan.petfinder.application.dagger.module.PetfinderApiModule
import personal.rowan.petfinder.application.dagger.module.RealmModule
import personal.rowan.petfinder.ui.pet.master.PetMasterFragment
import rx.functions.Action1

/**
 * Created by Rowan Hall
 */

@PetMasterScope
@Component(modules = [PetfinderApiModule::class, RealmModule::class], dependencies = [AppComponent::class])
interface PetMasterComponent {

    fun inject(petMasterFragment: PetMasterFragment)

    companion object {
        val injector: Action1<PetMasterFragment> = Action1 {
            DaggerPetMasterComponent.builder()
                    .appComponent(App.applicationComponent(it.context!!))
                    .build()
                    .inject(it)
        }
    }

}