package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityWinnerDetailBinding;
import com.meetfriend.app.ui.fragments.profile.ProfileFragment;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.ChallengeViewModal;

import java.util.HashMap;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class WinnerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityWinnerDetailBinding binding;
    Integer viewCount, likeCount, likedCount;
    String name, time, desc, path, id, pic, url, cid, pid, from, uid;
    Context mContext;
    ChallengeViewModal challengeViewModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_winner_detail);
        mContext = this;
        initViews();
    }

    private void initViews() {
        challengeViewModal = new ViewModelProvider(this).get(ChallengeViewModal.class);
        viewCount = getIntent().getIntExtra("viewCount", 0);
        likeCount = getIntent().getIntExtra("likeCount", 0);
        likedCount = getIntent().getIntExtra("likedCount", 0);
        uid = getIntent().getStringExtra("uid");
        pid = getIntent().getStringExtra("pid");
        cid = getIntent().getStringExtra("cid");
        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("userName");
        path = getIntent().getStringExtra("path");
        pic = getIntent().getStringExtra("pic");
        desc = getIntent().getStringExtra("desc");
        time = getIntent().getStringExtra("time");
        from = getIntent().getStringExtra("from");
        binding.headerLoginBackButton.setOnClickListener(this);
        binding.mLike.setOnClickListener(this);
        binding.mLikeImage.setOnClickListener(this);
        binding.tvUsername.setOnClickListener(this);
        binding.ivUserPicture.setOnClickListener(this);

        setData();
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("challenge_id", String.valueOf(cid));
        mHashMap.put("challenge_post_id", String.valueOf(pid));
        challengeViewModal.challengeView(mHashMap);
    }

    private void setData() {
        if (likedCount == 1) {
            binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.ic_post_liked_red_heart));
            binding.mLikeImage.setTag("1");
        } else
            binding.mLikeImage.setTag("0");
        if (path != null)
            if (path.endsWith("mp4") || path.endsWith("3gp") || path.endsWith("mpeg")) {
                CommonMethods.INSTANCE.loadImgGlide(url + path, mContext, binding.ivChallenge);

                binding.ivChallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.putExtra("path", url + "" + path);
                        startActivity(intent);
                    }
                });
                binding.ivChallenge.setBackgroundColor(Color.parseColor("#80000000"));
                binding.ivVideoIcon.setVisibility(View.VISIBLE);
            } else {
                binding.ivChallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FullScreenActivity.class);
                        intent.putExtra("url", url + "" + path);
                        startActivity(intent);
                    }
                });
                binding.ivVideoIcon.setVisibility(View.GONE);
                binding.ivChallenge.setBackgroundColor(0);
                CommonMethods.INSTANCE.setImage(binding.ivChallenge, url + path);
            }
        CommonMethods.INSTANCE.setImage(binding.ivUserPicture, url + pic);
        binding.mLike.setText(likeCount + " like");
        binding.mView.setText(viewCount + " views");
        binding.tvPostDesc.setText(desc);
        binding.tvPostTime.setText(time);
        binding.tvUsername.setText(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilsClass.updateUserStatus(mContext, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLoginBackButton:
                finish();
                break;
            case R.id.mLike:
                Intent intent1 = new Intent(mContext, LikedUserListActivity.class);
                intent1.putExtra("pid", pid);
                intent1.putExtra("id", cid);
                startActivity(intent1);
                break;
            case R.id.mLikeImage:
                if (!from.equalsIgnoreCase("done") && !PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "").equalsIgnoreCase(uid))
                    if (binding.mLikeImage.getTag().toString().equalsIgnoreCase("1")) {
                        binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.heart_icon_outline));
                        binding.mLikeImage.setTag("0");
                        HashMap<String, Object> mHashMap = new HashMap<>();
                        mHashMap.put("challenge_id", cid);
                        mHashMap.put("challenge_post_id", pid);
                        mHashMap.put("status", "0");
                        challengeViewModal.likeUnlikeChalenge(mHashMap);
                        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Unliked Successfully");
                        String like = binding.mLike.getText().toString().replace("like", "").trim();
                        if (!like.equalsIgnoreCase("0")) {
                            Integer count = Integer.parseInt(like) - 1;
                            binding.mLike.setText(count + " like");
                        }
                    } else {
                        binding.mLikeImage.setImageDrawable(getDrawable(R.drawable.ic_post_liked_red_heart));
                        binding.mLikeImage.setTag("1");
                        HashMap<String, Object> mHashMap = new HashMap<>();
                        mHashMap.put("challenge_id", cid);
                        mHashMap.put("challenge_post_id", pid);
                        mHashMap.put("status", "1");
                        challengeViewModal.likeUnlikeChalenge(mHashMap);
                        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Liked Successfully");
                        String like = binding.mLike.getText().toString().replace("like", "").trim();
                        Integer count = Integer.parseInt(like) + 1;
                        binding.mLike.setText(count + " like");
                    }
                break;
            case R.id.ivUserPicture:
            case R.id.tvUsername:
                Bundle bundle = new Bundle();
                bundle.putString("type", "other");
                bundle.putString("userId", uid);
                bundle.putString("from", "activity");

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment)
                        .addToBackStack("fas")
                        .commit();
                break;
        }
    }
}