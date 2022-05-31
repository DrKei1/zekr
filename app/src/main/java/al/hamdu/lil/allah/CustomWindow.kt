package al.hamdu.lil.allah

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.Button


class CustomWindow(private val context: Context) {
    private var view: View
    private lateinit var params: LayoutParams
    private var windowManager: WindowManager
    private var layoutInflater: LayoutInflater

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_APPLICATION_OVERLAY,
                LayoutParams.FLAG_NOT_FOCUSABLE
                        or LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT,
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.window_popup, null)
        view.findViewById<Button>(R.id.close_btn).setOnClickListener { close() }
        params.gravity = Gravity.TOP
        params.verticalMargin = 0.03f
        params.alpha = 1.0f

        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        view.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        //remember the initial position.
                        initialX = params.x
                        initialY = params.y


                        //get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return false
                    }
                    MotionEvent.ACTION_UP ->
                        //Add code for launching application and positioning the widget to nearest edge.
                        return false
                    MotionEvent.ACTION_MOVE -> {
                        val Xdiff = Math.round(event.rawX - initialTouchX).toFloat()
                        val Ydiff = Math.round(event.rawY - initialTouchY).toFloat()


                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + Xdiff.toInt()
                        params.y = initialY + Ydiff.toInt()

                        //Update the layout with new X & Y coordinates

                        windowManager.updateViewLayout(view, params)
                        return false
                    }
                }
                return false
            }
        })

    }

    fun open() {
        try {
            if (view.windowToken == null) {
                if (view.parent == null) {
                    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    val isScreenOn =
                        if (Build.VERSION.SDK_INT >= 20) pm.isInteractive else pm.isScreenOn
                    val flags = (PowerManager.FULL_WAKE_LOCK
                            or PowerManager.ACQUIRE_CAUSES_WAKEUP
                            or PowerManager.ON_AFTER_RELEASE)
                    if (!isScreenOn) {
                        pm.newWakeLock(flags, "FCMSample:full_lock").acquire(20000)
                        pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FCMSample:full_cpu_lock")
                            .acquire(20000)
                    }
                    windowManager.addView(view, params)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
//
//        while (true) {
//
//            runBlocking(Dispatchers.IO) {
//                withContext(Dispatchers.Main) {
//                    view.alpha += 0.1f
//                    windowManager.updateViewLayout(view, params)
//                    if (view.alpha > 0.8f) {
//                        return@withContext
//                    }
//                }
//                sleep(1000)
//            }
//        }
    }


    fun close() {
        try {
            view.invalidate()
            // (view.parent as ViewGroup).removeAllViews()
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(view)
//            val intent = Intent(context, ForegroundService::class.java)
//            intent.action = ForegroundService.ACTION_STOP_FOREGROUND_SERVICE
//            context.startService(intent)
//            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(view)
//            view.invalidate()
//            (view.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}