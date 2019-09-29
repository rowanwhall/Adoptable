package personal.rowan.petfinder.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Rowan Hall
 */

data class AnimalsResponse(val animals: List<Animal>,
                           val pagination: Pagination)

data class Animal(val id: String,
                  @SerializedName("organization_id") val organizationId: String,
                  val type: String,
                  val species: String,
                  val breeds: Breed,
                  val colors: Color,
                  val age: String,
                  val gender: String,
                  val size: String,
                  val coat: String?,
                  val attributes: Attributes,
                  val environment: Environment,
                  val name: String,
                  val description: String?,
                  val photos: List<Photo>,
                  val status: String,
                  val distance: String?,
                  val contact: Contact)

data class Breed(val primary: String,
                 val secondary: String?,
                 val mixed: Boolean,
                 val unknown: Boolean)

data class Color(val primary: String?,
                 val secondary: String?,
                 val tertiary: String?)

data class Attributes(@SerializedName("spayed_neutered") val spayedNeutered: Boolean,
                      @SerializedName("house_trained") val houseTrained: Boolean,
                      val declawed: Boolean?,
                      @SerializedName("special_needs") val specialNeeds: Boolean,
                      @SerializedName("shots_current") val shotsCurrent: Boolean)

data class Environment(val children: Boolean?,
                       val dogs: Boolean?,
                       val cats: Boolean?)

data class Photo(val small: String?,
                 val medium: String?,
                 val large: String?,
                 val full: String?)

data class Contact(val email: String,
                   val phone: String?,
                   val address: Address)

data class Address(val address1: String?,
                   val address2: String?,
                   val city: String,
                   val state: String,
                   val postcode: String,
                   val country: String)

data class Pagination(@SerializedName("count_per_page") val countPerPage: Int,
                      @SerializedName("total_count") val totalCount: Int,
                      @SerializedName("current_page") val currentPage: Int,
                      @SerializedName("total_pages") val totalPages: Int)