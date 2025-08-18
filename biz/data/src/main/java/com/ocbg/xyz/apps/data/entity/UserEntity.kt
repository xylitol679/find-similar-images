package com.ocbg.xyz.apps.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 用户数据类
 * User: Tom
 * Date: 2024/10/8 14:29
 *
 * @property token Token
 * @property nickName 昵称
 * @property avatar 头像
 * @property gender 性别，0:未知 1:男 2:女
 * @property age 年龄
 * @property constellation 星座 0是未知，从1至12依次类推星座
 * @property province 省
 * @property city 市
 * @property country 区
 * @property memberFlag 会员标识，true：会员，false：非会员
 * @property memberExpireDay 会员过期时间
 */
@Parcelize
data class UserEntity(
    val token: String,
    val nickName: String?,
    val avatar: String?,
    val gender: Int,
    val age: Int,
    val constellation: Int,
    val province: String?,
    val city: String?,
    val country: String?,
    val memberFlag: Boolean,
    val memberExpireDay: String?
) : Parcelable