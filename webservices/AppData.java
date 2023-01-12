package com.app.fap.webservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.app.fap.FapApplication;
import com.app.fap.librairies.Constant;
import com.app.fap.librairies.FapTools;
import com.app.fap.librairies.GenericTools;
import com.app.fap.librairies.UtilsFilter;
import com.app.fap.librairies.UtilsUser;
import com.app.fap.models.Addresses;
import com.app.fap.models.Campagnes;
import com.app.fap.models.IPBXInfo;
import com.app.fap.models.OpenCampagnes;
import com.app.fap.models.Panel;
import com.app.fap.models.Panels;
import com.app.fap.models.Result;
import com.app.fap.models.RgpdTextResponse;
import com.app.fap.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

/****************************************************
 * Created by Tahiana-MadiApps on 28/07/2016.
 ****************************************************/
public class AppData {
    private static AppData instance = null;

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    private IAppData interfaceData;
    HttpLoggingInterceptor interceptor;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private long TIMEOUT_DURATION = 60;

    private long CONNEXION_TIMEOUT_DURATION = 10;


    public AppData(Activity activity) {
        this.activity = activity;
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    public AppData(Context context) {
        this.context = context;
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /* public static AppData getInstance(Activity context) {
         if(instance == null) {
             instance = new AppData(context);
         }
         return instance;
     }*/
    public void setInterfaceData(IAppData interfaceData) {
        this.interfaceData = interfaceData;
    }


    public void login(String login, final String password) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("login", login);

        params.put("password", password);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/login")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            User user = new User();

                            if (result.isStateSuccess()) {

                                user.populateObject(jsonObject);

                                user.setPassword(password);

                                user.updateOrCreate();

                                if (UtilsUser.getLastUserId(activity) != user.getIdUser()) {
                                    Panels.deleteAllPanels(UtilsUser.getLastUserId(activity));
                                    Campagnes.deleteAllCampagnes(Realm.getDefaultInstance());
                                    Addresses.deleteAllAddresses(Realm.getDefaultInstance());
                                    UtilsUser.savePanelTimeStamp(activity, 0L);
                                    UtilsFilter.saveCampagneFilterToPreferences(context, (long) -1);

                                }
                            }

                            client.dispatcher().executorService().shutdown();

                            interfaceData.loginResponse(result, user.getIdUser(), user.getRgpd(), user.getRgpd_text());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }


    public void forgotPassword(String login) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("login", login);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/forgot-password")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.forgotPasswordResponse(result);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }


    public void updateUser(String api_key, long idUser, String nom, String prenom, String telephone, String codePostal, String login) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("idUser", idUser);
        params.put("nom", nom);
        params.put("prenom", prenom);
        params.put("telephone", telephone);
        params.put("codePostal", codePostal);
        params.put("login", login);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/update-user")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.updateUserResponse(result);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }


    public void updateUserPosition(String api_key, long idUser, String latitude, String longitude) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("idUser", idUser);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/update-user-position")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.updateUserPosition(result);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    public void updatePassword(String api_key, long idUser, String oldPassword, String newPassword) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("idUser", idUser);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/update-password")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    interfaceData.emptyResponseServer();
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.updatePasswordResponse(result);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    /**
     * Récupère les panneaux par index 1, 2, 3 ... selon le nombre de panneaux total (FapApplication.panels_count),
     * sachant que le WS renvoie un lot de 5000 panneaux
     *
     * @param api_key
     * @param idUser
     * @param index
     * @param latitude
     * @param longitude
     */
    public void getPanelsWithIndex(final String api_key, final long idUser, final int index, final double latitude, final double longitude, final long timestamp) {

        /**
         * GET
         * */


        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Log.d(AppData.class.getName(), "Start getPanelsWithIndex....... " + index);


        HashMap params = new HashMap();

        params.put("idUser", idUser);
        params.put("index", index);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("timestamp", timestamp);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/panel")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                //interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Log.d(AppData.class.getName(), "End getPanelsWithIndex......." + index);

                try {

                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // request the entire body.
                        Buffer buffer = source.buffer();
// clone buffer before reading from it
                        String responseJson = buffer.clone().readString(Charset.forName("UTF-8"));

//                        final String responseJson = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseJson);

                        Result result = new Result();

                        Log.d(AppData.class.getName(), "Begin parse Result getPanelsWithIndex......." + index);
                        result.populateObject(jsonObject);
                        Log.d(AppData.class.getName(), "End parse Result getPanelsWithIndex......." + index);

                        result.setState(true);

                        JSONObject recordsJsonObject = jsonObject.getJSONObject("records");
                        FapApplication.panels_count = recordsJsonObject.getLong("panels_count");
                        Panels panels = new Panels();
                        Log.d(AppData.class.getName(), "Begin parse Panels getPanelsWithIndex......." + index);


                        if (recordsJsonObject != null && recordsJsonObject.has("panels") && recordsJsonObject.get("panels") instanceof JSONArray /*&& recordsJsonObject.getJSONArray("panels").length() > 0*/) {
                            panels.populateObject(recordsJsonObject.getJSONArray("panels"));
                        } /*else {
                            //FapApplication.panels_index = nb_sync_panel+1;
                            UtilsUser.savePanelTimeStamp(context, System.currentTimeMillis() / 1000);
                        }*/
                        Panels.saveLocally(panels.array, FapApplication.panels_index, null, context);
                        Log.d(AppData.class.getName(), "End parse Panels getPanelsWithIndex......." + index + "Panel index : " + FapApplication.panels_index);

                        client.dispatcher().executorService().shutdown();
                        double nb_sync_panel_double_value = FapApplication.panels_count / Constant.NB_PANEL_LIMIT_DATA_SYNC;
                        int nb_sync_panel = FapTools.roundUp(nb_sync_panel_double_value);
                        //if (FapApplication.panels_count == User.getCountAllPanelsWithoutFilter(idUser, context) && !panels.array.isEmpty()) {
                        if (FapApplication.panels_index >= nb_sync_panel + 1) {
                            if (context != null)
                                UtilsUser.savePanelTimeStamp(context, System.currentTimeMillis() / 1000);
                            else
                                UtilsUser.savePanelTimeStamp(activity, System.currentTimeMillis() / 1000);
                        }
                        FapApplication.panels_index = index + 1;
                        interfaceData.getPanelsResponse(result, panels.array);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }


    public void getCampagnes(String api_key, final long idUser) {

        /**
         * GET
         * */

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/getCampagne/" + idUser)
                .addHeader("Authorization", api_key)
                .build();

        Log.e("id user : ", String.valueOf(idUser));


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                try {

                    JSONObject jsonObject = new JSONObject(responseJson);

                    Result result = new Result();

                    result.populateObject(jsonObject);

                    result.setState(true);

                    Campagnes campagnes = new Campagnes(idUser);

                    boolean isEmpty = campagnes.populateObject(jsonObject);

                    client.dispatcher().executorService().shutdown();

                    interfaceData.getCampagnesResponse(result, campagnes, isEmpty);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }


    public void getOpenCampagnes(String api_key, final long idUser) {

        /**
         * GET
         * */

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/getOpenedCampagne/" + idUser)
                .addHeader("Authorization", api_key)
                .build();


        Log.e("id user : ", String.valueOf(idUser));


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();
                Log.d("TAG", responseJson);
                try {

                    JSONObject jsonObject = new JSONObject(responseJson);

                    Result result = new Result();

                    result.populateObject(jsonObject);

                    result.setState(true);

                    OpenCampagnes campagnes = new OpenCampagnes(idUser);

                    boolean isEmpty = campagnes.populateObject(jsonObject);

                    client.dispatcher().executorService().shutdown();

                    interfaceData.getOpenCampagnesResponse(result, campagnes, isEmpty);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }

    public void uploadPhoto(String api_key, String idPanel, String idCampagne, File
            file_data, String datePrisePhoto, int turnNumber, int lapNumber) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        if (idPanel.equalsIgnoreCase("0"))
            idPanel = "-1";

        MediaType mediaTypeImage = MediaType.parse("image/jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("idPanel", idPanel)
                .addFormDataPart("idCampagne", idCampagne)
                .addFormDataPart("datePrisePhoto", datePrisePhoto)
                .addFormDataPart("lap_number", lapNumber + "")
                .addFormDataPart("turn_number", turnNumber + "")
                .addFormDataPart("file_data", file_data.getName(), RequestBody.create(mediaTypeImage, file_data))
                .build();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/uploadPhoto")
                .post(requestBody)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result result = new Result();

                        client.dispatcher().executorService().shutdown();

                        interfaceData.uploadPhotoResponse(result, -1);

                        //Utility.logFile(context.getApplication(), "uploadPhoto Panel failed::" , true);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = new Result();

                            client.dispatcher().executorService().shutdown();

                            interfaceData.uploadPhotoResponse(result, -1);

                            //Utility.logFile(context.getApplication(), "uploadPhoto response failed::" , true);

                        }
                    });

                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            int idPanelPhoto = -1;

                            if (jsonObject != null && jsonObject.has("idPanelPhoto")) {
                                idPanelPhoto = jsonObject.getInt("idPanelPhoto");
                            }

                            client.dispatcher().executorService().shutdown();

                            interfaceData.uploadPhotoResponse(result, idPanelPhoto);
                            //Utility.logFile(context.getApplication(), "uploadPhoto response ::"+idPanelPhoto , true);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            response.close();
                        }
                    }
                });


            }

        });

    }

    public void addPanel(long localPanelId, String api_key,
                         String latitude,
                         String longitude,
                         String state,
                         String rating,
                         String comments,
                         String adresse,
                         String campagneId,
                         String code_postal,
                         JSONArray photosJsonForm,
                         int turnNumber,
                         int lapNumber) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("state", state);
        params.put("rating", rating);
        params.put("comments", comments);
        params.put("adresse", adresse);
        params.put("campagneId", campagneId);
        params.put("code_postal", code_postal);
        params.put("photos", photosJsonForm);
        params.put("lap_number", lapNumber);
        params.put("turn_number", turnNumber);

        JSONObject jsonParams = new JSONObject(params);

        String jsonParamsClean = jsonParams.toString().replace("\\\"", "\"");
        jsonParamsClean = jsonParamsClean.replace("\"{", "{");
        jsonParamsClean = jsonParamsClean.replace("}\"", "}");

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParamsClean));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/addPanel")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result result = new Result();

                        client.dispatcher().executorService().shutdown();

                        interfaceData.resetSynch();

                        //Utility.logFile(context.getApplication(), "Add panel failed ::",true);

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            client.dispatcher().executorService().shutdown();

                            interfaceData.resetSynch();

                            //Utility.logFile(context.getApplication(), "Add panel Response not successful ::",true);

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();
                Log.e("response panel :", responseJson);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            int idPanel = 0;

                            if (result.isStateSuccess() && jsonObject.has("idPanel")) {
                                idPanel = jsonObject.getInt("idPanel");
                            }

                            client.dispatcher().executorService().shutdown();

                            if (idPanel != 0) {
                                interfaceData.addPanelResponse(result, idPanel);

                                //Utility.logFile(context.getApplication(), "Add panel Response successful ::" + idPanel, true);
                            } else {

                                //Utility.logFile(context.getApplication(), "Add panel Response successful Id Panel Zero::" + idPanel, true);

                                // As of now we need to remove this code as sync stuck issue solved from back end.
                                /*Realm realm = Realm.getDefaultInstance();
                                Panel panel = realm.where(Panel.class).equalTo("idPanelLocal", localPanelId).findFirst();
                                if (panel != null) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {

                                            Log.e("== Deleted",""+localPanelId);

                                            panel.deleteFromRealm();
                                            interfaceData.addPanelResponse(result,0);
                                            Log.e("== Sync Start again","::");

                                        }
                                    });
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    public void addReportPanel(long userId, String api_key,
                               String latitude,
                               String longitude,
                               String idRaisonSignalement,
                               String rating,
                               String comments,
                               String adresse,
                               String campagneId,
                               String code_postal,
                               JSONArray photosJsonForm,
                               int lap_number,
                               int turn_number) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("userId", userId);
        params.put("campagneId", campagneId);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("idRaisonSignalement", idRaisonSignalement);
        params.put("rating", rating);
        params.put("comments", comments);
        params.put("adresse", adresse);
        params.put("code_postal", code_postal);
        params.put("lap_number", lap_number);
        params.put("turn_number", turn_number);
        
        params.put("photos", photosJsonForm);

        JSONObject jsonParams = new JSONObject(params);

        String jsonParamsClean = jsonParams.toString().replace("\\\"", "\"");
        jsonParamsClean = jsonParamsClean.replace("\"{", "{");
        jsonParamsClean = jsonParamsClean.replace("}\"", "}");

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParamsClean));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/create-absent-panel")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result result = new Result();

                        client.dispatcher().executorService().shutdown();

                        interfaceData.resetSynch();

                        //Utility.logFile(context.getApplication(), "Add panel failed ::",true);

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            client.dispatcher().executorService().shutdown();

                            interfaceData.resetSynch();

                            //Utility.logFile(context.getApplication(), "Add panel Response not successful ::",true);

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();
                Log.e("response panel :", responseJson);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            int idPanel = 0;

                            if (result.isStateSuccess() && jsonObject.has("idPanel")) {
                                idPanel = jsonObject.getInt("idPanel");
                            }

                            client.dispatcher().executorService().shutdown();

                            if (idPanel != 0) {
                                interfaceData.addPanelResponse(result, idPanel);

                                //Utility.logFile(context.getApplication(), "Add panel Response successful ::" + idPanel, true);
                            } else {

                                //Utility.logFile(context.getApplication(), "Add panel Response successful Id Panel Zero::" + idPanel, true);

                                // As of now we need to remove this code as sync stuck issue solved from back end.
                                /*Realm realm = Realm.getDefaultInstance();
                                Panel panel = realm.where(Panel.class).equalTo("idPanelLocal", localPanelId).findFirst();
                                if (panel != null) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {

                                            Log.e("== Deleted",""+localPanelId);

                                            panel.deleteFromRealm();
                                            interfaceData.addPanelResponse(result,0);
                                            Log.e("== Sync Start again","::");

                                        }
                                    });
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    public void validatePanel(String api_key,
                              String userId,
                              String idPanel,
                              String latitude,
                              String longitude,
                              String idStatePanel,
                              String rating,
                              String comments,
                              String campagneId,
                              JSONArray photosJsonForm,
                              int turnNumber,
                              int lapNumber) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("userId", userId);
        params.put("campagneId", campagneId);
        params.put("idPanel", idPanel);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("idStatePanel", idStatePanel);
        params.put("rating", rating);
        params.put("comments", comments);
        params.put("photos", photosJsonForm);
        params.put("lap_number", lapNumber);
        params.put("turn_number", turnNumber);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/validate-panel")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        client.dispatcher().executorService().shutdown();

                        interfaceData.resetSynch();

                        //Utility.logFile(context.getApplication(), "Validate panel failed ::",true);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            client.dispatcher().executorService().shutdown();

                            interfaceData.resetSynch();

                            //Utility.logFile(context.getApplication(), "Validate Panel Response not successful ::",true);

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            int idPanel = 0;
                            if (result.isStateSuccess() && jsonObject.has("idPanel")) {
                                idPanel = jsonObject.getInt("idPanel");
                            }

                            //Utility.logFile(context.getApplication(), "Validate Panel Response successful ::"+idPanel,true);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.validatePanelResponse(result, idPanel);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }

    public void reportPanel(String api_key,
                            String userId,
                            String campagneId,
                            String idPanel,
                            String latitude,
                            String longitude,
                            String idRaisonSignalement,
                            String rating,
                            String comments,
                            JSONArray photosJsonForm,
                            int turnNumber,
                            int lapNumber) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("userId", userId);
        params.put("idPanel", idPanel);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("idRaisonSignalement", idRaisonSignalement);
        params.put("rating", rating);
        params.put("comments", comments);
        params.put("campagneId", campagneId);
        params.put("photos", photosJsonForm);
        params.put("lap_number", lapNumber);
        params.put("turn_number", turnNumber);

        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/report-panel")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        client.dispatcher().executorService().shutdown();

                        interfaceData.resetSynch();

                        //Utility.logFile(context.getApplication(), "reportPanel web service failed ::",true);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Result result = new Result();

                            client.dispatcher().executorService().shutdown();

                            interfaceData.resetSynch();

                            //Utility.logFile(context.getApplication(), "reportPanel web service response not successful ::",true);

                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            int idPanel = 0;
                            if (result.isStateSuccess() && jsonObject.has("idPanel")) {
                                idPanel = jsonObject.getInt("idPanel");
                            }

                            //Utility.logFile(context.getApplication(), "reportPanel web service response successful ::"+idPanel,true);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.reportPanelResponse(result, idPanel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

    }


    /**
     * Retourne les panneaux qui ont changé de statut après que l’afficheur ait validé ou ait signalé un panneau.
     *
     * @param api_key
     * @param last_sync_date
     * @param userId
     */
    public void refreshPanels(String api_key,
                              String last_sync_date,
                              long userId) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        if (GenericTools.isNullOrEmpty(last_sync_date)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            last_sync_date = dateFormat.format(calendar.getTime());
        }

        HashMap params = new HashMap();

        params.put("idUser", userId);
        params.put("last_date_sync", last_sync_date);


        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/refreshPanels")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        client.dispatcher().executorService().shutdown();
                        //Utility.logFile(context.getApplication(), "refreshPanel web service failed ::",true);
                    }
                });
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(AppData.class.getCanonicalName(), "response not successfull");
                            client.dispatcher().executorService().shutdown();
                            //Utility.logFile(context.getApplication(), "refresh Panel web service response not successful ::",true);
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                Log.d(AppData.class.getName(), "End refreshPanels.......");

                try {

                    final String responseJson = response.body().string();
                    Log.e("refresh panel response : ", responseJson);

                    JSONObject jsonObject = new JSONObject(responseJson);

//                    JSONObject resultObject = jsonObject.getJSONObject("result");

                    Result result = new Result();
                    result.populateObject(jsonObject);

                    if (result.isStateSuccess()) {

                        JSONObject recordsJsonObject = jsonObject.getJSONObject("records");

                        if (recordsJsonObject != null && !recordsJsonObject.isNull("panels")) {

                            JSONArray panelsJsonArray = recordsJsonObject.getJSONArray("panels");

                            for (int i = 0; i < panelsJsonArray.length(); i++) {

                                JSONObject miniJsonPanel = panelsJsonArray.getJSONObject(i);
//145143
                                if (miniJsonPanel != null) {

                                    long idPanel = miniJsonPanel.getLong("idPanel");
                                    final int idStatePanel = miniJsonPanel.getInt("idStatePanel");
                                    final int idRaisonSignalement = miniJsonPanel.getInt("idRaisonSignalement");

                                    if (idPanel > 0) {

                                        Realm realm = Realm.getDefaultInstance();

                                        try {
                                            Panel panelFromBDD = Panel.getPanelByServerId((int) idPanel, realm);

                                            if (panelFromBDD != null) {
                                                final Panel panel = realm.copyFromRealm(panelFromBDD);

                                                if (panel != null) {
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm bgRealm) {

                                                            panel.setIdStatePanel(idStatePanel);
                                                            panel.setIdRaisonSignalement(idRaisonSignalement);
                                                            panel.updateOrCreateViaWS(activity, bgRealm);

                                                        }
                                                    });
                                                }

                                            } else {
                                                // Here we need get the panel and save in the database
                                                // Call the panel details web service and save in the database

                                                //Utility.logFile(context.getApplication(), "Get Panel By Id as other user added this ::",true);

                                                FapApplication.panels_index = 1;
                                                getPanelById(UtilsUser.getUser(activity).getApi_key(), String.valueOf(UtilsUser.getUser(activity).getIdUser()), "" + idPanel);

                                            }
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        } finally {
                                            realm.close();
                                        }
                                    }
                                }
                            }
                        }

                        String last_sync_date = "";
                        if (recordsJsonObject != null && !recordsJsonObject.isNull("last_date_sync")) {
                            last_sync_date = recordsJsonObject.getString("last_date_sync");
                        }
                        FapTools.saveLastSyncDate(activity, last_sync_date);

                    } else {
                        Log.d(AppData.class.getCanonicalName(), "State result false .....");
                    }

                    client.dispatcher().executorService().shutdown();
                    interfaceData.refreshPanelsResponse(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }


    public void registerDeviceForUser(String api_key, long idUser, String deviceUDID, String
            appName, String appIdentifier, String appVersion, String osName, String osVersion) {

        /**
         * GET
         * */

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        HashMap params = new HashMap();

        params.put("user_id", idUser);
        params.put("device_udid", deviceUDID);
        params.put("app_name", appName);
        params.put("app_identifier", appIdentifier);
        params.put("app_version", appVersion);
        params.put("os_name", osName);
        params.put("os_version", osVersion);


        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/registerDevice")
                .put(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        client.dispatcher().executorService().shutdown();


                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("TAG", "Response <> 200 :" + response.message());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* if (interfaceData != null){
                                interfaceData.requestError(response.message());
                            }*/
                            client.dispatcher().executorService().shutdown();

                        }
                    });
                    return;
                }

                final String responseJson = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(responseJson);
                            Result result = new Result();
                            if (jsonObject.has("result") && !jsonObject.isNull("result")) {
                                result.populateObject(jsonObject);
                            }

                            IPBXInfo ipbxInfo = new IPBXInfo();

                            ipbxInfo.populateObject(jsonObject);


                            client.dispatcher().executorService().shutdown();

                            interfaceData.registerDeviceForUserResponse(result, ipbxInfo);
                        } catch (Exception ex) {

                        }


                    }
                });
            }

        });

    }


    public void getLastAppVersion(String api_key) {

        /**
         * GET
         * */

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/app-version")
                .get()
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interfaceData != null) {
                            interfaceData.requestError(e.getLocalizedMessage());
                        }
                        client.dispatcher().executorService().shutdown();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("TAG", "Response <> 200 :" + response.message());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (interfaceData != null) {
                                interfaceData.requestError(response.message());
                            }
                            client.dispatcher().executorService().shutdown();

                        }
                    });
                    return;
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);
                            Result result = new Result();
                            if (jsonObject.has("result") && !jsonObject.isNull("result")) {
                                result.populateObject(jsonObject);
                            }
                            String version_number = "";
                            String app_url = "";
                            boolean forceUpdate = false;

                            if (jsonObject.has("app-version") && !jsonObject.isNull("app-version")) {
                                JSONObject jsonObjectAppVersion = jsonObject.getJSONObject("app-version");


                                if (result.isStateSuccess() && (jsonObjectAppVersion.has("version_number") && !jsonObjectAppVersion.isNull("version_number"))) {
                                    version_number = jsonObjectAppVersion.getString("version_number");
                                }


                                if (result.isStateSuccess() && (jsonObjectAppVersion.has("app_url") && !jsonObjectAppVersion.isNull("app_url"))) {
                                    app_url = jsonObjectAppVersion.getString("app_url");
                                }


                                if (result.isStateSuccess() && (jsonObjectAppVersion.has("forceUpdate") && !jsonObjectAppVersion.isNull("forceUpdate"))) {
                                    forceUpdate = jsonObjectAppVersion.getBoolean("forceUpdate");
                                }
                            }


                            client.dispatcher().executorService().shutdown();

                            interfaceData.getLastAppVersionResponse(result, version_number, app_url, forceUpdate);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

        });

    }

    public void getProximityPanel(String api_key, String latitude, String longitude) {

        /**
         * GET
         * */

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        HashMap params = new HashMap();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/getProximityPanel" + "/1/" + latitude + "/" + longitude)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            client.dispatcher().executorService().shutdown();


                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    try {
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(AppData.class.getCanonicalName(), "response not successfull");
                                    client.dispatcher().executorService().shutdown();

                                }
                            });
                            // throw new IOException("Unexpected code " + response);
                        }
                    } catch (Exception ex) {

                    }


                } else {
                    Log.e(AppData.class.getCanonicalName(), "response successfull");
                }

                Log.d(AppData.class.getName(), "End getProximityPanel.......");

                try {

                    final String responseJson = response.body().string();

                    JSONObject jsonObject = new JSONObject(responseJson);

//                    JSONObject resultObject = jsonObject.getJSONObject("result");

                    Result result = new Result();
                    result.populateObject(jsonObject);

                    if (result.isStateSuccess()) {


                        if (jsonObject != null && !jsonObject.isNull("records")) {

                            JSONArray panelsJsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < panelsJsonArray.length(); i++) {

                                JSONObject miniJsonPanel = panelsJsonArray.getJSONObject(i);

                                if (miniJsonPanel != null) {

                                    long idPanel = miniJsonPanel.getLong("idPanel");
                                    final int idStatePanel = miniJsonPanel.getInt("idStatePanel");
                                    final int idRaisonSignalement = miniJsonPanel.getInt("idRaisonSignalement");
                                    final int idCampagne = miniJsonPanel.getInt("idCampagne");
                                    final int turnNumber = miniJsonPanel.getInt("turn_number");
                                    final int lapNumber = miniJsonPanel.getInt("passage_number");

                                    if (idPanel > 0) {
                                        Realm realm = Realm.getDefaultInstance();
                                        try {


                                            Panel panelFromBDD = Panel.getPanelByServerId((int) idPanel, realm);

                                            if (panelFromBDD != null) {
                                                final Panel panel = realm.copyFromRealm(panelFromBDD);

                                                if (panel != null) {
                                                    realm.executeTransaction(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm bgRealm) {

                                                            if ((panel == null)
                                                                    || (panel.getTurnNumber() != panel.getTurnNumber())
                                                                    || (panel.getIdStatePanel() == Constant.PANEL_STATE_TO_DISPLAY)) {
                                                                if (panel.addressObject != null) {
                                                                    panel.addressObject.updateOrCreate(bgRealm);
                                                                    panel.setIdStatePanel(idStatePanel);
                                                                    panel.setIdRaisonSignalement(idRaisonSignalement);
                                                                    panel.setIdCampagne(idCampagne);
                                                                    panel.setLapNumber(lapNumber);
                                                                    panel.setTurnNumber(turnNumber);
                                                                    panel.updateOrCreateViaWS(activity, bgRealm);
                                                                }

                                                            }
                                                        }
                                                    });
                                                }

                                            }

                                        } catch (Exception e) {
                                            //handle exceptions
                                        } finally {
                                            realm.close();
                                        }
                                    }

                                }
                            }
                        }

                    } else {
                        Log.d(AppData.class.getCanonicalName(), "State result false .....");
                    }

                    client.dispatcher().executorService().shutdown();

                    interfaceData.getProximityPanelResponse(result);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }


    public void updateRgpd(String api_key, long userId) {
        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("idUser", userId);
        JSONObject jsonParams = new JSONObject(params);

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/update-rgpd")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);


                            if (result.isStateSuccess()) {
                                interfaceData.updateRgpdResponse(result);
                            }

                            client.dispatcher().executorService().shutdown();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });
    }

    public void getRgpdText(String api_key) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/get-rgpd-text")
                .get()
                .addHeader("Authorization", api_key)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                interfaceData.timeOutServer();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            RgpdTextResponse rgpdTextResponse = new RgpdTextResponse();

                            if (result.isStateSuccess()) {

                                rgpdTextResponse.populateObject(jsonObject);

                                interfaceData.OnRgpdTextResponse(result, rgpdTextResponse.getRgpdText());
                            }

                            client.dispatcher().executorService().shutdown();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });
    }

    public void getPanelById(String api_key, String userId,
                             String idPanel
    ) {

        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNEXION_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DURATION, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        /** Création des paramètres */

        HashMap params = new HashMap();

        params.put("idUser", userId);
        params.put("idPanel", idPanel);

        JSONObject jsonParams = new JSONObject(params);

        String jsonParamsClean = jsonParams.toString().replace("\\\"", "\"");
        jsonParamsClean = jsonParamsClean.replace("\"{", "{");
        jsonParamsClean = jsonParamsClean.replace("}\"", "}");

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParamsClean));

        final Request request = new Request.Builder()
                .url(Constant.URL_SERVER + "/getPanelById")
                .post(body)
                .addHeader("Authorization", api_key)
                .build();

        Log.e("URL", Constant.URL_SERVER + "/getPanelById");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "Failed to execute " + request, e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result result = new Result();
                        client.dispatcher().executorService().shutdown();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            client.dispatcher().executorService().shutdown();
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                final String responseJson = response.body().string();
                Log.e("response panel :", responseJson);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONObject jsonObject = new JSONObject(responseJson);

                            Result result = new Result();

                            result.populateObject(jsonObject);

                            Panel panel = new Panel();

                            panel.populateObject(jsonObject.getJSONObject("panel"));

                            ArrayList<Panel> panelList = new ArrayList<>();
                            panelList.add(panel);

                            client.dispatcher().executorService().shutdown();

                            interfaceData.getPanelsResponse(result, panelList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });

    }
}
