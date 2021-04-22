package com.meetfriend.app.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.meetfriend.app.R
import com.meetfriend.app.utilclasses.CallProgressWheel
import com.squareup.picasso.Picasso
import contractorssmart.app.utilsclasses.PreferenceHandler


abstract class BaseActivity : AppCompatActivity() {


    abstract fun initializeLayout(): Int
    abstract fun inflateLayout()

    var baseActivity: BaseActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = this@BaseActivity
        setContentView(initializeLayout())
        inflateLayout()
    }

    fun showToast(msg: CharSequence) {
        Toast.makeText(baseActivity, msg, Toast.LENGTH_SHORT).show()
    }

    fun makeFullscreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun showLoading(show: Boolean?) {
        if (show!!) showLoading() else hideLoading()
    }

    protected fun showLoading() {
        CallProgressWheel.showLoadingDialog(this@BaseActivity)
    }

    protected fun hideLoading() {
        CallProgressWheel.dismissLoadingDialog()
    }

    protected fun showSnackbar(message: String) {
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setImage(imageView: ImageView, path: String) {
        Picasso.get().load(path).into(imageView)

    }

    fun setImageWithPlaceHolder(imageView: ImageView, path: String) {
        Picasso.get().load(path).placeholder(R.drawable.ic_placer_holder_image_new).error(R.drawable.ic_placer_holder_image_new)
            .into(imageView)

    }

    fun setImageWithLoader(imageView: ImageView, path: String, loader: ImageView) {
        loader.visibility = View.VISIBLE
        Picasso.get().load(path)
            .into(imageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    loader.visibility = View.GONE
                }

                override fun onError(e: Exception) {
                    loader.visibility = View.GONE
                }
            })
    }
    fun getFirstName():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.FIRSTNAME,"")
    }
    fun getLastName():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.LASTNAME,"")
    }
    fun getCity():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.CITY,"")
    }
    fun getEducation():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.EDUCATION,"")
    }
    fun getWork():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.WORK,"")
    }
    fun getDob():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.DOB,"")
    }
    fun getRelationShip():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.RELATIONSHIP,"")
    }
    fun getHobbies():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.HOBBIES,"")
    }
    fun getGender():String{
        return PreferenceHandler.readString(this@BaseActivity, PreferenceHandler.GENDER,"")
    }

}
