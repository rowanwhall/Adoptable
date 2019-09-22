package personal.rowan.petfinder.ui.shelter

import android.content.Context
import android.text.TextUtils
import personal.rowan.petfinder.R
import personal.rowan.petfinder.model.Organization
import personal.rowan.petfinder.util.StringUtils
import java.util.*

/**
 * Created by Rowan Hall
 */
class ShelterListViewState(context: Context, shelter: Organization) {

    private val mTitle: String?
    private val mSubtitle: String
    private val mSubtext: String

    private val mId: String?
    private val mAddress: String

    init {
        mTitle = shelter.name
        val address = shelter.address
        mSubtitle = context.getString(R.string.shelter_subtitle, address.city, address.state, address.postcode)

        val address1 = address.address1
        val address2 = address.address2
        val subtextList: MutableList<String?> = ArrayList()
        subtextList.add(address1)
        subtextList.add(address2)
        val phone = shelter.phone
        if (!TextUtils.isEmpty(phone)) {
            subtextList.add(context.getString(R.string.shelter_phone, phone))
        }
        val email = shelter.email
        if (!TextUtils.isEmpty(email)) {
            subtextList.add(context.getString(R.string.shelter_email, email))
        }
        mSubtext = StringUtils.separateWithDelimiter(subtextList, "\n")

        mId = shelter.id

        val addressList = mutableListOf<String?>()
        addressList.add(address1)
        addressList.add(address2)
        addressList.add(mSubtitle)
        mAddress = StringUtils.separateWithDelimiter(addressList, "\n")
    }

    fun title(): String? {
        return mTitle
    }

    fun subtitle(): String {
        return mSubtitle
    }

    fun subtext(): String {
        return mSubtext
    }

    fun id(): String? {
        return mId
    }

    fun address(): String {
        return mAddress
    }

}