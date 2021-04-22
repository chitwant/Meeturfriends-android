package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.os.Bundle;
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

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityLikedUserListBinding;
import com.meetfriend.app.databinding.LikedUserRowBinding;
import com.meetfriend.app.responseclasses.LikedUserDataModel;
import com.meetfriend.app.ui.fragments.profile.ProfileFragment;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.ChallengeViewModal;

import java.util.HashMap;
import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;

public class LikedUserListActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLikedUserListBinding binding;
    String id;
    String pid;
    Context mContext;
    ChallengeViewModal challengeViewModal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_liked_user_list);
        mContext = this;
        initViews();
        initializeObservers();

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("challenge_id", String.valueOf(id));
        mHashMap.put("challenge_post_id", String.valueOf(pid));
        challengeViewModal.likedListChalenge(mHashMap);
        UtilsClass.updateUserStatus(mContext,true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }
    private void initializeObservers() {
        final Observer<LikedUserDataModel> favsObserver = new Observer<LikedUserDataModel>() {
            @Override
            public void onChanged(LikedUserDataModel response) {
                if (response.getStatus() == true) {
                    if (response.getResult().getData().size() > 0) {
                        binding.mRecyclerView.setAdapter(new LikedUserAdapter(response.getResult().getData(), response.getMediaUrl()));

                    } else {
                        CommonMethods.INSTANCE.showToastMessageAtTop(mContext, response.getMessage());
                    }
                }
            }
        };
        challengeViewModal.getChallengeLikedlistReponse().observe(this, favsObserver);
    }


    private void initViews() {
        id = getIntent().getStringExtra("id");
        pid = getIntent().getStringExtra("pid");
        binding.headerLoginBackButton.setOnClickListener(this);
        challengeViewModal =
                new ViewModelProvider(this).get(ChallengeViewModal.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLoginBackButton:
                finish();
                break;
        }
    }

    private class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.ViewHolder> {
        List<LikedUserDataModel.Datum> data;
        String url;

        public LikedUserAdapter(List<LikedUserDataModel.Datum> data, String url) {
            this.data = data;
            this.url = url;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LikedUserRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.liked_user_row, parent, false);

            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CommonMethods.INSTANCE.setImage(holder.binding.ivUserPicture, url + data.get(position).getUser().getProfilePhoto());
            holder.binding.tvUsername.setText(data.get(position).getUser().getFirstName() + " " + data.get(position).getUser().getLastName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "other");
                    bundle.putString("userId", data.get(position).getUser().getId().toString());
                    bundle.putString("from", "activity");

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragment)
                            .addToBackStack("fas")
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LikedUserRowBinding binding;

            public ViewHolder(@NonNull LikedUserRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}