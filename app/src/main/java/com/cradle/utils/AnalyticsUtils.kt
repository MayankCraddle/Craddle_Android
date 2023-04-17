package com.cradle.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

object AnalyticsUtils {
    fun analyticReport(mContext: Context, source: String) {
      //  val mixpanel = MixpanelAPI.getInstance(mContext, Constaints.MAXPANEL_PROJECT_TOKEN)
        val props = JSONObject()
        props.put("source", source)
        props.put("Opted out of email", true)
      //  mixpanel.track(source, props)

        val firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()

        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, source)
        firebaseAnalytics.logEvent(source, bundle)
    }
}