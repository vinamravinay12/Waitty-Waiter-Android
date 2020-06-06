package com.waitty.waiter.retrofit;

public class API {
    public static final int OTHER_FAILED = 422;
    public static final int SESSION_EXPIRE = 401;
    public static final int BLOCK_ADMIN = 403;
    public static final String MESSAGE = "message";
    public static final String DATA = "data";
    public static final String AUTHORIZATION = "Authorization";
    public static final String NOTIFICATION_TOGGLE = "notification_toggle";
    public static final String APPVERSION = "appversion";

    // Base URL
    //public static final String BASE_URL = "http://18.224.15.124/";
    public static final String BASE_URL = "https://admin.waitty.com/api/";

    // API name
    public static final String LOGIN = "login_waiter";
    public static final String UPDATE_PROFILE = "patch/user";
    public static final String LOGOUT = "logout";
    public static final String GET_CATEGORY = "getcategory";
    public static final String GET_DISH = "getdish";
    public static final String GET_NEW_ORDER = "getneworder_waiter";
    public static final String GET_PROCESSING_ORDER = "getrunningorder_waiter";
    public static final String GET_SERVED_ORDER = "getcompleteorder_waiter";
    public static final String ACCEPT_ORDER = "accept_order";
    public static final String REJECT_ORDER = "reject_order";
    public static final String DELIVERD_ORDER = "make_deliverd_order";
    public static final String ORDER_STATUS = "getorderstatus";
    public static final String SEARCH_ORDER = "order_detail_bydisplayid";

    // API input parameter
    public static final String KEY= "key";
    public static final String RESTAURANT_ID= "restaurantId";
    public static final String PASSWORD= "password";
    public static final String DEVICE_TYPE= "device_type";
    public static final String SUBCATEGORY_ID= "subcategoryId";
    public static final String PAGE= "page";
    public static final String LIMIT= "limit";
    public static final String PLATFORM_TYPE= "platform_type";
    public static final String USER_TYPE= "user_type";
    public static final String VERSION= "version";
    public static final String ORDER_STATUS_ID= "orderstatusId";
    public static final String ORDER_ID= "orderId";
    public static final String ORDER_ID_DISPLAY= "order_id_display";

}
