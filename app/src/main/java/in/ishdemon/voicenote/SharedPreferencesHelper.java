package in.ishdemon.voicenote;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String API_URL = "api_url";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_DEPARTMENT_ID = "department_id";
    private static final String KEY_ORGANISATION_ID = "organisation_id";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "fullname";
    private static final String KEY_CURRENT_FACILITY = "facility";
    private static final String ROLES = "roles";
    private static final String ROLELIST = "rolelist";
    private static final String KEY_CURRENT_FACILITY_NAME = "FaciltyName";
    private static final String CURRENT_DATE = "current_date";
    private static final String TYPE = "type";
    private static final String TAB_POSITION = "0";
    private static final String FACILITY_POSITION = "1";
    private static final String PASSCODE = "pass_code";
    private static final String TIME_ZONE = "time_zone";

    public static void setApiUrl(Context context, String url) {
        SharedPreferences prefs = getSharedPreferences(context);
        prefs.edit().putString(API_URL, url).commit();
    }

    public static String getApiUrl(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(API_URL, "");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        return prefs;
    }
}
