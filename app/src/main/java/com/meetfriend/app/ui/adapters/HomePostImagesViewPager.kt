package com.meetfriend.app.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.homeposts.PostMedium
import com.meetfriend.app.ui.activities.FullScreenActivity
import contractorssmart.app.utilsclasses.CommonMethods


class HomePostImagesViewPager internal constructor(
    val context: Context,
    val postMedia: ArrayList<PostMedium>,
    val baseUrl: String
) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        return postMedia.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            mLayoutInflater.inflate(R.layout.home_view_pager_item, container, false)
        val ivPostPicture: ImageView = itemView.findViewById(R.id.ivPostPicture) as ImageView
        CommonMethods.setImage(ivPostPicture, baseUrl + postMedia.get(position).filePath)
        ivPostPicture.setOnClickListener {
            // val intent = Intent(context, FullScreenActivity::class.java)
            //intent.putExtra("url", baseUrl + postMedia.get(position).filePath)
            //context.startActivity(intent)
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}