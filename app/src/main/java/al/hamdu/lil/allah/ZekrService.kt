package al.hamdu.lil.allah

import al.hamdu.lil.allah.utils.getStringFromInputStream
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.util.*


class ZekrService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ZekrService.ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        @RequiresApi(Build.VERSION_CODES.N)
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

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.N)
     fun showToast(context: Context) {
        val aSecond = 1000
        val aMin = aSecond * 60
        val minSec = aMin * 2
        val maxSec = aMin * 4
        val nextInt = maxSec - minSec + 1

        while (true) {
            fun randomDelayAmount() = (Random().nextInt(nextInt) + minSec).toLong()
            CoroutineScope(Dispatchers.Main).launch {
                val txt = getGoodSentence(context).replace("\n" , "")
                    Toast.makeText(context, txt, Toast.LENGTH_LONG).show()
             }
                Thread.sleep(randomDelayAmount())
            }
        }
    }

    private fun getGoodSentence(context: Context) =
        getStringFromInputStream(BufferedInputStream(context.resources.openRawResource(R.raw.good_sentences))).split(
            '@'
        ).random()


