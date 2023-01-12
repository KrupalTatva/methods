package com.app.fap.librairies;


import static com.app.fap.base.BaseActivity.stopAndCancelSavePanelsTask;

import android.content.Context;
import android.util.Log;

import com.app.fap.FapApplication;
import com.app.fap.fragments.HomeFragment;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrationClass implements RealmMigration {

    private Context applicationContext;

    public RealmMigrationClass(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Log.e("RealmMigrationClass", "migrate: oldVersion ="+oldVersion+" newVersion = "+newVersion );
        if (oldVersion == 0) {
            RealmSchema sessionSchema = realm.getSchema();

            RealmObjectSchema sessionObjSchema = sessionSchema.get("Panel");
            if (!sessionObjSchema.getFieldNames().contains("region")) {
                sessionObjSchema.addField("region", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                obj.set("region", "");
                            }


                        });


                stopAndCancelSavePanelsTask();
                FapTools.savePasswordToPreferences(applicationContext, "");
                FapToolsSingleton.destroyInstance();
                HomeFragment.destroyInstance();
                FapApplication.needtoOpenMain = true;
                FapApplication.panels_index = 1;
                UtilsUser.saveUserToPreferences(applicationContext, null);
                UtilsUser.resetUser();

//                    openConnexionScreen();
                    /*Intent intent = new Intent(applicationContext, ConnectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(intent);*/


                oldVersion++;
            }
        } else if (oldVersion == 1) {
            RealmSchema sessionSchema = realm.getSchema();

            RealmObjectSchema sessionObjSchema = sessionSchema.get("User");
            if (!sessionObjSchema.getFieldNames().contains("rgpd") && !sessionObjSchema.getFieldNames().contains("rgpd_text")) {
                sessionObjSchema.addField("rgpd", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                obj.set("rgpd", "");
                            }


                        });

                sessionObjSchema.addField("rgpd_text", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                obj.set("rgpd_text", "");
                            }
                        });

                stopAndCancelSavePanelsTask();
                FapTools.savePasswordToPreferences(applicationContext, "");
                FapToolsSingleton.destroyInstance();
                HomeFragment.destroyInstance();
                FapApplication.needtoOpenMain = true;
                FapApplication.panels_index = 1;
                UtilsUser.saveUserToPreferences(applicationContext, null);
                UtilsUser.resetUser();

//                    openConnexionScreen();
                    /*Intent intent = new Intent(applicationContext, ConnectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(intent);*/


                oldVersion++;
            }
        } else if (oldVersion == 2) {

            RealmSchema sessionSchema = realm.getSchema();
//            tour_actuel = tour_numéro = turn_number
//            passage_actuel = passage_numéro = lap_number

            boolean isUpdated = false;

            RealmObjectSchema panelObjSchema = sessionSchema.get("Panel");
            if (!panelObjSchema.getFieldNames().contains("turnNumber")) {
                panelObjSchema.addField("turnNumber", Integer.class)
                        .transform(obj -> obj.set("turnNumber", Integer.MIN_VALUE));

                isUpdated = true;

            }
            if (!panelObjSchema.getFieldNames().contains("lapNumber")) {
                panelObjSchema.addField( "lapNumber", Integer.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                obj.set("lapNumber", Integer.MIN_VALUE);
                            }
                        });
                isUpdated = true;
            }
            RealmObjectSchema panelPhotoObjSchema = sessionSchema.get("PanelPhoto");
            if (!panelPhotoObjSchema.getFieldNames().contains("turnNumber")) {
                panelPhotoObjSchema.addField("turnNumber", Integer.class)
                        .transform(obj -> obj.set("turnNumber", Integer.MIN_VALUE));

                isUpdated = true;

            }
            if (!panelPhotoObjSchema.getFieldNames().contains("lapNumber")) {
                panelPhotoObjSchema.addField("lapNumber", Integer.class)
                        .transform(obj -> obj.set("lapNumber", Integer.MIN_VALUE));
                isUpdated = true;
            }

            RealmObjectSchema campagneObjSchema = sessionSchema.get("Campagne");
            if (!campagneObjSchema.getFieldNames().contains("turnNumber")) {
                campagneObjSchema.addField("turnNumber", Integer.class)
                        .transform(obj -> obj.set("turnNumber", Integer.MIN_VALUE));
                isUpdated = true;
            }
            if (!campagneObjSchema.getFieldNames().contains("lapNumber")) {
                campagneObjSchema.addField("lapNumber", Integer.class)
                        .transform(obj -> obj.set("lapNumber", Integer.MIN_VALUE));
                isUpdated = true;
            }
            if (isUpdated) {
                stopAndCancelSavePanelsTask();
                FapTools.savePasswordToPreferences(applicationContext, "");
                FapToolsSingleton.destroyInstance();
                HomeFragment.destroyInstance();
                FapApplication.needtoOpenMain = true;
                FapApplication.panels_index = 1;
                UtilsUser.saveUserToPreferences(applicationContext, null);
                UtilsUser.resetUser();

                oldVersion++;
            }
        }

    }
}

