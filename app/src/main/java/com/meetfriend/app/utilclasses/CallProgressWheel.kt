package com.meetfriend.app.utilclasses


import android.app.ProgressDialog
import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.meetfriend.app.ApplicationClass
import com.meetfriend.app.R
import com.wang.avi.AVLoadingIndicatorView


object CallProgressWheel {

    private var progressDialog: ProgressDialog? = null

    private val isDialogShowing: Boolean
        get() {
            try {
                return progressDialog != null && progressDialog!!.isShowing
            } catch (e: Exception) {
                return false
            }

        }

    /*
      Displays custom loading dialog
     */
    fun showLoadingDialog(context: Context?) {
        try {
            if (isDialogShowing) {
                dismissLoadingDialog()
            }

            if (context is AppCompatActivity) {
                if (context.isFinishing) {
                    return
                }
            }
            val avLoadingIndicatorView = AVLoadingIndicatorView(ApplicationClass.instance)
            avLoadingIndicatorView.setIndicator("LineSpinFadeLoaderIndicator")
            avLoadingIndicatorView.setIndicatorColor(ApplicationClass.instance.resources.getColor(R.color.colorPrimaryDark))
            progressDialog = ProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar)
            progressDialog!!.show()

            val layoutParams = progressDialog!!.window!!.attributes
            layoutParams.dimAmount = 0.5f
//            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
//            layoutParams.height =WindowManager.LayoutParams.WRAP_CONTENT

            layoutParams.width = 140
            layoutParams.height =140

            progressDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            progressDialog!!.setCancelable(false)
            //progressDialog!!.setContentView(R.layout.layout_progress)

            progressDialog!!.setContentView(avLoadingIndicatorView)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dismissLoadingDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}