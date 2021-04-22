package com.meetfriend.app.base


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.meetfriend.app.utilclasses.CallProgressWheel
import contractorssmart.app.utilsclasses.PreferenceHandler


/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanseState: Bundle?): View? {
        return provideYourFragmentView(inflater, parent, savedInstanseState)
    }
    fun showLoading(show: Boolean?) {
        if (show!!) showLoading() else hideLoading()
    }
    fun showToast(msg: CharSequence) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
    fun showKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.SHOW_FORCED)
        }
    }
    fun hideKeyboard(){
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }
    protected fun showLoading() {
        CallProgressWheel.showLoadingDialog(requireActivity())
       // spotsDialog!!.show()
    }

    protected fun hideLoading() {
        CallProgressWheel.dismissLoadingDialog()
//        if (spotsDialog != null) {
//            spotsDialog!!.dismiss()
//        }
    }
    abstract fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View

    fun setImage(imageView: ImageView, path: String) {
        if(path==null && equals("")){
            return
        }
        Picasso.get().load(path).into(imageView)
    }

    fun getAccessToken():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.AUTHORIZATION_TOKEN,"")
    }
    fun getUserId():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.USER_ID,"")
    }
    fun getProfilePic():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.PROFILE_PHOTO,"")
    }
    fun getCoverPic():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.COVER_PHOTO,"")
    }
    fun getUserName():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.FIRSTNAME,"")+" "+PreferenceHandler.readString(requireActivity(),PreferenceHandler.LASTNAME,"")
    }
    fun getFirstName():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.FIRSTNAME,"")
    }
    fun getLastName():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.LASTNAME,"")
    }
    fun getCity():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.CITY,"")
    }
    fun getEducation():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.EDUCATION,"")
    }
    fun getWork():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.WORK,"")
    }
    fun getDob():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.DOB,"")
    }
    fun getRelationShip():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.RELATIONSHIP,"")
    }
    fun getHobbies():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.HOBBIES,"")
    }
    fun getGender():String{
        return PreferenceHandler.readString(requireActivity(),PreferenceHandler.GENDER,"")
    }
}

