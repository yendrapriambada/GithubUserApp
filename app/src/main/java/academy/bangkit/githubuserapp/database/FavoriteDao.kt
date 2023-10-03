package academy.bangkit.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE login = :login")
    fun checkIsFavorited(login: String?): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteFromFavorite(id: Int): Int
}