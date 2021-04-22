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

class LiveChallengeAdapter(
    val myChallengeList: ArrayList<data>,
    var callBack: AdapterCallback,
    val context: Context,
    val baseUrl: String
) : RecyclerView.Adapter<LiveChallengeAdapter.ViewHolder>() {
    interface AdapterCallback {
        fun playVideo(path: String)
        fun viewDetails(id: Int)
        fun acceptChallenge(challengeId: Int, title: String)
        fun rejectChallenge(challengeId: Int)
        fun openProfile(id: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_livechallenge_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return myChallengeList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(myChallengeList[position], callBack, context, baseUrl)

    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItems(
            dataX: data,
            callBack: AdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            val userImage = itemView.findViewById(R.id.ivUserPicture) as ImageView
            val userName = itemView.findViewById(R.id.tvUsername) as TextView
            val createTime = itemView.findViewById(R.id.tvPostTime) as TextView
            val mRow = itemView.findViewById(R.id.mRow) as ConstraintLayout
            val tvTitle = itemView.findViewById(R.id.tvPostTitle) as TextView
            val challengeImage = itemView.findViewById(R.id.ivChallenge) as ImageView
            val tvDesc = itemView.findViewById(R.id.tvPostDesc) as TextView
            val tvCreateTime = itemView.findViewById(R.id.tvTime) as TextView
            val ivVideoIcon = itemView.findViewById(R.id.ivVideoIcon) as ImageView
            val ivAccept = itemView.findViewById(R.id.ivAccept) as ImageView
            val ivReject = itemView.findViewById(R.id.ivReject) as ImageView

            tvCreateTime.text = UtilsClass.printDifference(dataX.date_to + " " + dataX.time_to)
            tvTitle.text = dataX.title
            tvDesc.text = dataX.description
            createTime.text = dataX.created_at
            if (dataX.challenge_reactions != null)
                if (dataX.challenge_reactions.status != 0) {
                    ivAccept.visibility = View.GONE
                    ivReject.visibility = View.GONE
                }
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
                challengeImage.setOnClickListener {
                    callBack.playVideo(baseUrl + dataX.file_path)
                }
            }

            mRow.setOnClickListener {
                callBack.viewDetails(dataX.id)
            }
            userImage.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
            userName.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
            ivAccept.setOnClickListener {
                callBack.acceptChallenge(dataX.id, dataX.title)
            }
            ivReject.setOnClickListener {
                callBack.rejectChallenge(dataX.id)
            }
        }
    }
}