package al.hamdu.lil.allah.ui

import al.hamdu.lil.allah.data.db.entity.Zekr
import al.hamdu.lil.allah.databinding.WindowPopupBinding
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class DialogWinow(private val context: Context) {
    private lateinit var params: LayoutParams
    private var windowManager: WindowManager
    private var layoutInflater: LayoutInflater
    private var layout: View
    private var btn: Button
    private var binding: WindowPopupBinding
    companion object {
        private val showingState = MutableLiveData(false)
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
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = WindowPopupBinding.inflate(layoutInflater)
        layout = binding.root.rootView
        btn = binding.closeBtn
        btn.setOnClickListener { close() }
        params.gravity = Gravity.TOP
        params.verticalMargin = 0.03f
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        layout.alpha = 0f
        layout.animate().alpha(1f).duration = 3000
        layout.background.alpha = 200

        layout.setOnTouchListener(object : View.OnTouchListener {
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
                        windowManager.updateViewLayout(layout, params)
                        return false
                    }
                }
                return false
            }
        })
    }

    fun open(zekr: Zekr) {
        if (showingState.value != true){
            MainScope().launch(Dispatchers.Main) {
                try {
                    if (layout.windowToken == null) {
                        if (layout.parent == null) {
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

                            windowManager.addView(layout, params)
                            showingState.value = true
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Error1", e.toString())
                }
            }
        }
    }

    fun close() {
        try {
            MainScope().launch(Dispatchers.Main) {
                layout.animate().alpha(0f).setListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                        Log.d("ZekrTag" , "onAnimationStart")
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        showingState.value = false
                        windowManager.removeViewImmediate(layout)
                        Log.d("ZekrTag" , "onAnimationEnd")
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        Log.d("ZekrTag" , "onAnimationCancel")
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                        Log.d("ZekrTag" , "onAnimationRepeat")
                    }

                }).duration = 1000
            }
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}