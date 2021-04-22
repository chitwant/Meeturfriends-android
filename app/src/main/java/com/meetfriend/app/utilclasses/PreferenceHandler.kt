package contractorssmart.app.utilsclasses

import android.content.Context
import android.content.SharedPreferences
import com.meetfriend.app.BuildConfig

object PreferenceHandler {
    val PREF_NAME= BuildConfig.APPLICATION_ID
    val MODE = Context.MODE_PRIVATE
    val AUTHORIZATION_TOKEN="AUTHORIZATION_TOKEN"
    val EMAIL_OR_PHONE="EMAIL_OR_PHONE"
    val FIRSTNAME="FIRSTNAME"
    val LASTNAME="LASTNAME"
    val USER_ID="USER_ID"
    val EDUCATION="EDUCATION"
    val CITY="CITY"
    val WORK="WORK"
    val GENDER="GENDER"
    val DOB="DOB"
    val RELATIONSHIP="RELATIONSHIP"
    val PROFILE_UPDATED_STATUS="PROFILE_UPDATED_STATUS"
    val PROFILE_ID="PROFILE_ID"
    val GOOGLE_ID="GOOGLE_ID"
    val COVER_PHOTO="COVER_PHOTO"
    val PROFILE_PHOTO="PROFILE_PHOTO"
    val HOBBIES="HOBBIES"
    val BASE_URL="BASE_URL"
    val MEDIA_URL="MEDIA_URL"
    val SHOW_SUGGESTION="SHOW_SUGGESTION"
    fun writeBoolean(context: Context, key: String, value: Boolean) {
        getEditor(context).putBoolean(key, value).commit()
    }

    fun readBoolean(context: Context, key: String,
                    defValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defValue)
    }

    fun writeInteger(context: Context, key: String, value: Int) {
        getEditor(context).putInt(key, value).commit()
    }

    fun readInteger(context: Context, key: String, defValue: Int): Int {
        return getPreferences(context).getInt(key, defValue)
    }

    fun writeString(context: Context, key: String, value: String) {
        getEditor(context).putString(key, value).commit()
    }

    fun readString(context: Context, key: String, defValue: String): String {
        return getPreferences(context).getString(key, defValue)!!
    }

    fun writeFloat(context: Context, key: String, value: Float) {
        getEditor(context).putFloat(key, value).commit()
    }

    fun readFloat(context: Context, key: String, defValue: Float): Float {
        return getPreferences(context).getFloat(key, defValue)
    }

    fun writeLong(context: Context, key: String, value: Long) {
        getEditor(context).putLong(key, value).commit()
    }

    fun readLong(context: Context, key: String, defValue: Long): Long {
        return getPreferences(context).getLong(key, defValue)
    }

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE)
    }

    fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun clearSharePreferences(context: Context) {
        val preferences = context.getSharedPreferences(PREF_NAME, MODE)
        preferences.edit().clear().commit()
    }
}
