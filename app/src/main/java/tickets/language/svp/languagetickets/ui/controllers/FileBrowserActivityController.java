package tickets.language.svp.languagetickets.ui.controllers;

import java.io.File;

import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.ui.activities.FileBrowserActivity;

public class FileBrowserActivityController extends ActivityController<FileBrowserActivity> {
    public FileBrowserActivityController(FileBrowserActivity activity) {
        super(activity);
    }

    public void updateDatabaseFilePath(String filename) {
        userSettings.putDatabasePathToSettings(filename);
    }
}
