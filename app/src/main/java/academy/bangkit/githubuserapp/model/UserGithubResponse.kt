package academy.bangkit.githubuserapp.model

import com.google.gson.annotations.SerializedName

data class UserGithubResponse(
    @field:SerializedName("items")
    val items: ArrayList<Items>
)

data class Items(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int
)

data class DetailUserResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("followers_url")
    val followers_url: String,

    @field:SerializedName("following_url")
    val following_url: String,

    @field:SerializedName("repos_url")
    val repos_url: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("public_repos")
    val public_repos: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("id")
    val id: Int
)