package com.waitty.waiter.utility;

import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager;
import com.waitty.waiter.R;
import com.waitty.waiter.activity.LoginActivity;
import com.waitty.waiter.appinterface.getResponseData;
import com.waitty.waiter.constant.constant;
import com.waitty.waiter.model.CustomizationCategory;
import com.waitty.waiter.model.DishData;
import com.waitty.waiter.model.LoginUser;
import com.waitty.waiter.model.OrderDetails;
import com.waitty.waiter.retrofit.API;
import com.waitty.waiter.retrofit.APICall;
import com.waitty.waiter.retrofit.ApiClient;
import com.waitty.waiter.retrofit.ApiInterface;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;

public class Utility {

    private static long mLastClickTime = 0;

    private static Snackbar snackbar;

    // String value SET on SharedPreferences
    public static void setSharedPreferencesString(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    // Integer value SET on SharedPreferences
    public static void setSharedPreferencesInteger(Context context, String name, int value) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    // Boolean value SET on SharedPreferences
    public static void setSharedPreferencesBoolean(Context context, String name, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    // String value GET on SharedPreferences
    public static String getSharedPreferencesString(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        return settings.getString(name, "");
    }

    // Integer value GET on SharedPreferences
    public static int getSharedPreferencesInteger(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        return settings.getInt(name, 0);
    }

    // Boolean value GET on SharedPreferences
    public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        return settings.getBoolean(name, false);
    }

    // Check internet connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Show Snackbar for alert
    public static void ShowSnackbar(Context context, View v, String msg) {

        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });

        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorYallow));

        View snackbarView = snackbar.getView();
        int snackbarTextId = R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
        textView.setMaxLines(3);

        snackbar.show();

    }

    // Show Toast
    public static void ShowToast(Context context, String msg, int duration) {

        int gravity = Gravity.CENTER; // the position of toast
        int xOffset = 0; // horizontal offset from current gravity
        int yOffset = 0;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();

    }

    // Application logout function
    public static void LogOut(Context context) {

        if (Utility.isNetworkAvailable(context))
            logoutAPI(context);

        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();

        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(context.getString(R.string.app_name));
        editor.clear();
        editor.commit();
        context.getSharedPreferences(context.getString(R.string.app_name), 0).edit().clear().commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Call logout application API
    public static void logoutAPI(Context context) {
        try {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.logoutApplication(Utility.getSharedPreferencesString(context, constant.USER_SECURITY_TOKEN));
            new APICall(context).Server_Interaction(call, new getResponseData() {
                @Override
                public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {

                }

                @Override
                public void onFailed(String msg, String typeAPI) {

                }
            }, API.LOGOUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Increase Clicking Time
    public static boolean buttonClickTime() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    // Check value null and return
    public static String checkNull(String value) {
        return value == null ? "" : value;
    }

    // Check application version API
    public static void CheckApplicationVersion(final Context context) {

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(API.PLATFORM_TYPE, constant.DEVICE_TYPE);
            jsonObject.addProperty(API.USER_TYPE, "WAITER");
            jsonObject.addProperty(API.VERSION, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = apiInterface.checkVersion(jsonObject);
            new APICall(context).Server_Interaction(call, new getResponseData() {
                @Override
                public void onSuccess(JSONObject OBJ, String msg, String typeAPI) {
                    //   Dialog.showApplicationUpdateDialog(context,msg);
                }

                @Override
                public void onFailed(String msg, String typeAPI) {

                }
            }, API.APPVERSION);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Set toolbar with back icon
    public static void setToolbar(Context context, Toolbar mToolbar) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    // Change date on specific formate
    public static String ChangeDateFormate(SimpleDateFormat dateFormaterServer, String date, SimpleDateFormat dateFormaterConvert) {
        try {
            Date NewDate = dateFormaterServer.parse(date);
            return dateFormaterConvert.format(NewDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // UTC to local date
    public static String chageUTC_ToLocalDate(SimpleDateFormat dateFormaterServer, String date, SimpleDateFormat dateFormaterConvert) {
        String returnValue = "";
        try {

            if (date == null || date.isEmpty())
                return "";

            dateFormaterServer.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = dateFormaterServer.parse(date);

            dateFormaterConvert.setTimeZone(TimeZone.getDefault());
            returnValue = dateFormaterConvert.format(value);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    // Open url
    public static void openURL(Context ctx, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(browserIntent);
    }

    // Get dish total price
    public static double getDishTotalPrice(DishData dishData) {
        double totalAmount = dishData.getPrice();

        if (dishData.getCustomizationCategories() != null && dishData.getCustomizationCategories().size() > 0) {
            for (int j = 0; j < dishData.getCustomizationCategories().size(); j++) {
                CustomizationCategory categoryData = dishData.getCustomizationCategories().get(j);
                if (categoryData.getCustomizationCategoryOptions() != null && categoryData.getCustomizationCategoryOptions().size() > 0) {
                    for (int k = 0; k < categoryData.getCustomizationCategoryOptions().size(); k++) {
                        if (!categoryData.getCustomizationCategoryOptions().get(k).getIsSoldout() && categoryData.getCustomizationCategoryOptions().get(k).getIsDefault()) {
                            totalAmount = totalAmount + categoryData.getCustomizationCategoryOptions().get(k).getPrice();
                        }// check conditions
                    }// option for loop
                }// option if
            }// category for loop
        }// category if

        return totalAmount;
    }

    // Get dish total selling price
    public static double getDishTotalSellingPrice(DishData dishData) {
        double totalAmount = dishData.getSellingPrice();

        if (dishData.getCustomizationCategories() != null && dishData.getCustomizationCategories().size() > 0) {
            for (int j = 0; j < dishData.getCustomizationCategories().size(); j++) {
                CustomizationCategory categoryData = dishData.getCustomizationCategories().get(j);
                if (categoryData.getCustomizationCategoryOptions() != null && categoryData.getCustomizationCategoryOptions().size() > 0) {
                    for (int k = 0; k < categoryData.getCustomizationCategoryOptions().size(); k++) {
                        if (!categoryData.getCustomizationCategoryOptions().get(k).getIsSoldout() && categoryData.getCustomizationCategoryOptions().get(k).getIsDefault()) {
                            totalAmount = totalAmount + categoryData.getCustomizationCategoryOptions().get(k).getSellingPrice();
                        }// check conditions
                    }// option for loop
                }// option if
            }// category for loop
        }// category if

        return totalAmount;
    }


    public static void doShakeAnimation(View view) {
        ObjectAnimator
                .ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
                .setDuration(200)
                .start();
    }


    public static boolean isTablet(Context context) {
        try {
            DisplayMetrics dm =
                    context.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));

            return size >= 6;
        } catch (Throwable t) {
            Log.e("LogError", t.toString());
            return false;
        }

    }

    public static String getToken(Context context) {
        return new SharedPreferenceManager(context, constant.LOGIN_SP).getStringPreference(API.AUTHORIZATION);
    }


    public static String getMessageOnErrorCode(@Nullable Integer errorCode, Context context) {
        switch (errorCode) {
          //     case API.NETWORK_ERROR : return context.getString(R.string.network_not_found);
            case API.BLOCK_ADMIN:
                return context.getString(R.string.block_admin_msg);
            case API.SESSION_EXPIRE:
                return context.getString(R.string.session_expire_msg);
            default:
                return context.getString(R.string.something_went_wrong);
        }

    }


    public static String getStringFrom(@Nullable Date arrivalDate, @NotNull String displayDateTimeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(displayDateTimeFormat);
        return simpleDateFormat.format(arrivalDate);
    }

    public static boolean isCreatedToday(@Nullable String createdAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(constant.DATE_FORMAT);
        String formattedCreatedAt = chageUTC_ToLocalDate(constant.dateFormaterServer, createdAt, simpleDateFormat);
        String currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());


        try {
            Date createdAtDate = simpleDateFormat.parse(formattedCreatedAt);
            Date today = simpleDateFormat.parse(currentDate);
            return Math.abs(today.getTime() - createdAtDate.getTime()) < 1000*60*60*24;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean hasOrderItems(OrderDetails orderDetails) {
        return !orderDetails.getOrderItems().isEmpty();
    }


    public static String getWaiterName(Context context) {
       String userJson =  new SharedPreferenceManager(context,constant.LOGIN_SP).getStringPreference(API.WAITER_USER);
       LoginUser waiterUser = new Gson().fromJson(userJson,LoginUser.class);
       return waiterUser.getName();
    }
}// final class ends here

