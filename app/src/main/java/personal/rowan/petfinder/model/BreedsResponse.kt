package personal.rowan.petfinder.model

/**
 * Created by Rowan Hall
 */
data class BreedsResponse(val breeds: List<BreedChoice>)

// Note that this "Breed" object is different than the one in AnimalsResponse
data class BreedChoice(val name: String)