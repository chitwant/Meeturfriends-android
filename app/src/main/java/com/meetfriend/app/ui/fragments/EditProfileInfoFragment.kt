package com.meetfriend.app.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.network.ApiHelper
import com.meetfriend.app.responseclasses.UpdatePhotoResponse
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.HomeActivity
import com.meetfriend.app.ui.activities.LoginActivity
import com.meetfriend.app.ui.fragments.profile.ProfileFragment
import com.meetfriend.app.utilclasses.AppConstants.DOB_FORMAT
import com.meetfriend.app.utilclasses.AppConstants.UPDATE_PROFILE_RECIEVER
import com.meetfriend.app.utilclasses.ErrorHandler
import com.meetfriend.app.viewmodal.RegisterViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_edit_profile_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.collections.HashMap


class EditProfileInfoFragment : BaseFragment() {
    private var registerViewModal: RegisterViewModal? = null
    val myCalendar: Calendar = Calendar.getInstance()
    private var genderSelectedValue = ""
    private var relationShipValue = ""
    private var imageType = ""
    private var came_from = ""
    private var dob_string = ""
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_profile_info, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            came_from = bundle.getString("came_from", "")
        }

        registerViewModal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        initializeObservers()
        setData()
        tvUpdateInfo.setOnClickListener {
            validations()
        }
        etDOB.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(requireActivity())
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .maxDateRange(Calendar.getInstance().time)
                .listener({ date: Date? ->
                    val currentDateObject = getInstance().time
                    val yearsDifference = getDiffYears(date, currentDateObject)
                    if (yearsDifference >= 13) {
                        var myCalendar = Calendar.getInstance()
                        myCalendar.time = date
                        // dob_string = myCalendar.timeInMillis.toString()
                        // dob_string=date!!.time.toString()
                        //  val myFormat = "dd-MM-yyyy" //In which you need put here
                        val sdf = SimpleDateFormat(DOB_FORMAT, Locale.US)
                        etDOB.setText(sdf.format(myCalendar.time))
                        // dob_string=myCalendar.time.toString()
                        dob_string = myCalendar.timeInMillis.toString()
                    } else {
                        CommonMethods.showToastMessageAtTop(
                            requireActivity(),
                            "Date of birth should be minimum 13 years"
                        )
                    }

                })
                .display()
            /*   val datepicker = DatePickerDialog(
                   requireActivity(), date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                   myCalendar[Calendar.DAY_OF_MONTH]
               )
               datepicker.datePicker.maxDate = myCalendar.timeInMillis
               datepicker.show()*/
        }
        if (came_from.equals("profile")) {
            tvSkip.visibility = View.GONE
            headerLoginBackButton.visibility = View.VISIBLE
        Handler().postDelayed(Runnable {     (activity as HomeActivity).showAndHideBottomNavigation(false)
            (activity as HomeActivity).toolbar!!.visibility = View.GONE },300)
        } else {

            tvSkip.visibility = View.GONE
            headerLoginBackButton.visibility = View.GONE
        }
        tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileInfoFragment_to_homeActivity)
            requireActivity().finish()
            PreferenceHandler.writeString(
                requireActivity(),
                PreferenceHandler.SHOW_SUGGESTION,
                "no"
            )
        }
        headerLoginBackButton.setOnClickListener {
            if (!came_from.equals("profile")) {
                PreferenceHandler.clearSharePreferences(requireContext())
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }else{
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (came_from.equals("profile")) {
            (activity as HomeActivity).showAndHideBottomNavigation(true)
            (activity as HomeActivity).toolbar!!.visibility = View.VISIBLE
        }
    }

    fun getDiffYears(first: Date?, last: Date?): Int {
        val a = getCalendar(first)
        val b = getCalendar(last)
        var diff = b[YEAR] - a[YEAR]
        if (a[MONTH] > b[MONTH] ||
            a[MONTH] === b[MONTH] && a[DATE] > b[DATE]
        ) {
            diff--
        }
        return diff
    }

    fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    private fun setData() {
        etFirstName.setText(
            PreferenceHandler.readString(
                requireActivity(),
                PreferenceHandler.FIRSTNAME,
                ""
            )
        )
        etLastName.setText(
            PreferenceHandler.readString(
                requireActivity(),
                PreferenceHandler.LASTNAME,
                ""
            )
        )
        etCity.setText(PreferenceHandler.readString(requireActivity(), PreferenceHandler.CITY, ""))
        etEducation.setText(
            PreferenceHandler.readString(
                requireActivity(),
                PreferenceHandler.EDUCATION,
                ""
            )
        )
        etWork.setText(PreferenceHandler.readString(requireActivity(), PreferenceHandler.WORK, ""))
        etDOB.setText(PreferenceHandler.readString(requireActivity(), PreferenceHandler.DOB, ""))
        if (getDob() != null && !getDob().equals("")) {
            dob_string = milliseconds(getDob()).toString()
        }
        etHobbies.setText(
            PreferenceHandler.readString(
                requireActivity(),
                PreferenceHandler.HOBBIES,
                ""
            )
        )
        radioGroupGender.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            genderSelectedValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        radioRelationShip.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            relationShipValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        if (PreferenceHandler.readString(requireActivity(), PreferenceHandler.GENDER, "")
                .equals("Male", true)
        ) {
            radioMale.isChecked = true
        } else if (PreferenceHandler.readString(requireActivity(), PreferenceHandler.GENDER, "")
                .equals("Female", true)
        ) {
            radioFemale.isChecked = true
        } else {
            radioOther.isChecked = true
        }
        if (getRelationShip().equals("Single", true)) {
            radioSingle.isChecked = true
        } else if (getRelationShip().equals("Married", true)) {
            radioMarried.isChecked = true
        } else {
            radioUnmarried.isChecked = true
        }
        CommonMethods.setUserImage(ivProfilePic, getProfilePic())
        CommonMethods.setUserImage(ivCoverPic, getCoverPic())
        ivProfilePic.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra(
                "url",
                getProfilePic()
            )
            requireActivity().startActivity(intent)
        }
        ivEditProfilePic.setOnClickListener {
            //ivProfilePic.performClick()
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
        ivCoverPic.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra(
                "url",
                getCoverPic()
            )
            requireActivity().startActivity(intent)
        }
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
    }

    fun milliseconds(date: String?): Long {
        //String date_ = date;
        val sdf = SimpleDateFormat(DOB_FORMAT)
        try {
            val mDate = sdf.parse(date)
            val timeInMilliseconds = mDate.time
            println("Date in milli :: $timeInMilliseconds")
            return timeInMilliseconds
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return 0
    }

    private fun validations() {
        if (etFirstName.text.toString().trim().equals("")) {
            textFieldName.error = "First name is empty"
            return
        }
        textFieldName.error = null
        if (etLastName.text.toString().trim().equals("")) {
            textFieldLastname.error = "Last name is empty"
            return
        }
        textFieldLastname.error = null
        if (etCity.text.toString().trim().equals("")) {
            textFieldCity.error = "City is empty"
            return
        }
        textFieldCity.error = null
        if (etEducation.text.toString().trim().equals("")) {
            textFieldEducation.error = "Education is empty"
            return
        }
        textFieldEducation.error = null
        if (etWork.text.toString().trim().equals("")) {
            textFieldWork.error = "Work is empty"
            return
        }
        textFieldWork.error = null
        if (etDOB.text.toString().trim().equals("")) {
            textFieldDOB.error = "Date Of Birth is empty"
            return
        }
        textFieldDOB.error = null
        if (etHobbies.text.toString().trim().equals("")) {
            textFieldHobbies.error = "Hobbies is empty"
            return
        }
        textFieldHobbies.error = null
        updateAPI()
    }

    var date =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = DOB_FORMAT //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            etDOB.setText(sdf.format(myCalendar.time))
        }

    private fun updateAPI() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["firstName"] = etFirstName.text.toString().trim()
        mHashMap["lastName"] = etLastName.text.toString().trim()
        mHashMap["city"] = etCity.text.toString().trim()
        mHashMap["education"] = etEducation.text.toString().trim()
        mHashMap["work"] = etWork.text.toString().trim()
        mHashMap["gender"] = genderSelectedValue
        mHashMap["dob"] = etDOB.text.toString().trim()
        mHashMap["dob_string"] = dob_string
        mHashMap["relationship"] = relationShipValue
        mHashMap["hobbies"] = etHobbies.text.toString().trim()
        registerViewModal?.editProfileInfo(mHashMap)
    }

    private fun initializeObservers() {
        registerViewModal?.editProfileInfoResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.FIRSTNAME,
                        etFirstName.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.LASTNAME,
                        etLastName.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.CITY,
                        etCity.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.WORK,
                        etWork.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.EDUCATION,
                        etEducation.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.GENDER,
                        genderSelectedValue
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.DOB,
                        etDOB.text.toString().trim()
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.RELATIONSHIP,
                        relationShipValue
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.HOBBIES,
                        etHobbies.text.toString().trim()
                    )
                    if (came_from != null && !came_from.equals("")) {
                        requireActivity().onBackPressed()
                        sendBroadcast()
                    } else {
                        findNavController().navigate(R.id.action_editProfileInfoFragment_to_homeActivity)
                        requireActivity().finish()
                        PreferenceHandler.writeString(
                            requireActivity(),
                            PreferenceHandler.SHOW_SUGGESTION,
                            "yes"
                        )
                    }

                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    //CommonMethods.showSnackbarMessageWithoutColor(requireActivity(), it.message)
                }
            }

        })
        registerViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                // CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        registerViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        registerViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ErrorHandler.getMessage(it, requireActivity())
                )
                /*  CommonMethods.showSnackbarMessageWithoutColor(
                      requireActivity(),
                      ApiFailureTypes().getFailureMessage(it, requireActivity())
                  )*/
            }
        })
    }

    /**
     * Send broadcast method
     */
    fun sendBroadcast() {
        val intent = Intent(UPDATE_PROFILE_RECIEVER)
        intent.putExtra("key", "Your data")
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent);
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
                if (response.body() != null && response.body()!!.media_url != null && response.body()!!.profile_photo != null) {
                    Toast.makeText(
                        requireActivity(),
                        "Profile picture updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    CommonMethods.setImage(
                        ivProfilePic,
                        response.body()!!.media_url + response.body()!!.profile_photo
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.PROFILE_PHOTO,
                        response.body()!!.media_url + response.body()!!.profile_photo
                    )
                }
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())

                //requireActivity().onBackPressed()
            }

            override fun onFailure(call: Call<UpdatePhotoResponse?>?, t: Throwable) {
                Log.d("AddStoryPostFragment", "Error " + t.message)
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ErrorHandler.getMessage(t, activity!!)
                )
                /* CommonMethods.showToastMessageAtTop(

                     requireActivity(),
                     ApiFailureTypes().getFailureMessage(t, requireActivity())
                 )*/
                showLoading(false)
            }
        })
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
                if (response.body() != null && response.body()!!.media_url != null && response.body()!!.cover_photo != null) {
                    Toast.makeText(
                        requireActivity(),
                        "Cover picture updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    CommonMethods.setImage(
                        ivCoverPic,
                        response.body()!!.media_url + response.body()!!.cover_photo
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.COVER_PHOTO,
                        response.body()!!.media_url + response.body()!!.cover_photo
                    )
                }
                //CommonMethods.showToastMessageAtTop(requireActivity(), response.message())

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
}