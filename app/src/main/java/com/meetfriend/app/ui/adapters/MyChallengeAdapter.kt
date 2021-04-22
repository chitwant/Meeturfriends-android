package com.meetfriend.app.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.challenge.mychallenge.data
import com.meetfriend.app.utilclasses.UtilsClass
import contractorssmart.app.utilsclasses.CommonMethods
import java.io.File
import java.util.*

class MyChallengeAdapter(
    val myChallengeList: ArrayList<data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String,
    val from: String
) : RecyclerView.Adapter<MyChallengeAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun playVideo(path: String)
        fun viewDetails(id: Int)
        fun openProfile(id: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_mychallenge_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return myChallengeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(myChallengeList[position], callBack, context, baseUrl, from)

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(
            dataX: data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String,
            from: String
        ) {
            val lytCotainer = itemView.findViewById(R.id.lytCotainer) as ConstraintLayout
            val userImage = itemView.findViewById(R.id.ivUserPicture) as ImageView
            val userName = itemView.findViewById(R.id.tvUsername) as TextView
            val createTime = itemView.findViewById(R.id.tvPostTime) as TextView
            val tvTitle = itemView.findViewById(R.id.tvPostTitle) as TextView
            val challengeImage = itemView.findViewById(R.id.ivChallenge) as ImageView
            val tvDesc = itemView.findViewById(R.id.tvPostDesc) as TextView
            val tvCreateTime = itemView.findViewById(R.id.tvTime) as TextView
            val ivVideoIcon = itemView.findViewById(R.id.ivVideoIcon) as ImageView

            tvTitle.text = dataX.title
            tvDesc.text = dataX.description
            if (from.equals("done"))
                tvCreateTime.text = "Challenge Over"
            else if (from.equals("pending")) {
                tvCreateTime.text =
                    "Starts From " + dataX.time_from + " " + UtilsClass.formatDate(dataX.date_from)
            } else tvCreateTime.text =
                UtilsClass.printDifference(dataX.date_to + " " + dataX.time_to)

            createTime.text = dataX.created_at
            if (dataX.user != null) {
                userName.text = dataX.user.firstName + " " + dataX.user.lastName
                CommonMethods.setUserImage(
                    userImage,
                    baseUrl + dataX.user.profile_photo
                )
            }
            if (File(dataX.file_path).extension == "mov" ||File(dataX.file_path).extension == "mp4" ||
                File(dataX.file_path).extension == "3gp" ||
                File(dataX.file_path).extension == "mpeg"
            ) {
                CommonMethods.loadImgGlide(baseUrl + dataX.file_path, context, challengeImage)

                challengeImage.setOnClickListener {
                    callBack.playVideo(baseUrl + dataX.file_path)
                }
                challengeImage.setBackgroundColor(Color.parseColor("#80000000"))
                ivVideoIcon.visibility = View.VISIBLE
            } else {
                ivVideoIcon.visibility = View.GONE
                challengeImage.setBackgroundColor(0)
                CommonMethods.setImage(challengeImage, baseUrl + dataX.file_path)
            }

            lytCotainer.setOnClickListener {
                callBack.viewDetails(dataX.id)
            }
            userImage.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
            userName.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
        }
    }
}