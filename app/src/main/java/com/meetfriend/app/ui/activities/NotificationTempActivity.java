package com.meetfriend.app.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.meetfriend.app.R;
import com.meetfriend.app.ui.fragments.settings.NotificationsFragment;
import com.meetfriend.app.utilclasses.UtilsClass;

public class NotificationTempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_temp);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new NotificationsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilsClass.updateUserStatus(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(this, false);
    }
}