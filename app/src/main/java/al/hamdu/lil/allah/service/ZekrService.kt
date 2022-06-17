package al.hamdu.lil.allah.service

import al.hamdu.lil.allah.App
import al.hamdu.lil.allah.data.db.dao.ZekrDao
import al.hamdu.lil.allah.data.db.entity.Zekr
import al.hamdu.lil.allah.ui.DialogWinow
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.log


class ZekrService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ZekrService.ServiceHandler? = null
    private var dao : ZekrDao? = null
    private var time_interval_show_dialog : String = "2" //milisecond
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun handleMessage(msg: Message) {
            try {
                MainScope().launch {
                    showDialog()
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
        dao = App.getDao(this@ZekrService)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, ForegroundNotification().buildNotification(this, " دائم الذکر ",
                dao!!.getRandomItemTopic(App.listOfRawNames.random()).content
            ))
        }

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null


    fun showDialog() {
        val aSecond = 1000
        val aMin = aSecond * 60
        val minSec = aMin * 2
        val maxSec = aMin * 4
        val nextInt = maxSec - minSec + 1


        fun randomDelayAmount() = (Random().nextInt(nextInt) + minSec).toLong()

        MainScope().launch(Dispatchers.IO){
            while (true) {
                val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
                time_interval_show_dialog=pref.getString("time_interval_show_dialog", null).toString()
                Log.e("yy",time_interval_show_dialog)

                val dialogWinow = DialogWinow(this@ZekrService)
                Log.e("zz",dao!!.getRandomItemTopic(App.listOfRawNames.random()).toString())
                var zekr1 : Zekr =dao!!.getRandomItemTopic(App.listOfRawNames.random())

                dialogWinow.open(zekr1)
                Thread.sleep((zekr1.content.length.toLong()*100))
                dialogWinow.close()

                Thread.sleep((time_interval_show_dialog.toLong()*60*1000))

            }
        }
    }
}


