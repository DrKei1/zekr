package al.hamdu.lil.allah.service

import al.hamdu.lil.allah.App
import al.hamdu.lil.allah.R
import al.hamdu.lil.allah.data.db.dao.ZekrDao
import al.hamdu.lil.allah.data.db.entity.Zekr
import al.hamdu.lil.allah.ui.DialogWinow
import al.hamdu.lil.allah.data.utils.getStringFromInputStream
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.util.*


class ZekrService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ZekrService.ServiceHandler? = null
    private var dao : ZekrDao? = null
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun handleMessage(msg: Message) {
            try {
                MainScope().launch {
                    showZekr(this@ZekrService)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        dao = App.getDao(applicationContext)
        fillDataBase(applicationContext)
        startForeground(1, ForegroundNotification().buildNotification(this, " a ", dao!!.getRandomItem().content.toString()))
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null


    @RequiresApi(Build.VERSION_CODES.M)
    fun showZekr(context: Context) {
        val aSecond = 1000
        val aMin = aSecond * 60
        val minSec = aMin * 2
        val maxSec = aMin * 4
        val nextInt = maxSec - minSec + 1

        fun randomDelayAmount() = (Random().nextInt(nextInt) + minSec).toLong()

        MainScope().launch(Dispatchers.IO){
            while (true) {
                val dialogWinow = DialogWinow(this@ZekrService)
                dialogWinow.open(dao!!.getRandomItem())
                Thread.sleep(4000)
                dialogWinow.close()
                Thread.sleep(randomDelayAmount())
            }
        }
    }
    private fun fillDataBase(context: Context){
        for (i in getGoodSentences(context)){
            dao!!.insertAll(Zekr(topic = "General" , title = "" , content = i))
        }
    }
}



private fun getGoodSentences(context: Context): List<String> {
    return getStringFromInputStream(
        BufferedInputStream(
            context.resources.openRawResource(
                R.raw.good_sentences
            )
        )
    ).split(
        '@'
    ) //sentencesList[Random().nextInt(sentencesList.size)].replace("\n" , "")
}


