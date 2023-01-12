package com.app.fap.webservices;

import com.app.fap.models.Campagnes;
import com.app.fap.models.IPBXInfo;
import com.app.fap.models.OpenCampagnes;
import com.app.fap.models.Panel;
import com.app.fap.models.Result;

import java.util.ArrayList;

/****************************************************
 * Created by Tahiana-MadiApps on 28/07/2016.
 ****************************************************/
public interface IAppData {

    void timeOutServer();

    void emptyResponseServer();

    void requestError(String error);

    void jsonKitException(Exception ex);

    void getTestResponse(Result result);

    void loginResponse(Result result, long user, String rgpd, String rgpd_text);

    void forgotPasswordResponse(Result result);

    void updateUserResponse(Result result);

    void updatePasswordResponse(Result result);

    void getPanelsResponse(Result result, ArrayList<Panel> panels);

    void getCampagnesResponse(Result result, Campagnes campagnes, boolean isCampagneEmpty);

    void getOpenCampagnesResponse(Result result, OpenCampagnes campagnes, boolean isCampagneEmpty);

    void uploadPhotoResponse(Result result, int idPanelPhoto);

    void addPanelResponse(Result result, int idPanel);

    void validatePanelResponse(Result result, int idPanel);

    void reportPanelResponse(Result result, int idPanel);

    void updateUserPosition(Result result);

    void refreshPanelsResponse(Result result);

    void resetSynch();

    void getLastAppVersionResponse(Result result, String version_number, String app_url, boolean forceUpdate);

    void registerDeviceForUserResponse(Result result, IPBXInfo ipbxInfo);

    void getProximityPanelResponse(Result result);

    void updateRgpdResponse(Result result);

    void OnRgpdTextResponse(Result result, String rgpdText);
}
