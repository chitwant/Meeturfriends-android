package com.meetfriend.app.ui.fragments.challenge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
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
import kotlinx.android.synthetic.main.fragment_create_challenge_2.*
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
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

class CreateChallenge : BaseFragment() {
    private var challengeViewModal: ChallengeViewModal? = null
    private var arrayList: ArrayList<Uri>? = ArrayList()
    private var startDate: String? = null
    private var endDate: String? = null
    private var startTime: String? = null
    private var endTime: String? = null
    private var imageUrl: String? = null

    private var seenById: Int? = null
    private var cntryIds: String? = null
    private var stateIds: String? = null
    private var cityIds: String? = null
    private var isFragmentVisible = false

    companion object {
        const val filePickerRequest = 1
    }

    private var startDateAsDate: Date? = null
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_challenge_2, parent, false)
    }

    override fun onDestroyView() {
        isFragmentVisible = false
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  initializeObservers()
        isFragmentVisible = true
        seenById = arguments?.getInt(CountryChooseFragment.CHALLENGE_SEEN_BY)
        cntryIds = arguments?.getString(CountryChooseFragment.CHALLENGE_COUNTRY)
        stateIds = arguments?.getString(CountryChooseFragment.CHALLENGE_STATE)
        cityIds = arguments?.getString(CountryChooseFragment.CHALLENGE_CITY)
        Log.e("seenid:", "" + seenById)
        Log.e("CHALLENGE_COUNTRY:", "" + cntryIds)
        Log.e("CHALLENGE_STATE:", "" + stateIds)
        Log.e("CHALLENGE_CITY:", "" + cityIds)

        tvCreateChallengeNext.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            if (etTitle.text.toString().trim().equals("")) {
                etTitle.error = "Title is empty"
                return@setOnClickListener
            }
            //textFieldEmail.error = null
            if (etDesc.text.toString().trim().equals("")) {
                etDesc.error = "Description is empty"
                return@setOnClickListener
            }
        }
        tvStartTime.setOnClickListener {
            if (tvStartDate.text.toString().equals("")) {
                CommonMethods.showToastMessageAtTop(
                    requireContext(),
                    "Please select start date first"
                )
            } else  startTime(tvStartTime)
        }
        tvEndTime.setOnClickListener {
            endTime(tvEndTime)
        }
        tvStartDate.setOnClickListener {
            startDate()
        }
        tvEndDate.setOnClickListener {
            if (tvStartDate.text.toString().equals("")) {
                CommonMethods.showToastMessageAtTop(
                    requireContext(),
                    "Please select start date first"
                )
            } else
                endDate()
        }
        tvSlctdImagevideo.setOnClickListener {
            FilePickerBuilder.instance
                .setMaxCount(1)
                .enableVideoPicker(true)
                .enableImagePicker(true)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                .pickPhoto(this, filePickerRequest)
        }
        tvCreateChallengeNext.setOnClickListener {
            if (validation()) {
                createChallengeApi()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == filePickerRequest) {
            arrayList = ArrayList()
            if (resultCode == Activity.RESULT_OK && data != null) {
                val pathVideo =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                if (checkIsImage(requireActivity(), pathVideo[0])) {
                    arrayList!!.add(pathVideo[0])
                    Log.e("url---", "" + pathVideo[0])
                    val path = ContentUriUtils.getFilePath(requireContext(), pathVideo[0])
                    Glide.with(requireActivity()).load(path).into(ivChallenge)
                    tvUploadImg.text = "";
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
                                quality(AppConstants.POST_PIC_QUALITY)
                                default()
                                destination(file)
                                println("sajdnasj")
                                showLoading(true)
                                Log.e("url---2", "" + file.absolutePath)
                                imageUrl = file.absolutePath

                            }
                    }

                } else {
                    arrayList!!.add(pathVideo[0])
                    imageUrl = pathVideo?.get(0)
                        ?.let { ContentUriUtils.getFilePath(requireActivity(), it) }
                    Glide.with(requireActivity()).load(imageUrl).into(ivChallenge)
                    tvUploadImg.text = "";
                    Log.e("video--", "" + pathVideo[0])
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

    private fun startTime(ontime: TextView) {
        val calendar = Calendar.getInstance()
        var startDateAsDate1: Date? = null
        if (startDateAsDate != null)
            if (startDateAsDate == calendar.time) {
                calendar.time = startDateAsDate
            }
        SingleDateAndTimePickerDialog.Builder(requireActivity())
            .bottomSheet()
            .curved()
            .minDateRange(calendar.time)
            .displayMinutes(true)
            .displayHours(true)
            .displayDays(false)
            .displayMonth(false)
            .displayYears(false)
            .displayDaysOfMonth(false)
//            .minDateRange(calendar.time)

            .listener(SingleDateAndTimePickerDialog.Listener() { date: Date? ->
                var myCalendar = Calendar.getInstance()
                myCalendar.time = date
                // dob_string=myCalendar.timeInMillis.toString()
                // val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(AppConstants.CHALLENGE_TIME_FORMAT, Locale.US)
                ontime.text = sdf.format(myCalendar.time)
                startTime = sdf.format(myCalendar.time)
//                endDate = sdf.format(myCalendar.time)
//                endTime = "0" + hour.toString() + ":" + minute.toString()
//                ontime.setText("0" + hour.toString() + ":" + minute.toString())
            })
            .display()

//        var mDialog = Dialog(requireContext())
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        mDialog.setContentView(R.layout.timepickerdia)
////            mDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
//        mDialog.window!!.setLayout(
//            RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        mDialog.window!!.setGravity(Gravity.BOTTOM)
//        mDialog.setCanceledOnTouchOutside(false)
//        mDialog.setCancelable(false)
//
//        mDialog.show()
//
//        val timepicker = mDialog.findViewById<TimePicker>(R.id.timePicker1)
//        val tvdone = mDialog.findViewById<TextView>(R.id.tvdone)
//        var hour: Int
//        var minute: Int
//        var am_pm: String
////        timepicker. setCurrentHour(Date().hours)
////        timepicker.setCurrentMinute(Date().getMinutes())
////        timepicker.currentMinute=Calendar.getInstance().time
//
//        tvdone.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= 23) {
//                hour = timepicker.getHour();
//                minute = timepicker.getMinute();
//            } else {
//                hour = timepicker.getCurrentHour();
//                minute = timepicker.getCurrentMinute();
//            }
////                if(hour > 12) {
////                    am_pm = "PM";
////                    hour = hour - 12;
////                }
////                else
////                {
////                    am_pm="AM";
////                }
//            if (minute < 10) {
//                if (hour < 10) {
//                    startTime = "0" + hour.toString() + ":0" + minute.toString()
//                    ontime.setText("0" + hour.toString() + ":0" + minute.toString())
//                } else {
//                    startTime = hour.toString() + ":0" + minute.toString()
//                    ontime.setText(hour.toString() + ":0" + minute.toString())
//                }
//
//            } else {
//                if (hour < 10) {
//                    startTime = "0" + hour.toString() + ":" + minute.toString()
//                    ontime.setText("0" + hour.toString() + ":" + minute.toString())
//                } else {
//                    startTime = hour.toString() + ":" + minute.toString()
//                    ontime.setText(hour.toString() + ":" + minute.toString())
//                }
//            }
//
//            mDialog.dismiss()
//        }

    }

    private fun endTime(ontime: TextView) {
        SingleDateAndTimePickerDialog.Builder(requireActivity())
            .bottomSheet()
            .curved()
//            .minDateRange(startDateAsDate)
            .displayMinutes(true)
            .displayHours(true)
            .displayDays(false)
            .displayMonth(false)
            .displayYears(false)
            .displayDaysOfMonth(false)
//            .minDateRange(calendar.time)

            .listener(SingleDateAndTimePickerDialog.Listener() { date: Date? ->
                var myCalendar = Calendar.getInstance()
                myCalendar.time = date
                // dob_string=myCalendar.timeInMillis.toString()
                // val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(AppConstants.CHALLENGE_TIME_FORMAT, Locale.US)
                ontime.text = sdf.format(myCalendar.time)
                endTime = sdf.format(myCalendar.time)
//                endDate = sdf.format(myCalendar.time)
//                endTime = "0" + hour.toString() + ":" + minute.toString()
//                ontime.setText("0" + hour.toString() + ":" + minute.toString())
            })
            .display()

//        var mDialog = Dialog(requireContext())
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        mDialog.setContentView(R.layout.timepickerdia)
////            mDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
//        mDialog.window!!.setLayout(
//            RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        mDialog.window!!.setGravity(Gravity.BOTTOM)
//        mDialog.setCanceledOnTouchOutside(false)
//        mDialog.setCancelable(false)
//        mDialog.show()
//
//        val timepicker = mDialog.findViewById<TimePicker>(R.id.timePicker1)
//        val tvdone = mDialog.findViewById<TextView>(R.id.tvdone)
//        var hour: Int
//        var minute: Int
//        var am_pm: String
//
//        tvdone.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= 23) {
//                hour = timepicker.getHour();
//                minute = timepicker.getMinute();
//            } else {
//                hour = timepicker.getCurrentHour();
//                minute = timepicker.getCurrentMinute();
//            }
////                if(hour > 12) {
////                    am_pm = "PM";
////                    hour = hour - 12;
////                }
////                else
////                {
////                    am_pm="AM";
////                }
//
//            if (minute < 10) {
//                if (hour < 10) {
//                    endTime = "0" + hour.toString() + ":0" + minute.toString()
//                    ontime.setText("0" + hour.toString() + ":0" + minute.toString())
//                } else {
//                    endTime = hour.toString() + ":0" + minute.toString()
//                    ontime.setText(hour.toString() + ":0" + minute.toString())
//                }
//            } else {
//                if (hour < 10) {
//                    endTime = "0" + hour.toString() + ":" + minute.toString()
//                    ontime.setText("0" + hour.toString() + ":" + minute.toString())
//                } else {
//                    endTime = hour.toString() + ":" + minute.toString()
//                    ontime.setText(hour.toString() + ":" + minute.toString())
//                }
//            }
//
//            mDialog.dismiss()
//        }
    }

    private fun startDate() {
        Log.e("date:", "working")
//        val calendar = Calendar.getInstance()
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            OnDateSetListener { view, year, month, dayOfMonth ->
//                calendar[year, month] = dayOfMonth
//                val myFormat = AppConstants.CHALLENGE_DATE_FORMAT //In which you need put here
//                val sdf = SimpleDateFormat(myFormat, Locale.US)
//                tvStartDate.text = sdf.format(calendar.time)
//                startDate = sdf.format(calendar.time)
//                startDateAsDate = calendar.time
//            },
//            calendar[Calendar.YEAR],
//            calendar[Calendar.MONTH],
//            calendar[Calendar.DAY_OF_MONTH]
//        )
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
//        datePickerDialog.show()

        SingleDateAndTimePickerDialog.Builder(requireActivity())
            .bottomSheet()
            .curved()
            .displayMinutes(false)
            .displayHours(false)
            .displayDays(false)
            .displayMonth(true)
            .displayYears(true)
            .displayDaysOfMonth(true)
            .minDateRange(Calendar.getInstance().time)
            .listener(SingleDateAndTimePickerDialog.Listener() { date: Date? ->
                var myCalendar = Calendar.getInstance()
                myCalendar.time = date
                // dob_string=myCalendar.timeInMillis.toString()
                // val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(AppConstants.CHALLENGE_DATE_FORMAT, Locale.US)
                val sdf1 = SimpleDateFormat(AppConstants.DOB_FORMAT, Locale.US)
                tvStartDate.text = sdf1.format(myCalendar.time)
                startDate = sdf.format(myCalendar.time)
                startDateAsDate = date
            })
            .display()
    }

    private fun endDate() {
        Log.e("date:", "working")
        val calendar = Calendar.getInstance()
        calendar.time = startDateAsDate
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            OnDateSetListener { view, year, month, dayOfMonth ->
//                calendar[year, month] = dayOfMonth
//                val myFormat = AppConstants.CHALLENGE_DATE_FORMAT //In which you need put here
//                val sdf = SimpleDateFormat(myFormat, Locale.US)
//                tvEndDate.text = sdf.format(calendar.time)
//                endDate = sdf.format(calendar.time)
//            },
//            calendar[Calendar.YEAR],
//            calendar[Calendar.MONTH],
//            calendar[Calendar.DAY_OF_MONTH]
//        )
//        datePickerDialog.datePicker.minDate = startDateAsDate!!.time
//        datePickerDialog.show()
        SingleDateAndTimePickerDialog.Builder(requireActivity())
            .bottomSheet()
            .curved()
            .minDateRange(startDateAsDate)
            .displayMinutes(false)
            .displayHours(false)
            .displayDays(false)
            .displayMonth(true)
            .displayYears(true)
            .displayDaysOfMonth(true)
            .minDateRange(calendar.time)

            .listener(SingleDateAndTimePickerDialog.Listener() { date: Date? ->
                var myCalendar = Calendar.getInstance()
                myCalendar.time = date
                // dob_string=myCalendar.timeInMillis.toString()
                // val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(AppConstants.CHALLENGE_DATE_FORMAT, Locale.US)
                val sdf1 = SimpleDateFormat(AppConstants.DOB_FORMAT, Locale.US)
                tvEndDate.text = sdf1.format(myCalendar.time)
                endDate = sdf.format(myCalendar.time)
            })
            .display()
    }

    private fun validation(): Boolean {
        var valid = true
        var errorMsg: String? = null
        if (etTitle.getText().toString().trim({ it <= ' ' }).isEmpty()) {
            valid = false
            errorMsg = "Title is empty"
        } else if (etDesc.getText().toString().trim({ it <= ' ' }).isEmpty()) {
            valid = false
            errorMsg = "Description is empty"
        } else if (startTime.isNullOrEmpty()) {
            valid = false
            errorMsg = "Start Time is empty"
        } else if (imageUrl.isNullOrEmpty()) {
            valid = false
            errorMsg = "Image/video is empty"
        } else if (startDate.isNullOrEmpty()) {
            valid = false
            errorMsg = "Start Date is empty"
        } else if (endTime.isNullOrEmpty()) {
            valid = false
            errorMsg = "End time is empty"
        } else if (endDate.isNullOrEmpty()) {
            valid = false
            errorMsg = "End Date is empty"
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun createChallengeApi() {
        var timeZone: String? = null

        if (Build.VERSION.SDK_INT >= 26) {
            timeZone = ZonedDateTime.now().zone.toString();
        } else {
            val tz = TimeZone.getDefault()
            val isDaylite = tz.inDaylightTime(Date())
            timeZone = tz.getDisplayName(isDaylite, TimeZone.SHORT)
        }
        Log.e("zone-", "" + timeZone)
        showLoading(true)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("privacy", seenById.toString())
        builder.addFormDataPart("country", cntryIds)
        builder.addFormDataPart("state", stateIds)
        builder.addFormDataPart("city", cityIds)
        builder.addFormDataPart("title", etTitle.text.toString())
        builder.addFormDataPart("description", etDesc.text.toString())
        builder.addFormDataPart("time_from", startTime)
        builder.addFormDataPart("date_from", startDate)
        builder.addFormDataPart("time_to", endTime)
        builder.addFormDataPart("date_to", endDate)
        builder.addFormDataPart("timezone", timeZone)

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images

        val file = File(imageUrl)
        builder.addFormDataPart(
            "media",
            file.getName(),
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )


        val requestBody = builder.build()
        val call: Call<ResponseBody> =
            ApiHelper.createAppService().uploadChallengeFile(requestBody)!!
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>?,
                response: Response<ResponseBody?>
            ) {
                if (isFragmentVisible) {
                    Toast.makeText(
                        requireActivity(),
                        "Challenge created successfully...",
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
//                    CreateChallengeActivity.back()
//                    activity?.supportFragmentManager?.popBackStack()
//                    remove()
                    requireActivity().finish();
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

    interface backListener {
        fun onBack()
    }
}
