package tickets.language.svp.languagetickets.ui.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import tickets.language.svp.languagetickets.AppSettingsActivity;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.domain.db.DatabaseStorage;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;


public class UserSettings {
    public AppSettingsActivity.OnSettingsDBSelectingChangeListener.SelectingStorage selectingStorage;
    private final Activity activity;
    private DatabaseStorage currentDatabaseStorage;

    public UserSettings(Activity activity){
        this.activity = activity;


        //DbActivateSettings


        //
//        String pp = preferences.getString(PreferenceConts.appsettings_select_db_path,null);
//
//        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(activity).edit();
//        prefs.putString(PreferenceConts.appsettings_select_db_path, "put path!");
//
//        prefs.commit();
    }

    public DbActivateSettings getDbActivateSettings(){
        SharedPreferences.Editor editor = setter();
        SharedPreferences preferences = getter();

        selectingStorage = AppSettingsActivity.OnSettingsDBSelectingChangeListener.SelectingStorage.parse(
                preferences.getString(PreferenceConts.appsettings_create_db_from, null));
        DatabaseStorage ds = selectingStorage == AppSettingsActivity.OnSettingsDBSelectingChangeListener.SelectingStorage.Application ?
                DatabaseStorage.Application :DatabaseStorage.LocalStore;
        File path;
        switch (ds){
            case Application:
                if(currentDatabaseStorage == ds) {
                    //create default path for application
                    File data = Environment.getDataDirectory();
                    String currentDBPath =
                            "/data/tickets.language.svp.languagetickets/databases/"
                            + Repository.dbName;
                    path = new File(data, currentDBPath);
                    editor.putString(PreferenceConts.appsettings_select_db_path, path.getPath());
                    editor.commit();
                }
            break;
            case LocalStore:
//                File path1 = new File(Environment.getExternalStorageDirectory() + "/Download/db_languagetickets");
//                editor.putString(PreferenceConts.appsettings_select_db_path, path1.getPath());
//                editor.commit();
                break;
            default:
                throw new InternalError("Incorrect type of db storage: " + ds.toString());
        }
        String text = preferences.getString(PreferenceConts.appsettings_select_db_path, null);
        path = new File(text);
        currentDatabaseStorage = ds;
        return new DbActivateSettings(ds, path);
    }

    public void putDatabasePathToSettings(String path){
        SharedPreferences.Editor editor = setter();
        editor.putString(PreferenceConts.appsettings_select_db_path, path);
        editor.commit();
    }



    private SharedPreferences getter(){
        return PreferenceManager.getDefaultSharedPreferences(activity);
    }
    private SharedPreferences.Editor setter() {
        return PreferenceManager.getDefaultSharedPreferences(activity).edit();
    }
}
