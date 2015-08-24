package tickets.language.svp.languagetickets.queries;

import tickets.language.svp.languagetickets.domain.db.Database;
import tickets.language.svp.languagetickets.domain.IQueryObject;

/**
 * Created by Pasha on 2/1/2015.
 */
public abstract class AQueryObject<T> extends Database implements IQueryObject<T> {
    protected final StringBuilder query;
    protected AQueryObject(){
        query = new StringBuilder();
    }
}
