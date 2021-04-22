package com.meetfriend.app.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.addpost.AddPostResponse
import com.meetfriend.app.responseclasses.friends.Data
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.adapters.PostImagesAdapter
import com.meetfriend.app.ui.adapters.TagPeopleAdapter
import com.meetfriend.app.ui.fragments.profile.ProfileFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.AppConstants
import com.meetfriend.app.utilclasses.AppConstants.POST_PIC_QUALITY
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.viewmodal.HomeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import kotlinx.android.synthetic.main.fragment_add_story_post.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class AddStoryPostFragment : BaseFragment(), PostImagesAdapter.AdapterCallback,
    TagPeopleAdapter.AdapterCallback {
    //  private var imageList: ArrayList<com.esafirm.imagepicker.model.Image>? = ArrayList()
    private var postImagesAdapter: PostImagesAdapter? = null
    private var postPrivacySpinnerValue = ""
    private var filePickerRequest = 1
    private var privacy = 1

    private var AUTOCOMPLETE_REQUEST_CODE = 100
    private var homeViewModal: HomeViewModal? = null
    private var friendsList: ArrayList<Data>? = ArrayList()
    private var baseUrl = ""
    private var taggedPeopleList: ArrayList<Data>? = ArrayList()
    private var selectedLocation = ""
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    var dialog: Dialog? = null
    private val arrayList: ArrayList<Uri>? = ArrayList()
    val picsList: ArrayList<String>? = ArrayList()
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_story_post, parent, false)
    }

    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val ExtstorePermission: Int = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission: Int = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: ArrayList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (ExtstorePermission !== PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), listPermissionsNeeded
                    .toArray(arrayOfNulls<String>(listPermissionsNeeded.size)),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        showLoading(false)
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
        postImagesAdapter = PostImagesAdapter(arrayList!!, this, requireActivity(), "add")
        recyclerViewPhotos.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = postImagesAdapter

        }
        ivGallery.setOnClickListener {
            if (arrayList!!.size > 4) {
                CommonMethods.showSnackbarMessageWithoutColor(
                    requireActivity(),
                    "You can select upto 5 pictures/videos"
                )
                mAdd.visibility = View.GONE
                return@setOnClickListener
            }
            FilePickerBuilder.instance
                .setMaxCount(1)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                .pickPhoto(this, 1)
            /*ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .single() // single mode
                .showCamera(true)
                .includeVideo(true)
                .includeAnimation(true)
                // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .start()*/
        }
        mAdd.setOnClickListener {
            if (arrayList!!.size > 4) {
                CommonMethods.showSnackbarMessageWithoutColor(
                    requireActivity(),
                    "You can select upto 5 pictures/videos"
                )
                mAdd.visibility = View.GONE
                return@setOnClickListener
            }
            FilePickerBuilder.instance
                .setMaxCount(1)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                .pickPhoto(this, 1)
            /*ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .single() // single mode
                .showCamera(true)
                .includeVideo(true)
                .includeAnimation(true)
                // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .start()*/
        }
        tvPostStory.setOnClickListener {
            validations()

        }
        ivCloseIcon.setOnClickListener {
            requireActivity().onBackPressed()
        }
        ivAddLocation.setOnClickListener {
            val fields: List<Place.Field> =
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(requireActivity())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
        ivTagPeople.setOnClickListener {
            showTagPeopleDialog()
        }
        setData()
        initialObservers()
        getFriendsListApi()
    }

    private fun showTagPeopleDialog() {
        if (dialog == null) {
            dialog = Dialog(requireContext(), R.style.full_screen_dialog)
            //  dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            /* dialog!!.getWindow()!!
                 .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)*/
            dialog!!.setContentView(R.layout.tag_people_dialog)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.setCancelable(true)
        }


        dialog!!.show()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        dialog!!.getWindow()!!.setLayout((6 * width) / 7, RelativeLayout.LayoutParams.WRAP_CONTENT);
        val ivClose: ImageView = dialog!!.findViewById(R.id.ivClose) as ImageView
        val tvNoFriends: TextView = dialog!!.findViewById(R.id.tvNoFriends) as TextView
        ivClose.setOnClickListener {
            dialog!!.dismiss()
        }
        val tagPeopleRV: com.meetfriend.app.utilclasses.RecyclerViewEmptySupport =
            dialog!!.findViewById(R.id.tagPeopleRV) as com.meetfriend.app.utilclasses.RecyclerViewEmptySupport
        tagPeopleRV.setHasFixedSize(true)
        tagPeopleRV.setLayoutManager(LinearLayoutManager(context))
        tagPeopleRV.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        tagPeopleRV.setEmptyView(tvNoFriends)
        //tagPeopleRV.addItemDecoration(SimpleDividerItemDecoration(context, R.drawable.divider))

        val rvAdapter = TagPeopleAdapter(friendsList!!, this, requireActivity(), baseUrl, "tag")
        tagPeopleRV.setAdapter(rvAdapter)
    }

    private fun initialObservers() {
        homeViewModal?.friendsListResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        baseUrl = it.media_url
                        friendsList = it.result.data
                        updateText()
                        if (checkAndRequestPermissions(requireActivity())) {
                        }
                    } else {
                        CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }

            }

        })
        homeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        homeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        homeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    private fun validations() {
        if (etStoryText.text.toString().trim().equals("")) {
            // etStoryText.error = "Write post here"
            //CommonMethods.showToastMessageAtTop(requireActivity(), "Write Your Post Here is empty")
            // return
        }
        etStoryText.error = null
        hideKeyboard()
        hideKeyboard(requireActivity())
        /*  if (arrayList!!.size < 1) {
              CommonMethods.showSnackbarMessageWithoutColor(
                  requireActivity(),
                  "Select media to add story"
              )
          }*/
        //addPostApi()
        addStoryApi()
    }

    private fun getFriendsListApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.friendsList(mHashMap)

    }

    private fun setData() {
        tvUsername.text = PreferenceHandler.readString(
            requireActivity(),
            PreferenceHandler.FIRSTNAME,
            ""
        ) + " " + PreferenceHandler.readString(requireActivity(), PreferenceHandler.LASTNAME, "")
        CommonMethods.setUserImage(ivUserPicture, getProfilePic())

        postPrivacySpinner.setOnClickListener(View.OnClickListener {
            showDialogbox(postPrivacySpinner)
        })
    }

    private fun showDialogbox(postPrivacySpinner: AppCompatTextView?) {
        val popup = PopupMenu(requireContext(), postPrivacySpinner!!)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.getMenuInflater()
            .inflate(R.menu.popup_menu, popup.getMenu())

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.publics -> {
                    postPrivacySpinner.setText(item.title)
               privacy=1
                } R.id.friend ->
            {
                postPrivacySpinner.setText(item.title)
                privacy=2
            }
                R.id.fof ->
                {
                    postPrivacySpinner.setText(item.title)
                    privacy=3
                }
            }
            true
        })
        popup.show()

    }

    fun getCommonSeperatedString(actionObjects: ArrayList<Data>): String? {
        if (taggedPeopleList!!.size < 1) {
            return ""
        }
        val sb = StringBuffer()
        for (actionObject in actionObjects) {
            sb.append(actionObject.friend_id).append(",")
        }
        sb.deleteCharAt(sb.lastIndexOf(","))
        return sb.toString()
    }

    private fun addPostApi() {
        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (postPrivacySpinnerValue.equals("Public", true)) {
            builder.addFormDataPart("privacy", "1")
        } else if (postPrivacySpinnerValue.equals("Friends", true)) {
            builder.addFormDataPart("privacy", "2")
        } else if (postPrivacySpinnerValue.equals("Friends of friends", true)) {
            builder.addFormDataPart("privacy", "3")
        }
        builder.addFormDataPart("location", tvStoryLocation.text.toString())
        builder.addFormDataPart("content", etStoryText.text.toString().trim())
        if (taggedPeopleList!!.size > 0) {
            builder.addFormDataPart("tagged_user_id", getCommonSeperatedString(taggedPeopleList!!))
        }

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (i in 0 until picsList!!.size) {
            // val path = ContentUriUtils.getFilePath(requireActivity(), arrayList.get(i))
            val file = File(picsList.get(i))
            builder.addFormDataPart(
                "media[]",
                file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        }


        val requestBody = builder.build()
        val call: Call<ResponseBody> = ApiHelper.createAppService().uploadMultiFile(requestBody)!!
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>?,
                response: Response<ResponseBody?>
            ) {
                if (isFragmentVisible) {
                    Toast.makeText(
                        requireActivity(),
                        "Post created successfully...",
                        Toast.LENGTH_SHORT
                    ).show()
                    //showLoading(false)
                    requireActivity().onBackPressed()
                }
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                if (isFragmentVisible) {
                    Log.d("AddStoryPostFragment", "Error " + t.message)
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ApiFailureTypes().getFailureMessage(t, requireActivity())
                    )
                    //showLoading(false)
                }

            }
        })
    }

    fun addStoryApi() {
        showLoading(true)
        val multipartTypedOutput =
            arrayOfNulls<MultipartBody.Part>(picsList!!.size)
        for (i in 0 until picsList.size) {
            Log.d(
                "Upload request",
                "requestUploadSurvey: survey image " + i + "  " + picsList.get(i).toString()
            )
            val file = File(picsList.get(i))
            if (checkIsImage(requireActivity(), arrayList!!.get(i))) {
                val surveyBody: RequestBody =
                    RequestBody.create(MediaType.parse("image/*"), file)
                multipartTypedOutput[i] =
                    MultipartBody.Part.createFormData("media[]", file.path, surveyBody)
            } else {
                val surveyBody: RequestBody =
                    RequestBody.create(MediaType.parse("video/*"), file)
                multipartTypedOutput[i] =
                    MultipartBody.Part.createFormData("media[]", file.path, surveyBody)
            }
        }
        var privacy_value = ""
        if (postPrivacySpinner.text.toString().equals("Public", true)) {
            privacy_value = "1"
        } else if (postPrivacySpinner.text.toString().equals("Friends", true)) {
            privacy_value = "2"
        } else if (postPrivacySpinner.text.toString().equals("FOF", true)) {
            privacy_value = "3"
        }
        val privacy =
            RequestBody.create(MediaType.parse("text/plain"), privacy_value)
        val location =
            RequestBody.create(MediaType.parse("text/plain"), tvStoryLocation.text.toString())
        val content =
            RequestBody.create(MediaType.parse("text/plain"), etStoryText.text.toString())

        val tagged_user_id =
            RequestBody.create(
                MediaType.parse("text/plain"),
                getCommonSeperatedString(taggedPeopleList!!)
            )
        ApiHelper.createAppService()
            .addPost(privacy, location, content, tagged_user_id, multipartTypedOutput)!!
            .enqueue(object : Callback<AddPostResponse?> {
                override fun onResponse(
                    call: Call<AddPostResponse?>,
                    response: Response<AddPostResponse?>
                ) {
                    // Log.d("fb_regist_response", "--->$response")
                    if (isFragmentVisible) {
                        showLoading(false)
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            response.body()!!.message
                        )
                        if (response.body()!!.status) {
                            requireActivity().onBackPressed()
                        }
                    }

                }

                override fun onFailure(
                    call: Call<AddPostResponse?>,
                    t: Throwable
                ) {
                    if (isFragmentVisible) {
                        showLoading(false)
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            ErrorHandler.getMessage(t, requireActivity())
                        )
                        /*  CommonMethods.showToastMessageAtTop(
                              requireActivity(),
                              ApiFailureTypes().getFailureMessage(t, requireActivity())
                          )*/
                    }

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode === RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                locationLayout.visibility = View.VISIBLE
                tvStoryLocation.text = place.address
                selectedLocation = place.address!!
                updateText()
                //Log.i(TAG, "Place: " + place.name + ", " + place.id)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("location_error", status.getStatusMessage())
            } else if (resultCode === RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        }
        if (requestCode == filePickerRequest) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mAdd.visibility = View.VISIBLE
                val pathVideo =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    requireActivity().resources.getString(R.string.application_name)
                )
                if (!dir.exists()) {
                    Log.d(
                        ProfileFragment::class.java.name,
                        "Folder doesn't exist, creating it..."
                    )
                    val rv: Boolean = dir.mkdir()
                    Log.d(
                        ProfileFragment::class.java.name,
                        "Folder creation " + if (rv) "success" else "failed"
                    )
                } else {
                    Log.d(ProfileFragment::class.java.name, "Folder already exists.")
                }

                if (checkIsImage(requireActivity(), pathVideo[0])) {
                    showLoading(true)
                    val file =
                        File(dir, "image" + System.currentTimeMillis() + ".png")
                    arrayList!!.add(pathVideo[0])
                    if (arrayList.size == 5) {
                        mAdd.visibility = View.VISIBLE
                    }
                    postImagesAdapter!!.notifyDataSetChanged()
                    showLoading(true)
                    lifecycleScope.launch {
                        val path = ContentUriUtils.getFilePath(requireActivity(), pathVideo[0])
                        val compressedImageFile =
                            Compressor.compress(requireActivity(), File(path)) {
                                //resolution(1280, 720)
                                quality(POST_PIC_QUALITY)
                                default()
                                destination(file)
                                println("sajdnasj")
                                //filePath=file.absolutePath
                                picsList!!.add(file.absolutePath)
                                val handler =
                                    Handler(Looper.getMainLooper())
                                val r = Runnable {
                                    showLoading(false)
                                }
                                handler.postDelayed(r, AppConstants.POST_PIC_LOADING.toLong())
                            }
                    }

                } else {
                    val file =
                        File(dir, "image" + System.currentTimeMillis() + ".mp4")
                    val retriever = MediaMetadataRetriever();
                    retriever.setDataSource(
                        ContentUriUtils.getFilePath(
                            requireActivity(),
                            pathVideo[0]
                        )
                    )
                    val duration =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            .toLong()
                    /* val mp: MediaPlayer = MediaPlayer.create(requireActivity(), pathVideo[0])
                     val duration: Int = mp.getDuration()
                     mp.release()*/
                    if (duration / 1000 > 180) {
                        showToast("Video length is greater than 3 minutes")
                        // Show Your Messages
                    } else {
                        showToast("Compressing video, Please wait...")
                        VideoCompressor.start(
                            ContentUriUtils.getFilePath(requireActivity(), pathVideo[0])!!,
                            file.absolutePath,
                            object : CompressionListener {
                                override fun onProgress(percent: Float) {
                                    requireActivity()?.runOnUiThread(Runnable {
                                        //on main thread
                                        println("onProgress")

                                        // update a text view
                                        // progress.text = "${percent.toLong()}%"
                                        // update a progress bar
                                        // progressBar.progress = percent.toInt()
                                    })

                                }

                                override fun onStart() {
                                    // Compression start
                                    println("onStart")
                                    showLoading(true)
                                }

                                override fun onSuccess() {
                                    println("onSuccess")
                                    hideLoading()
                                    requireActivity()?.runOnUiThread(Runnable {

                                        arrayList!!.add(file.toUri())
                                        picsList!!.add(
                                            file.absolutePath
                                        )
                                        if (arrayList!!.size == 5) {
                                            mAdd.visibility = View.VISIBLE
                                        }
                                        postImagesAdapter!!.notifyDataSetChanged()
                                    })

                                    // On Compression success

                                }

                                override fun onFailure(failureMessage: String) {
                                    // On Failure
                                    hideLoading()
                                    println("onFailure")
                                }

                                override fun onCancelled() {
                                    // On Cancelled
                                    hideLoading()
                                    println("onCancelled")
                                }

                            },
                            VideoQuality.HIGH,
                            isMinBitRateEnabled = false,
                            keepOriginalResolution = false
                        )
                        //  VideoCompressAsyncTask(requireActivity()).execute(ContentUriUtils.getFilePath(requireActivity(), pathVideo[0])!!, dir.getPath())
                        /*   arrayList!!.add(pathVideo[0])
                           picsList!!.add(
                               ContentUriUtils.getFilePath(
                                   requireActivity(),
                                   pathVideo[0]
                               )!!
                           )
                           postImagesAdapter!!.notifyDataSetChanged()*/
                    }
                    /* val timee = getMediaDuration(this@MainActivity, ContentUriUtils.getFilePath(this@MainActivity, pathVideo[0])!!)
                     val minutes = (timee % 3600) / 60;
                     //val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timee)
                     println("fgh")*/
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    /*internal class VideoCompressAsyncTask(context: Context) :
        AsyncTask<String?, String?, String?>() {
        var mContext: Context
        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(compressedFilePath: String?) {
            super.onPostExecute(compressedFilePath)
            println("compressed path----"+compressedFilePath)

        }

        init {
            mContext = context
        }

        override fun doInBackground(vararg p0: String?): String? {
            var filePath: String? = null
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(p0[0], p0[1])
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return filePath
        }
    }*/
    fun getImageContentUri(
        context: Context,
        imageFile: File
    ): Uri? {
        val filePath = imageFile.absolutePath
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA.toString() + "=? ",
            arrayOf(filePath),
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "" + id
            )
        } else {
            if (imageFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val resolver: ContentResolver = context.contentResolver
                    val picCollection: Uri = MediaStore.Images.Media
                        .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    val picDetail = ContentValues()
                    picDetail.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
                    picDetail.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                    picDetail.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "DCIM/" + UUID.randomUUID().toString()
                    )
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 1)
                    val finaluri: Uri = resolver.insert(picCollection, picDetail)!!
                    picDetail.clear()
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(picCollection, picDetail, null, null)
                    finaluri
                } else {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, filePath)
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )
                }
            } else {
                null
            }
        }
    }

    fun getMediaDuration(context: Context, path: String): Int {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.parse(path))
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        return duration.toInt() ?: 0
    }

    fun checkIsImage(context: Context, uri: Uri?): Boolean {
        val contentResolver = context.contentResolver
        val type = contentResolver.getType(uri!!)
        if (type != null) {
            return type.startsWith("image/")
        } else {
            // try to decode as image (bounds only)
            var inputStream: InputStream? = null
            try {
                inputStream = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeStream(inputStream, null, options)
                    return options.outWidth > 0 && options.outHeight > 0
                }
            } catch (e: IOException) {
                // ignore
            } finally {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.closeQuietly(inputStream)
                }
            }
        }
        // default outcome if image not confirmed
        return false
    }
    /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
           if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
               // or get a single image only
               if (resultCode == Activity.RESULT_OK) {
                   var image = ImagePicker.getFirstImageOrNull(data)
                   imageList!!.add(image)
                   postImagesAdapter!!.notifyDataSetChanged()
               }
           }
           super.onActivityResult(requestCode, resultCode, data)

       }*/

    override fun onItemClicked(position: Int) {
        arrayList!!.removeAt(position)
        picsList!!.removeAt(position)
        postImagesAdapter!!.notifyDataSetChanged()

    }

    override fun onTagClicked(position: Int) {
        dialog!!.dismiss()
        if (taggedPeopleList!!.contains(friendsList!!.get(position))) {
            showToast("User already tagged!!!")
        } else {
            taggedPeopleList!!.add(friendsList!!.get(position))
            updateText()
        }

    }

    override fun unBlockClicked(position: Int) {
    }

    override fun unFriendClicked(position: Int) {
    }

    override fun openUserProfileFromTaggedList(position: Int) {
    }

    override fun blockFriend(position: Int) {

    }

    override fun chat(position: Int) {


    }

    override fun remove(position: Int) {

    }


    private fun updateText() {
        var text = ""
        if (taggedPeopleList!!.size > 0) {
            if (taggedPeopleList!!.size == 1) {
                text =
                    text + "- with " + taggedPeopleList!!.get(0).accepted_user.firstName + " " + taggedPeopleList!!.get(
                        0
                    ).accepted_user.lastName
            } else {
                text =
                    text + "- with " + taggedPeopleList!!.get(0).accepted_user.firstName + " " + taggedPeopleList!!.get(
                        0
                    ).accepted_user.lastName + " and " + taggedPeopleList!!.size.minus(1) + " other"

            }

            // tvTaggedLocation.setText("- with")
        }

        if (!selectedLocation.equals("")) {
            text = text + " in " + selectedLocation
        }
        if (text.equals("")) {

            tvTaggedLocation.visibility = View.GONE
        } else {
            tvTaggedLocation.visibility = View.VISIBLE
        }
        tvTaggedLocation.setText(text)


    }
}