package academy.bangkit.githubuserapp.viewmodel

import academy.bangkit.githubuserapp.api.ApiConfig
import academy.bangkit.githubuserapp.database.Favorite
import academy.bangkit.githubuserapp.model.DetailUserResponse
import academy.bangkit.githubuserapp.repository.FavoriteRepository
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getDetailUser(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addToFavorite(id: Int, username: String?, avatarUrl: String?) {
        val user = Favorite(username, avatarUrl, id)
        mFavoriteRepository.addToFavorite(user)
    }

    fun checkIsFavorited(username: String?): LiveData<List<Favorite>> =
        mFavoriteRepository.checkIsFavorited(username)

    fun deleteFromFavorite(id: Int) {
        mFavoriteRepository.deleteFromFavorite(id)
    }
}
