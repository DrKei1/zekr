package al.hamdu.lil.allah.ui

import android.animation.Animator
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object WindowsAnimator {
    fun animateIt(dialogView: View, alpha: Float, onStart : () -> Unit, onEnd : () -> Unit, duration: Long){
        try {
            MainScope().launch(Dispatchers.Main) {
                dialogView.animate().alpha(alpha).setListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) = onStart()
                    override fun onAnimationEnd(animation: Animator?) = onEnd()
                    override fun onAnimationCancel(animation: Animator?) { Log.d("ZekrTag" , "onAnimationCancel") }
                    override fun onAnimationRepeat(animation: Animator?) { Log.d("ZekrTag" , "onAnimationRepeat") }
                }).duration = duration
            }
        }catch (e:Exception){ Toast.makeText(dialogView.context, e.toString(),Toast.LENGTH_LONG).show() }
    }
}