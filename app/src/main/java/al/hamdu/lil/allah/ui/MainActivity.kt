package al.hamdu.lil.allah.ui

import al.hamdu.lil.allah.App
import al.hamdu.lil.allah.databinding.ActivityMainBinding
import al.hamdu.lil.allah.service.ZekrService
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
        activityMainBinding.button.setOnClickListener {

            val intentToService = Intent(this, ZekrService()::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    val intentToPermission = Intent(
                        ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    resultLauncher.launch(intentToPermission)
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intentToService)
                    }else {
                        startService(intentToService)
                    }
                }
            }else{
                startService(intentToService)
            }
        }

        with(activityMainBinding){
            switchSalams.setOnCheckedChangeListener { buttonView, isChecked ->
                val topic = "salams"
                with(App.listOfRawNames){
                    if (!isChecked){
                        remove(topic)
                    }else{
                        if (!contains(topic)){
                            add(topic)
                        }
                    }
                }
            }
            switchAyat.setOnCheckedChangeListener { buttonView, isChecked ->
                val topic = "ayat"
                with(App.listOfRawNames){
                    if (!isChecked){
                        remove(topic)
                    }else{
                        if (!contains(topic)){
                            add(topic)
                        }
                    }
                }
            }
            switchAhadith.setOnCheckedChangeListener { buttonView, isChecked ->
                val topic = "ahadith_godsi"
                with(App.listOfRawNames){
                    if (!isChecked){
                        remove(topic)
                    }else{
                        if (!contains(topic)){
                            add(topic)
                        }
                    }
                }
            }
            switchPoems.setOnCheckedChangeListener { buttonView, isChecked ->
                val topic = "poems"
                with(App.listOfRawNames){
                    if (!isChecked){
                        remove(topic)
                    }else{
                        if (!contains(topic)){
                            add(topic)
                        }
                    }
                }
            }
            switchZekrs.setOnCheckedChangeListener { buttonView, isChecked ->
                val topic = "zekrs"
                with(App.listOfRawNames){
                    if (!isChecked){
                        remove(topic)
                    }else{
                        if (!contains(topic)){
                            add(topic)
                        }
                    }
                }
            }
        }
    }
}

