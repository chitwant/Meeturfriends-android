package com.meetfriend.app.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.challenge.winner.data
import com.meetfriend.app.responseclasses.homeposts.Datum
import com.meetfriend.app.ui.activities.ChallangeDetailActivity
import com.meetfriend.app.ui.activities.LikedUserListActivity
import com.meetfriend.app.ui.activities.WinnerDetailActivity
import contractorssmart.app.utilsclasses.CommonMethods

class WinnerChallengeAdapter(
    val winnerList: ArrayList<data>,
    var callBack: WinnerChallengeAdapterCallback,
    val context: Context,
    val baseUrl: String
) : RecyclerView.Adapter<WinnerChallengeAdapter.ViewHolder>() {
    interface WinnerChallengeAdapterCallback {
        fun viewDetails(id: Int)
        fun openProfile(id: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_winnerchallenge_item, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return winnerList.size
    }

    override fun onBindViewHolder(holder: WinnerChallengeAdapter.ViewHolder, position: Int) {
        holder.bindItems(winnerList[position], callBack, context, baseUrl)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            dataX: data,
            callBack: WinnerChallengeAdapterCallback,
            context: Context,
            baseUrl: String
        ) {
            val lytCotainer = itemView.findViewById(R.id.lytCotainer) as ConstraintLayout
            val tvWinnerLike = itemView.findViewById(R.id.tvWinnerLike) as TextView
            val tvWinnerView = itemView.findViewById(R.id.tvWinnerView) as TextView
            val tvUsername = itemView.findViewById(R.id.tv_name) as TextView
            val tvChallenge = itemView.findViewById(R.id.tvWinnerChallege) as TextView
            val ivUserPicture = itemView.findViewById(R.id.ivProfilePic) as ImageView
            val ivWinnerLike = itemView.findViewById(R.id.ivWinnerLike) as ImageView
            tvWinnerLike.text = dataX.no_of_likes_count.toString() + " Like"
            tvWinnerView.text = dataX.no_of_views_count.toString() + " View"
            tvChallenge.text ="Challenge: "+ dataX.challenge.title
            tvUsername.text = dataX.user.firstName + " " + dataX.user.lastName
            CommonMethods.setUserImage(
                ivUserPicture,
                baseUrl + dataX.user.profile_photo
            )
            lytCotainer.setOnClickListener {
                val intent = Intent(context, ChallangeDetailActivity::class.java)
                intent.putExtra("id", dataX.challenge_id);
                intent.putExtra("from", "done");
               context.startActivity(intent)
            }
            ivUserPicture.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
            tvUsername.setOnClickListener {
                callBack.openProfile(dataX.user.id)
            }
            tvWinnerLike.setOnClickListener {
                val intent = Intent(context, LikedUserListActivity::class.java)
                intent.putExtra("id",dataX.challenge_id.toString());
                intent.putExtra("pid",dataX.id.toString());
                context. startActivity(intent)
            }
            ivWinnerLike.setOnClickListener {
                val intent = Intent(context, LikedUserListActivity::class.java)
                intent.putExtra("pid",dataX.id.toString());
                intent.putExtra("id",dataX.challenge_id.toString());
                context. startActivity(intent)
            }
        }
    }
}
