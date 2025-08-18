package xyz.xyzmu.sitest.apps.pc.home.impl

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import com.ocbg.xyz.apps.router.home.IHomeService
import xyz.xyzmu.sitest.apps.pc.home.HomeActivity

/**
 * 主页
 * User: Tom
 * Date: 2025/6/13 16:02
 */
@AutoService(IHomeService::class)
class HomeServiceImpl : IHomeService {

    override fun getIndexIntent(context: Context): Intent {
        return HomeActivity.newIntent(context)
    }

}