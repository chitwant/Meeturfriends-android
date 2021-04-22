package com.meetfriend.app.ui.fragments.challenge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.ui.fragments.profile.ProfileFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.AppConstants
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import kotlinx.android.synthetic.main.fragment_challengeaccept_post.*
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

class ChallengeAcceptPostFragment(var callback: responseCallback) : BaseFragment() {
    private lateinit var challengeViewModal: ChallengeViewModal
    private var arrayList: ArrayList<Uri>? = ArrayList()
    private var imageUrl: String? = null
    private var from: String? = null
    private var isFragmentVisible = false
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_challengeaccept_post, parent, false)
    }

    interface responseCallback {
        fun getDetails()
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentVisible = true
        headerLoginBackButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        from = arguments?.getString("from")
        challengeViewModal =
            ViewModelProvider(this).get(ChallengeViewModal::class.java)
        initializeObservers()
        tvPostChallenge.setOnClickListener {
            if (validation()) {
                arguments?.getInt("id")
                    ?.let { it1 -> createChallengeApi(it1, etDesc.text.toString()) }
            }
        }
        tvTitle.setText(arguments?.getString("title"))

        tvSlctdImagevideo.setOnClickListener {
            FilePickerBuilder.instance
                .setMaxCount(1)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                .pickPhoto(this, CreateChallenge.filePickerRequest)
        }
        challengeViewModal?.challengeAcceptPostReponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    showLoading(false)
                    if (from.equals("detail")) {
                        callback.getDetails()
                    } else {
                        callback.getDetails()
                    }
                    activity?.supportFragmentManager?.popBackStack()

                } else {

                }
            }
        })

    }

    private fun validation(): Boolean {
        var valid = true
        var errorMsg: String? = null
        if (etDesc.getText().toString().trim({ it <= ' ' }).isEmpty()) {
            valid = false
            errorMsg = "Description is empty"
        } else if (imageUrl.isNullOrEmpty()) {
            valid = false
            errorMsg = "Image/video is empty"
        }
        if (!valid) {
            Toast.makeText(
                requireActivity(),
                errorMsg,
                Toast.LENGTH_SHORT
            ).show()
        }
        return valid
    }

    fun createChallengeApi(id: Int, desc: String) {

        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("challenge_id", id.toString())
        builder.addFormDataPart("description", desc)

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        // val path = ContentUriUtils.getFilePath(requireActivity(), arrayList.get(i))
        val file = File(imageUrl)
        builder.addFormDataPart(
            "media",
            file.getName(),
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )


        val requestBody = builder.build()
        val call: Call<ResponseBody> =
            ApiHelper.createAppService().uploadChallengeAcceptFile(requestBody)!!
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>?,
                response: Response<ResponseBody?>
            ) {
                if (isFragmentVisible) {
                    val mHashMap = HashMap<String, Any>()
                    mHashMap["challenge_id"] = id
                    mHashMap["status"] = 1
                    challengeViewModal?.challengeAcceptPost(mHashMap)
                    showLoading(true)

//                     requireActivity().onBackPressed()
//                    val intent = Intent(requireContext(), ChallangeDetailActivity::class.java)
//                    intent.putExtra("id", id);
//                    intent.putExtra("from", "live");
//                    startActivity(intent)
//                    activity?.supportFragmentManager?.popBackStack()
                }
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                if (isFragmentVisible) {
                    Log.d("CreateChallengeFragment", "Error " + t.message)
                    CommonMethods.showToastMessageAtTop(
                        requireActivity(),
                        ApiFailureTypes().getFailureMessage(t, requireActivity())
                    )
                    showLoading(false)
                }

            }
        })
    }

    private fun initializeObservers() {
        challengeViewModal?.challengeAcceptPostReponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                } else {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        challengeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        challengeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        challengeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateChallenge.filePickerRequest) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                arrayList = ArrayList()
                val pathVideo =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                if (checkIsImage(requireActivity(), pathVideo[0])) {
                    arrayList!!.add(pathVideo[0])
                    Log.e("url---", "" + pathVideo[0])
                    val path = ContentUriUtils.getFilePath(requireContext(), pathVideo[0])
                    Glide.with(requireActivity()).load(path).into(ivChallenge)
                    tvUploadImg.text = "";
                    // postImagesAdapter!!.notifyDataSetChanged()
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
                    val file =
                        File(dir, "image" + System.currentTimeMillis() + ".png")
                    showLoading(true)
                    lifecycleScope.launch {
                        val path = ContentUriUtils.getFilePath(requireActivity(), pathVideo[0])
                        val compressedImageFile =
                            Compressor.compress(requireActivity(), File(path)) {
                                //resolution(1280, 720)
                                quality(AppConstants.POST_PIC_QUALITY)
                                default()
                                destination(file)
                                println("sajdnasj")
                                showLoading(true)
                                //filePath=file.absolutePath
                                // picsList!!.add(file.absolutePath)
                                Log.e("url---2", "" + file.absolutePath)
                                imageUrl = file.absolutePath

                            }
                    }

                } else {
//                    val mp: MediaPlayer = MediaPlayer.create(requireActivity(), pathVideo[0])
//                    val duration: Int = mp.getDuration()
//                    mp.release()
//                    if (duration / 1000 > 180) {
//                        showToast("Video length is greater than 3 minutes")
//                         Show Your Messages
//                    } else {
                    arrayList!!.add(pathVideo[0])
                    Log.e("url---", "" + pathVideo[0])
                    imageUrl = pathVideo?.get(0)
                        ?.let { ContentUriUtils.getFilePath(requireActivity(), it) }
                    Glide.with(requireActivity()).load(imageUrl).into(ivChallenge)
                    tvUploadImg.text = "";

                    // postImagesAdapter!!.notifyDataSetChanged()
//                    }
                    /* val timee = getMediaDuration(this@MainActivity, ContentUriUtils.getFilePath(this@MainActivity, pathVideo[0])!!)
                     val minutes = (timee % 3600) / 60;
                     //val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timee)
                     println("fgh")*/
                }
            }
        }
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

}