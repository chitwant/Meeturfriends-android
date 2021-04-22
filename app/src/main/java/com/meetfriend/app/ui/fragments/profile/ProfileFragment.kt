package com.meetfriend.app.ui.fragments.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragmentNew
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.UpdatePhotoResponse
import com.meetfriend.app.responseclasses.viewprofile.Result
import com.meetfriend.app.ui.activities.ChatActivity
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.viewmodal.HomeViewModal
import com.meetfriend.app.viewmodal.ProfileViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.CommonMethods.showToastMessageAtTop
import contractorssmart.app.utilsclasses.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_profile.ivCoverPic
import kotlinx.android.synthetic.main.fragment_profile.ivEditCoverPic
import kotlinx.android.synthetic.main.fragment_profile.ivEditProfilePic
import kotlinx.android.synthetic.main.fragment_profile.ivProfilePic
import kotlinx.android.synthetic.main.fragment_profile.profileTabsSection
import kotlinx.android.synthetic.main.fragment_profile.tvAddFriend
import kotlinx.android.synthetic.main.fragment_profile.tvUserName
import kotlinx.android.synthetic.main.fragment_profile.view_pager_tab
import kotlinx.android.synthetic.main.fragment_profile.vpPager
import kotlinx.android.synthetic.main.fragment_profile_new.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : BaseFragmentNew() {
    private var from = ""
    private var type = ""
    private var otherUserId = ""
    private var profileViewModal: ProfileViewModal? = null
    private var homeViewModal: HomeViewModal? = null
    private var result: Result? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var imageType = ""
    private var came_from = ""
    private var profilePicUrl = ""
    private var coverPicUrl = ""
    private var isFragmentVisible = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(
            inflater,
            container,
            savedInstanceState,
            R.layout.fragment_profile_new
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            iniData()
        }
    }
    /*   override fun provideYourFragmentView(
           inflater: LayoutInflater,
           parent: ViewGroup?,
           savedInstanceState: Bundle?
       ): View {
           return inflater.inflate(R.layout.fragment_profile_new, parent, false)
       }*/

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
        if (from.length > 0 && !from.equals("detail", false) && !from.equals("activity", true)) {
            (activity as HomeActivity)?.toolbar!!.visibility = View.GONE
            (activity as HomeActivity)?.showAndHideBottomNavigation(false)
        } else {
            if (!from.equals("activity", true)) {
                (activity as HomeActivity)?.toolbar!!.visibility = View.VISIBLE
                (activity as HomeActivity)?.showAndHideBottomNavigation(true)
            }
        }

    }

    private fun iniData() {
        from = this.requireArguments()!!.getString("from", "")

        if (from == "winner" || from == "live" || from == "pending" || from == "done" || from == "detail") {
            ivBlockUser.visibility = View.GONE
            ivAddFriend.visibility = View.GONE
        }
        type = this.requireArguments()!!.getString("type", "")
        otherUserId = this.requireArguments()!!.getString("userId", "")
        profileViewModal = ViewModelProvider(this).get(ProfileViewModal::class.java)
        homeViewModal = ViewModelProvider(this).get(HomeViewModal::class.java)

        // It is used to join TabLayout with ViewPager.

        headerLoginBackButton.setOnClickListener {
            if (from.equals("activity", true) || from.equals("detail", true)) {
                requireActivity()?.onBackPressed()
            } else
                findNavController().popBackStack()
        }
        ivChat.setOnClickListener {
            if (result?.myFriend?.block_status.equals("block")) {
                showToastMessageAtTop(
                    requireContext(),
                    "You can not continue chat with blocked user"
                )
            } else {
                val intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra("user_id", otherUserId);
                intent.putExtra(
                    "image",
                    "https://meeturfriends.s3.amazonaws.com" + result?.profile_photo
                );
                intent.putExtra("name", result?.firstName + " " + result?.lastName);
                intent.putExtra("msg", "");
                intent.putExtra("type", "");
                startActivity(intent)
            }
        }

        ivProfilePic.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra(
                "url",
                profilePicUrl
            )
            requireActivity().startActivity(intent)
        }
        ivCoverPic.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra(
                "url",
                coverPicUrl
            )
            requireActivity().startActivity(intent)
        }

        tvAddFriend.setOnClickListener {
            val mHashMap = HashMap<String, Any>()
            mHashMap["friend_id"] = otherUserId
            homeViewModal?.addFriend(mHashMap)
            showLoading(true)
        }
        ivBlockUser.setOnClickListener {
            blockUserApi()
        }
//        headerLoginBackButton.setOnClickListener { requireActivity().onBackPressed() }
        initializeObservers()
        getProfileData()
    }

    fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            if (!from.equals("detail", false) && !from.equals("activity", true)) {
                (activity as HomeActivity)?.showAndHideBottomNavigation(false)
                (activity as HomeActivity)?.toolbar!!.visibility = View.GONE
            }
        }, 300)
    }

    private fun updateStatus(userType: String) {
        if (userType.equals("own", true)) {
            // tvAddFriend.visibility = View.GONE
            ivChat.visibility = View.GONE
            profileTabsSection.visibility = View.GONE
            ivEditCoverPic.visibility = View.VISIBLE
            ivEditProfilePic.visibility = View.VISIBLE
            ivEditCoverPic.setOnClickListener {
                imageType = "Cover"
                ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(true) // folder mode (false by default)
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .start()
            }
            ivEditProfilePic.setOnClickListener {
                imageType = "Profile"
                ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(true) // folder mode (false by default)
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera")
                    // directory name for captured image  ("Camera" folder by default)
                    .start();
            }
            CommonMethods.setImage(
                ivProfilePic,
                getProfilePic()
            )
            CommonMethods.setImage(
                ivCoverPic,
                getCoverPic()
            )

        } else {
            profileTabsSection.visibility = View.GONE
            ivEditCoverPic.visibility = View.GONE
            ivEditProfilePic.visibility = View.GONE
            if (result!!.myFriend != null) {
                ivChat.visibility = View.VISIBLE

            } else {
                ivChat.visibility = View.GONE

            }
        }
    }

    private fun blockUserApi() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["friend_id"] = otherUserId
        mHashMap["block_status"] = "block"
        homeViewModal?.blockUnBlockPeople(mHashMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            if (resultCode == Activity.RESULT_OK) {
                var image = ImagePicker.getFirstImageOrNull(data)
                showLoading(true)
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    requireActivity().resources.getString(R.string.application_name)
                )
                if (!dir.exists()) {
                    Log.d(ProfileFragment::class.java.name, "Folder doesn't exist, creating it...")
                    val rv: Boolean = dir.mkdir()
                    Log.d(
                        ProfileFragment::class.java.name,
                        "Folder creation " + if (rv) "success" else "failed"
                    )
                } else {
                    Log.d(ProfileFragment::class.java.name, "Folder already exists.")
                }
                if (imageType.equals("Cover", true)) {
                    updateCoverPic(File(image.path))
                } else {

                    updateProfilePic(File(image.path))

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateCoverPic(file: File) {
        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        // val file = File(path)
        builder.addFormDataPart(
            "image",
            file.getName(),
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )
        val requestBody = builder.build()
        val call: Call<UpdatePhotoResponse> =
            ApiHelper.createAppService().uploadCoverPicture(requestBody)!!
        call.enqueue(object : Callback<UpdatePhotoResponse?> {
            override fun onResponse(
                call: Call<UpdatePhotoResponse?>?,
                response: Response<UpdatePhotoResponse?>
            ) {
                showLoading(false)
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())
                Toast.makeText(
                    requireActivity(),
                    "Cover picture updated",
                    Toast.LENGTH_SHORT
                ).show()
                CommonMethods.setImage(
                    ivCoverPic,
                    response.body()!!.media_url + response.body()!!.cover_photo
                )
                coverPicUrl = response.body()!!.media_url + response.body()!!.cover_photo
                PreferenceHandler.writeString(
                    requireActivity(),
                    PreferenceHandler.COVER_PHOTO,
                    response.body()!!.media_url + response.body()!!.cover_photo
                )
                // requireActivity().onBackPressed()
            }

            override fun onFailure(call: Call<UpdatePhotoResponse?>?, t: Throwable) {
                Log.d("AddStoryPostFragment", "Error " + t.message)
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ErrorHandler.getMessage(t, activity!!)
                )
                showLoading(false)
            }
        })
    }

    private fun getProfileData() {
        val mHashMap = HashMap<String, Any>()
        if (type.equals("own")) {
            mHashMap["user_id"] = getUserId()
        } else {
            mHashMap["user_id"] = otherUserId
        }
        profileViewModal?.viewProfile(mHashMap)
    }

    private fun updateProfilePic(file: File) {
        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        // val file = File(path)
        builder.addFormDataPart(
            "image",
            file.getName(),
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )
        val requestBody = builder.build()
        val call: Call<UpdatePhotoResponse> =
            ApiHelper.createAppService().uploadProfilePicture(requestBody)!!
        call.enqueue(object : Callback<UpdatePhotoResponse?> {
            override fun onResponse(
                call: Call<UpdatePhotoResponse?>?,
                response: Response<UpdatePhotoResponse?>
            ) {
                showLoading(false)
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())
                Toast.makeText(
                    requireActivity(),
                    "Profile picture updated",
                    Toast.LENGTH_SHORT
                ).show()
                CommonMethods.setImage(
                    ivProfilePic,
                    response.body()!!.media_url + response.body()!!.profile_photo
                )
                profilePicUrl = response.body()!!.media_url + response.body()!!.profile_photo
                PreferenceHandler.writeString(
                    requireActivity(),
                    PreferenceHandler.PROFILE_PHOTO,
                    response.body()!!.media_url + response.body()!!.profile_photo
                )
                //requireActivity().onBackPressed()
            }

            override fun onFailure(call: Call<UpdatePhotoResponse?>?, t: Throwable) {
                Log.d("AddStoryPostFragment", "Error " + t.message)
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ErrorHandler.getMessage(t, activity!!)
                )
                showLoading(false)
            }
        })
    }

    private fun initializeObservers() {
        profileViewModal?.viewProfileResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (isFragmentVisible) {
                    if (it.status) {
                        if (it.result == null) {
                            return@let
                        }
                        result = it.result
                        if (getUserId().equals(it.result.id.toString(), true)) {
                            updateStatus("own")
                            viewPagerAdapter = ViewPagerAdapter(
                                requireActivity().getSupportFragmentManager(),
                                result!!,
                                getUserId(),
                                "own"
                            )
                        } else {
                            updateStatus("other")
                            viewPagerAdapter = ViewPagerAdapter(
                                requireActivity().getSupportFragmentManager(),
                                result!!,
                                otherUserId,
                                "other"
                            )
                        }
                        vpPager.setAdapter(viewPagerAdapter)
                        view_pager_tab.setupWithViewPager(vpPager)
                        setIconsForTabs()
                        if (it.result.profile_photo != null && !it.result.profile_photo.equals("")) {
                            CommonMethods.setImage(
                                ivProfilePic,
                                it.media_url + it.result.profile_photo
                            )
                            profilePicUrl = it.media_url + it.result.profile_photo
                            if (type.equals("own", true)) {
                                PreferenceHandler.writeString(
                                    requireActivity(),
                                    PreferenceHandler.PROFILE_PHOTO,
                                    profilePicUrl
                                )
                            }
                        } else {
                            ivProfilePic.setImageResource(R.drawable.ic_user_profile_pic_new)
                        }
                        if (it.result.cover_photo != null && !it.result.cover_photo.equals("")) {
                            CommonMethods.setImage(ivCoverPic, it.media_url + it.result.cover_photo)
                            coverPicUrl = it.media_url + it.result.cover_photo
                            if (type.equals("own", true)) {
                                PreferenceHandler.writeString(
                                    requireActivity(),
                                    PreferenceHandler.COVER_PHOTO,
                                    coverPicUrl
                                )
                            }
                        } else {
                            ivCoverPic.setImageResource(R.drawable.ic_placer_holder_image_new)
                        }
                        tvUserName.setText(it.result.firstName + " " + it.result.lastName)
                        if (it.result.myFriend == null) {
                            ivBlockUser.visibility = View.GONE
                            ivAddFriend.visibility = View.GONE
                        } else {
                            if (!it.result.myFriend.request_status.equals("accepted")) {
                                // ivAddFriend.visibility = View.VISIBLE
                            } else {
                                ivAddFriend.visibility = View.GONE
                            }
                            if (it.result.myFriend.block_status.equals("unblock")) {
                                //ivBlockUser.visibility = View.VISIBLE
                            } else {
                                ivBlockUser.visibility = View.GONE
                            }
                        }


                    } else {
                        CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                        /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                         }
                         if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                         }*/
                        // CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                    }
                }
            }

        })
        homeViewModal!!.addFriendResponse.observe(requireActivity(), Observer {
            it.let {
                showLoading(false)
                CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        homeViewModal!!.blockUnBlockPeopleResponse.observe(requireActivity(), Observer {
            it.let {
                showLoading(false)
                CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
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
                if (isFragmentVisible) {
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ErrorHandler.getMessage(it, requireActivity())
                    )
                }

                /* CommonMethods.showSnackbarMessageWithoutColor(
                     requireActivity(),
                     ApiFailureTypes().getFailureMessage(it, requireActivity())
                 )*/
            }
        })
    }

    private fun setIconsForTabs() {
//        view_pager_tab.getTabAt(0)!!.setIcon(R.drawable.ic_chat_icon_grey)
//        view_pager_tab.getTabAt(0)!!.setIcon(R.drawable.ic_photos_icon_grey)
//        view_pager_tab.getTabAt(1)!!.setIcon(R.drawable.ic_posts_icon_grey)
//        view_pager_tab.getTabAt(2)!!.setIcon(R.drawable.ic_about_info_icon)
        // view_pager_tab.getTabAt(4)!!.setIcon(R.drawable.ic_friends_icon)
    }

    class ViewPagerAdapter(
        fm: FragmentManager,
        val result: Result,
        val otherUserId: String,
        val type: String
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {

            var fragment: Fragment? = null
//            if (position == 0) {
//                fragment = ChatFragment(otherUserId,result)
//            } else
            if (position == 0) fragment =
                PhotosFragment(otherUserId, type)
            else if (position == 1) fragment =
                PostsFragment(otherUserId, type)
            else if (position == 2) fragment =
                PersonalInfoFragment(result, type)
            /* else if (position == 4)
                 fragment = MyfriendsFragment()*/

            return fragment!!
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title: String? = null
            /*  if (position == 0) title = "Chat" else*/ if (position == 0) title =
                "Photos" else if (position == 1) title = "Posts" else if (position == 2) title =
                "Info"
            return title
        }
    }


}