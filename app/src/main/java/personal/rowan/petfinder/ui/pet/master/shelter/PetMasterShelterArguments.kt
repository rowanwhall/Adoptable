package personal.rowan.petfinder.ui.pet.master.shelter

import android.os.Parcel
import android.os.Parcelable
import personal.rowan.petfinder.ui.pet.master.PetMasterArguments

/**
 * Created by Rowan Hall
 */
class PetMasterShelterArguments (private val mShelterId: String, private val mStatus: String) : PetMasterArguments {

    fun shelterId(): String {
        return mShelterId
    }

    fun status(): String {
        return mStatus
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PetMasterShelterArguments> = object : Parcelable.Creator<PetMasterShelterArguments> {
            override fun createFromParcel(source: Parcel): PetMasterShelterArguments = PetMasterShelterArguments(source)
            override fun newArray(size: Int): Array<PetMasterShelterArguments?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString() ?: "", source.readString() ?: "")

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(mShelterId)
        dest?.writeString(mStatus)
    }

}