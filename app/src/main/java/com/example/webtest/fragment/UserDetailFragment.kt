package com.example.webtest.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.webtest.R
import com.example.webtest.db.AppDb
import com.example.webtest.db.UserEntity
import com.example.webtest.utils.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_userdata.*
import kotlinx.android.synthetic.main.fragment_userdata.view.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class UserDetailFragment : Fragment(),View.OnClickListener {


    //---Camera
    private val PICK_IMAGE_ID = 234
    var mCurrentPhotoPath: String? = null
    var photoFile: File? = null
    val REQUEST_TAKE_PHOTO = 1
    private var imageName = ""
    private val IMAGE_DIRECTORY = "/SurveyorApp"
    private var imagePosition = ""
    private var imgOneFile: File? = null
    private var imgTwoFile: File? = null
    private var imgThreeFile: File? = null
    private var imgFourFile: File? = null
    var isProfilePicSet = false
    val REQUEST_READ_CONTACTS = 79

    var rootView:View?=null
    var userEmail:TextView?=null
    var userName:TextView?=null

    var permissionsNeeded: java.util.ArrayList<String>? = null
    var permissionsList: java.util.ArrayList<String>? = null
    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    var path: File? = null
    var date = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_userdata, container, false)
        userEmail = rootView!!.findViewById<TextView>(R.id.tvEmail)
        userName = rootView!!.findViewById<TextView>(R.id.tvUserName)
        val userProfile = rootView!!.findViewById<CircleImageView>(R.id.ciUserProfile)
        rootView!!.btnAdd.setOnClickListener(this)
        rootView!!.iv_user_photo_edit.setOnClickListener(this)
        return rootView
    }

    fun permissions() {
        permissionsList = java.util.ArrayList()
        permissionsNeeded = java.util.ArrayList()
        /*if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Get Your Location");
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Get Your COARSE Location");
                if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("Read Your Sms for OTP");
        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add("Read Your Sms for OTP");
         */
        if (!addPermission(
                permissionsList!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) permissionsNeeded!!.add("Get Your Phone State")
        if (!addPermission(
                permissionsList!!,
                Manifest.permission.CAMERA
            )
        ) permissionsNeeded!!.add("Take Picture from CAMERA")
        if (!addPermission(
                permissionsList!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) permissionsNeeded!!.add("Get your STORAGE Data")
        if (permissionsList!!.size > 0) {
            if (permissionsNeeded!!.size > 0) {
                // Need Rationale
                var message =
                    "You need to grant access to " + permissionsNeeded!!.get(0)
                for (i in 1 until permissionsNeeded!!.size) message =
                    message + ", " + permissionsNeeded!!.get(i)
                /*showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {*/ActivityCompat.requestPermissions(
                    activity!!, permissionsList!!.toTypedArray(),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                )
                /*}
                        });*/return
            }
            ActivityCompat.requestPermissions(
                activity!!, permissionsList!!.toTypedArray(),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
            return
        }
        selectImage()
        //splash_permission();
    }

    //------------------------------------------------------------------------------
    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsList.add(permission)
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                permission
            )
        } else {
            // startSplashTimer()
        }
        return true
    }


    //------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> =
                    java.util.HashMap()
                // Initial
                //  perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                //perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                //perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);


                // Fill with results
                var i = 0
                while (i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] ==
                    PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] ==
                    PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //  startSplashTimer()
                    selectImage()
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    // startSplashTimer()
                    Toast.makeText(activity!!, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                    //  finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Choose from Gallery", "Cancel")
        val builder =
            AlertDialog.Builder(activity!!)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                dispatchTakePictureIntent()
                dialog.dismiss()
            } else if (options[item] == "Choose from Gallery") {
                val chooseImageIntent = ImagePicker.getPickImageIntent(activity)
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID)
                dialog.dismiss()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    //-----------------------------------------------------------------------
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    activity!!,
                    "com.example.webtest.fileprovider", photoFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }


    //--------------------------------------------------------------------------
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = timeStamp
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        /*val image = File.createTempFile(
                imageFileName, *//* prefix *//*
                ".png", *//* suffix *//*
                storageDir      *//* directory *//*
        )*/
        val image = File(storageDir, "$imageFileName.png")
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    //----------------------------------------------------------------------------
    /**
     *@param a set the alpha component [0..255] of the paint's color.
     * @param textSize set the paint's text size in pixel units.  Set the paint's text size. This value must be > 0
     */

    private fun mark(src: Bitmap, watermark: String, size: Float, underline: Boolean): Bitmap {
        val w = src.width
        val h = src.height
        val result = Bitmap.createBitmap(w, h, src.config)

        val resources = activity!!.resources
        val scale = resources.displayMetrics.density

        val canvas = Canvas(result)
        val s = Rect(0, 0, w, h)
        val d = Rect(0, 0, w, h)
        canvas.drawBitmap(src, s, d, null)

        val paint = Paint()

        paint.color = Color.BLACK
        paint.alpha = 150
        canvas.drawRect(0f, h - 40f, w.toFloat(), h.toFloat(), paint)

        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.alpha = 255
        //textPaint.setTextSize(size)
        textPaint.textSize = (14 * scale).toFloat()
        //textPaint.setTextAlign(Paint.Align.CENTER)
        textPaint.isAntiAlias = true
        textPaint.isUnderlineText = false

        /*paint.setColor(Color.WHITE)
        paint.setAlpha(255)
        paint.setTextSize(size)
        paint.setAntiAlias(true)
        paint.setUnderlineText(false)*/

        //val xPos = (canvas.getWidth() / 2)
        val xPos = (w - textPaint.textSize * Math.abs(watermark.length / 2)) / 2
        val yPos = ((canvas.height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2))

        /**
         * Draw the text, with origin at (x,y), using the specified paint. The origin is interpreted
         * based on the Align setting in the paint.
         *
         * @param text The text to be drawn
         * @param x The x-coordinate of the origin of the text being drawn
         * @param y The y-coordinate of the baseline of the text being drawn
         * @param paint The paint used for the text (e.g. color, size, style)
         */
        //canvas.drawText(watermark, 10.0f, h-10f, textPaint)
        //canvas.drawText(watermark, xPos, yPos, textPaint)
        canvas.drawText(watermark, xPos, h - 10f, textPaint)

        return result
    }


    //-------------------------------------------------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    PICK_IMAGE_ID -> {

                        val bitmap = ImagePicker.getImageFromResult(activity, resultCode, data)
                        path = saveImage(bitmap)
                        val contentUri = Uri.fromFile(path)
                        getFileCopy(data!!.data)
                        Glide.with(this)
                            .load(path)
                            //.placeholder(R.drawable.user)
                            // .error(R.drawable.user)
                            //.override(200, 200)
                            //.centerCrop()
                            .into(user_photo)
                        user_photo.setImageBitmap(bitmap)
                    }
                    else -> {

                        galleryAddPic()
                        setPic(user_photo, imagePosition)

                    }
                }
            } else {
                Toast.makeText(activity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.v("dip", "photo error : " + e.message)

        }
    }
    fun getFileCopy(fileUri: Uri?) {
        val parcelFileDescriptor = activity!!.contentResolver.openFileDescriptor(fileUri!!, "r", null)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val fileNewName = "UserImage" + Calendar.getInstance().time
            path = File(activity!!.cacheDir, getFileName(fileUri))
           // val outputStream = FileOutputStream(path)
            //com.google.android.gms.common.util.IOUtils.copyStream(inputStream, outputStream)
        }
    }

    fun getFileName(fileUri: Uri): String {

        var name = ""
        val returnCursor = activity!!.contentResolver.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }
    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        path = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(path)
        mediaScanIntent.data = contentUri
        activity!!.sendBroadcast(mediaScanIntent)
        Glide.with(this)
            .load(path)
            // .placeholder(R.drawable.user)
            //   .error(R.drawable.user)
            //.override(200, 200)
            //.centerCrop()
            .into(user_photo)
    }

    private fun setPic(mImageView: ImageView, imagePosition: String) {
        // Get the dimensions of the View
        val targetW = mImageView.width
        val targetH = mImageView.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        //mImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        mImageView.setImageBitmap(bitmap)
        when (imagePosition) {
            "One" -> imgOneFile = saveImage(bitmap)
            "Two" -> imgTwoFile = saveImage(bitmap)
            "Three" -> imgThreeFile = saveImage(bitmap)
            "Four" -> imgFourFile = saveImage(bitmap)
        }
        //width 0.78  and height 1.29
    }

    //-----------------------------------------------------------------------------------
    private fun saveImage(myBitmap: Bitmap): File {

        val markBitmap = mark(myBitmap, imageName, 30.0f, true)

        val bytes = ByteArrayOutputStream()
        markBitmap.compress(Bitmap.CompressFormat.JPEG, 91, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        val f = File(
            wallpaperDirectory, imageName + Calendar.getInstance()
                .timeInMillis.toString() + ".jpg")

        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                activity!!,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return f
    }
    private fun saveUser(){
        var db= Room.databaseBuilder(activity!!, AppDb::class.java,"UserDB").build()
        val thread = Thread {
            var bookEntity = UserEntity()
            bookEntity.bookId = 1
            bookEntity.userName = userName!!.text.toString().trim()
            bookEntity.userEmail = userEmail!!.text.toString().trim()

            db.bookDao().saveUserNameEmail(bookEntity,bookEntity)

            //fetch Records
            db.bookDao().getAllUser().forEach()
            {
                Log.i("Fetch Records", "Id:  : ${it.bookId}")
                Log.i("Fetch Records", "Name:  : ${it.userName}")
            }
        }
        thread.start()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnAdd->{
                saveUser()
            }
            R.id.iv_user_photo_edit->{
                permissions()
            }
        }
    }
}