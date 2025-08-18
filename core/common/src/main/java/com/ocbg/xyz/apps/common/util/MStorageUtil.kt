package com.ocbg.xyz.apps.common.util

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

/**
 * MMKV包装类
 * User: Tom
 * Date: 2023/12/22 16:26
 */
object MStorageUtil {

    private var kv: MMKV? = null

    fun initialize(context: Context) {
        if (kv == null) {
            val rootDir = MMKV.initialize(context)
            kv = MMKV.defaultMMKV()
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return kv?.decodeBool(key, default) ?: default
    }

    fun saveBoolean(key: String, value: Boolean = false): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getInt(key: String, default: Int = 0): Int {
        return kv?.decodeInt(key, default) ?: default
    }

    fun saveInt(key: String, value: Int = 0): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return kv?.decodeLong(key, default) ?: default
    }

    fun saveLong(key: String, value: Long = 0L): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getFloat(key: String, default: Float = 0.0F): Float {
        return kv?.decodeFloat(key, default) ?: default
    }

    fun saveFloat(key: String, value: Float = 0.0F): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getDouble(key: String, default: Double = 0.0): Double {
        return kv?.decodeDouble(key, default) ?: default
    }

    fun saveDouble(key: String, value: Double = 0.0): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getString(key: String, default: String? = null): String? {
        return kv?.decodeString(key, default) ?: default
    }

    fun saveString(key: String, value: String? = null): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getStringSet(key: String, default: Set<String>? = null): Set<String>? {
        return kv?.decodeStringSet(key, default) ?: default
    }

    fun saveStringSet(key: String, value: Set<String>? = null): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun getBytes(key: String, default: ByteArray? = null): ByteArray? {
        return kv?.decodeBytes(key, default) ?: default
    }

    fun saveBytes(key: String, value: ByteArray? = null): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun <T : Parcelable> getParcelable(key: String, cls: Class<T>, default: T? = null): T? {
        return kv?.decodeParcelable(key, cls, default) ?: default
    }

    fun <T : Parcelable> saveParcelable(key: String, value: T? = null): Boolean {
        return kv?.encode(key, value) ?: false
    }

    fun containsKey(key: String): Boolean {
        return kv?.containsKey(key) ?: false
    }

    fun allKeys(): Array<String>? {
        return kv?.allKeys()
    }

    fun allNonExpireKeys(): Array<String>? {
        return kv?.allNonExpireKeys()
    }

    fun removeValueForKey(key: String) {
        kv?.removeValueForKey(key)
    }

    fun removeValuesForKeys(keys: Array<String>) {
        kv?.removeValuesForKeys(keys)
    }

    fun clearAll() {
        kv?.clearAll()
    }

    fun <T : Parcelable> saveParcelableList(key: String, list: List<T>): Boolean {
        val gson = Gson()
        val json = gson.toJson(list)
        return kv?.encode(key, json) ?: false
    }

    fun <T : Parcelable> getParcelableList(key: String, clazz: Class<T>): List<T>? {
        val gson = Gson()
        val json = kv?.decodeString(key, null) ?: return null
        val type = TypeToken.getParameterized(List::class.java, clazz).type
        return gson.fromJson(json, type)
    }
}