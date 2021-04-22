package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityCallendedBinding;

public class CallendedActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    ActivityCallendedBinding binding;
    private String friendId = "";
    private String nick_name = "";
    private String time = "";
    private String from = "";
    private String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_callended);
        mContext = this;

        initViews();
    }

    private void initViews() {
        from = getIntent().getStringExtra("from");
        friendId = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        nick_name = getIntent().getStringExtra("name");
        time = getIntent().getStringExtra("time");
        binding.mName.setText(nick_name);
        binding.mTime.setText(time + " Call Ended");
        Glide.with(mContext).load(image).placeholder(R.drawable.ic_user_profile_pic_new).into(binding.mImage);
        binding.mCall.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCall:
                startActivity(new Intent(mContext, AudioCallActivity.class)
                        .putExtra("from", from)
                        .putExtra("id", friendId)
                        .putExtra("name", nick_name)
                        .putExtra("image", image));

                break;

        }
    }
}