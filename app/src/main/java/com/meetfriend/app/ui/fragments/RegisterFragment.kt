package com.meetfriend.app.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.firebase.iid.FirebaseInstanceId
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.AppConstants.DOB_FORMAT
import com.meetfriend.app.viewmodal.RegisterViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.header_before_login_screens.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


class RegisterFragment : BaseFragment() {
    private var deviceToken: String = ""
    var locationTXT: String = ""
    private var type = ""
    private var number = ""
    private var country = ""
    private var registerViewModal: RegisterViewModal? = null
    val myCalendar: Calendar = Calendar.getInstance()
    private var genderSelectedValue = "Male"
    private var locationManager: LocationManager? = null
    private var getLocation: Boolean = true
    private var dob_string = ""

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                deviceToken = instanceIdResult.token
            }
        type = this.requireArguments()!!.getString("type", "")
        if (this.requireArguments().getString("number", "") != null) {
            number = this.requireArguments().getString("number", "")
        }
        if (this.requireArguments().getString("country", "") != null) {
            country = this.requireArguments().getString("country", "")
        }
        registerViewModal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        iniData()

    }

    private fun iniData() {
        callPerMission()

        initializeObservers()
        tvLogin.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            validations()
        }
        etDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -13)
            calendar.add(Calendar.MONTH, 0)
            calendar.add(Calendar.DAY_OF_MONTH, 0)
            SingleDateAndTimePickerDialog.Builder(requireActivity())
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .maxDateRange(calendar.time)
                .listener(SingleDateAndTimePickerDialog.Listener() { date: Date? ->
                    var myCalendar = Calendar.getInstance()
                    myCalendar.time = date
                    if (calendar.time.before(myCalendar.time)) {
                        CommonMethods.showToastMessageAtTop(
                            requireContext(),
                            "Age can not be less then 13 years"
                        )
                    } else {
                        // dob_string=myCalendar.timeInMillis.toString()
                        // val myFormat = "dd-MM-yyyy" //In which you need put here
                        val sdf = SimpleDateFormat(DOB_FORMAT, Locale.US)
                        etDateOfBirth.setText(sdf.format(myCalendar.time))
                        dob_string = myCalendar.timeInMillis.toString()
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
        if (type.equals("email")) {
            textFieldEmailPhone.hint = "Email"
            etEmailPhone.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        } else {
            textFieldEmailPhone.hint = "Phone"
            etEmailPhone.inputType = InputType.TYPE_CLASS_PHONE
            //etEmailPhone.setText(country + " - " + number)
            etEmailPhone.setText(number)
            etEmailPhone.isEnabled = false
        }

        radioGroupGender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            genderSelectedValue = rb.text.toString()
            /* if (null != rb && checkedId > -1) {
                 Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
             }*/
        })
        tvLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        radioGroupGender.check(R.id.radioMale)

        headerLoginBackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
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
        if (type.equals("Email", true) && etEmailPhone.text.toString().trim().equals("")) {
            textFieldEmailPhone.error = "Email is empty"
            return
        }
        if (type.equals("Email", true) && !CommonMethods.isValidEmail(
                etEmailPhone.text.toString().trim()
            )
        ) {
            textFieldEmailPhone.error = "Invalid email"
            return
        }
        textFieldEmailPhone.error = null
        if (etDateOfBirth.text.toString().trim().equals("")) {
            textFieldDateOfBirth.error = "Date Of Birth is empty"
            return
        }
        textFieldDateOfBirth.error = null
        if (etPassword.text.toString().trim().equals("")) {
            textFieldPassword.error = "Password is empty"
            return
        }
        textFieldPassword.error = null
        /*if (etPassword.length() < 8) {
            textFieldPassword.error = "Password length should minimum 8"
            return
        }
        textFieldPassword.error = null*/
        if (!isValidPasswordFormat(etPassword.text.toString().trim())) {
            textFieldPassword.error =
                "Password must be atleast 8 characters long, containing lower, upper case, number and special characters"
            return
        }
        textFieldPassword.error = null
        if (etConfirmPassword.text.toString().trim().equals("")) {
            textFieldConfirmPassword.error = "Confirm Password is empty"
            return
        }
        textFieldConfirmPassword.error = null
        if (!etPassword.text.toString().trim().equals(etConfirmPassword.text.toString().trim())) {
            textFieldConfirmPassword.error = "Password and Confirm Password should be same"
            return
        }
        textFieldConfirmPassword.error = null
        hideKeyboard()
        hideKeyboard(requireActivity())
        if (!checkboxTerms.isChecked) {
            CommonMethods.showSnackbarMessageWithoutColor(
                requireActivity(),
                "Accept terms and conditions to proceed"
            )
            return
        }
        registerAPI()
    }

    private fun callPerMission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 111
            ) else {
                locationManager =
                    requireActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("keshav", "Gps already enabled")
                    enableLoc()
                } else {

                    try {
                        // Request location updates
                        locationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        )
                    } catch (ex: SecurityException) {
                        Log.d("myTag", "Security Exception, no location available")
                    }
                }
            }
        } else {
            locationManager =
                requireActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                // Request location updates
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }
        }
    }

    private fun enableLoc() {
        val googleApiClient = GoogleApiClient.Builder(requireContext())
            .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2.toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //                            status.startResolutionForResult(getActivity(), 222);
                    startIntentSenderForResult(
                        status?.resolution?.intentSender,
                        222,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    /* "(?=.*[0-9])" +   */      //at least 1 digit
                    /*    "(?=.*[a-z])" + */        //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=?])" +    //at least 1 special character
                    /*"(?=\\S+$)" + */          //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        return passwordREGEX.matcher(password).matches()
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (getLocation) {
                var geo = Geocoder(requireContext(), Locale.getDefault());
                var addresses = geo.getFromLocation(location.latitude, location.longitude, 1);
                if (addresses.isEmpty()) {
                } else {
                    locationTXT = addresses.get(0).getAddressLine(0)
                }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onPause() {
        super.onPause()
        getLocation = false
    }

    override fun onResume() {
        super.onResume()
        getLocation = true
    }

    private fun registerAPI() {
        if (locationTXT.equals("")) {
            callPerMission()
            CommonMethods.showToastMessageAtTop(
                requireContext(),
                "We are trying to get your location.Please wait or Please try again"
            )
            return
        }
        if (deviceToken.equals("")) {
            callPerMission()
            CommonMethods.showToastMessageAtTop(
                requireContext(),
                "Unable to get device token. Please try again"
            )
            return
        }
        if (CommonMethods.getDeviceName().equals("")) {
            CommonMethods.getDeviceName()
            CommonMethods.showToastMessageAtTop(
                requireContext(),
                "Unable to get device name. Please try again"
            )
            return
        }
        val mHashMap = HashMap<String, Any>()
        mHashMap["firstName"] = etFirstName.text.toString().trim()
        mHashMap["lastName"] = etLastName.text.toString().trim()
        mHashMap["email_or_phone"] = etEmailPhone.text.toString().trim()
        mHashMap["country_code"] = country
        mHashMap["dob"] = etDateOfBirth.text.toString().trim()
        mHashMap["dob_string"] = dob_string
        mHashMap["gender"] = genderSelectedValue
        mHashMap["password"] = etPassword.text.toString().trim()
        mHashMap["confirmPassword"] = etConfirmPassword.text.toString().trim()
        mHashMap["device_type"] = "Android"
        mHashMap["device_token"] = deviceToken
        mHashMap["device_model"] = CommonMethods.getDeviceName()
        mHashMap["device_location"] = locationTXT
        registerViewModal?.register(mHashMap)
    }

    private fun initializeObservers() {
        registerViewModal?.registerResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    println("Data recieved")
                    if (type.equals("email", true)) {
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        if (it.access_token != null) {
                            PreferenceHandler.writeString(
                                requireActivity(),
                                PreferenceHandler.AUTHORIZATION_TOKEN,
                                it.access_token
                            )
                        }
                        PreferenceHandler.writeInteger(
                            requireActivity(),
                            "bankUpdated",
                            it.result.user_customer_count
                        )

                        PreferenceHandler.writeString(
                            requireActivity(),
                            PreferenceHandler.PROFILE_UPDATED_STATUS,
                            it.profile_updated_status.toString()
                        )
                        PreferenceHandler.writeString(
                            requireActivity(),
                            PreferenceHandler.SHOW_SUGGESTION,
                            "yes"
                        )
                        CommonMethods.saveUserData(it.result, requireActivity(), it.media_url)
                        findNavController().navigate(R.id.action_registerFragment_to_editProfileInfoFragment)
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

        })
        registerViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
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
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
                /* CommonMethods.showSnackbarMessageWithoutColor(
                     requireActivity(),
                     ApiFailureTypes().getFailureMessage(it, requireActivity())
                 )*/
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 222) {
            locationManager =
                requireActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                // Request location updates
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasAllPermissionsGranted(grantResults)) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                return
            }
            Log.e("location request", "reached")
            locationManager =
                requireActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("keshav", "Gps already enabled")
                enableLoc()
            } else {
                try {
                    // Request location updates
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0L,
                        0f,
                        locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("myTag", "Security Exception, no location available")
                }
            }
        }
    }

    fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }


}