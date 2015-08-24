package tickets.language.svp.languagetickets.domain;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import tickets.language.svp.languagetickets.domain.db.Database;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;

/**
 * Created by Pasha on 1/4/2015.
 */
public class Repository extends Database {
//    private static boolean isTest = false;
    private final DbActivateSettings activateSettings;
    public Repository(DbActivateSettings activateSettings){
        this.activateSettings = activateSettings;
        activate(activateSettings);
//        if(isTest) return;
//        SQLiteDatabase db = null;
//        try {
//            db = open();
//            TestData(db);
//        }finally {
//            db.close();
//        }
//        isTest = true;
    }

    public<T> T[] get(IQueryObject<T> query){
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = open(activateSettings);
            String[] queries = query.getQuery();
            if(queries.length != 1){
                throw new InternalError("get - queries.length != 1");
            }
            String q = queries[0];
         //   Log.d("get", q);
            c = db.rawQuery(q,null);
            return query.parse(c);
        } catch (SQLiteException sqle){
            sqle.toString();
           // Log.e("get",sqle.getMessage(),sqle);
        } finally {
            if (c != null) { c.close(); }
            if (db != null) { db.close(); }
        }
        //@SuppressWarnings("unchecked")
        //final T[] arr = (T[])new Object[0];
        return null;
       // return (T[]) Array.newInstance(new Class<T>(), 0);
    }
    public void add(IQueryObject query){
        execute(query);
    }
    public void remove(IQueryObject query){
        execute(query);
    }
    public void update(IQueryObject update) {
        execute(update);
    }
    private void execute(IQueryObject query){
            SQLiteDatabase db = null;
            try {
                db = open(activateSettings);
                String[] queries = query.getQuery();
                for (int i =0; i < queries.length; ++i) {
                    db.execSQL(queries[i]);
                }
            }finally {
                if(db != null){
                    db.close();
                }
            }
    }

}
