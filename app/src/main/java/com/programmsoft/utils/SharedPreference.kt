package com.programmsoft.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val NAME = "Aphorisms"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreference: SharedPreferences
    fun init(context: Context) {
        sharedPreference = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var mode: Int?
        get() = sharedPreference.getInt("Mode", 0)
        set(value) = sharedPreference.edit {
            it.putInt("Mode", value!!)
        }
    var lastPage: Int?
        get() = sharedPreference.getInt("PageCount", 0)
        set(value) = sharedPreference.edit {
            it.putInt("PageCount", value!!)
        }
    var lastPageItemCount: Int?
        get() = sharedPreference.getInt("lastPageItemCount", 0)
        set(value) = sharedPreference.edit {
            it.putInt("lastPageItemCount", value!!)
        }

    var uploadManager: Int?
        get() = sharedPreference.getInt("upload", 0)
        set(value) = sharedPreference.edit {
            it.putInt("upload", value!!)
        }
    var isAppFirstOpen: Int?
        get() = sharedPreference.getInt("isAppFirstOpen", 0)
        set(value) = sharedPreference.edit {
            it.putInt("isAppFirstOpen", value!!)
        }

    var isAllowNotification: Boolean
        get() = sharedPreference.getBoolean("isAllowNotification", false)
        set(value) = sharedPreference.edit {
            it.putBoolean("isAllowNotification", value)
        }

    var lastAphorismId: Long
        get() = sharedPreference.getLong("lastAphorismId", 1)
        set(value) = sharedPreference.edit {
            it.putLong("lastAphorismId", value)
        }
}