package al.hamdu.lil.allah

import al.hamdu.lil.allah.utils.getStringFromInputStream
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.util.*


class ZekrService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ZekrService.ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                GlobalScope.launch {
                    showToast(this@ZekrService)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(1, MyNotification().buildNotification(this, "", getGoodSentence(this)))
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

     fun showToast(context: Context) {
        val aSecond = 1000
        val aMin = aSecond * 60
        val minSec = aMin * 2
        val maxSec = aMin * 4
        val nextInt = maxSec - minSec + 1

        while (true) {
            throw (java.lang.NullPointerException())
            fun randomDelayAmount() = (Random().nextInt(nextInt) + minSec).toLong()
            Handler(Looper.getMainLooper()).post(Runnable {
                fun View.snack(str: String) = Snackbar.make(this, str, Snackbar.LENGTH_SHORT).show()
                MainActivity().window?.decorView?.rootView?.snack("Done !!")
            })
            Thread.sleep(100)
        }
        }
    }

    private fun getGoodSentence(context: Context): String {
        val sentencesList = getStringFromInputStream(BufferedInputStream(context.resources.openRawResource(R.raw.good_sentences))).split(
            '@'
        ).filter { x -> x.length < 55 }.filter { x -> x.length > 5 }
        return sentencesList[Random().nextInt(sentencesList.size)].replace("\n" , "")
    }
