package al.hamdu.lil.allah.data.db.dao

import al.hamdu.lil.allah.data.db.entity.Zekr
import androidx.room.*

@Dao
interface ZekrDao {
    @Query("SELECT * FROM zekr")
    fun getAll(): List<Zekr>

    @Query("SELECT * FROM zekr WHERE topic LIKE :topic ORDER BY RANDOM() LIMIT 1 ")
    fun getRandomItemTopic(topic: String): Zekr

    @Query("SELECT * FROM zekr ORDER BY RANDOM() LIMIT 1")
    fun getRandomItem() : Zekr

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg zekr: Zekr)

    @Delete
    fun delete(zekr: Zekr)
}