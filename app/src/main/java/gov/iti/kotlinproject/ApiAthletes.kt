package gov.iti.kotlinproject

import io.reactivex.Observable
import retrofit2.http.GET

interface ApiAthletes {
    @GET("/MohamedWael/1406437f14e9a769a3a572a78906388f/raw/5be50e67c96c5ed1da9fe6344d2dd7befef0ba25/")
  //  fun getMovies() : Observable<MovieResponse>
    fun getAthletes() :  Observable<AthletesResponse>
}