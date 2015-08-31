package tickets.language.svp.languagetickets.queries;

import tickets.language.svp.languagetickets.domain.db.Database;
import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;

/**
 * Created by Pasha on 2/1/2015.
 */
public abstract class AQueryObject<T> extends Database implements IQueryObject<T> {
    protected final StringBuilder query;
    private final DbActivateSettings settings;
    protected AQueryObject(DbActivateSettings settings){
        query = new StringBuilder();
        this.settings = settings;
    }
    public DbActivateSettings getConnectSettings(){
        return settings;
    }
}
