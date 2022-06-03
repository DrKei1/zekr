package al.hamdu.lil.allah.data.db

import al.hamdu.lil.allah.data.db.dao.ZekrDao
import al.hamdu.lil.allah.data.db.entity.Zekr
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Zekr::class], version = 1)
abstract class ZekrDatabase : RoomDatabase() {
    abstract fun zekrDao(): ZekrDao
}