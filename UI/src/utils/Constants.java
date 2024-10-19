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
    public final static String VIEW_SHEET = FULL_SERVER_PATH + "/viewSheet";
    public final static String GET_RANGE = FULL_SERVER_PATH + "/getRange";
    public final static String GET_TEMP_RANGE = FULL_SERVER_PATH + "/getTempRange";
    public final static String GET_RANGES_NAME = FULL_SERVER_PATH + "/getRangesName";
    public final static String UPDATE_CELL = FULL_SERVER_PATH + "/updateCell";
    public final static String ADD_NEW_RANGE = FULL_SERVER_PATH + "/assNewRange";
    public final static String DELETE_RANGE = FULL_SERVER_PATH + "/deleteRange";
    public final static String SORT = FULL_SERVER_PATH + "/sort";
    public final static String GET_THE_RANGE_OF_THE_RANGE = FULL_SERVER_PATH + "/getTheRangeOfTheRange";
    public final static String FILTER = FULL_SERVER_PATH + "/filter";
    public final static String GET_COL_ITEMS = FULL_SERVER_PATH + "/getColItems";
    public final static String DYNMIC_ANLYZE = FULL_SERVER_PATH + "/dynmicAnlyze";
    public final static String DELETE_DYNAMIC_SHEET = FULL_SERVER_PATH + "/deleteDynamicSheet";
    public final static String SHEET_BY_VERSION = FULL_SERVER_PATH + "/sheetByVersion";
    public final static String PREDICT_CALCULATE = FULL_SERVER_PATH + "/predictCalculate";
    public final static String REFRESH_SHEET_VERSIONS = FULL_SERVER_PATH + "/refreshSheetVersions";
    public final static String GET_UPDATE_SHEET_VERSION = FULL_SERVER_PATH + "/getUpdateSheetVersion";
    public final static String RESET_SESSION = FULL_SERVER_PATH + "/resetSession";
    public final static String SEND_MESSAGE = FULL_SERVER_PATH + "/sendMessage";
    public final static String GET_MESSAGES = FULL_SERVER_PATH + "/getMessages";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
