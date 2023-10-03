package academy.bangkit.githubuserapp.repository

import academy.bangkit.githubuserapp.database.Favorite
import academy.bangkit.githubuserapp.database.FavoriteDao
import academy.bangkit.githubuserapp.database.FavoriteRoomDatabase
import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavDao.getAllFavorite()
    fun checkIsFavorited(username: String?): LiveData<List<Favorite>> =
        mFavDao.checkIsFavorited(username)

    fun addToFavorite(favorite: Favorite) {
        executorService.execute { mFavDao.addToFavorite(favorite) }
    }

    fun deleteFromFavorite(id: Int) {
        executorService.execute { mFavDao.deleteFromFavorite(id) }
    }
}