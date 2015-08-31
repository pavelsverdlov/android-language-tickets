package tickets.language.svp.languagetickets.domain;

import android.database.Cursor;

import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;

/**
 * Created by Pasha on 2/1/2015.
 */
public interface IQueryObject<T> {
    public DbActivateSettings getConnectSettings();
    public String[] getQuery();
    public T[] parse(Cursor cursor);
}

