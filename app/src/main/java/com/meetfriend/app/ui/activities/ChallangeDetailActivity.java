package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ChallengeReplyRowBinding;
import com.meetfriend.app.databinding.FragmentChallengeDetailsBinding;
import com.meetfriend.app.responseclasses.ChallengeDetailDataModel;
import com.meetfriend.app.ui.fragments.challenge.ChallengeAcceptPostFragment;
import com.meetfriend.app.ui.fragments.profile.ProfileFragment;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.ChallengeViewModal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class ChallangeDetailActivity extends AppCompatActivity implements View.OnClickListener, ChallengeAcceptPostFragment.responseCallback {
    Integer id;
    ChallengeViewModal challengeViewModal;
    Context mContext;
    String from = "";
    FragmentChallengeDetailsBinding binding;
    private Integer userId;

    public void getData() {
        HashMap<String, String> mHashMap = new HashMap<>();
        mHashMap.put("challenge_id", String.valueOf(id));
        challengeViewModal.fetchChalllengeDetails(mHashMap);
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_challenge_details);
        mContext = this;
        initViews();

        binding.ivBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.ivUserPicture.setOnClickListener(this);
        binding.tvUsername.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeObservers();
        getData();
        UtilsClass.updateUserStatus(mContext, true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }

    private void initializeObservers() {
        binding.mMainLayout.setVisibility(View.GONE);

        final Observer<ChallengeDetailDataModel> favsObserver = new Observer<ChallengeDetailDataModel>() {
            @Override
            public void onChanged(ChallengeDetailDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == false) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "This challenge has been deleted");
                    finish();
                }else {
                    binding.mMainLayout.setVisibility(View.VISIBLE);
                    userId = response.getResult().getUser().getId();
                    binding.tvUsername.setText(response.getResult().getUser().getFirstName() + " " + response.getResult().getUser().getLastName());
                    binding.tvPostTime.setText(response.getResult().getCreatedAt());
                    binding.tvPostTitle.setText(response.getResult().getTitle());
                    binding.tvPostDesc.setText(response.getResult().getDescription());

                    SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdfmt2 = new SimpleDateFormat("MM/dd/yyyy");
                    Date dDate = null;
                    try {
                        dDate = sdfmt1.parse(response.getResult().getDateFrom());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date dDate1 = null;
                    try {
                        dDate1 = sdfmt1.parse(response.getResult().getDateTo());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String startDate = sdfmt2.format(dDate);
                    String endDate = sdfmt2.format(dDate1);
                    binding.tvStartDate.setText(response.getResult().getTimeFrom() + ", " + startDate);
                    binding.tvEndDate.setText(response.getResult().getTimeTo() + ", " + endDate);

                    String country = "";
                    if (response.getResult().getChallengeCountry().get(0).getCountryData() == null) {
                        country = "All";
                    } else
                        for (int i = 0; i < response.getResult().getChallengeCountry().size(); i++) {
                            if (country.equalsIgnoreCase("")) {
                                country = response.getResult().getChallengeCountry().get(i).getCountryData().getName();
                            } else
                                country = country + "," + response.getResult().getChallengeCountry().get(i).getCountryData().getName();
                        }
                    binding.tvChllngeCntry.setText("-"+country);
                    binding.tvChllngetimezone.setText("Note: Please following " + response.getResult().getTimezone() + " time zone of challenge");
                    CommonMethods.INSTANCE.setUserImage(
                            binding.ivUserPicture,
                            response.getMediaUrl() + response.getResult().getUser().getProfilePhoto()
                    );
                    if (response.getResult().getFilePath().toString().endsWith("mp4") ||
                            response.getResult().getFilePath().toString().endsWith("3gp") ||
                            response.getResult().getFilePath().toString().endsWith("mpeg")
                    ) {
//                    CommonMethods.INSTANCE.setImage(binding.ivChallenge, response.getMediaUrl() + response.getResult().getFilePath());
                        Glide.with(ChallangeDetailActivity.this).load(response.getMediaUrl() + response.getResult().getFilePath()).into(binding.ivChallenge);
                        binding.ivChallenge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChallangeDetailActivity.this, VideoPlayActivity.class);
                                intent.putExtra("path", response.getMediaUrl() + "" + response.getResult().getFilePath());
                                startActivity(intent);
                            }
                        });
                        binding.ivChallenge.setBackgroundColor(Color.parseColor("#80000000"));
                        binding.ivVideoIcon.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivChallenge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChallangeDetailActivity.this, FullScreenActivity.class);
                                intent.putExtra("url", response.getMediaUrl() + "" + response.getResult().getFilePath());
                                startActivity(intent);
                            }
                        });
                        binding.ivVideoIcon.setVisibility(View.GONE);

                        binding.ivChallenge.setBackgroundColor(0);
                        CommonMethods.INSTANCE.setImage(binding.ivChallenge, response.getMediaUrl() + response.getResult().getFilePath());
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    try {
                        Date date1 = new Date();
                        Date date2 = simpleDateFormat.parse(response.getResult().getDateTo() + " " + response.getResult().getTimeTo());

                        String time = printDifference(date1, date2);
                        if (time.equalsIgnoreCase("Challenge Over")) {
                            from = "done";
                        }
                        if (from.equalsIgnoreCase("my") || from.equalsIgnoreCase("live"))
                            binding.tvTimeLeft.setText(time);
                        else if (from.equalsIgnoreCase("winner") || from.equalsIgnoreCase("done"))
                            binding.tvTimeLeft.setText("Challenge Over");
                        else if (from.equalsIgnoreCase("pending"))
                            binding.tvTimeLeft.setText("Start From: " + response.getResult().getTimeFrom() + " " + UtilsClass.formatDate(response.getResult().getDateFrom()));
                        if (from.equalsIgnoreCase("live")) {
                            binding.mBottomView.setVisibility(View.VISIBLE);
                            if (response.getResult().getChallengeReactions() != null)
                                if (response.getResult().getChallengeReactions().getStatus() != 0) {
                                    binding.mBottomView.setVisibility(View.GONE);
                                }
                        }
                        if (response.getResult().getChallengePosts().size() > 0) {
                            List<ChallengeDetailDataModel.ChallengePost> list = response.getResult().getChallengePosts();
                            Collections.sort( list,new Comparator<ChallengeDetailDataModel.ChallengePost>(){
                                public int compare(ChallengeDetailDataModel.ChallengePost obj1, ChallengeDetailDataModel.ChallengePost obj2) {
                                    // ## Ascending order
                                    return obj2.getNoOfLikesCount().compareTo(obj1.getNoOfLikesCount()); // To compare string values
                                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                                     }
                            });
                            binding.mRecyclerView.setAdapter(new ChallengePostAdapter(list, response.getMediaUrl()));
                        } else {
                            binding.mRecyclerView.setVisibility(View.GONE);
//                        binding.tvTxt.setVisibility(View.GONE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        };
        challengeViewModal.getChallengeDetailsReponse().observe(this, favsObserver);
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long difference_In_Time = endDate.getTime() - startDate.getTime();

        long difference_In_Seconds
                = (difference_In_Time
                / 1000)
                % 60;

        long elapsedMinutes
                = (difference_In_Time
                / (1000 * 60))
                % 60;

        long elapsedHours
                = (difference_In_Time
                / (1000 * 60 * 60))
                % 24;

        long difference_In_Years
                = (difference_In_Time
                / (1000l * 60 * 60 * 24 * 365));

        long elapsedDays
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        if (Integer.parseInt(String.format("%d", elapsedDays)) > 0)
            return String.format("%d", elapsedDays) + " Day Left";
        else if (Integer.parseInt(String.format("%d", elapsedHours)) > 0) {
            return String.format("%d", elapsedHours) + " Hour Left";
        } else if (Integer.parseInt(String.format("%d", elapsedMinutes)) > 0) {
            return String.format("%d", elapsedMinutes) + " Minutes Left";
        } else if (Integer.parseInt(String.format("%d", difference_In_Seconds)) > 0) {
            return String.format("%d", difference_In_Seconds) + " Seconds Left";
        } else return "Challenge Over";
    }

    private void initViews() {
        challengeViewModal =
                new ViewModelProvider(this).get(ChallengeViewModal.class);
        id = getIntent().getIntExtra("id", 0);
        from = getIntent().getStringExtra("from");
        binding.mAccept.setOnClickListener(this);
        binding.mReject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mAccept:
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("from", "detail");
                bundle.putString("title", binding.tvPostTitle.getText().toString());

                Fragment fragment = new ChallengeAcceptPostFragment(this);
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("Accept")
                        .commit();
                break;
            case R.id.mReject:
                HashMap mHashMap = new HashMap<String, Object>();
                mHashMap.put("challenge_id", id);
                mHashMap.put("status", 2);
                challengeViewModal.challengeAcceptPost(mHashMap);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CommonMethods.INSTANCE.showToastMessageAtTop(ChallangeDetailActivity.this, "challenge Rejected successfully.");
                        binding.mBottomView.setVisibility(View.GONE);
                    }
                }, 1000);
                break;
            case R.id.ivUserPicture:
            case R.id.tvUsername:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "other");
                bundle1.putString("userId", userId.toString());
                bundle1.putString("from", "activity");

                Fragment fragment1 = new ProfileFragment();
                fragment1.setArguments(bundle1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment1)
                        .addToBackStack("fas")
                        .commit();
                break;
        }

    }

    @Override
    public void getDetails() {
        getData();
    }

    private class ChallengePostAdapter extends RecyclerView.Adapter<ChallengePostAdapter.ViewHolder> {
        List<ChallengeDetailDataModel.ChallengePost> challengePosts;
        String mediaUrl;

        public ChallengePostAdapter(List<ChallengeDetailDataModel.ChallengePost> challengePosts, String mediaUrl) {
            this.challengePosts = challengePosts;
            this.mediaUrl = mediaUrl;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ChallengeReplyRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(ChallangeDetailActivity.this), R.layout.challenge_reply_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.tvUsername.setText(challengePosts.get(position).getUser().getFirstName() + " " + challengePosts.get(position).getUser().getLastName());
            holder.binding.tvPostTime.setText(challengePosts.get(position).getCreatedAt());
            holder.binding.tvPostTitle.setText(challengePosts.get(position).getDescription());
            holder.binding.mView.setText(challengePosts.get(position).getNoOfViewsCount() + " views");
            holder.binding.mLike.setText(challengePosts.get(position).getNoOfLikesCount() + " likes");
            if (challengePosts.get(position).getIsLikedCount() == 1) {
                holder.binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.ic_post_liked_red_heart));
                holder.binding.mLikeImage.setTag("1");
            } else
                holder.binding.mLikeImage.setTag("0");

            CommonMethods.INSTANCE.setUserImage(
                    holder.binding.ivUserPicture,
                    mediaUrl + challengePosts.get(position).getUser().getProfilePhoto()
            );

            holder.binding.ivUserPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", "other");
                    bundle1.putString("userId", challengePosts.get(position).getUser().getId().toString());
                    bundle1.putString("from", "activity");

                    Fragment fragment1 = new ProfileFragment();
                    fragment1.setArguments(bundle1);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragment1)
                            .addToBackStack("fas")
                            .commit();
                }
            });
            holder.binding.tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", "other");
                    bundle1.putString("userId", challengePosts.get(position).getUser().getId().toString());
                    bundle1.putString("from", "activity");

                    Fragment fragment1 = new ProfileFragment();
                    fragment1.setArguments(bundle1);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragment1)
                            .addToBackStack("fas")
                            .commit();
                }
            });
            if (challengePosts.get(position).getFilePath() != null)
                if (challengePosts.get(position).getFilePath().toString().endsWith("mp4") ||
                        challengePosts.get(position).getFilePath().toString().endsWith("3gp") ||
                        challengePosts.get(position).getFilePath().toString().endsWith("mpeg")
                ) {
                    holder.binding.ivVideoIcon.setVisibility(View.VISIBLE);
                    CommonMethods.INSTANCE.loadImgGlide(mediaUrl + challengePosts.get(position).getFilePath(), ChallangeDetailActivity.this, holder.binding.ivChallenge);

                } else {
                    CommonMethods.INSTANCE.setImage(
                            holder.binding.ivChallenge,
                            mediaUrl + challengePosts.get(position).getFilePath()
                    );
                }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int liked = 0;
                    if (holder.binding.mLikeImage.getTag().toString().equalsIgnoreCase("1")) {
                        liked = 1;
                    } else {
                        liked = 0;
                    }
                    Intent intent = new Intent(ChallangeDetailActivity.this, WinnerDetailActivity.class);
                    intent.putExtra("userName", challengePosts.get(position).getUser().getFirstName() + " " + challengePosts.get(position).getUser().getLastName());
                    intent.putExtra("url", mediaUrl);
                    intent.putExtra("path", challengePosts.get(position).getFilePath());
                    intent.putExtra("desc", challengePosts.get(position).getDescription());
                    intent.putExtra("time", challengePosts.get(position).getCreatedAt());
                    intent.putExtra("likedCount", liked);
                    intent.putExtra("viewCount", challengePosts.get(position).getNoOfViewsCount());
                    intent.putExtra("likeCount", Integer.parseInt(holder.binding.mLike.getText().toString().replace("likes", "").trim()));
                    intent.putExtra("pic", challengePosts.get(position).getUser().getProfilePhoto());
                    intent.putExtra("pid", challengePosts.get(position).getId().toString());
                    intent.putExtra("cid", challengePosts.get(position).getChallengeId().toString());
                    intent.putExtra("id", challengePosts.get(position).getId().toString());
                    intent.putExtra("uid", challengePosts.get(position).getUser().getId().toString());
                    intent.putExtra("from", from);
                    startActivity(intent);
                }
            });
            holder.binding.ivChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (challengePosts.get(position).getFilePath() != null)
                        if (challengePosts.get(position).getFilePath().toString().endsWith("mp4") ||
                                challengePosts.get(position).getFilePath().toString().endsWith("3gp") ||
                                challengePosts.get(position).getFilePath().toString().endsWith("mpeg")
                        ) {
                            Intent intent = new Intent(ChallangeDetailActivity.this, VideoPlayActivity.class);
                            intent.putExtra("path", mediaUrl + "" + challengePosts.get(position).getFilePath());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ChallangeDetailActivity.this, FullScreenActivity.class);
                            intent.putExtra("url", mediaUrl + "" + challengePosts.get(position).getFilePath());
                            startActivity(intent);
                        }
                }
            });
            if (PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "").equalsIgnoreCase(challengePosts.get(position).getUser().getId().toString())) {
                holder.binding.mLikeImage.setVisibility(View.GONE);
            }
            holder.binding.mLikeImage.setOnClickListener(v -> {
                if (!from.equalsIgnoreCase("done") && !PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "").equalsIgnoreCase(challengePosts.get(position).getUser().getId().toString()))
                    if (holder.binding.mLikeImage.getTag().toString().equalsIgnoreCase("1")) {
                        holder.binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.heart_icon_outline));
                        holder.binding.mLikeImage.setTag("0");
                        HashMap<String, Object> mHashMap = new HashMap<>();
                        mHashMap.put("challenge_id", challengePosts.get(position).getChallengeId().toString());
                        mHashMap.put("challenge_post_id", challengePosts.get(position).getId().toString());
                        mHashMap.put("status", "0");
                        challengeViewModal.likeUnlikeChalenge(mHashMap);
                        CommonMethods.INSTANCE.showToastMessageAtTop(ChallangeDetailActivity.this, "Unliked Successfully");
                        String like = holder.binding.mLike.getText().toString().replace("likes", "").trim();
                        if (!like.equalsIgnoreCase("0")) {
                            Integer count = Integer.parseInt(like) - 1;
                            holder.binding.mLike.setText(count + " likes");
                        }
                    } else {
                        holder.binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.ic_post_liked_red_heart));
                        holder.binding.mLikeImage.setTag("1");
                        HashMap<String, Object> mHashMap = new HashMap<>();
                        mHashMap.put("challenge_id", challengePosts.get(position).getChallengeId().toString());
                        mHashMap.put("challenge_post_id", challengePosts.get(position).getId().toString());
                        mHashMap.put("status", "1");
                        challengeViewModal.likeUnlikeChalenge(mHashMap);
                        CommonMethods.INSTANCE.showToastMessageAtTop(ChallangeDetailActivity.this, "Liked Successfully");
                        String like = holder.binding.mLike.getText().toString().replace("likes", "").trim();
                        Integer count = Integer.parseInt(like) + 1;
                        holder.binding.mLike.setText(count + " likes");
                    }
            });
            holder.binding.mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChallangeDetailActivity.this, LikedUserListActivity.class);
                    intent.putExtra("pid", challengePosts.get(position).getId().toString());
                    intent.putExtra("id", challengePosts.get(position).getChallengeId().toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return challengePosts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ChallengeReplyRowBinding binding;

            public ViewHolder(@NonNull ChallengeReplyRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}