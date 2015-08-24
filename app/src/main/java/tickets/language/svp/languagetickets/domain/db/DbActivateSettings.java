package tickets.language.svp.languagetickets.domain.db;

import android.os.Environment;

import java.io.File;

/**
 * Created by Pasha on 8/23/2015.
 */
public final class DbActivateSettings {
    public final DatabaseStorage type;
    /*
    * full path to database file
    * */
    public final File path;

    public DbActivateSettings(DatabaseStorage type, File path) {
        this.type = type;
        this.path = path;
    }
}
