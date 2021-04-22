package com.meetfriend.app.utilclasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.meetfriend.app.responseclasses.ChatStatusPOJO;
import com.meetfriend.app.responseclasses.InboxPOJO;
import com.meetfriend.app.responseclasses.friends.Data;
import com.meetfriend.app.ui.adapters.FriendAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class UtilsClass {
    public static String FIREBASE_USER_STATUS = "user_status";
    public static String NODE_STATUS = "status";
    public static String NODE_LAST_SEEN = "last_seen";
    public static String FIREBASE_NODE = "chat";
    static DatabaseReference fireMessages, fireUserStatus, fireUserLastSeen;
    static ValueEventListener fireMessagesListener, fireUserStatusListener, fireUserLastSeenListener;
    private static Boolean status = false;
   static FriendAdapter adapters;
    static  List<InboxPOJO> chatList ;
    public static void loadAd(AdView adViewFull) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewFull.loadAd(adRequest);
        adViewFull.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("error_code",errorCode+"");
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    public static List<InboxPOJO> checkFriends(ArrayList<Data> list, Activity mContext,  final FriendAdapter adapter , final RecyclerView recyclerView) {
        adapters=adapter;
        fireUserStatus = FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS);
        fireUserStatusListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.getValue() != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (String.valueOf(list.get(i).getAccepted_user().getId()).equalsIgnoreCase(data.getKey())&& data.child(NODE_STATUS).getValue().toString().equalsIgnoreCase("true")) {
                                    InboxPOJO item = data.getValue(InboxPOJO.class);
                                    chatList.add(item);
                                }
                            }

                        } else {

                        }
                    }
                    if (chatList.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        adapters = new FriendAdapter(chatList, mContext, "");
                        recyclerView.setAdapter(adapters);
                        adapters.notifyDataSetChanged();
                    }else {
                        recyclerView.setVisibility(View.GONE);
                    }

                } else {
                    status = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                status = false;
            }
        };
        fireUserStatus.addValueEventListener(fireUserStatusListener);
        return chatList;
    }

    @BindingAdapter("loadProfileImage")
    public static void loadProfileImage(ImageView view, String url) {
        CommonMethods.INSTANCE.setUserImage(view, url);
    }

    public static void updateUserStatus(Context mContext, boolean status) {
        String FIREBASE_USERS = "users";

        String user_id = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
        String userName = PreferenceHandler.INSTANCE.readString(mContext, "FIRSTNAME", "");
        String lastName = PreferenceHandler.INSTANCE.readString(mContext, "LASTNAME", "");
        String userImage = PreferenceHandler.INSTANCE.readString(mContext, "PROFILE_PHOTO", "");
//        if (getLoggedInUser(mContext) != null) {
        FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS)
                .child(user_id).child(NODE_STATUS).setValue(status);
        FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS)
                .child(user_id).child(NODE_LAST_SEEN).setValue(ServerValue.TIMESTAMP);

        DatabaseReference fireTable = FirebaseDatabase.getInstance().getReference().child(FIREBASE_USER_STATUS)
                .child(user_id).child(FIREBASE_USERS);
        ChatStatusPOJO item;
        item = new ChatStatusPOJO(user_id,
                userName + " " + lastName,
                userImage);

        fireTable.setValue(item);
//        }
    }

    public static void animateView(ViewGroup root) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(root);
        }
    }


    public static String getPassedTimeString(Context mContext, String timeStamp) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        long epochInMillis = Long.parseLong(timeStamp);
        Calendar now = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(epochInMillis);
        timeToCheck.setTimeZone(tz);
        if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return "Today, " + getTimeFromTimeStamp(mContext, timeStamp);
        } else if (yesterday.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR) &&
                now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)) {
            return "Yesterday, " + getTimeFromTimeStamp(mContext, timeStamp);
        } else {
            if (daysBetween(timeToCheck, now) == 1)
                return daysBetween(timeToCheck, now) + " day ago";
            else return daysBetween(timeToCheck, now) + " days ago";
        }
    }

    private static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(), dayTwo = (Calendar) day2.clone();
        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;
            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);
            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public static String getTimeFromTimeStamp(Context mContext, String TimeInMilis) {
        return DateUtils.formatDateTime(mContext, Long.parseLong(TimeInMilis), DateUtils.FORMAT_SHOW_TIME);
    }

    public static String decodeFile(String path) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;
        int desiredWidth = 600;
        int desiredHeight = 600;
        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, desiredWidth, desiredHeight, ScalingUtilities.ScalingLogic.FIT);
            if (!(unscaledBitmap.getWidth() <= desiredWidth && unscaledBitmap.getHeight() <= desiredHeight)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, desiredWidth, desiredHeight, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }
            // Store to tmp file
            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/Psychic_Heroes");
            if (!mFolder.exists()) {
                boolean isDir = mFolder.mkdir();
            }
            String s = "IMAGE" + "_" + System.currentTimeMillis() + ".png";
            File f = new File(mFolder.getAbsolutePath(), s);
            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scaledBitmap.recycle();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;
    }


    public static String getDateTimeFromTimeStamp(String TimeInMilis) {
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("d MMM yyyy, h:mm a", Locale.US);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(TimeInMilis));
        weekDay = dayFormat.format(calendar.getTime());
        return weekDay;
    }

    public static AlertDialog.Builder showDialog(Context mContext, String message, String title, String positiveText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, String negativeText, String neutralText, DialogInterface.OnClickListener neutralListener, boolean isCancelable) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setNegativeButton(negativeText, negativeListener);
        alert.setPositiveButton(positiveText, positiveListener);
        alert.setNeutralButton(neutralText, neutralListener);
        alert.setCancelable(isCancelable);
        try {
            alert.show();
        } catch (Exception e) {
        }
        return alert;
    }

    public static String printDifference(String endDate1) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        Date date1 = new Date();
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //milliseconds
        long difference_In_Time = date2.getTime() - date1.getTime();

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

    public static String formatDate(String date) {
        SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfmt2 = new SimpleDateFormat("MM/dd/yyyy");

        Date dDate1 = null;
        try {
            dDate1 = sdfmt1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfmt2.format(dDate1);
    }
}
