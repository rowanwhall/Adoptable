package personal.rowan.petfinder.network

import personal.rowan.petfinder.BuildConfig
import personal.rowan.petfinder.model.pet.BreedListResult
import personal.rowan.petfinder.model.pet.PetResult
import personal.rowan.petfinder.model.shelter.ShelterResult
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Rowan Hall
 */

interface PetfinderService {

    @GET("pet.find")
    fun getNearbyPets(@Query("location") location: String,
                      @Query("animal") animal: String?,
                      @Query("size") size: String?,
                      @Query("age") age: String?,
                      @Query("sex") sex: String?,
                      @Query("breed") breed: String?,
                      @Query("offset") offset: String?): Observable<PetResult>

    @GET("shelter.find")
    fun getNearbyShelters(@Query("location") location: String,
                          @Query("offset") offset: String?): Observable<ShelterResult>

    @GET("shelter.getPets")
    fun getPetsForShelter(@Query("id") shelterId: String,
                          @Query("status") status: Char,
                          @Query("offset") offset: String?): Observable<PetResult>

    @GET("breed.list")
    fun getBreedList(@Query("animal") animal: String): Observable<BreedListResult>

    companion object {
        const val BASE_URL = "http://api.petfinder.com/"
        const val API_KEY = BuildConfig.PET_FINDER_API_KEY
    }

}
