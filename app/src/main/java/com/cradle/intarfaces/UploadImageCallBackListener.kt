package com.cradle.intarfaces

interface UploadImageCallBackListener {
    fun onAwsUploadSuccess(url: String, showUrl: String)
    fun onAwsUploadFailure(error: String)
}