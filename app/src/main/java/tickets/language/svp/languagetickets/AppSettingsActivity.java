package tickets.language.svp.languagetickets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;


import java.util.List;

import tickets.language.svp.languagetickets.ui.controllers.AppSettingsActivityController;
import tickets.language.svp.languagetickets.ui.listeners.OnSettingsListPreferenceChangeListener;
import tickets.language.svp.languagetickets.ui.listeners.OnTClickListener;
import tickets.language.svp.languagetickets.ui.settings.PreferenceConts;

import static tickets.language.svp.languagetickets.AppSettingsActivity.OnSettingsDBSelectingChangeListener.*;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class AppSettingsActivity extends PreferenceActivity {
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private AppSettingsActivityController contoller;
    private boolean isPreferencesScreenBuilt;

    private static class OnDBSelectingClickListener implements OnTClickListener<SelectingStorage>{
        private final AppSettingsActivityController contoller;
        public OnDBSelectingClickListener(AppSettingsActivityController contoller){
            this.contoller = contoller;
        }
        @Override
        public void onClick(SelectingStorage v) {
            contoller.setDBSelected(v);
        }
    }

    private OnSettingsDBSelectingChangeListener dbSelectingListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contoller = new AppSettingsActivityController(this);
        dbSelectingListener = new OnSettingsDBSelectingChangeListener(new OnDBSelectingClickListener(contoller));
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        updatePreferencesScreen();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(RESULT_CANCELED == resultCode){
            return;
        }
        if(resultCode == RESULT_OK){
            //restartActivity();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Add 'database' preferences, and a corresponding header.
        PreferenceCategory databaseHeader = new PreferenceCategory(this);
        databaseHeader.setTitle(R.string.pref_header_database);
        getPreferenceScreen().addPreference(databaseHeader);
        addPreferencesFromResource(R.xml.pref_database);
        
        bindPreferenceSummaryToValue(findPreference(PreferenceConts.appsettings_create_db_from),dbSelectingListener);

        isPreferencesScreenBuilt = true;

        updatePreferencesScreen();
    }
    private void updatePreferencesScreen(){
        if(!isPreferencesScreenBuilt){
            return;
        }
        //database
        findPreference(PreferenceConts.appsettings_select_db_path)
                .setSummary(contoller.userSettings.getDbActivateSettings().path.getPath());
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     */
    private static void bindPreferenceSummaryToValue(Preference preference, Preference.OnPreferenceChangeListener listener) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(listener);

        // Trigger the listener immediately with the preference's
        // current value.
        listener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DatabasePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_database);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("example_text"), sBindPreferenceSummaryToValueListener);
            //bindPreferenceSummaryToValue(findPreference("appsettings_create_db_from"), dbSelectingListener);
        }
    }

    //inner classes

    public static class OnSettingsDBSelectingChangeListener extends
            OnSettingsListPreferenceChangeListener<OnSettingsDBSelectingChangeListener.SelectingStorage> {
        public enum SelectingStorage {
            Application(0),
            Device(1),
            GoogleDrive(2);
            private final int code;

            private SelectingStorage(int code) {
                this.code = code;
            }
            public int toInt() {
                return code;
            }
            public String toString() {
                return String.valueOf(code);
            }
            public static SelectingStorage parse(Object value){
                try {
                    int val = Integer.parseInt(value.toString());
                    switch (val) {
                        case 0: return SelectingStorage.Application;
                        case 1: return SelectingStorage.Device;
                        case 2: return SelectingStorage.GoogleDrive;
                    }
                }catch (Exception ex){

                }
                return SelectingStorage.Application;
            }
        }

        public OnSettingsDBSelectingChangeListener(OnTClickListener<SelectingStorage> listener){
            super(listener);
        }

        @Override
        protected SelectingStorage parseResult(Object value) {
            return SelectingStorage.parse(value);
        }
    }
}
