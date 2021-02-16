package anviifotechs.haritkandharadmin.helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class Functions {

    private static String MAIN_URL = "***** ENTER YOUR SERVER URL HERE ******";

    // Login URL
    public static String LOGIN_URL = MAIN_URL + "login.php";

    public static String OTP_VERIFY_URL = MAIN_URL + "verification.php";

    // Forgot Password
    public static String RESET_PASS_URL = MAIN_URL + "reset-password.php";

    public static String UPLOAD_PROFILE_URL = MAIN_URL + "upload_profile.php";

    public static String FETCH_PROFILE_URL = MAIN_URL + "fetch_profile.php";

    public static String VIEW_BLOCK_USERS_URL = MAIN_URL + "ViewBlockedUsers.php";

    public static String BLOCK_USERS_URL = MAIN_URL + "BlockUsers.php";

    public static String VIEW_UNBLOCK_USERS_URL = MAIN_URL + "ViewUnblockedUsers.php";

    public static String UNBLOCK_USERS_URL = MAIN_URL + "UnblockUsers.php";

    public static String VIEW_PLANTATION_APPROVAL_URL = MAIN_URL + "ViewPlantationApproval.php";

    public static String VIEW_PLANT_PICTURE_URL = MAIN_URL + "ViewPlantPicture.php";

    public static String PLANTATION_APPROVAL_URL = MAIN_URL + "ApprovePlantation.php";

    public static String PICTURE_APPROVAL_URL = MAIN_URL + "ApprovePlantPicture.php";

    public static String DENY_PICTURE_APPROVAL_URL = MAIN_URL + "DenyPlantPicture.php";

    public static String SEARCH_USERS_URL = MAIN_URL + "Search_Users.php";

    /**
    /**
     *  Email Address Validation
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
