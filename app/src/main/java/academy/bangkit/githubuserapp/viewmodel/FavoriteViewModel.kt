package academy.bangkit.githubuserapp.viewmodel

import academy.bangkit.githubuserapp.database.Favorite
import academy.bangkit.githubuserapp.repository.FavoriteRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavoriteUser(): LiveData<List<Favorite>> {
        return mFavoriteRepository.getAllFavorite()
    }
}