package com.meetfriend.app.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
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
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.responseclasses.homeposts.PostMedium
import com.meetfriend.app.responseclasses.homeposts.TaggedUser
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.adapters.EditPostImagesAdapter
import com.meetfriend.app.ui.adapters.PostImagesAdapter
import com.meetfriend.app.ui.adapters.TagPeopleAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.AppConstants
import com.meetfriend.app.utilclasses.AppConstants.POST_PIC_QUALITY
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.viewmodal.HomeViewModal
import com.meetfriend.app.viewmodal.ProfileViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import kotlinx.android.synthetic.main.fragment_edit_post.*
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

class EditPostFragment : BaseFragment(), EditPostImagesAdapter.AdapterCallback,
    TagPeopleAdapter.AdapterCallback, PostImagesAdapter.AdapterCallback {
    private var content = ""
    private var privacy = 1

    //private var arrayListEditPics: ArrayList<String>? = ArrayList()
    private var editPostImagesAdapter: EditPostImagesAdapter? = null
    private var post_id = -1
    private var profileViewModal: ProfileViewModal? = null
    private var taggedPeopleList: ArrayList<Data>? = ArrayList()
    private var selectedLocation = ""
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    private var homeViewModal: HomeViewModal? = null
    private var friendsList: ArrayList<Data>? = ArrayList()
    private var baseUrl = ""
    var dialog: Dialog? = null
    private var filePickerRequest = 1
    private var AUTOCOMPLETE_REQUEST_CODE = 100
    private var homePostList: ArrayList<Datum>? =
        ArrayList()
    private var position_clicked = -1
    private var deletedPics = ""
    private var mediaUrl = ""
    private var deletedPicsFromEditArrayList: ArrayList<PostMedium>? = ArrayList()
    private var postImagesAdapter: PostImagesAdapter? = null
    val arrayList: ArrayList<Uri>? = ArrayList()
    val picsList: ArrayList<String>? = ArrayList()
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_post, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)
        (activity as HomeActivity).showAndHideBottomNavigation(false)
        (activity as HomeActivity).toolbar!!.visibility = View.GONE
        post_id = this.requireArguments().getInt("post_id", -1)
        //arrayListEditPics = this.requireArguments().getStringArrayList("list")
        content = this.requireArguments()!!.getString("content", "")
        privacy = this.requireArguments()!!.getInt("privacy", -1)
        homePostList = this.requireArguments()!!.getParcelableArrayList("main_list")
        position_clicked = this.requireArguments()!!.getInt("position_clicked", -1)
        mediaUrl = this.requireArguments()!!.getString("mediaUrl", "")
        iniListeners()
        iniData()
        initialObservers()
        getFriendsListApi()
    }

    private fun getFriendsListApi() {
        val mHashMap = java.util.HashMap<String, Any>()
        mHashMap["page"] = 1
        mHashMap["per_page"] = 1000
        homeViewModal?.friendsList(mHashMap)

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
                if (it.status) {
                    baseUrl = it.media_url
                    friendsList = it.result.data
                    if (checkAndRequestPermissions(requireActivity())) {

                    }
                } else {
                    CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
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

    private fun iniData() {
        CommonMethods.setUserImage(ivUserPicture, getProfilePic())
        tvUsername.text = PreferenceHandler.readString(
            requireActivity(),
            PreferenceHandler.FIRSTNAME,
            ""
        ) + " " + PreferenceHandler.readString(requireActivity(), PreferenceHandler.LASTNAME, "")
        profileViewModal = ViewModelProvider(this).get(ProfileViewModal::class.java)
        etStoryText.setText(content)
        editPostImagesAdapter = EditPostImagesAdapter(
            homePostList!!.get(position_clicked).postMedia!!,
            this,
            requireActivity(),
            "add",
            mediaUrl
        )
        if (  homePostList!!.get(position_clicked).postMedia.size > 0) {
            mAdd.visibility = View.VISIBLE
        }
        recyclerViewPhotos.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = editPostImagesAdapter
            isNestedScrollingEnabled = false
        }
        postImagesAdapter = PostImagesAdapter(arrayList!!, this, requireActivity(), "add")
        recyclerViewPhotosNew.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = postImagesAdapter
            isNestedScrollingEnabled = false
        }

//        postPrivacySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                arg0: AdapterView<*>?,
//                arg1: View?,
//                position: Int,
//                id: Long
//            ) {
//                if (position == 0) {
//                    privacy = 1
//
//                } else if (position == 1) {
//                    privacy = 2
//                } else if (position == 2) {
//                    privacy = 3
//                }
//
//            }
//
//            override fun onNothingSelected(arg0: AdapterView<*>?) {
//
//            }
//        })
        if (privacy == 1) {
            postPrivacySpinners.setText("Public")
        } else if (privacy == 2) {
            postPrivacySpinners.setText("Friends")

        } else if (privacy == 3) {
            postPrivacySpinners.setText("FOF")
        }
        initializeObservers()
        updateTextBeforeEdit()
    }

    private fun updateApi() {
        var taggedUserIds = ""
        /*val mHashMap = HashMap<String, Any>()
        mHashMap["content"] = etStoryText.text.toString().trim()
        mHashMap["post_id"] = post_id
        mHashMap["privacy"] = privacy
        mHashMap["location"] = selectedLocation
        if (taggedPeopleList!!.size > 0) {
            mHashMap["tagged_user_id"] = getCommonSeperatedString(taggedPeopleList!!)!!
        }
        profileViewModal?.editPost(mHashMap)*/
        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("privacy", privacy.toString())
        builder.addFormDataPart("post_id", post_id.toString())
        builder.addFormDataPart("location", selectedLocation)
        builder.addFormDataPart("content", etStoryText.text.toString().trim())
        if (taggedPeopleList!!.size > 0) {
            taggedUserIds = getCommonSeperatedString(taggedPeopleList!!)!!
            // builder.addFormDataPart("tagged_user_id", getCommonSeperatedString(taggedPeopleList!!))
        }
        if (homePostList!!.get(position_clicked).tagged_users_list != null && homePostList!!.get(
                position_clicked
            ).tagged_users_list!!.size > 0
        ) {
            if (taggedUserIds.equals("")) {
                taggedUserIds =
                    getCommonSeperatedStringEditPost(homePostList!!.get(position_clicked).tagged_users_list)!!

            } else {
                taggedUserIds = taggedUserIds + "," + getCommonSeperatedStringEditPost(
                    homePostList!!.get(position_clicked).tagged_users_list
                )

            }
        }
        builder.addFormDataPart("tagged_user_id", taggedUserIds)
        if (deletedPicsFromEditArrayList!!.size > 0) {
            builder.addFormDataPart(
                "deleted_media_ids",
                getDeletedPicsIds(deletedPicsFromEditArrayList!!)
            )
        }
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (i in 0 until picsList!!.size) {
            //  val path = ContentUriUtils.getFilePath(requireActivity(), arrayList.get(i))
            val file = File(picsList!!.get(i))
            builder.addFormDataPart(
                "media[]",
                file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        }
        val requestBody = builder.build()
        val call: Call<ResponseBody> = ApiHelper.createAppService().editPost(requestBody)!!
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>?,
                response: Response<ResponseBody?>
            ) {
                showLoading(false)
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())
                Toast.makeText(
                    requireActivity(),
                    "Post updated successfully...",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().onBackPressed()
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                Log.d("AddStoryPostFragment", "Error " + t.message)
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(t, requireActivity())
                )
                showLoading(false)
            }
        })

    }

    fun editStoryApi() {
        showLoading(true)
        var taggedUserIds = ""
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

        val post_id =
            RequestBody.create(MediaType.parse("text/plain"), post_id.toString())
        val privacy =
            RequestBody.create(MediaType.parse("text/plain"), privacy.toString())
        val location =
            RequestBody.create(MediaType.parse("text/plain"), selectedLocation)
        val content =
            RequestBody.create(MediaType.parse("text/plain"), etStoryText.text.toString())
        if (taggedPeopleList!!.size > 0) {
            taggedUserIds = getCommonSeperatedString(taggedPeopleList!!)!!
        }
        if (homePostList!!.get(position_clicked).tagged_users_list != null && homePostList!!.get(
                position_clicked
            ).tagged_users_list!!.size > 0
        ) {
            if (taggedUserIds.equals("")) {
                taggedUserIds =
                    getCommonSeperatedStringEditPost(homePostList!!.get(position_clicked).tagged_users_list)!!

            } else {
                taggedUserIds = taggedUserIds + "," + getCommonSeperatedStringEditPost(
                    homePostList!!.get(position_clicked).tagged_users_list
                )

            }
        }
        val tagged_user_id =
            RequestBody.create(
                MediaType.parse("text/plain"),
                taggedUserIds
            )
        val deleted_media_ids =
            RequestBody.create(
                MediaType.parse("text/plain"),
                getDeletedPicsIds(deletedPicsFromEditArrayList!!)
            )
        ApiHelper.createAppService()
            .editPost(
                post_id,
                privacy,
                location,
                content,
                tagged_user_id,
                deleted_media_ids,
                multipartTypedOutput
            )!!
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
                        /* CommonMethods.showToastMessageAtTop(
                             requireActivity(),
                             ApiFailureTypes().getFailureMessage(t, requireActivity())
                         )*/
                    }

                }
            })
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

    fun getCommonSeperatedStringEditPost(actionObjects: ArrayList<TaggedUser>): String? {
        val sb = StringBuffer()
        for (actionObject in actionObjects) {
            sb.append(actionObject.taggedUserId).append(",")
        }
        sb.deleteCharAt(sb.lastIndexOf(","))
        return sb.toString()
    }

    fun getDeletedPicsIds(actionObjects: ArrayList<PostMedium>): String? {
        if (deletedPicsFromEditArrayList!!.size < 1) {
            return ""
        }
        val sb = StringBuffer()
        for (actionObject in actionObjects) {
            sb.append(actionObject.id).append(",")
        }
        sb.deleteCharAt(sb.lastIndexOf(","))
        return sb.toString()
    }

    private fun initializeObservers() {
        profileViewModal!!.editPostResponse.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                requireActivity().onBackPressed()
            }
        })
        profileViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        profileViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        profileViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ErrorHandler.getMessage(it, requireActivity())
                )
            }
        })
    }

    private fun iniListeners() {
        ivCloseIcon.setOnClickListener { requireActivity().onBackPressed() }
        tvPostStory.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            editStoryApi()
            //updateApi()
        }
        ivTagPeople.setOnClickListener {
            showTagPeopleDialog()
        }
        postPrivacySpinners?.setOnClickListener(View.OnClickListener {
            showDialogbox(postPrivacySpinners)
        })
        ivAddLocation.setOnClickListener {
            val fields: List<Place.Field> =
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(requireActivity())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
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
        ivGallery.setOnClickListener {
            val totalSize = arrayList!!.size + homePostList!!.get(position_clicked).postMedia.size
            if (totalSize > 4) {
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
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
        (activity as HomeActivity).showAndHideBottomNavigation(true)
        (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
    }

    override fun onDeleteEditPics(position: Int) {
        deletedPicsFromEditArrayList!!.add(
            homePostList!!.get(position_clicked).postMedia.get(
                position
            )
        )
        homePostList!!.get(position_clicked).postMedia.removeAt(position)
        editPostImagesAdapter!!.notifyDataSetChanged()
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

    override fun onTagClicked(position: Int) {
        dialog!!.dismiss()
        if (taggedPeopleList!!.contains(friendsList!!.get(position))) {
            showToast("User already tagged!!!")
        } else {
            var isTagged = false
            for (i in 0 until homePostList!!.get(position_clicked).tagged_users_list.size) {
                if (friendsList!!.get(position).friend_id == homePostList!!.get(position_clicked).tagged_users_list.get(
                        i
                    ).taggedUserId
                ) {
                    isTagged = true
                    taggedPeopleList!!.add(friendsList!!.get(position))
                    homePostList!!.get(position_clicked).tagged_users_list.removeAt(i)
                    showToast("User already tagged!!!")
                    break
                }
            }
            if (!isTagged) {
                taggedPeopleList!!.add(friendsList!!.get(position))
                updateText()
            }
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

    private fun updateTextBeforeEdit() {
        var text = ""
        if (homePostList!!.get(position_clicked).tagged_users_list!!.size > 0) {
            if (homePostList!!.get(position_clicked).tagged_users_list!!.size == 1) {
                text =
                    text + "- with " + homePostList!!.get(position_clicked).tagged_users_list!!.get(
                        0
                    ).user.firstName + " " + homePostList!!.get(position_clicked).tagged_users_list!!.get(
                        0
                    ).user.lastName
            } else {
                text =
                    text + "- with " + homePostList!!.get(position_clicked).tagged_users_list!!.get(
                        0
                    ).user.firstName + " " + homePostList!!.get(position_clicked).tagged_users_list!!.get(
                        0
                    ).user.lastName + " and " + homePostList!!.get(position_clicked).tagged_users_list!!.size.minus(
                        1
                    ) + " other"

            }

            // tvTaggedLocation.setText("- with")
        }

        if (homePostList!!.get(position_clicked).location != null && !homePostList!!.get(
                position_clicked
            ).location.equals("")
        ) {
            text = text + " in " + homePostList!!.get(position_clicked).location
            selectedLocation = homePostList!!.get(position_clicked).location
        }
        if (text.equals("")) {
            tvTaggedLocation.visibility = View.GONE
        } else {
            tvTaggedLocation.visibility = View.VISIBLE
        }
        tvTaggedLocation.setText(text)


    }

    private fun showDialogbox(postPrivacySpinner: AppCompatTextView?) {
        val popup = PopupMenu(requireContext(), postPrivacySpinner!!)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.getMenuInflater()
            .inflate(R.menu.popup_menu, popup.getMenu())

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.publics -> {
                    postPrivacySpinner.setText(item.title)
                    privacy = 1
                }
                R.id.friend -> {
                    postPrivacySpinner.setText(item.title)
                    privacy = 2
                }
                R.id.fof -> {
                    postPrivacySpinner.setText(item.title)
                    privacy = 3
                }
            }
            true
        })
        popup.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                // locationLayout.visibility = View.VISIBLE
                // tvStoryLocation.text = place.address
                selectedLocation = place.address!!
                updateText()
                //Log.i(TAG, "Place: " + place.name + ", " + place.id)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                //Log.i(TAG, status.getStatusMessage())
            } else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        }
        if (requestCode == filePickerRequest) {
            mAdd.visibility = View.VISIBLE
            if (resultCode == Activity.RESULT_OK && data != null) {
                val pathVideo =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    requireActivity().resources.getString(R.string.application_name)
                )
                if (!dir.exists()) {
                    Log.d(
                        EditPostFragment::class.java.name,
                        "Folder doesn't exist, creating it..."
                    )
                    val rv: Boolean = dir.mkdir()
                    Log.d(
                        EditPostFragment::class.java.name,
                        "Folder creation " + if (rv) "success" else "failed"
                    )
                } else {
                    Log.d(EditPostFragment::class.java.name, "Folder already exists.")
                }
                if (checkIsImage(requireActivity(), pathVideo[0])) {
                    showLoading(true)
                    arrayList!!.add(pathVideo[0])
                    postImagesAdapter!!.notifyDataSetChanged()
                    if (arrayList.size == 5) {
                        mAdd.visibility = View.VISIBLE
                    }
                    val file =
                        File(dir, "image" + System.currentTimeMillis() + ".png")
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
                    val mp: MediaPlayer = MediaPlayer.create(requireActivity(), pathVideo[0])
                    val duration: Int = mp.getDuration()
                    mp.release()
                    if (duration / 1000 > 180) {
                        showToast("Video length is greater than 3 minutes")
                        // Show Your Messages
                    } else {
                        showToast("Compressing video, Please wait...")
                        val file =
                            File(dir, "image" + System.currentTimeMillis() + ".mp4")
                        VideoCompressor.start(
                            ContentUriUtils.getFilePath(requireActivity(), pathVideo[0])!!,
                            file.absolutePath,
                            object : CompressionListener {
                                override fun onProgress(percent: Float) {
                                    requireActivity()?.runOnUiThread(Runnable {
                                        //on main thread
                                        println("onProgress---" + percent.toLong() + "%")

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
                                        postImagesAdapter!!.notifyDataSetChanged()
                                    })
                                    if (arrayList!!.size == 5) {
                                        mAdd.visibility = View.VISIBLE
                                    }
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
                        /* arrayList!!.add(pathVideo[0])
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

    override fun onItemClicked(position: Int) {
        arrayList!!.removeAt(position)
        picsList!!.removeAt(position)
        postImagesAdapter!!.notifyDataSetChanged()
    }

}