package academy.bangkit.githubuserapp.viewmodel

import academy.bangkit.githubuserapp.api.ApiConfig
import academy.bangkit.githubuserapp.model.Items
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _listFollowers = MutableLiveData<ArrayList<Items>>()
    val listFollowers: LiveData<ArrayList<Items>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLottieVisible = MutableLiveData<Boolean>()
    val isLottieVisible: LiveData<Boolean> = _isLottieVisible

    companion object {
        private const val TAG = "FollowersViewModel"
    }

    fun getFollowersUser(username: String) {
        _isLoading.value = true
        _isLottieVisible.value = false
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<Items>> {
            override fun onResponse(
                call: Call<ArrayList<Items>>,
                response: Response<ArrayList<Items>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    _isLottieVisible.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Items>>, t: Throwable) {
                _isLoading.value = false
                _isLottieVisible.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}