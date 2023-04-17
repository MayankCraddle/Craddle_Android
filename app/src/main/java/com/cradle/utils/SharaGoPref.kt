package com.cradle.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharaGoPref constructor() {

    private enum class Keys private constructor(val label: String) {
        LOGIN("LOGIN"),SOCIALMIDALOGIN("SocialLogin"),SETTOOLBARINCATE("TOOLBAR"),BACKPRESS("back_press"),SHORTDIS("shortdis"),LOGDIS("logdis"),WHICHLISTSHOW("LISTSHOW"),LIST("other"),LOGIN_TOKEN("LOGIN"),CART_ID("CARTID"),YOUTUBE_ID("youtube_id"),CART_SIZE("0"),
        CART_ITEM("cart_item"),
        ENCRYPTEDID("encryptedId"),
        SECTION_ID("section_id"),
        STOCK("stock"),UUSERID("user_id"),
        COUNTRYFLAG("countryflag"),
        USERNAME("user_name"),COUNTRYNAME("country_name"),ENCRYPTED_KEY("Encrypted_key"),TYPE("type"), ACCESS_TOKEN("access_token"),BUSINESS_NAME("business-name"), EMAIL("email_id"), SERVERKEY("server_key"), LANGUAGE("language"), TOKEN("token"),PRODUCT_CAT("cat"), FCMKEY("fcm_key"),  WHICHFRG("which_frg"), USERID(
            "user_id"),
        ROAL("roal"),FONT_FOLOR("font_color"),SITE_ID("site_id"),SITE_IMG_URL("site_image_url"), USERTYPE("user_type"), GOOGLE_ID("google_id"), PHONE("phone"), USERPROFILE("user_pic"),SEARCHTYPE("search_type");


    }

    /**
     * This Method Clear shared preference.
     */
    fun clear() {
        val editor = _pref!!.edit()
        editor.clear()
        editor.commit()
    }


    fun setSocialMediaLoggedIn(value: Boolean){
        setBoolean(Keys.SOCIALMIDALOGIN.label,value)

    }
    fun  getSocialMediaLoggedIn(value: Boolean):Boolean{
        return getBoolean(Keys.SOCIALMIDALOGIN.label, value)
    }
    fun setShortDis(value: String) {
        setString(Keys.SHORTDIS.label, value)
    }

    fun getShortDis(defaultValue: String): String? {
        return getString(Keys.SHORTDIS.label, defaultValue)
    }
    fun setLogDis(value: String) {
        setString(Keys.LOGDIS.label, value)
    }

    fun getLogDis(defaultValue: String): String? {
        return getString(Keys.LOGDIS.label, defaultValue)
    }

    fun setEncryptedId(value: String) {
        setString(Keys.ENCRYPTEDID.label, value)
    }
    fun getEncryptedId(value: String) {
        setString(Keys.ENCRYPTEDID.label, value)
    }

    fun setGoogleID(value: String) {
        setString(Keys.GOOGLE_ID.label, value)
    }


    fun getGoogleID(defaultValue: String): String? {
        return getString(Keys.GOOGLE_ID.label, defaultValue)
    }
    fun setLoginToken(value: String) {
        setString(Keys.LOGIN_TOKEN.label, value)
    }
    fun getLoginToken(defaultValue: String): String? {
        return getString(Keys.LOGIN_TOKEN.label, defaultValue)
    }

    fun setFcmKey(value: String) {
        setString(Keys.FCMKEY.label, value)
    }

    fun getFcmKey(defaultValue: String): String? {
        return getString(Keys.FCMKEY.label, defaultValue)
    }

    fun setToolBarInCate(value: String) {
        setString(Keys.SETTOOLBARINCATE.label, value)
    }

    fun getToolBarInCate(defaultValue: String): String? {
        return getString(Keys.SETTOOLBARINCATE.label, defaultValue)
    }
    fun setBACKPRESS(value: String) {
        setString(Keys.BACKPRESS.label, value)
    }

    fun getBACKPRESS(defaultValue: String): String? {
        return getString(Keys.BACKPRESS.label, defaultValue)
    }
    fun setShowList(value: String) {
        setString(Keys.WHICHLISTSHOW.label, value)
    }

    fun getShowList(defaultValue: String): String? {
        return getString(Keys.WHICHLISTSHOW.label, defaultValue)
    }
    fun setList(value: String) {
        setString(Keys.LIST.label, value)
    }

    fun getList(defaultValue: String): String? {
        return getString(Keys.LIST.label, defaultValue)
    }


    fun setVideoID(value: String) {
        setString(Keys.YOUTUBE_ID.label, value)
    }
    fun getVideoID(defaultValue: String): String? {
        return getString(Keys.YOUTUBE_ID.label, defaultValue)
    }
    fun setEmail(value: String) {
        setString(Keys.EMAIL.label, value)
    }
    fun getEmail(defaultValue: String): String? {
        return getString(Keys.EMAIL.label, defaultValue)
    }
    fun setServerKey(value: String) {
        setString(Keys.SERVERKEY.label, value)
    }
    fun getServerKey(defaultValue: String): String? {
        return getString(Keys.SERVERKEY.label, defaultValue)
    }

    fun setUserName(value: String) {
        setString(Keys.USERNAME.label, value)
    }
    fun getUserName(defaultValue: String): String? {
        return getString(Keys.USERNAME.label, defaultValue)
    }

    fun setColor(value: Int) {
        setInt(Keys.FONT_FOLOR.label, value)
    }

    fun getColor(defaultValue: Int): Int? {
        return getInt(Keys.FONT_FOLOR.label, defaultValue)
    }
    fun setCountry(value: String) {
        setString(Keys.COUNTRYNAME.label, value)
    }
    fun setUserType(value: String) {
        setString(Keys.TYPE.label, value)
    }

    fun setEncrypetedkey(value: String) {
        setString(Keys.ENCRYPTED_KEY.label, value)
    }
    fun getEncrypetedkey(defaultValue: String): String? {
        return getString(Keys.ENCRYPTED_KEY.label, defaultValue)
    }


    fun getUserType(defaultValue: String): String? {
        return getString(Keys.TYPE.label, defaultValue)
    }

    fun getCountry(defaultValue: String): String? {
        return getString(Keys.COUNTRYNAME.label, defaultValue)
    }
    fun setCountryFlag(value: String) {
        setString(Keys.COUNTRYFLAG.label, value)
    }

    fun getCountryFlag(defaultValue: String): String? {
        return getString(Keys.COUNTRYFLAG.label, defaultValue)
    }




    private fun getString(key: String?, defaultValue: String): String? {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getString(key, defaultValue)
        } else defaultValue
    }

    private fun getInt(key: String?, defaultValue: Int): Int {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getInt(key, defaultValue)
        } else defaultValue
    }

    private fun getLong(key: String?, defaultValue: Long): Long {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getLong(key, defaultValue)
        } else defaultValue
    }

    fun setInt(key: String?, value: Int) {
        if (key != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putInt(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                        TAG, "Unable to set " + key + "= " + value
                        + "in shared preference", e
                )
            }

        }
    }


    private fun setBoolean(key: String?, value: Boolean) {
        if (key != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putBoolean(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }
    }
    fun setUSERID(value: String) {
        setString(Keys.USERID.label, value)
    }
    fun getUSERID(defaultValue: String): String? {
        return getString(Keys.USERID.label, defaultValue)
    }
    fun setWhichFrag(value: String) {
        setString(Keys.WHICHFRG.label, value)
    }

    fun getWhichFrag(defaultValue: String): String? {
        return getString(Keys.WHICHFRG.label, defaultValue)
    }

    fun setStock(value: String) {
        setString(Keys.STOCK.label, value)
    }

    fun getStock(defaultValue: String): String? {
        return getString(Keys.STOCK.label, defaultValue)
    }

    fun setCartId(value: String) {
        setString(Keys.CART_ID.label, value)
    }

    fun getCartId(defaultValue: String): String? {
        return getString(Keys.CART_ID.label, defaultValue)
    }
    fun setCartSize(value: String) {
        setString(Keys.CART_SIZE.label, value)
    }

    fun getCartSize(defaultValue: String): String? {
        return getString(Keys.CART_SIZE.label, defaultValue)
    }

    fun setYotubeID(value: String) {
        setString(Keys.YOUTUBE_ID.label, value)
    }

    fun getYoutubeID(defaultValue: String): String? {
        return getString(Keys.YOUTUBE_ID.label, defaultValue)
    }

    fun setSectionId(value: String) {
        setString(Keys.SECTION_ID.label, value)
    }

    fun getSectionId(defaultValue: String): String? {
        return getString(Keys.CART_ITEM.label, defaultValue)
    }
    fun setSearchType(value: String) {
        setString(Keys.SEARCHTYPE.label, value)
    }

    fun getSearchType(defaultValue: String): String? {
        return getString(Keys.SEARCHTYPE.label, defaultValue)
    }

    fun setCartInItem(value: String) {
        setString(Keys.CART_ITEM.label, value)
    }

    fun getsetCartInItem(defaultValue: String): String? {
        return getString(Keys.SECTION_ID.label, defaultValue)
    }


    private fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return if (_pref != null && key != null && _pref!!.contains(key)) {
            _pref!!.getBoolean(key, defaultValue)
        } else defaultValue
    }

    private fun setString(key: String?, value: String?) {
        if (key != null && value != null) {
            try {
                if (_pref != null) {
                    val editor = _pref!!.edit()
                    editor.putString(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                Log.e(
                    TAG, "Unable to set " + key + "= " + value
                            + "in shared preference", e
                )
            }

        }





    }




    companion object {
        val TAG = SharaGoPref::class.java.name
        private var _pref: SharedPreferences? = null
        private var _instance: SharaGoPref? = null
        private val PRIVATE_MODE = 0
        val SHARED_PREF_NAME = "RMC_"

        fun getInstance(context: Context): SharaGoPref {
            if (_pref == null) {
                _pref = context
                    .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            }
            if (_instance == null) {
                _instance = SharaGoPref()
            }
            return _instance as SharaGoPref
        }
    }
}