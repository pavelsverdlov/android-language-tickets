package tickets.language.svp.languagetickets.ui.controllers;

import tickets.language.svp.languagetickets.AppSettingsActivity;

public class AppSettingsActivityController extends ActivityController<AppSettingsActivity> {
//    private AppSettingsActivity.OnSettingsDBSelectingChangeListener.SelectingStorage currentDbType;

    public AppSettingsActivityController(AppSettingsActivity activity) {
        super(activity);
//        currentDbType = userSettings.selectingStorage;
    }

    public void setDBSelected(AppSettingsActivity.OnSettingsDBSelectingChangeListener.SelectingStorage type){
//        if(currentDbType != type){
//            switch (type){
//                case GoogleDrive:
//                    goToGoogleDriveActivity();
//                    break;
//            }
//            currentDbType = type;
//        }
    }

}
