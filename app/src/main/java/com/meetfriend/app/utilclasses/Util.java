package com.meetfriend.app.utilclasses;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meetfriend.app.ApplicationClass;
import com.meetfriend.app.network.ApiInterface;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import contractorssmart.app.utilsclasses.PreferenceHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {

    /**
     * Check internet availabilty
     *
     * @param mContext Context of activity or fragment
     * @return Returns true is internet connected and false if no internet connected
     */
    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Show alert dialog
     *
     * @param mContext         Context of activity o fragment
     * @param message          Message that shows on Dialog
     * @param title            Title for dialog
     * @param positiveText     Set positive text
     * @param positiveListener Set functionality on positive button click
     * @param negativeListener Set functionality on negative button click
     * @param negativeText     Negative button text
     * @param neutralText      Neturat button text
     * @param neutralListener  Set Netural button listener
     * @param isCancelable     true -> Cancelable True ,false -> Cancelable False
     * @return dialog
     */
    public static AlertDialog.Builder showDialog(Context mContext, String message, String title, String positiveText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, String negativeText, String neutralText, DialogInterface.OnClickListener neutralListener, boolean isCancelable) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setNegativeButton(negativeText, negativeListener);
        alert.setPositiveButton(positiveText, positiveListener);
        alert.setNeutralButton(neutralText, neutralListener);
        alert.setCancelable(isCancelable);
        alert.show();
        return alert;
    }

    /**
     * show Toast message in app
     *
     * @param activity
     * @param msg
     */
    public static void showMessage(Context activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    public static boolean compareTimeBetweenTwoTimes(String currenttime) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getCurrentDateTime = sdf1.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startCompare = sdf.parse(currenttime);
            Date endCompare = sdf.parse(getCurrentDateTime);
            if (startCompare.after(endCompare)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("exp", "in catch");
            e.printStackTrace();
            return false;
        }
    }



    public static String getTimeFromTimeStamp(Context mContext, String TimeInMilis) {
        return DateUtils.formatDateTime(mContext, Long.parseLong(TimeInMilis), DateUtils.FORMAT_SHOW_TIME);
    }

    public static String getDuration(String startDate) {
        Date past = new Date();
        past.setTime(Long.parseLong(startDate));
        String diff = "";
        String suffix = "Ago";
        try {
            Date now = new Date();
            long dateDiff = now.getTime() - (past.getTime());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hours = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long days = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (seconds < 60) {
                System.out.println(seconds + " sec ago");
                if (seconds < 0) {
                    diff = "0 sec ago";
                } else
                    diff = seconds + " sec ago";
            } else if (minutes < 60) {
                diff = minutes + " min ago";
            } else if (hours < 24) {
                diff = hours + " hours ago";
            } else if (days >= 7) {
                if (days > 30) {
                    diff = (days / 30) + " Months " + suffix;
                } else if (days > 360) {
                    diff = (days / 360) + " Years " + suffix;
                } else {
                    diff = (days / 7) + " Week " + suffix;
                }
            } else if (days < 2) {
                diff = days + " Day " + suffix;
            } else if (days < 7) {
                diff = days + " Days " + suffix;
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return diff;
    }

    public static String getDurationArabic(String startDate) {
        Date past = new Date();
        past.setTime(Long.parseLong(startDate));
        String diff = "";
        String suffix = "منذ";
        try {
            Date now = new Date();
            long dateDiff = now.getTime() - (past.getTime());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hours = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long days = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (seconds < 60) {
                if (seconds < 0) {
                    diff = "ثانية" + " 0 " + suffix;
                } else
                    diff = "ثانية" + " " + seconds + " " + suffix;
            } else if (minutes < 60) {
                diff = "دقيقة" + " " + minutes + " " + suffix;
            } else if (hours < 24) {
                diff = "ساعة" + " " + hours + " " + suffix;
            } else if (days >= 7) {
                if (days > 30) {
                    diff = "شهر" + " " + (days / 30) + " " + suffix;
                } else if (days > 360) {
                    diff = "عام" + " " + (days / 360) + " " + suffix;
                } else {
                    diff = "أسبوع" + " " + (days / 7) + " " + suffix;
                }
            } else if (days < 2) {
                diff = "يوم" + " " + days + " " + suffix;
            } else if (days < 7) {
                diff = "يوم" + " " + days + " " + suffix;
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return diff;
    }

    /**
     * @param mContext
     * @param title
     * @param message
     * @return
     */
    public static AlertDialog.Builder showMessageDialog(Context mContext, String title, String message) {
        return showDialog(mContext, message, title, "OK", null, null, null, null, null, true);
    }



        public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
            return true;
        }

    public static String convertDate(String date) {
        String cDate = null;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        cDate = spf.format(newDate);

        return cDate;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];
        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    /**
     * Intent to Phone
     *
     * @param mContext Context of the Activity or Fragment.
     * @param number   Number on which want to make a call
     */
    public static void intentToPhone(Context mContext, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        mContext.startActivity(Intent.createChooser(intent, "Go To : "));
    }



    public static void intentToWhatsApp(Context mContext, String number) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=" + number)));
    }

    public static ApiInterface requestApiDefault() {
        String  token = PreferenceHandler.INSTANCE.readString(
                ApplicationClass.instance.getApplicationContext(),
               "AUTHORIZATION_TOKEN",
                ""
        );
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)

                .readTimeout(1000, TimeUnit.SECONDS);
        clientBuilder.addInterceptor(chain -> {
            Request request;
            {
//                if (new QuickStartPreferences(MyApplication.getInstance()).get_Boolean(Constants.IS_LOGIN) == true) {
//                    request = chain.request().newBuilder()
//                            .addHeader(Constants.Authorization, Constants.Authorization_Value)
//                            .addHeader(Constants.LOGIN_ID_KEY, new QuickStartPreferences(MyApplication.getInstance()).getLoginData().getData().id)
//                            .addHeader(Constants.LOGIN_TOKEN_KEY, new QuickStartPreferences(MyApplication.getInstance()).getLoginData().getData().getLoginToken())
//                            .build();
//                } else {
                request = chain.request().newBuilder()

               . addHeader("Authorization", "Bearer " + token)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();
//                }
            }
            return chain.proceed(request);
        });

        clientBuilder.addInterceptor(logging);

        OkHttpClient client = clientBuilder.build();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.INSTANCE.getAPIS_BASE_URL()).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();

        ApiInterface apis = retrofit.create(ApiInterface.class);
        return apis;
    }

    public static String changeNumberT0EN(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static void intentToActivity(Context mContext, Class activityToOpen, boolean clearStack) {
        Intent intent = new Intent(mContext, activityToOpen);
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        mContext.startActivity(intent);
    }

    public static void intentToActivityWithBundle(Context mContext, Class activityToOpen, Bundle bundle, boolean clearStack) {
        Intent intent = new Intent(mContext, activityToOpen);
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    public static void intentToActivity(Context mContext, Intent intent, boolean clearStack) {
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
//        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }


    /**
     * Show Log
     *
     * @param message Message that want to show into Log
     */
    public static void showLog(String message) {
        Log.e("Log Message", "" + message);
    }

    public static void removeCurrentFragment(Context mContext) {
        ((FragmentActivity) mContext).getSupportFragmentManager().popBackStackImmediate();
    }

    public static void removeAllFragment(Context mContext) {
        int count = ((FragmentActivity) mContext).getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            ((FragmentActivity) mContext).getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public static void goToFragment(Context mContext, Fragment fragment, int container, boolean addtoBackstack) {
        FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addtoBackstack) {
            transaction.replace(container, fragment);
            transaction.addToBackStack(null);
        } else {
            transaction.replace(container, fragment);
        }
        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToFragment(Context mContext, Fragment fragment, int container) {
        FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(fragment.getTag());
        transaction.commitAllowingStateLoss();
    }

    public static void goToFragment1(Context mContext, Fragment fragment, int container) {
        FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        transaction.add(container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    public static String getServerFailureMsg(Context mContext, Response response) {
        String msg = null;
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());

        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return msg;
    }
}