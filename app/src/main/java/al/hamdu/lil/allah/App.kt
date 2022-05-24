package al.hamdu.lil.allah

import android.app.Application
import android.content.Context
import cat.ereza.customactivityoncrash.config.CaocConfig


class App : Application() {
        override fun attachBaseContext(base: Context) {
            super.attachBaseContext(base)


            CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .showRestartButton(false) //default: true
                .logErrorOnRestart(true) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(1000) //default: 3000
                //.errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
                //.restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
                //.errorActivity(YourCustomErrorActivity::class.java) //default: null (default error activity)
                //.eventListener(YourCustomEventListener()) //default: null
                .apply()

        }
}