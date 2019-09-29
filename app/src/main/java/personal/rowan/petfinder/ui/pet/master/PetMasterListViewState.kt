package personal.rowan.petfinder.ui.pet.master

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.R
import personal.rowan.petfinder.network.Animal
import personal.rowan.petfinder.util.PetUtils
import personal.rowan.petfinder.util.StringUtils

/**
 * Created by Rowan Hall
 */
open class PetMasterListViewState : Parcelable {

    constructor(context: Context, animal: Animal, favorite: Boolean) {
        mId = animal.id
        mPhotoUrl = PetUtils.findFirstLargePhotoUrl(animal.photos)
        mName = animal.name
        mHeader = context.getString(R.string.pet_master_header, animal.type, animal.breeds.primary)
        mDetail = context.getString(R.string.pet_master_detail, animal.size, animal.age, animal.gender, animal.contact.address.city, animal.contact.address.state)
        mFavorite = favorite
    }

    constructor(id: String?, photoUrl: String?, name: String?, header: String, detail: String, favorite: Boolean) {
        mId = id
        mPhotoUrl = photoUrl
        mName = name
        mHeader = header
        mDetail = detail
        mFavorite = favorite
    }

    private val mId: String?
    private val mPhotoUrl: String?
    private val mName: String?
    private val mHeader: String
    private val mDetail: String
    private var mFavorite: Boolean

    fun id(): String {
        return StringUtils.emptyIfNull(mId)
    }

    fun photoUrl(): String {
        return StringUtils.emptyIfNull(mPhotoUrl)
    }

    fun name(): String {
        return StringUtils.emptyIfNull(mName)
    }

    fun header(): String {
        return mHeader
    }

    fun detail(): String {
        return mDetail
    }

    fun favorite(): Boolean {
        return mFavorite
    }

    fun toggleFavorite(favorite: Boolean) {
        mFavorite = favorite
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetMasterListViewState> = object : Parcelable.Creator<PetMasterListViewState> {
            override fun createFromParcel(source: Parcel): PetMasterListViewState = PetMasterListViewState(source)
            override fun newArray(size: Int): Array<PetMasterListViewState?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString() ?: "", source.readString() ?: "", source.readInt() != 0)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id())
        dest?.writeString(photoUrl())
        dest?.writeString(name())
        dest?.writeString(header())
        dest?.writeString(detail())
        dest?.writeInt(if(favorite()) 1 else 0)
    }
}