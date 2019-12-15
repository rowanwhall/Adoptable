package personal.rowan.petfinder.ui.pet.detail

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.network.Animal
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import personal.rowan.petfinder.ui.pet.master.PetMasterListViewState
import personal.rowan.petfinder.util.PetUtils
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailViewState : PetMasterListViewState, Parcelable {

    constructor(context: Context, animal: Animal, favorite: Boolean): super(context, animal, favorite) {
        mDescription = animal.description

        val attributes = animal.attributes
        val attributeList = mutableListOf<String>()
        if (attributes.spayedNeutered) {
            attributeList.add(context.getString(R.string.pet_detail_fixed))
        }
        if (attributes.houseTrained) {
            attributeList.add(context.getString(R.string.pet_detail_house_trained))
        }
        if (attributes.specialNeeds) {
            attributeList.add(context.getString(R.string.pet_detail_special_needs))
        }
        if (attributes.shotsCurrent) {
            attributeList.add(context.getString(R.string.pet_detail_shots))
        }
        mAttributes = if (attributeList.isNotEmpty())
            context.getString(R.string.pet_detail_attributes, StringUtils.separateWithDelimiter(attributeList, ", ")) else
            null

        val environment = animal.environment
        val goodWith = mutableListOf<String>()
        if (environment.children == true) {
            goodWith.add(context.getString(R.string.pet_detail_environment_children))
        }
        if (environment.dogs == true) {
            goodWith.add(context.getString(R.string.pet_detail_environment_dogs))
        }
        if (environment.cats == true) {
            goodWith.add(context.getString(R.string.pet_detail_environment_cats))
        }
        mEnvironment = if (goodWith.isNotEmpty())
            context.getString(R.string.pet_detail_environment, StringUtils.separateWithDelimiter(goodWith, ", ")) else
            null


        val contact = animal.contact
        mPhone = contact.phone
        mEmail = contact.email

        val address = contact.address
        val addressStrings = mutableListOf<String?>()
        addressStrings.add(address.address1)
        addressStrings.add(address.address2)
        addressStrings.add(context.getString(R.string.shelter_subtitle, address.city, address.state, address.postcode))
        mAddress = StringUtils.separateWithDelimiter(addressStrings, "\n")
        mPhotos = PetUtils.findLargePhotoUrls(animal.photos)
    }

    constructor(id: String?, photoUrl: String?, name: String?, header: String, detail: String, favorite: Boolean, description: String, phone: String?, email: String?, address: String?, attributes: String?, environment: String?):
            super(id, photoUrl, name, header, detail, favorite) {
        mDescription = description
        mPhone = phone
        mEmail = email
        mAddress = address
        mAttributes = attributes
        mEnvironment = environment
        mPhotos = ArrayList()
    }

    constructor(id: String, photoUrl: String, name: String, header: String, detail: String, favorite: Boolean, description: String, phone: String, email: String, address: String, attributes: String?, environment: String?, photos: List<String>):
            super(id, photoUrl, name, header, detail, favorite) {
        mDescription = description
        mPhone = phone
        mEmail = email
        mAddress = address
        mAttributes = attributes
        mEnvironment = environment
        mPhotos = photos
    }

    private val mDescription: String?
    private val mAttributes: String?
    private val mEnvironment: String?
    private val mPhone: String?
    private val mEmail: String?
    private val mAddress: String?
    private var mPhotos: List<String>

    fun description(): String {
        return StringUtils.emptyIfNull(mDescription)
    }

    fun attributes(): String {
        return StringUtils.emptyIfNull(mAttributes)
    }

    fun environment(): String {
        return StringUtils.emptyIfNull(mEnvironment)
    }

    fun phone(): String {
        return StringUtils.emptyIfNull(mPhone)
    }

    fun address(): String {
        return StringUtils.emptyIfNull(mAddress)
    }

    fun email(): String {
        return StringUtils.emptyIfNull(mEmail)
    }

    fun photos(): List<String> {
        return mPhotos
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetDetailViewState> = object : Parcelable.Creator<PetDetailViewState> {
            override fun createFromParcel(source: Parcel): PetDetailViewState = PetDetailViewState(source)
            override fun newArray(size: Int): Array<PetDetailViewState?> = arrayOfNulls(size)
        }

        fun fromAnimalList(context: Context, animals: List<Animal>, favoritesManager: RealmFavoritesManager): List<PetDetailViewState> {
            val viewStates: MutableList<PetDetailViewState> = ArrayList()
            for (animal in animals) {
                viewStates.add(PetDetailViewState(context, animal, favoritesManager.isFavorite(animal.id)))
            }
            return viewStates
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString() ?: "",
            source.readString() ?: "",
            source.readInt() != 0,
            source.readString() ?: "",
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()) {
        source.readStringList(mPhotos)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeString(description())
        dest?.writeString(phone())
        dest?.writeString(email())
        dest?.writeString(address())
        dest?.writeString(attributes())
        dest?.writeString(environment())
        dest?.writeStringList(photos())
    }
}