package com.ocbg.xyz.apps.common.util

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * 有关Gson的工具类
 * User: Tom
 * Date: 2024/10/11 16:39
 */
object MGsonUtil {

    private val gson: Gson by lazy { Gson() }

    fun <T : Parcelable> getList(jsonStr: String?, clazz: Class<T>): List<T>? {
        if (jsonStr.isNullOrEmpty()) {
            return null
        }

        try {
            val type = TypeToken.getParameterized(List::class.java, clazz).type
            return gson.fromJson<List<T>>(jsonStr, type)
        } catch (tr: Throwable) {
            return null
        }
    }

    fun toJson(src: Any): String? =
        try {
            gson.toJson(src)
        } catch (tr: Throwable) {
            null
        }

    fun toJson(src: Any, typeOfSrc: Type): String? =
        try {
            gson.toJson(src, typeOfSrc)
        } catch (tr: Throwable) {
            null
        }

    fun <T> fromJson(src: String, typeOfSrc: Type): T? =
        try {
            gson.fromJson<T>(src, typeOfSrc)
        } catch (tr: Throwable) {
            null
        }

}