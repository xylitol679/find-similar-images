package xyz.xyzmu.sitest.apps.pc.duplicate.impl

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import com.ocbg.xyz.apps.router.duplicate.IDuplicateImagesService

/**
 * 主页
 * User: Tom
 * Date: 2025/8/13 19:37
 */
@AutoService(IDuplicateImagesService::class)
class DuplicateServiceImpl : IDuplicateImagesService {

    override fun getIndexIntent(context: Context): Intent {
        return xyz.xyzmu.sitest.apps.pc.duplicate.ScanResultActivity.newIntent(context)
    }

}