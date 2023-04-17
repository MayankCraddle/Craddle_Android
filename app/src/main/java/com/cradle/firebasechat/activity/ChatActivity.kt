package com.cradle.firebasechat.activity

import NewConsultList
import ResponseData
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.cradle.R
import com.cradle.firebasechat.adapter.ChatAdapter
import com.cradle.firebasechat.fcm.FcmNotificationsSender
import com.cradle.firebasechat.model.ActiveConsultantModel
import com.cradle.firebasechat.model.Consersation
import com.cradle.firebasechat.model.conversations
import com.cradle.user.userActivity.UserMainActivity
import com.cradle.utils.FunctionHelper
import com.cradle.utils.SharaGoPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_box_bottom.*
import kotlinx.android.synthetic.main.chat_header_top.*
import java.io.FileInputStream
import java.io.IOException
import java.util.*


class ChatActivity  : AppCompatActivity(), View.OnClickListener {
    private var healthPref: SharaGoPref? = null
    private var newChatList: ActiveConsultantModel? = null
    private var activeConsultList: NewConsultList? = null
    private var responseData: ResponseData? = null
    private var chatAdapter: ChatAdapter? = null
    private var manager: GridLayoutManager? = null
    private var isChat = true
    private var isSend = true
    private var isImage = false
    private var isFile = false
    private var locationkey = ""
    private var emailId: String? = null
    private var queryId: String? = null
    private var patientName: String? = null
    private var physicianName: String? = null
    private var fcmKeyTo: String? = null
    private var fcmId: ArrayList<String?> = ArrayList()
    private var idFriends: ArrayList<CharSequence>? = null
    private var myId = ""
    private var consersation: Consersation? = null
    private var progress: ProgressDialog? = null
    private var fcmKey: String? = null
    private var vendorEncryptedId: String? = null
    companion object{
        var isActivePatient :String=""
        var isActivePhysician :String="" }

   /* var permissions = arrayOf(PermissionKeys.PERMISSION_CAMERA, PermissionKeys.PERMISSION_READ_EXTERNAL_STORAGE, PermissionKeys.PERMISSION_WRITE_EXTERNAL_STORAGE)
    private var marshmallowPermission: MarshmallowPermission? = null*/

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        healthPref = SharaGoPref.getInstance(this)
        val result: ActivityManager = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services: List<ActivityManager.RunningTaskInfo> = result.getRunningTasks(Int.MAX_VALUE)

   /*     marshmallowPermission = MarshmallowPermission(this)
        if (marshmallowPermission!!.isPermissionGrantedAll(PermissionKeys.REQUEST_CODE_PERMISSION_ALL, *permissions)) {
        }
        PermissionKeys.requestPermission(this)*/
       /* progress = ProgressDialog(this)
        progress?.setMessage(resources.getString(R.string.please_enter_otp_and_reset_your_password))
        progress?.setCancelable(false)
        progress?.isIndeterminate = true
        progress?.show()*/

        chat_header_name_text.text=intent.getStringExtra("vendor_name")
        if(intent.getStringExtra("Product_name")!!.isNotEmpty()){
         //   et_writeComments.hint="Hey, I would like to inquire about "+intent.getStringExtra("Product_name")
            et_writeComments.setText("Hey, I would like to inquire about "+intent.getStringExtra("Product_name"), TextView.BufferType.EDITABLE)
        }

         vendorEncryptedId   =intent.getStringExtra("id")
        consult_send_message.setOnClickListener(this)
        consult_attache_file.setOnClickListener(this)
        nchat_header_back_press.setOnClickListener(this)
        chat_header_option.setOnClickListener(this)

        consersation = Consersation()
        et_writeComments.addTextChangedListener {
            if (et_writeComments.text.toString().trim().isNotEmpty()) {
                consult_send_message.visibility = View.VISIBLE
            } else {
                consult_send_message.visibility = View.GONE
            }
        }
        findIDEmail(intent.getStringExtra("email").toString())
        chat_header_specility_text.text = newChatList?.specialityRequested
    }

    private fun setAdapter(consersation: Consersation?) {
        Log.e("consersation",consersation.toString())
        manager = GridLayoutManager(this, 1)
        chat_recyecler.layoutManager = manager
        chatAdapter = ChatAdapter(this, consersation, queryId)
        chat_recyecler.adapter = chatAdapter
        chat_recyecler.setHasFixedSize(true)
        chat_recyecler.setItemViewCacheSize(consersation?.listMessageData!!.size)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.chat_header_option -> {
               // actionChatBottomSheet()
            }
            R.id.consult_attache_file -> {
              /*  if (marshmallowPermission!!.isPermissionGrantedAll(PermissionKeys.REQUEST_CODE_PERMISSION_ALL, *permissions)) {
                  //  filterBottomSheet()
                }*/
            }
            R.id.consult_send_message -> {
                sendChatMessage("", "")

            }
            R.id.nchat_header_back_press -> {
                onBackPressed()
            }
        }
    }

    private fun sendChatMessage(file: String?, originalFileName: String?) {
        try {
            val newMessage = conversations()
            val content: String = et_writeComments.text.toString().trim { it <= ' ' }
            if (content.isNotEmpty() || file != null || file != "") {
                if (file != null && file != "") {
                    val extension = file.substring(file.lastIndexOf("."))
                    if (extension == ".jpg" || extension == ".jpeg" || extension == ".png") {
                        newMessage.type = getString(R.string.chat_image)
                    } else {
                        newMessage.type = getString(R.string.chat_file)
                    }
                    newMessage.fileName = originalFileName
                    newMessage.content = file
                    newMessage.queryId = queryId

                } else if (content.isNotEmpty()) {
                    newMessage.type = getString(R.string.chat_text)
                    newMessage.content = content
                    newMessage.queryId = queryId
                }
            //    fcmId.add(intent.getStringExtra("fcmkey"))
                // send chat notification
                if (fcmId.isNotEmpty()) {
                    val notificationSend = FcmNotificationsSender(fcmId, getString(R.string.message_send_from) + SharaGoPref.getInstance(this).getUserName("") + "", newMessage.content, emailId, queryId, SharaGoPref.getInstance(this).getServerKey(""),this@ChatActivity)
                    notificationSend.SendNotifications()
                }
                et_writeComments.setText("")
               val fromId= SharaGoPref.getInstance(this).getEncrypetedkey("").toString()
                newMessage.fromID = SharaGoPref.getInstance(this).getEncrypetedkey("").toString()
                // newMessage.toID = idFriends!![0].toString()
                newMessage.toID = intent.getStringExtra("id").toString()
                val toID= intent.getStringExtra("id").toString()
                newMessage.timestamp = System.currentTimeMillis().toString()
                Log.e("fromID",SharaGoPref.getInstance(this).getEncrypetedkey("").toString())
                Log.e("toID",intent.getStringExtra("id").toString())
                /*   newMessage.fromID = healthPref?.getGoogleID("")
                   newMessage.toID = idFriends!![0].toString()*/
                newMessage.timestamp = System.currentTimeMillis().toString()
                if (locationkey == "") {
                    locationkey = UUID.randomUUID().toString()
                    Log.e("fromID",locationkey.toString())
                    val h = HashMap<String, String>()
                    h["location"] = locationkey
                    if (isSend) {
                        sendMessage()
                    }
                    // FirebaseDatabase.getInstance().getReference().child("conversations").child(locationkey).child(UUID.randomUUID().toString()).push().setValue(h);
                    FirebaseDatabase.getInstance().reference.child("users")
                        .child(fromId).child("conversations")
                        .child(toID).setValue(h)
                    FirebaseDatabase.getInstance().reference.child("users")
                        .child(toID).child("conversations")
                        .child(fromId).setValue(h)
                }
                //FirebaseDatabase.getInstance().getReference().child("users").child(spManager.getSenderId()).child("conversations").child(idFriend.get(0).toString()).push().setValue(newMessage);
                FirebaseDatabase.getInstance().reference.child("conversations")
                    .child(locationkey)/*.child(UUID.randomUUID().toString())*/.push()
                    .setValue(newMessage)
                //update timestamp and last message into firebase
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(fromId).child("conversations")
                    .child(toID).child("lastMessage").setValue(content)

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(fromId).child("conversations")
                    .child(toID)
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(toID).child("conversations")
                    .child(fromId).child("lastMessage")
                    .setValue(content)
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(fromId).child("conversations")
                    .child(toID).child("timestamp")
                    .setValue(System.currentTimeMillis().toString())
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(toID).child("conversations")
                    .child(fromId).child("timestamp")
                    .setValue(System.currentTimeMillis().toString())

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(toID).child("conversations")
                    .child(fromId)
                    .child("metaData").child("user").child("name").setValue("vendor")

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(fromId).child("conversations")
                    .child(toID)
                    .child("metaData").child("user").child("name").setValue(SharaGoPref.getInstance(this).getUserName(""))

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(toID).child("conversations")
                    .child(fromId)
                    .child("metaData").child("user").child("name").setValue(SharaGoPref.getInstance(this).getUserName(""))

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(fromId).child("conversations")
                    .child(toID)
                    .child("metaData").child("vendor").child("name").setValue("vendor")

                FirebaseDatabase.getInstance().reference.child("users")
                    .child(toID).child("conversations")
                    .child(fromId)
                    .child("metaData").child("vendor").child("name").setValue("vendor")

            } else {
                FunctionHelper.showToastLong(this@ChatActivity, "Please type Something")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun captureImage() {
        ImagePicker.with(this)
            .cameraOnly()    //User can only capture image using Camera
            .start()
    }
    private fun circleCropLoadImage() {
        ImagePicker.with(this)
            .galleryOnly()    //User can only select image from Gallery
            .galleryMimeTypes(  //Exclude gif images
                mimeTypes = arrayOf("image/jpg", "image/jpeg"))
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       /* if (resultCode == Activity.RESULT_OK && isImage && data != null) {
            isImage = false
            val uploadFileUri: Uri = data.data!!
            val filePath = UriUtils.getPathFromUri(this, uploadFileUri)
            val imageFile = File(filePath)
            val imageZipperFile = ImageZipper(this).compressToFile(imageFile)
            uploadFileInChatAPi(imageZipperFile)

        } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC && isFile && data != null) {
            isFile = false
            val dataList = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
            if (dataList!!.isNotEmpty()) {
                for (i in dataList) {
                    MyConstants.filePath = Utility.getFilePath(this,i)
                }
                val imageFile = File(MyConstants.filePath!!)
                uploadFileInChatAPi(imageFile)
            }
        }*/
    }

    private fun findIDEmail(email11: String) {

        FirebaseDatabase.getInstance().reference.child("users")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    if (dataSnapshot.value != null) {
                        // .iterator().next().toString()
                        progress?.dismiss()
                        val email = ((dataSnapshot.value as HashMap<*, *>?))!!["emailMobile"] as String?

                        try {
                            if (dataSnapshot.key == vendorEncryptedId) {
                                fcmKey = ((dataSnapshot.value as HashMap<*, *>?))!!["fcmKey"] as String?
                                fcmId.add(fcmKey)
                                if (isChat) {
                                    isChat = false
                                    val id = dataSnapshot.key.toString()
                                    healthPref?.setGoogleID(id)
                                    val idF = ArrayList<CharSequence>()
                                    idF.add(id)
                                    idFriends = idF
                                    myId = healthPref?.getGoogleID("").toString()
                                    checkBeforAddFriend(healthPref!!.getEncrypetedkey("").toString())
                                    if (idFriends != null) {
                                        try {
                                            setAdapter(consersation)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }

                            }
                        }catch (e:Exception){
                            Log.e("email",e.toString())
                        }

                    }
                }
                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun sendMessage() {
        isSend = false
        FirebaseDatabase.getInstance().reference.child("conversations").child(locationkey)
            .addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    if (dataSnapshot.value != null) {
                        try {
                            /* String kay = ((HashMap) dataSnapshot.getValue()).keySet().toString();
                            kay = kay.replace("]","").replace("[","");
                           */
                            val mapMessage = dataSnapshot.value as HashMap<*, *>?
                            val newMessage = conversations()
                            newMessage.fromID = mapMessage!!["fromID"] as String?
                            // newMessage.idSender = spManager.getSenderId();
                            newMessage.toID = mapMessage["toID"] as String?
                            newMessage.content = mapMessage["content"] as String?
                            newMessage.timestamp = mapMessage["timestamp"] as String?
                            newMessage.type = mapMessage["type"] as String?
                            newMessage.queryId = mapMessage["queryId"] as String?
                            newMessage.fileName = mapMessage["fileName"] as String?
                            consersation?.listMessageData?.add(newMessage)
                            consersation?.listMessageData?.sort()
                            chatAdapter!!.notifyDataSetChanged()
                            manager?.scrollToPosition(consersation?.listMessageData!!.size - 1)
                        } catch (e: Exception) {
                        }
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun checkBeforAddFriend(idFriend: String) {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(healthPref?.getGoogleID("").toString()).child("conversations").child(idFriend)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    Log.d("rammmm", "" + dataSnapshot)
                    if (dataSnapshot.value != null) {
                        try {
                            // HashMap hashMap = (HashMap)dataSnapshot.getValue();
                            if (dataSnapshot.key.equals("location")) {
                                locationkey = dataSnapshot.value.toString()
                                if (isSend) {
                                    sendMessage()
                                }
                            }
                        } catch (e: Exception) {
                        }
                    } else {
                        locationkey = UUID.randomUUID().toString()
                        val h = HashMap<String, String>()
                        h["location"] = locationkey
                        FirebaseDatabase.getInstance().reference.child("users")
                            .child(healthPref?.getGoogleID("").toString()).child("conversations")
                            .child(idFriend).setValue(h)
                        FirebaseDatabase.getInstance().reference.child("users").child(idFriend)
                            .child("conversations").child(healthPref?.getGoogleID("").toString())
                            .setValue(h)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    Log.d("rammmm", "" + dataSnapshot)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d("rammmm", "" + dataSnapshot)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                    Log.d("rammmm", "" + dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("rammmm", "" + databaseError)
                }
            })
    }

  /*  private fun uploadFileInChatAPi(uri: File?) {
        if (CheckNetworkConnection.isConnection1(this, true)) {
            val progressDialog= DialogClass.showProgressDialog(this)
            val apiInterface = ApiClient.getConnection(this)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            val bodyList = ArrayList<MultipartBody.Part>()
            val requestFile = uri!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val bodyData = MultipartBody.Part.createFormData("file", uri.name, requestFile)
            bodyList.add(bodyData)
            call = apiInterface!!.uploadFileInChat(bodyList, healthPref?.getToken("")!!)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: retrofit2.Response<JsonObject>) {
                    DialogClass.hideProgressDialog(progressDialog)
                    if (response.code() in 200..209) {
                        try {
                            val jsonObject = JSONObject(response.body()!!.toString())
                            val chatFile = jsonObject.optString("file")
                            val originalFileName = jsonObject.optString("originalFilename")
                            sendChatMessage(chatFile, originalFileName)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Utility.errorBody(response, this@ChatCounsultantActivity)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    DialogClass.hideProgressDialog(progressDialog)
                    FunctionHelper.showToastLong(this@ChatCounsultantActivity, getString(R.string.Something_went_worng))
                }
            })
        }
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        isActivePhysician=""
        isActivePatient=""
        if (intent.getStringExtra("newPhysician") == "true") {
            startActivity(
                Intent(
                    this,
                    UserMainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }


}