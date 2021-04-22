package com.meetfriend.app.ui.fragments

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.firebase.iid.FirebaseInstanceId
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.viewmodal.RegisterViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import contractorssmart.app.utilsclasses.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.header_before_login_screens.*
import java.util.*
import kotlin.collections.HashMap

class LoginFragment : BaseFragment() {
    private var registerViewModal: RegisterViewModal? = null
    private var deviceToken: String? = ""
    var googleSignInClient: GoogleSignInClient? = null
    val RC_SIGN_IN: Int = 1
    var locationTXT: String = ""
    private var getLocation: Boolean = true
    private var isRember: Boolean = false
    private var locationManager: LocationManager? = null

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                deviceToken = instanceIdResult.token
            }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        registerViewModal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        initializeObservers()
        textForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        checboxRememberMe.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            isRember = isChecked
        })
        tvLogin.setOnClickListener {
            hideKeyboard()
            hideKeyboard(requireActivity())
            if (etEmailPhone.text.toString().trim().equals("")) {
                textFieldEmail.error = "Email is empty"
                return@setOnClickListener
            }
            textFieldEmail.error = null
            if (etPassword.text.toString().trim().equals("")) {
                textFieldPassword.error = "Password is empty"
                return@setOnClickListener
            }
            textFieldPassword.error = null
            loginAPI(
                etEmailPhone.text.toString().trim(),
                etPassword.text.toString().trim(),
                "normal",
                "", "", ""
            )
        }
        tvSignupText.setOnClickListener {
            dialog()
            //findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        tvLoginGoogle.setOnClickListener {
            signIn()
        }

        headerLoginBackButton.setOnClickListener { requireActivity().onBackPressed() }
        callPerMission()

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
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result!!)
        } else
            if (requestCode == 222) {
                locationManager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    override fun onPause() {
        super.onPause()
        getLocation = false
    }

    override fun onResume() {
        super.onResume()
        getLocation = true
    }

    fun dialog() {
        val options = arrayOf("Email", "Phone")
        var selectedItem = 0
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Register using")
        builder.setSingleChoiceItems(options, 0, { dialogInterface: DialogInterface, item: Int ->
            selectedItem = item
        })
        builder.setPositiveButton("Proceed", { dialogInterface: DialogInterface, p1: Int ->
            // Toast.makeText(requireActivity(),
            //   "selected item = " + options[selectedItem], Toast.LENGTH_SHORT).show();
            dialogInterface.dismiss()
            if (selectedItem == 0) {
                val bundle = bundleOf(
                    "type" to "email"
                )
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment, bundle)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment)
            }
        })
        /* builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, p1: Int ->
             dialogInterface.dismiss()
         })*/
        builder.create()
        builder.show()
    }

    private fun loginAPI(
        email: String,
        password: String,
        login_type: String,
        google_id: String,
        etFirstName: String,
        etLastName: String
    ) {
        var check: String = "0"
        if (isRember) {
            check = "1"
        }
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
//        CommonMethods.showToastMessageAtTop(requireContext(),locationTXT+" "+CommonMethods.getDeviceName())

        val mHashMap = HashMap<String, Any>()
        mHashMap["email_or_phone"] = email
        mHashMap["password"] = password
        mHashMap["remember"] = check
        mHashMap["device_type"] = "Android"
        mHashMap["device_token"] = deviceToken.toString()
        mHashMap["device_model"] = CommonMethods.getDeviceName()
        mHashMap["device_location"] = locationTXT.toString()
        mHashMap["login_type"] = login_type
        mHashMap["google_id"] = google_id
        mHashMap["firstName"] = etFirstName.trim()
        mHashMap["lastName"] = etLastName.trim()
        registerViewModal?.login(mHashMap)
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (getLocation) {
                var geo = Geocoder(requireContext(), Locale.getDefault())
                var addresses = geo.getFromLocation(location.latitude, location.longitude, 1)
                Log.e("addresses",addresses.get(0).toString())
                if (addresses.isEmpty()) {
                } else {
                    locationTXT = addresses.get(0).getAddressLine(0)
                    PreferenceHandler.writeString(requireContext(),"countryCode",addresses.get(0).countryCode)
                }
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun initializeObservers() {
        registerViewModal?.loginResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    println("Data recieved")
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.AUTHORIZATION_TOKEN,
                        it.access_token
                    )
                    PreferenceHandler.writeString(
                        requireActivity(),
                        PreferenceHandler.PROFILE_UPDATED_STATUS,
                        it.profile_updated_status.toString()
                    )
                    CommonMethods.saveUserData(it.result, requireActivity(), it.media_url)
                    if (it.profile_updated_status) {
                        findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                        requireActivity().finish()
                        PreferenceHandler.writeString(
                            requireActivity(),
                            PreferenceHandler.SHOW_SUGGESTION,
                            "no"
                        )
                    } else {
                        PreferenceHandler.writeString(
                            requireActivity(),
                            PreferenceHandler.SHOW_SUGGESTION,
                            "yes"
                        )
                        findNavController().navigate(R.id.action_loginFragment_to_editProfileInfoFragment)
                    }

                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient?.signInIntent!!
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct: GoogleSignInAccount = result.signInAccount!!
            if (acct != null) {
                Log.e("TAG", "display name: " + acct.displayName)
                val personName = acct.displayName
                if (personName.toString().contains(" ")) {
                    val fname = personName!!.split(" ")[0]
                    val lname = personName.split(" ")[1]
                    val email = acct.email
                    val id = acct.id
                    Log.e("TAG", "Name: " + personName + ", email: " + email)
                    loginAPI(email!!, "", "google", id!!, fname, lname)
                } else {

                    val email = acct.email
                    val id = acct.id
                    Log.e("TAG", "Name: " + personName + ", email: " + email)
                    loginAPI(email!!, "", "google", id!!, personName!!, "")
                }
                //loginAPI(true, acct.id.toString())
                // findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
            }

        } else {
            // Signed out, show unauthenticated UI.
        }
    }
}