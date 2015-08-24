package tickets.language.svp.languagetickets.domain;

import android.database.Cursor;

/**
 * Created by Pasha on 2/1/2015.
 */
public interface IQueryObject<T> {
    public String[] getQuery();
    public T[] parse(Cursor cursor);
}

