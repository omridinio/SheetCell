package utils;

import com.google.gson.Gson;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOAD_SHEET = FULL_SERVER_PATH + "/loadSheet";
    public final static String REFRESH_SHEET = FULL_SERVER_PATH + "/refreshSheet";
    public final static String PERMISSON_REQUEST = FULL_SERVER_PATH + "/permissionRequest";
    public final static String PERMISSON_REFRESH = FULL_SERVER_PATH + "/permissionRefresh";
    public final static String PERMISSON_APPROVE = FULL_SERVER_PATH + "/permissionApprove";
    public final static String PERMISSION_OWNER = FULL_SERVER_PATH + "/permissionOwner";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
