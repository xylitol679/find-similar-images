package com.ocbg.xyz.apps.data

import android.content.Context
import android.os.Parcelable
import com.ocbg.xyz.apps.common.util.MStorageUtil
import com.ocbg.xyz.apps.data.entity.UserEntity
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 用于存储数据
 * User: Tom
 * Date: 2023/10/20 11:29
 */
object StorageUtil {

    /** 项目根目录名 */
    private const val ROOT_DIR = "dx_translate_0"

    /** 存储临时文件的目录名 */
    private const val TEMP_DIR = "temp"

    /**
     * 获取存储临时文件的目录路径
     */
    fun getTempDirPath(context: Context): String {
        return "${context.cacheDir.path}/$ROOT_DIR/$TEMP_DIR"
    }

    /**
     * 保存是否同意条款
     */
    fun saveAcceptPolicyAndTerms(flag: Boolean) {
        MStorageUtil.saveBoolean("AcceptPolicyAndTerms", flag)
    }

    /**
     * 判断是否同意了条款
     */
    fun isAcceptPolicyAndTerms(): Boolean {
        return MStorageUtil.getBoolean("AcceptPolicyAndTerms", false)
    }

    /**
     * 保存用户登录状态
     * @param token 用户Token
     * @param login 指定用户的登录状态，true表示已登录，false反之
     */
    fun saveLoginState(token: String, login: Boolean) {
        MStorageUtil.saveBoolean("LoginState_$token", login)
    }

    /**
     * 获取用户登录状态
     * @param token 用户Token
     * @return 返回指定用户的登录状态，true表示已登录，false反之
     */
    fun getLoginState(token: String): Boolean {
        return MStorageUtil.getBoolean("LoginState_$token")
    }

    /**
     * 保存用户数据
     * @param user 用户数据
     * @return true表示成功，false表示失败
     */
    fun saveUserData(user: UserEntity?): Boolean {
        if (user == null) {
            return false
        }
        return MStorageUtil.saveParcelable("UserInfo_${user.token}", user)
    }

    /**
     * 获取用户数据
     * @param token 用户token
     * @return 用户数据，null表示失败
     */
    fun getUserData(token: String?): UserEntity? {
        return MStorageUtil.getParcelable("UserInfo_${token}", UserEntity::class.java, null)
    }

    /**
     * 删除指定用户数据
     * @param token 用户token
     * @return true表示成功，false表示失败
     */
    fun deleteUserData(token: String?): Boolean {
        if (token.isNullOrEmpty()) {
            return false
        }
        return MStorageUtil.saveParcelable("UserInfo_${token}", null)
    }

    /**
     * 保存绑定的手机号
     * @param token 用户Token
     * @param num 号码
     */
    fun savePhoneNum(token: String, num: String?) {
        MStorageUtil.saveString("PhoneNum_$token", num)
    }

    /**
     * 获取绑定的手机号
     * @param token 用户Token
     * @return 返回号码
     */
    fun getPhoneNum(token: String): String? {
        return MStorageUtil.getString("PhoneNum_$token")
    }

    /**
     * 移除key对应的值
     */
    fun removeValueForKey(key: String) {
        MStorageUtil.removeValueForKey(key)
    }

    /**
     * 移除keys对应的值
     */
    fun removeValuesForKeys(keys: MutableSet<String>) {
        MStorageUtil.removeValuesForKeys(keys.toTypedArray())
    }

    /**
     * 移除keys对应的值
     */
    fun removeValuesForKeys(keys: Array<String>) {
        MStorageUtil.removeValuesForKeys(keys)
    }

    /**
     * 获取当天指定key中保存的次数
     */
    fun getTodayCount(key: String): Int {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
        val date = sdf.format(Calendar.getInstance().time)
        val todayCount = MStorageUtil.getParcelable(key, TodayCount::class.java)
        return if (todayCount != null && todayCount.date == date) {
            todayCount.count
        } else {
            0
        }
    }

    /**
     * 保存当天指定key中的次数
     */
    fun saveTodayCount(key: String, count: Int) {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
        val date = sdf.format(Calendar.getInstance().time)
        MStorageUtil.saveParcelable(
            key,
            TodayCount(date, count)
        )
    }

    /**
     * 今日次数
     */
    @Parcelize
    data class TodayCount(
        /** 日期（yyyyMMdd） */
        val date: String = "",
        /** 次数 */
        val count: Int = 0
    ) : Parcelable

    /**
     * 保存设备信息
     */
    fun saveAlreadyRequestPermission() {
        MStorageUtil.saveBoolean("RequestPermission", true)
    }

    /**
     * 获取设备信息
     */
    fun getAlreadyRequestPermission(): Boolean {
        return MStorageUtil.getBoolean("RequestPermission", false)
    }

    fun saveLanguage(data: String?) {
        MStorageUtil.saveString("Language", data)
    }

    fun getLanguage(): String? {
        return MStorageUtil.getString("Language")
    }

}