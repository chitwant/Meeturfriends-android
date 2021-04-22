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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityTaggedListBinding;
import com.meetfriend.app.databinding.ItemviewTagPeopleItemBinding;
import com.meetfriend.app.responseclasses.homeposts.TaggedUser;
import com.meetfriend.app.ui.fragments.profile.ProfileFragment;

import java.util.ArrayList;

public class TaggedListActivity extends AppCompatActivity {
    Context mContext = this;
    ActivityTaggedListBinding binding;
    String baseUrl;
    private ArrayList<TaggedUser> homePostList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tagged_list);
        initViews();
    }

    private void initViews() {
        homePostList = getIntent().getParcelableArrayListExtra("list");
        baseUrl = getIntent().getStringExtra("baseUrl");
        position = getIntent().getIntExtra("position", 0);
        binding.ivBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.recyclerViewSearchListing.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        binding.recyclerViewSearchListing.setAdapter(new TaggedAdspter(mContext, homePostList));
    }

    private class TaggedAdspter extends RecyclerView.Adapter<TaggedAdspter.ViewHolder> {
        Context mContext;
        ArrayList<TaggedUser> tagged_users_list;

        public TaggedAdspter(Context mContext, ArrayList<TaggedUser> tagged_users_list) {
            this.mContext = mContext;
            this.tagged_users_list = tagged_users_list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ItemviewTagPeopleItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.tvUpdateInfo.setVisibility(View.GONE);
            holder.binding.tvUserName.setText(tagged_users_list.get(position).getUser().getFirstName() + " " + tagged_users_list.get(position).getUser().getLastName());
            Glide.with(mContext).load(baseUrl + tagged_users_list.get(position).getUser().getProfilePhoto()).placeholder(getDrawable(R.drawable.image_placeholder)).into(holder.binding.ivUserProfilePic);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", "other");
                    bundle1.putString("userId", tagged_users_list.get(position).getUser().getId().toString());
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
        }

        @Override
        public int getItemCount() {
            return tagged_users_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemviewTagPeopleItemBinding binding;

            public ViewHolder(@NonNull ItemviewTagPeopleItemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}