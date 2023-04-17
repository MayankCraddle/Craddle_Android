package com.cradle.utils

import android.Manifest
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.cradle.intarfaces.UploadImageCallBackListener

class ImageUploadUtill  {
    companion object {
        /**
         * @param type image,video
         */
        fun chooseImage(context: Context, fragmentManager: FragmentManager,type:String) {
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            PickImageDialog.getInstance(type).show(fragmentManager, MyConstants.PIC_IMAGE_DIALOG)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        fun uploadFile(path: String, context: Context, listener: UploadImageCallBackListener) {
          //  UploadAwsFiles.uploadFile(File(path), context, listener)
        }

        fun uploadImage2(path: String, context: Context,listener: UploadImageCallBackListener) {
           // UploadAwsFiles.uploadFile2(File(path), context, listener)
        }
    }
}