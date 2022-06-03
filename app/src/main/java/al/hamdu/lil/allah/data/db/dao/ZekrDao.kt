package al.hamdu.lil.allah.data.db.dao

import al.hamdu.lil.allah.data.db.entity.Zekr
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ZekrDao {
    @Query("SELECT * FROM zekr")
    fun getAll(): List<Zekr>

    @Query("SELECT * FROM zekr WHERE topic Like :topic")
    fun loadAllByTopic(topic: String): List<Zekr>

    @Query("SELECT * FROM zekr ORDER BY RANDOM() LIMIT 1")
    fun getRandomItem() : Zekr

    @Insert
    fun insertAll(vararg zekr: Zekr)

    @Delete
    fun delete(zekr: Zekr)
}