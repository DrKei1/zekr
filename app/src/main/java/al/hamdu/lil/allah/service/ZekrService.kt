package al.hamdu.lil.allah.service

import al.hamdu.lil.allah.App
import al.hamdu.lil.allah.data.db.dao.ZekrDao
import al.hamdu.lil.allah.ui.DialogWinow
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
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
                    showZekr()
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
        startForeground(1, ForegroundNotification().buildNotification(this, " دائم الذکر ",
            dao!!.getRandomItemTopic(App.listOfRawNames.random()).content
        ))
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null


    @RequiresApi(Build.VERSION_CODES.M)
    fun showZekr() {
        val aSecond = 1000
        val aMin = aSecond * 60
        val minSec = aMin * 2
        val maxSec = aMin * 4
        val nextInt = maxSec - minSec + 1

        fun randomDelayAmount() = (Random().nextInt(nextInt) + minSec).toLong()

        MainScope().launch(Dispatchers.IO){
            while (true) {
                val dialogWinow = DialogWinow(this@ZekrService)
                dialogWinow.open(dao!!.getRandomItemTopic(App.listOfRawNames.random()))
                Thread.sleep(5000)
                dialogWinow.close()
                Thread.sleep(randomDelayAmount())
            }
        }
    }
}


