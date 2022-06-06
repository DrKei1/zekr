package al.hamdu.lil.allah

import al.hamdu.lil.allah.data.db.ZekrDatabase
import al.hamdu.lil.allah.data.db.dao.ZekrDao
import al.hamdu.lil.allah.data.db.entity.Zekr
import al.hamdu.lil.allah.data.utils.getStringFromInputStream
import android.app.Application
import android.content.Context
import androidx.room.Room
import java.io.BufferedInputStream


class App : Application() {
    var context: Context = this
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    companion object{
        val listOfRawNames = arrayListOf(
            "salams",
            "ayat",
            "ahadith_godsi",
            "poems",
            "zekrs")
        private var dao : ZekrDao? = null
        fun getDao(context: Context) : ZekrDao =
            dao ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    ZekrDatabase::class.java, "zekr"
                ).allowMainThreadQueries().build().zekrDao()
            }.also {
                fun fillDataBase(context: Context){
                    for ( rawName in listOfRawNames){
                        for (sentence in getStringFromInputStream(BufferedInputStream(context.resources.openRawResource(context.resources.getIdentifier(rawName, "raw", context.packageName)))).split('@'))
                        {
                            it.insertAll(Zekr(topic = rawName , title = "" , content = sentence))
                        }
                    }

                }
                fillDataBase(context)
            }
    }

    override fun onCreate() {
        super.onCreate()
        getDao(this)
    }
}