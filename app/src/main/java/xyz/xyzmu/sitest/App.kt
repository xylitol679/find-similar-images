package xyz.xyzmu.sitest

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.ocbg.xyz.apps.common.util.MLog
import com.ocbg.xyz.apps.common.util.MStorageUtil
import com.ocbg.xyz.apps.data.StorageUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MStorageUtil.initialize(this)
        MLog.setDebug(BuildConfig.DEBUG)
        initLanguage()
    }

    /**
     * 初始化应用语言
     */
    private fun initLanguage() {
        val savedLanguage = StorageUtil.getLanguage()
        val appLocale = LocaleListCompat.forLanguageTags(savedLanguage)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

}