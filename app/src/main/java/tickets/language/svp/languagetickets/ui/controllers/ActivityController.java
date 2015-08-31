package tickets.language.svp.languagetickets.ui.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import tickets.language.svp.languagetickets.AddTicketActivity;
import tickets.language.svp.languagetickets.AppSettingsActivity;
import tickets.language.svp.languagetickets.GameLearningActivity;
import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.RootActivity;
import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.queries.DictionaryQueries;
import tickets.language.svp.languagetickets.queries.LanguageQueries;
import tickets.language.svp.languagetickets.queries.TicketQueries;
import tickets.language.svp.languagetickets.ui.ActivityOperationResult;
import tickets.language.svp.languagetickets.ui.BaseActivity;
import tickets.language.svp.languagetickets.ui.gdrive.GoogleDriveActivity;
import tickets.language.svp.languagetickets.ui.settings.UserSettings;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.AddNewTicket;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.AppSettings;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.EditTicket;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.GameLearning;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.GoogleDrive;

/**
 * Created by Pasha on 2/8/2015.
 */
public abstract class ActivityController<T extends Activity> extends Controller {
    public final T activity;
    public final UserSettings userSettings;

    public ActivityController(T activity) {
        this.activity = activity;
        userSettings = new UserSettings(activity);
        init(userSettings.getDbActivateSettings());
    }

    public ArrayList<DictionaryViewModel> GetDictionaries() {
        return super.GetDictionaries(userSettings.getDbActivateSettings());
    }
    public void removeTicket(TicketViewModel ticket) {
        super.removeTicket(userSettings.getDbActivateSettings(),ticket);
    }
    public LanguageViewModel[] getLanguages(){
        return super.getLanguages(userSettings.getDbActivateSettings());
    }
    public void addNewTicket(TicketViewModel ticket) {
        super.addNewTicket(userSettings.getDbActivateSettings(),ticket);
    }
    public TicketViewModel[] getTickets(DictionaryViewModel dictionary){
        return super.getTickets(userSettings.getDbActivateSettings(),dictionary);
    }

    public void goToAddTicketActivity(){
        goToActivity(AddNewTicket);
    }
    public void goToEditTicketActivity(){
        goToActivity(EditTicket);
    }
    public void goToGameLearningActivity(){
        goToActivity(GameLearning);
    }
    public void goToAppSettingsActivity(){
        goToActivity(AppSettings);
    }
    public void goToGoogleDriveActivity(){ goToActivity(GoogleDrive); }

    public void goToActivity(ActivityOperationResult type){
        switch (type){
            case Root:
                break;
            case EditTicket:
                BaseActivity ba = (BaseActivity)activity;
                Intent edit = new Intent(activity.getBaseContext(), AddTicketActivity.class);
                ba.storage.putTo(edit);
                activity.startActivityForResult(edit, EditTicket.toInt());
                break;
            case AddNewTicket:
                BaseActivity aba = (BaseActivity)activity;
                Intent add = new Intent(activity.getBaseContext(), AddTicketActivity.class);
                aba.storage.setSelectedTicket(TicketViewModel.createNew());
                aba.storage.putTo(add);
                activity.startActivityForResult(add, EditTicket.toInt());
                break;
            case GameLearning:
                Intent game = new Intent(activity.getBaseContext(), GameLearningActivity.class);
                activity.startActivityForResult(game, GameLearning.toInt());
                break;
            case AppSettings:
                Intent appsett = new Intent(activity.getBaseContext(), AppSettingsActivity.class);
                activity.startActivityForResult(appsett, AppSettings.toInt());
                break;
            case GoogleDrive:
                goToActivity(activity,type,GoogleDriveActivity.class);
                break;
        }
    }
    public static <T extends Activity> void goToActivity(Activity activity, ActivityOperationResult type, Class<T> _class){
        activity.startActivityForResult(new Intent(activity.getBaseContext(), _class), type.toInt());
    }
}
