package personal.rowan.petfinder.ui.pet.detail

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.pet.master.favorite.RealmFavoritesManager
import personal.rowan.petfinder.model.pet.Pet
import personal.rowan.petfinder.ui.pet.master.PetMasterListViewState
import personal.rowan.petfinder.util.PetUtils
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class PetDetailViewState : PetMasterListViewState, Parcelable {

    constructor(context: Context, pet: Pet, favorite: Boolean): super(context, pet, favorite) {
        mDescription = pet.description?.`$t`

        val contact = pet.contact
        mPhone = contact?.phone?.`$t`
        mEmail = contact?.email?.`$t`

        val addressStrings: MutableList<String?> = ArrayList()
        addressStrings.add(contact?.address1?.`$t`)
        addressStrings.add(contact?.address2?.`$t`)
        addressStrings.add(context.getString(R.string.shelter_subtitle, contact?.city?.`$t`, contact?.state?.`$t`, contact?.zip?.`$t`))
        mAddress = StringUtils.separateWithDelimiter(addressStrings, "\n")
        mPhotos = PetUtils.findLargePhotoUrls(pet.media?.photos?.photo)
    }

    constructor(id: String?, photoUrl: String?, name: String?, header: String, detail: String, favorite: Boolean, description: String, phone: String?, email: String?, address: String?):
            super(id, photoUrl, name, header, detail, favorite) {
        mDescription = description
        mPhone = phone
        mEmail = email
        mAddress = address
        mPhotos = ArrayList()
    }

    constructor(id: String, photoUrl: String, name: String, header: String, detail: String, favorite: Boolean, description: String, phone: String, email: String, address: String, photos: List<String>):
            super(id, photoUrl, name, header, detail, favorite) {
        mDescription = description
        mPhone = phone
        mEmail = email
        mAddress = address
        mPhotos = photos
    }

    private val mDescription: String?
    private val mPhone: String?
    private val mEmail: String?
    private val mAddress: String?
    private var mPhotos: List<String>

    fun description(): String {
        return StringUtils.emptyIfNull(mDescription)
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

        fun fromPetList(context: Context, pets: List<Pet>?, favoritesManager: RealmFavoritesManager): List<PetDetailViewState> {
            val viewStates: MutableList<PetDetailViewState> = ArrayList()
            if (pets != null) {
                for (pet in pets) {
                    viewStates.add(PetDetailViewState(context, pet, favoritesManager.isFavorite(pet.id?.`$t`)))
                }
            }
            return viewStates
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt() != 0,
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
        dest?.writeStringList(photos())
    }
}