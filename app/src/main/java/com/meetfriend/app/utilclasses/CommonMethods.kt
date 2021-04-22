package contractorssmart.app.utilsclasses

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.Result
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.URL


object CommonMethods {
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    fun fromHtml(html: String?): Spanned? {
        return if (html == null) {
            // return an empty spannable if the html is null
            SpannableString("")
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun createRequestBody(data: String?): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), data)
    }

    fun showSnackbarMessageWithoutColor(context: Context, message: String) {
        val snackbar = Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    fun showToastMessageAtTop(context: Context, message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 150)
        toast.show()
    }

    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
        }
    }

    fun saveUserData(
        userData: Result,
        requireActivity: FragmentActivity,
        mediaUrl: String
    ) {
        if (userData.id != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.USER_ID,
                userData.id.toString()
            )
        }
        if (userData.firstName != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.FIRSTNAME,
                userData.firstName
            )
        }
        if (userData.lastName != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.LASTNAME,
                userData.lastName
            )
        }

        if (userData.city != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.CITY,
                userData.city
            )
        }
        if (userData.education != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.EDUCATION,
                userData.education
            )
        }

        if (userData.work != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.WORK,
                userData.work
            )
        }
        if (userData.gender != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.GENDER,
                userData.gender
            )
        }
        if (userData.dob != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.DOB,
                userData.dob
            )
        }

        if (userData.relationship != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.RELATIONSHIP,
                userData.relationship
            )
        }
        if (userData.hobbies != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.HOBBIES,
                userData.hobbies
            )
        }
        if (userData.email_or_phone != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.EMAIL_OR_PHONE,
                userData.email_or_phone
            )
        }
        if (userData.profile_photo != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.PROFILE_PHOTO,
                mediaUrl + userData.profile_photo
            )
        }
        if (userData.cover_photo != null) {
            PreferenceHandler.writeString(
                requireActivity,
                PreferenceHandler.COVER_PHOTO,
                mediaUrl + userData.cover_photo
            )
        }
    }

    fun setLocalImage(imageView: ImageView, path: String) {
        if (path == null || path.equals("")) {
            return
        }
        Picasso.get().load("file://" + path).placeholder(R.drawable.ic_placer_holder_image_new)
            .error(R.drawable.ic_placer_holder_image_new).into(imageView)
    }

    fun setImage(imageView: ImageView, path: String) {
        if (path == null || path.equals("")) {
            return
        }
        Glide.with(imageView.context)
            .load(URL(path))

            .placeholder(R.drawable.ic_placer_holder_image_new)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setImageDrawable(resource)
                    return false
                }
            })
            .into(imageView)
//        Glide.with(imageView.context).load(path)                .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .placeholder(R.drawable.ic_placer_holder_image_new).into(imageView)
//        Picasso.get().load(path).placeholder(R.drawable.ic_placer_holder_image_new)
//            .error(R.drawable.ic_placer_holder_image_new).into(imageView)
    }

    fun setImagePicassoResize(imageView: ImageView, path: String) {
        if (path == null || path.equals("")) {
            return
        }
        Picasso.get().load(path).placeholder(R.drawable.ic_placer_holder_image_new).resize(256, 256)
            .error(R.drawable.ic_placer_holder_image_new).into(imageView)
    }

    fun setImageWithGlideForVideo(imageView: ImageView, path: String, context: Context) {
        if (path == null || path.equals("")) {
            return
        }
        Glide.with(imageView.context)
            .load(Uri.parse(path.trim()))
            .override(300, 400)
            .skipMemoryCache(true)
            .disallowHardwareConfig()  .placeholder(R.drawable.ic_placer_holder_image_new)
            .thumbnail()
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {


                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setImageDrawable(resource)
                    return false
                }
            })
            .into(imageView)
    }

    fun setUserImage(imageView: ImageView, path: String) {
        if (path == null || path.equals("")) {
            return
        }
        Picasso.get().load(path).placeholder(R.drawable.ic_user_profile_pic_new)
            .error(R.drawable.ic_user_profile_pic_new).into(imageView)
    }

    fun setUserImageManPlaceHolder(imageView: ImageView, path: String) {
        if (path == null || path.equals("")) {
            return
        }
        Picasso.get().load(path).placeholder(R.drawable.man_new)
            .error(R.drawable.man_new).into(imageView)
    }

    open fun loadImgGlide(path: String, activity: Context, imageView: ImageView) {
        if (path == null || path.equals("")) {
            return
        }
        Glide.with(imageView.context)
            .load(Uri.parse(path.trim()))
            .thumbnail()
            .disallowHardwareConfig()     .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placer_holder_image_new)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setImageDrawable(resource)
                    return false
                }
            })
            .into(imageView)
    }
}