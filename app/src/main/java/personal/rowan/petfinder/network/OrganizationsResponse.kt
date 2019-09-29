package personal.rowan.petfinder.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Rowan Hall
 */

data class OrganizationsResponse(val organizations: List<Organization>,
                                 val pagination: Pagination)

data class Organization(val id: String,
                        val name: String,
                        val email: String?,
                        val phone: String?,
                        val address: Address,
                        val hours: Hours,
                        val url: String?,
                        val website: String?,
                        @SerializedName("mission_statement") val missionStatement: String?,
                        val adoption: Adoption,
                        @SerializedName("social_media") val socialMedia: SocialMedia,
                        val photos: List<Photo>,
                        val distance: String?)

data class Hours(val monday: String?,
                 val tuesday: String?,
                 val wednesday: String?,
                 val thursday: String?,
                 val friday: String?,
                 val saturday: String?,
                 val sunday: String?)

data class Adoption(val policy: String?,
                    val url: String?)

data class SocialMedia(val facebook: String?,
                       val twitter: String?,
                       val youtube: String?,
                       val instagram: String?,
                       val pinterest: String?)