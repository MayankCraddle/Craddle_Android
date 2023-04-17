package com.cradle.utils

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.cradle.R

class PickImageDialog : DialogFragment() {

    private lateinit var parentView: View
    private lateinit var type: String

    companion object {
        var PICK_IMAGE_FROM_GALLERY = 9090
        var PICK_MULTIPE_IMAGE_FROM_GALLERY = 9091

        var PICK_IMAGE_FROM_CAMERA = 1010

        var PICK_VIDEO_FROM_GALLERY = 8080
        var PICK_VIDEO_FROM_CAMERA = 2020

        fun getInstance(type: String): PickImageDialog {
            val args = Bundle()
            args.putString("type", type)
            val fragment = PickImageDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setStyle(STYLE_NORMAL, R.style.dialog_theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentView = inflater.inflate(R.layout.pick_image_layout, container, false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black)))
//        dialog!!.window!!.setWindowAnimations(R.style.DialogAnimation)
       // ButterKnife.bind(this, parentView)
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
    }

    fun setTitle() {
        type = arguments?.getString("type")!!
        if (type.equals("video")) {
            //Choose only Video
         //   parentView.findViewById<TextView>(R.id.tv_title).text = getString(R.string.choose_video)
        } else {
            // Choose Only Image
            parentView.findViewById<TextView>(R.id.tv_title).text = getString(R.string.choose_image)
        }
    }

   // @OnClick(R.id.tv_camera)
    fun openCamera() {
        if (type.equals("video")) {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            MyHelper.fileUri = MyHelper.getOutputMediaFileUri(requireContext(),1)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MyHelper.fileUri)
            requireActivity().startActivityForResult(intent, PICK_VIDEO_FROM_CAMERA)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            MyHelper.fileUri = MyHelper.getOutputMediaFileUri(requireContext(),2)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MyHelper.fileUri)
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            requireActivity().startActivityForResult(intent, PICK_IMAGE_FROM_CAMERA)
        }
        dismiss()
    }

  //  @OnClick(R.id.tv_gallery)
    fun openGallery() {
        if (type == "video") {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            requireActivity().startActivityForResult(galleryIntent, PICK_VIDEO_FROM_GALLERY)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            requireActivity().startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY)


        }
        dismiss()

    }

  //  @OnClick(R.id.tv_close)
    fun closeDialog(){
        dismiss()
    }
}