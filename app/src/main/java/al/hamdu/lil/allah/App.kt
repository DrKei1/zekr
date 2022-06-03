package al.hamdu.lil.allah

import al.hamdu.lil.allah.data.db.ZekrDatabase
import al.hamdu.lil.allah.data.db.dao.ZekrDao
import android.app.Application
import android.content.Context
import androidx.room.Room


class App : Application() {
    var context: Context = this
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    companion object{
        private var dao : ZekrDao? = null
        fun getDao(context: Context) =
            dao ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    ZekrDatabase::class.java, "zekr"
                ).allowMainThreadQueries().build().zekrDao()
            }
    }
    override fun onCreate() {
        super.onCreate()
    }
}