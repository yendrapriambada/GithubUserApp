package academy.bangkit.githubuserapp.viewmodel

import academy.bangkit.githubuserapp.api.ApiConfig
import academy.bangkit.githubuserapp.model.Items
import academy.bangkit.githubuserapp.model.UserGithubResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUserGithub = MutableLiveData<ArrayList<Items>>()
    val listUserGithub: LiveData<ArrayList<Items>> = _listUserGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _isLottieVisible = MutableLiveData<Boolean>()
    val isLottieVisible: LiveData<Boolean> = _isLottieVisible

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun searchUserGithub(query: String) {
        _isLoading.value = true
        _isLottieVisible.value = false
        val client = ApiConfig.getApiService().getSearchUserGithub(query)
        client.enqueue(object : Callback<UserGithubResponse> {
            override fun onResponse(
                call: Call<UserGithubResponse>,
                response: Response<UserGithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUserGithub.value = response.body()?.items
                } else {
                    _isEmpty.value = true
                    _isLottieVisible.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserGithubResponse>, t: Throwable) {
                _isLoading.value = false
                _isLottieVisible.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}