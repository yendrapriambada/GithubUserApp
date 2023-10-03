package academy.bangkit.githubuserapp.api

import academy.bangkit.githubuserapp.BuildConfig
import academy.bangkit.githubuserapp.model.DetailUserResponse
import academy.bangkit.githubuserapp.model.Items
import academy.bangkit.githubuserapp.model.UserGithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getSearchUserGithub(
        @Query("q") query: String
    ): Call<UserGithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUserDetail(
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Items>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Items>>
}