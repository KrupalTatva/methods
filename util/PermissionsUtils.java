

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionsUtils {

    private Context context;
    private Activity current_activity;

    private PermissionResultCallback permissionResultCallback;


    private ArrayList<String> listPermissionsNeeded = new ArrayList<>();
    private int req_code;
    private ArrayList<String> pending_permissions = new ArrayList<>();

    public ArrayList<String> getPendingPermission() {
        return pending_permissions;
    }

    public PermissionsUtils(Context context) {
        this.context = context;
        this.current_activity = (Activity) context;

        permissionResultCallback = (PermissionResultCallback) context;


    }

    public PermissionsUtils(Context context, PermissionResultCallback callback) {
        this.context = context;
        this.current_activity = (Activity) context;

        permissionResultCallback = callback;


    }


    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param request_code
     */

    public void check_permission(ArrayList<String> permissions, int request_code) {
        //ArrayList<String> permission_list = permissions;
        this.req_code = request_code;

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permissions, request_code)) {
                permissionResultCallback.PermissionGranted(request_code);
                Log.i("all permissions", "granted");
                Log.i("proceed", "to callback");
            }
        } else {
            permissionResultCallback.PermissionGranted(request_code);

            Log.i("all permissions", "granted");
            Log.i("proceed", "to callback");
        }

    }


    /**
     * Check and request the Permissions
     *
     * @param permissions
     * @param request_code
     * @return
     */

    public boolean checkAndRequestPermissions(ArrayList<String> permissions, int request_code) {

        if (permissions.size() > 0) {
            listPermissionsNeeded = new ArrayList<>();

            for (int i = 0; i < permissions.size(); i++) {
                int hasPermission = ContextCompat.checkSelfPermission(current_activity, permissions.get(i));

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(current_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), request_code);
                return false;
            }
        }

        return true;
    }

    public boolean checkPermission(ArrayList<String> permissions) {
        boolean hasPermission = false;
        for (int i = 0; i < permissions.size(); i++) {
            hasPermission = ContextCompat.checkSelfPermission(current_activity, permissions.get(i)) == PackageManager.PERMISSION_GRANTED;
            if (!hasPermission)
                break;
        }
        return hasPermission;
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                   pending_permissions = new ArrayList<>();
                    final ArrayList<String> never_ask_permissions = new ArrayList<>();
                    final ArrayList<String> granted_permissions = new ArrayList<>();
                    for (String permission : permissions) {
                        if (ContextCompat.checkSelfPermission(context, permission)
                                != PackageManager.PERMISSION_GRANTED) {

                            if(ActivityCompat.shouldShowRequestPermissionRationale(current_activity, permission)){
                                Log.d("TAG", "onRequestPermissionsResult: ");
                                pending_permissions.add(permission);
                            }else{
                                Log.d("TAG", "onRequestPermissionsResult: naver show ");
                                never_ask_permissions.add(permission);
                            }

                        }
                        /*if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity, permission)) {
                            //denied
                            Log.e("denied", permission);
                            permissionResultCallback.PermissionDenied(req_code);
                            return;
                        } else {
                            if (ActivityCompat.checkSelfPermission(current_activity, permission) == PackageManager.PERMISSION_GRANTED) {
                                //allowed
                                Log.e("allowed", permission);
                                if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity, permission)) {
                                    pending_permissions.add(permission);
                                }
                            } else {
                                //set to never ask again
                                Log.e("set to never ask again", permission);
                                permissionResultCallback.NeverAskAgain(req_code);
                                return;
                            }
                        }*/
                    }

                    if(never_ask_permissions.size() > 0){
                        permissionResultCallback.NeverAskAgain(req_code);
                    }

                    if (pending_permissions.size() > 0) {
                        permissionResultCallback.PermissionDenied(req_code);
                    } else {
                        Log.i("all", "permissions granted");
                        Log.i("proceed", "to next step");
                        permissionResultCallback.PermissionGranted(req_code);

                    }
                }


                break;
        }
    }


    /**
     * Explain why the app needs permissions
     * <p>
     * //     * @param message
     * //     * @param okListener
     */
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(current_activity)
//                .setMessage(message)
//                .setPositiveButton("Ok", okListener)
//                .setNegativeButton("Cancel", okListener)
//                .create()
//                .show();
//    }

    public interface PermissionResultCallback {
        void PermissionGranted(int request_code);

        void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);

        void PermissionDenied(int request_code);

        void NeverAskAgain(int request_code);
    }
}
