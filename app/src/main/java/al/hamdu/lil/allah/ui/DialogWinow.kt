package al.hamdu.lil.allah.ui

import al.hamdu.lil.allah.data.db.entity.Zekr
import al.hamdu.lil.allah.databinding.WindowPopupBinding
import android.animation.Animator
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*


class DialogWinow(private val context: Context) {
    private var windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    private var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var binding: WindowPopupBinding = WindowPopupBinding.inflate(layoutInflater)
    private var windowLayout: View = binding.root.rootView
    private var btn: Button = binding.closeBtn
    private var params: LayoutParams

    companion object {
        val showingState = MutableLiveData(false)
    }

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
            windowLayout.alpha = 0.0f
        }else{
                params = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    TYPE_SYSTEM_ALERT,
                    LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_LAYOUT_NO_LIMITS, //or LayoutParams.FLAG_DIM_BEHIND,
                    PixelFormat.TRANSLUCENT
                )
            }
        btn.setOnClickListener { close() }
        params.gravity = Gravity.TOP
        params.verticalMargin = 0.03f
        windowLayout.let {
            it.background.alpha = 200
            it.alpha = 0f
            WindowsAnimator.animateIt(it, 1f, onStart = {}, onEnd = {},3000)
            windowLayout.setOnTouchListener(object : View.OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return false
                        }
                        MotionEvent.ACTION_UP ->
                            return false
                        MotionEvent.ACTION_MOVE -> {
                            val Xdiff = Math.round(event.rawX - initialTouchX).toFloat()
                            val Ydiff = Math.round(event.rawY - initialTouchY).toFloat()
                            params.x = initialX + Xdiff.toInt()
                            params.y = initialY + Ydiff.toInt()
                            windowManager.updateViewLayout(windowLayout, params)
                            return false
                        }
                    }
                    return false
                }
            })
        }
    }

    fun open(zekr: Zekr) {
        if (showingState.value != true){
            MainScope().launch(Dispatchers.Main) {
                try {
                    if (windowLayout.windowToken == null) {
                        if (windowLayout.parent == null) {
                            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                            val isScreenOn = pm.isInteractive
                            val flags = (PowerManager.FULL_WAKE_LOCK
                                    or PowerManager.ACQUIRE_CAUSES_WAKEUP
                                    or PowerManager.ON_AFTER_RELEASE)
                            if (!isScreenOn) {
                                pm.newWakeLock(flags, "FCMSample:full_lock").acquire(20000)
                                pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FCMSample:full_cpu_lock")
                                    .acquire(20000)
                            }
                            if (zekr.title.isNotEmpty()){
                                binding.titleTxtView.text = zekr.title
                                binding.contentTxtView.text = zekr.content
                            }else{
                                binding.titleTxtView.text = zekr.content
                                binding.contentTxtView.text = ""
                            }
                            windowManager.addView(windowLayout, params)
                            showingState.value = true
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error1", e.toString())
                    throw e
                }
            }
        }
    }

    fun close() {
        WindowsAnimator.animateIt(windowLayout, 0f, onStart = {}, onEnd = {
            showingState.value = false
            if (windowLayout.isAttachedToWindow) windowManager.removeViewImmediate(windowLayout)
        },1000)
    }
}