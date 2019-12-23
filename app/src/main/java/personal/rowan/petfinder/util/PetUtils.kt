package personal.rowan.petfinder.util

import android.content.Context
import personal.rowan.petfinder.R
import personal.rowan.petfinder.network.Animal
import personal.rowan.petfinder.network.Photo
import java.net.URLDecoder

/**
 * Created by Rowan Hall
 */
object PetUtils {

    // dog, cat, rabbit, small-furry, horse, bird, scales-fins-other, barnyard

    const val ANIMAL_OPTION_DOG = "dog"
    const val ANIMAL_OPTION_CAT = "cat"
    const val ANIMAL_OPTION_BIRD = "bird"
    const val ANIMAL_OPTION_REPTILE = "scales-fins-other"
    const val ANIMAL_OPTION_SMALL_FURRY = "small-furry"
    const val ANIMAL_OPTION_HORSE = "horse"
    const val ANIMAL_OPTION_RABBIT = "rabbit"
    const val ANIMAL_OPTION_BARNYARD = "barnyard"

    fun searchAnimalByIndex(index: Int): String? {
        return when(index) {
            1 -> ANIMAL_OPTION_DOG
            2 -> ANIMAL_OPTION_CAT
            3 -> ANIMAL_OPTION_BIRD
            4 -> ANIMAL_OPTION_REPTILE
            5 -> ANIMAL_OPTION_SMALL_FURRY
            6 -> ANIMAL_OPTION_HORSE
            7 -> ANIMAL_OPTION_RABBIT
            8 -> ANIMAL_OPTION_BARNYARD
            else -> null
        }
    }

    fun searchSizeByIndex(index: Int): String? {
        return when(index) {
            1 -> "small"
            2 -> "medium"
            3 -> "large"
            4 -> "xlarge"
            else -> null
        }
    }

    fun searchAgeByIndex(index: Int): String? {
        return when(index) {
            1 -> "baby"
            2 -> "young"
            3 -> "adult"
            4 -> "senior"
            else -> null
        }
    }

    fun petDetail(context: Context, animal: Animal): String {
        val detailList = mutableListOf(animal.size, animal.age, animal.gender, animal.contact.address.city + ", " + animal.contact.address.state)
        val attributes = animal.attributes
        if (attributes.spayedNeutered) {
            detailList.add(context.getString(R.string.pet_detail_fixed))
        }
        if (attributes.houseTrained) {
            detailList.add(context.getString(R.string.pet_detail_house_trained))
        }
        if (attributes.specialNeeds) {
            detailList.add(context.getString(R.string.pet_detail_special_needs))
        }
        if (attributes.shotsCurrent) {
            detailList.add(context.getString(R.string.pet_detail_shots))
        }
        return StringUtils.separateWithDelimiter(detailList, " - ")
    }

    fun petEnvironment(context: Context, animal: Animal): String? {
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
        return if (goodWith.isNotEmpty())
            context.getString(R.string.pet_detail_environment, StringUtils.separateWithDelimiter(goodWith, ", ")) else
            null
    }

    fun findFirstLargePhotoUrl(photos: List<Photo>): String? {
        if (photos.isNotEmpty()) {
            for(photo in photos) {
                if (photo.full != null) return decodePhotoUrl(photo.full)
            }
            for(photo in photos) {
                if (photo.large != null) return decodePhotoUrl(photo.large)
            }
            for(photo in photos) {
                if (photo.medium != null) return decodePhotoUrl(photo.medium)
            }
            for(photo in photos) {
                if (photo.small != null) return decodePhotoUrl(photo.small)
            }
        }
        return null
    }

    fun findLargePhotoUrls(photos: List<Photo>): List<String> {
        val largePhotos = mutableListOf<String>()
        for (photo in photos) {
            val largestPhoto = photo.full ?: photo.large ?: photo.medium ?: photo.small
            if (largestPhoto != null) {
                largePhotos.add(decodePhotoUrl(largestPhoto))
            }
        }
        return largePhotos
    }

    private fun decodePhotoUrl(photoUrl: String): String {
        return URLDecoder.decode(photoUrl, "UTF-8")
    }

}