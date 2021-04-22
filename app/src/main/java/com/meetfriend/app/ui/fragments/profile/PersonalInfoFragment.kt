package com.meetfriend.app.ui.fragments.profile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.viewprofile.Result
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.AppConstants.UPDATE_PROFILE_RECIEVER
import com.meetfriend.app.viewmodal.RegisterViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_personal_info.*
import java.util.*

class PersonalInfoFragment(
    val result: Result,
    val type: String
) : BaseFragment() {
    private var userType = ""
    private var registerViewModal: RegisterViewModal? = null
    private var genderSelectedValue = ""
    private var relationShipValue = ""

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_personal_info, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        initializeObservers()
        setData()
        tvUpdateInfo.setOnClickListener {
            validations()
        }
        // register broadcast manager
        val localBroadcastManager = LocalBroadcastManager.getInstance(requireActivity())
        localBroadcastManager.registerReceiver(receiver, IntentFilter(UPDATE_PROFILE_RECIEVER))
    }

    // broadcast receiver
    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val str = intent.getStringExtra("key")
                setLocalData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (type.equals("own", true)) {
            setLocalData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
    }

    private fun setData() {
        etFirstName.setText(result.firstName)
        etLastName.setText(result.lastName)
        etCity.setText(result.city)
        etEducation.setText(result.education)
        etWork.setText(result.work)
        etDOB.setText(result.dob)
        etHobbies.setText(result.hobbies)
        radioGroupGender.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            genderSelectedValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        //radioGroupGender.check(R.id.radioMale)
        radioRelationShip.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            relationShipValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        if (result.gender.equals("Male", true)) {
            radioMale.isChecked = true
        } else if (result.gender.equals("Female", true)) {
            radioFemale.isChecked = true
            radioOther.isChecked = false
        } else {
            radioOther.isChecked = true
            radioFemale.isChecked = false
        }

        if (result.relationship.equals("Single", true)) {
            radioSingle.isChecked = true
            radioUnmarried.isChecked = false
            radioMarried.isChecked = false
        } else if (result.relationship.equals("Married", true)) {
            radioMarried.isChecked = true
            radioSingle.isChecked = false
        } else {
            radioUnmarried.isChecked = true
            radioSingle.isChecked = false
            radioMarried.isChecked = false
        }

        if (type.equals("own", true)) {
            ivEditProfile.visibility = View.VISIBLE
        } else {
            ivEditProfile.visibility = View.GONE
        }
        ivEditProfile.setOnClickListener {
            //tvUpdateInfo.visibility = View.VISIBLE
            val bundle = bundleOf(
                "came_from" to "profile"
            )
            findNavController().navigate(
                R.id.action_profileFragment_to_editProfileInfoFragment2,
                bundle
            )
        }
    }

    private fun setLocalData() {
        etFirstName.setText(getFirstName())
        etLastName.setText(getLastName())
        etCity.setText(getCity())
        etEducation.setText(getEducation())
        etWork.setText(getWork())
        etDOB.setText(getDob())
        etHobbies.setText(getHobbies())
        radioGroupGender.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            genderSelectedValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        //radioGroupGender.check(R.id.radioMale)
        radioRelationShip.setOnCheckedChangeListener({ group, checkedId ->
            val rb =
                group.findViewById<View>(checkedId) as RadioButton
            relationShipValue = rb.text.toString()
            /*if (null != rb && checkedId > -1) {
                Toast.makeText(this@MainActivity, rb.text, Toast.LENGTH_SHORT).show()
            }*/
        })
        if (getGender().equals("Male", true)) {
            radioMale.isChecked = true
        } else if (getGender().equals("Female", true)) {
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


    }

    private fun initializeObservers() {
        registerViewModal?.editProfileInfoResponse!!.observe(requireActivity(), Observer {
            it.let {
                if (it.status) {
                    println("Data recieved")
                    tvUpdateInfo.visibility = View.GONE
                    //findNavController().navigate(R.id.action_editProfileInfoFragment_to_homeActivity)
                    // requireActivity().finish()
                    /* PreferenceHandler.writeString(
                         requireActivity(),
                         PreferenceHandler.SHOW_SUGGESTION,
                         "yes"
                     )*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
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
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
                /*  CommonMethods.showSnackbarMessageWithoutColor(
                      requireActivity(),
                      ApiFailureTypes().getFailureMessage(it, requireActivity())
                  )*/
            }
        })
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


    private fun updateAPI() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["firstName"] = etFirstName.text.toString().trim()
        mHashMap["lastName"] = etLastName.text.toString().trim()
        mHashMap["city"] = etCity.text.toString().trim()
        mHashMap["education"] = etEducation.text.toString().trim()
        mHashMap["work"] = etWork.text.toString().trim()
        mHashMap["gender"] = genderSelectedValue
        mHashMap["dob"] = etDOB.text.toString().trim()
        mHashMap["relationship"] = relationShipValue
        mHashMap["hobbies"] = etHobbies.text.toString().trim()
        mHashMap["dob_string"] = etHobbies.text.toString().trim()
        registerViewModal?.editProfileInfo(mHashMap)
    }

}