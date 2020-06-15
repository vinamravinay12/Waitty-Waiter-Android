package com.waitty.waiter.constant;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class constant {

    public static final String PRIVACY_POLICY_URL = "https://waitty.com/privacy-policy/";
    public static final String TERMS_CONDITIONS_URL = "https://waitty.com/terms-and-conditions/";
    public static final String HELP_URL = "https://waitty.com/help/";

    // public static final DecimalFormat FORMATOR = new DecimalFormat("#,##,##,##,##,##,###");
    public static final DecimalFormat FORMATOR_DECIMAL = new DecimalFormat("#,##,##,##,##,##,###.00");
    public static final SimpleDateFormat dateFormaterServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final SimpleDateFormat dateFormaterServedOrder = new SimpleDateFormat("h:mma - dd/MM/yy");
    public static final SimpleDateFormat dateFormaterTrackOrder = new SimpleDateFormat("h:mm a");
    public static final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL";
    public static final String IS_CHANNEL_PREPARED = "IS_CHANNEL_PREPARED";

    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String NOTIFICATIONS_SHOW = "NOTIFICATIONS_SHOW";
    public static final String NOTIFICATION_COUNT_NEW = "NOTIFICATION_COUNT_NEW";
    public static final String NOTIFICATION_COUNT_PROCESSING = "NOTIFICATION_COUNT_PROCESSING";
    public static final String NEW_RELOAD_BY_FCM = "NEW_RELOAD_BY_FCM";
    public static final String PROCESSING_RELOAD_BY_FCM = "PROCESSING_RELOAD_BY_FCM";

    public static final String USER_FCMTOKENID = "device_token";
    public static final String USER_DEVICEID = "device_id";
    public static final String DEVICE_TYPE = "android";

    public static final String USER_INFORMATION = "USER_INFORMATION";
    public static final String USER_SECURITY_TOKEN = "USER_SECURITY_TOKEN";

    public static final String NEW_ORDER_RELOAD = "NEW_ORDER_RELOAD";
    public static final String PROCESSING_ORDER_RELOAD = "PROCESSING_ORDER_RELOAD";
    public static final String SERVED_ORDER_RELOAD = "SERVED_ORDER_RELOAD";

    /*ORDER STATUS VALUE*/
    public static final int ORDER_PLACED = 1;
    public static final int ORDER_PREPARING = 2;
    public static final int ORDER_READY_SERVE = 3;
    public static final int ORDER_DELIVERED = 4;
    public static final int ORDER_BLOCK_ADMIN = 5;
    public static final int ORDER_REFUND_INITIATED = 6;
    public static final int ORDER_AMOUNT_REFUNDED = 7;

    public static final String LOGIN_SP = "LoginSP";

    public static final String KEY_IS_LOGGED_IN = "IsLoggedIn";
    public static final String TAG_MENU = "MenuFragment";
    public static final String TAG_PROFILE = "ProfileHomeFragment";
    public static final String TAG_SETTINGS = "SettingsFragment";
    public static final String TAG_HOME = "HomeFragment";
    public static final String KEY_PREPARED_ORDERS = "PreparedOrders";
    public static final String DISPLAY_DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm a";
    public static final String TAG_NEW_ORDER_DETAILS=  "NewOrderDetailsFragment";
}
