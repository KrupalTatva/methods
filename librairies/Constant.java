package com.app.fap.librairies;

/****************************************************
 * Created by Tahiana-MadiApps on 28/07/2016.
 ****************************************************/
public class Constant {
    public static final String KEY_FIRST_RUN = "KEY_FIRST_RUN";
    public static final String KEY_LOGIN = "KEY_LOGIN";
    public static final String KEY_LAST_DATE_SYNC = "KEY_LAST_DATE_SYNC";
    public static final String KEY_IPBX_INFO = "IPBX_INFO";
    public static final float LOCATION_ACCURENCY = 50.0f;
    public static final String PICTURE_DIRECTORY = "FAP";
    public static final long PHOTO_SIZE_MAX = 10 * 100000;
    public static final String IPBX_INFO_FAP = "01-40-03-96-73";
    public static final String IS_PROCHAIN_PANNEAU = "isProchainPanneau";

    public static final int RC_CAMERA = 1001;

    /** URL PROD */
//    public static String URL_SERVER="https://fapmobile.fr/fap/api/mobile";

    /**
     * URL PRE-PROD
     */
//    public static String URL_SERVER = "http://213.32.72.183/fap/api/mobile";
    public static String URL_SERVER = "http://217.182.140.187/api/mobile";

    public static String KEY_APP_UPDATE_LATER = "KEY_APP_UPDATE_LATER";
    public static String KEY_PASSWORD = "KEY_PASSWORD";
    public static String KEY_UPDATED = "KEY_UPDATED";
    public static String KEY_CAMPAGNE_UPDATE = "KEY_CAMPAGNE_UPDATE";
    public static String KEY_PANEL = "KEY_PANEL";
    public static String KEY_PANEL_TIME = "KEY_PANEL_TIME";
    public static String KEY_PANEL_LAST_TIMESTAMP = "KEY_PANEL_LAST_TIMESTAMP";


    public static final String TOUS_DEPARTEMENTS = "Tous les départements";
    public static final String TOUS_CANTONS = "Tous les cantons";
    public static final String TOUTES_COMMUNES = "Toutes les communes";
    public static final String TOUS_ARRONDISSEMENT = "Tous les arrondissements";
    public static final String TOUS_REGION = "Toutes les régions";

    public static final int PANEL_ALL_STATES = 0;
    public static final int PANEL_STATE_NOT_GEOCODED = 1;
    public static final int PANEL_STATE_TO_DISPLAY = 2;   // Orange
    public static final int PANEL_STATE_DISPLAYED = 3;   // Green
    public static final int PANEL_STATE_MISSING = 4;
    public static final int PANEL_STATE_NEW_AND_DISPLAYED = 5; // Blue
    public static final int PANEL_STATE_DELETED = 6;
    public static final int PANEL_STATE_NEW_AND_DELETED = 7;

    public static int NO_REASON = 0;
    public static final int REASON_NOT_ENOUGH_PANELS = 1;
    public static final int REASON_MISSING_PANEL = 2;
    public static final int REASON_DAMAGED_PANELS = 3;
    public static final int REASON_RECOVERED_BY_OTHER = 4;
    public static final int REASON_PANEL_OTHERS = 5;
    public static final int REASON_PANEL_TO_DELETE = 6;

    public static final int IS_NOT_SENT = 1;
    public static final int IS_IN_PROGRESS = 2;
    public static final int IS_SENT = 3;

    public static String JPEG_EXTENSION = ".jpg";

    public static String KEY_VALIDATION_PANEL = "KEY_VALIDATION_PANEL";

    public static int IS_NEW_PANEL = 1;
    public static int IS_REPORTING_PANEL = 2;
    public static int IS_VALIDATION_PANEL = 3;
    public static int IS_NEW_REPORTING_PANEL = 4;
    public static int PANEL_INTERVAL_TIMER = 2;

    public static String KEY_ID_PANEL = "KEY_ID_PANEL";
    public static String KEY_ID_LOCAL_PANEL = "KEY_ID_LOCAL_PANEL";
    public static String CURSCREEN_USER = "CURSCREEN_USER";
    public static String CURSCREEN_USER_BUNDLE = "CURSCREEN_USER_BUNDLE";
    public static String ID_NEXT_PANEL = "ID_NEXT_PANEL";
    public static String COUNT_PANEL_SYNCRONYSED = "COUNT_PANEL_SYNCRONYSED";
    public static String COUNT_PANEL_TO_SYNC = "COUNT_PANEL_TO_SYNC";

    //todo test a changer 17
    public static float DEFAULT_ZOOM = 17.0f;
    public static int NB_PANEL_LIMIT = 500;
    public static double NB_PANEL_LIMIT_DATA_SYNC = 5000;
    public static int PANEL_NUMBER_MAX_MAP = 1000;

    public static final String RGPD_TEXT = "rgpd_text";
}
