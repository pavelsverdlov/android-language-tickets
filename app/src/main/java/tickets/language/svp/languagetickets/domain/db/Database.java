package tickets.language.svp.languagetickets.domain.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by Pasha on 2/1/2015.
 */
public class Database {

    private static final int dbVersion = 2;
    public static final String dbName = "db_languagetickets";
    protected static final String dbLearnTicketsTableName = "tb_learntickets";
    private static final String dbSideTicketsTableName = "tb_sidetickets";
  //  private static final String dbLearnGroupsTableName = "tb_learngroups";
    private static final String dbDictionariesTableName = "tb_dictionaries";
    private static final String dbTicketDictionariesTableName = "tb_ticket_dictionaries";
    protected static final String dbTicketsLearnedTableName = "tb_tickets_learned";
    protected static final String dbLanguagesTableName = "tb_languages";



    private static SQLiteDatabase open1(){
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/tickets.language.svp.languagetickets/databases/"+dbName;
        File currentDB = new File(data, currentDBPath);
        String path = currentDB.getPath();

        File sd = Environment.getExternalStorageDirectory();
        File backupDB = new File(sd + "/Download", dbName);
        path = backupDB.getPath();
        return SQLiteDatabase.openDatabase(path,null, SQLiteDatabase.OPEN_READWRITE);
    }
//    private static SQLiteDatabase openFromApplication(){
//        File data = Environment.getDataDirectory();
//        String currentDBPath = "/data/"+ "tickets.language.svp.languagetickets" +"/databases/"+dbName;
//        File path = new File(data, currentDBPath);
//        return openFromLocalStore(path);
//    }
    private static SQLiteDatabase openFromLocalStore(File path){
        return SQLiteDatabase.openOrCreateDatabase(path, null);
//        return SQLiteDatabase.openDatabase(path.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }
    protected static SQLiteDatabase open(DbActivateSettings as){
        return openFromLocalStore(as.path);
//        switch (as.type){
//            case Application:
//                return openFromApplication();
//            case LocalStore:
//                return openFromLocalStore(as.path);
//            default:
//                throw new InternalError("Incorrect type of db storage: " + as.type.toString());
//        }
    }

    protected static void activate(DbActivateSettings as){
        //SQLiteDatabase.deleteDatabase(as.path);
        SQLiteDatabase db = open(as);
       // db = open();
        //SQLiteDatabase.openDatabase("path",null, SQLiteDatabase.OPEN_READWRITE);
        //http://stackoverflow.com/rquestions/3037767/create-sqlite-database-in-android
        //http://stackoverflow.com/questions/9583196/android-open-sqlite-database
        //http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android
       // File sd = Environment.getExternalStorageDirectory();
//        SQLiteDatabase db1 = context.openOrCreateDatabase(dbName, context.MODE_PRIVATE, null);
//        String path = db1.getPath();
        try {
            if(db.needUpgrade(dbVersion)){
            //db.execSQL("DROP TABLE IF EXISTS " + dbTicketsLearnedTableName);
            //dropAllTables(db);
//            db.execSQL("DROP TABLE IF EXISTS " + dbDictionariesTableName);
//            db.execSQL("DROP TABLE IF EXISTS " + dbTicketDictionariesTableName);

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbTicketsLearnedTableName +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, idDictionary INTEGER, idTicket VARCHAR, added VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbDictionariesTableName +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, created VARCHAR,sys INTEGER);");
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbTicketDictionariesTableName +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, idDictionary INTEGER, idTicket VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbLearnTicketsTableName
                    + " (id VARCHAR PRIMARY KEY, created TEXT, idSideFirst VARCHAR, idSideSecond VARCHAR," +
                    " learningSide TINYINT, background INT(3));");
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbSideTicketsTableName
                    + " (id VARCHAR PRIMARY KEY, created TEXT, learningText VARCHAR, language INT(3), correctCount INT(3), incorrectCount INT(3));");
//            db.execSQL("CREATE TABLE IF NOT EXISTS "
//                    + dbLearnGroupsTableName
//                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, created TEXT, title VARCHAR);");
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + dbLanguagesTableName
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR);");

            db.execSQL("INSERT INTO tb_languages (name) VALUES ('russian');");
            db.execSQL("INSERT INTO tb_languages (name) VALUES ('english');");

            //database2.rawQuery("select max(UnixTimeStamp) from Quote where EmoticonID=? and SubCategoryID=?" ,new String [] {String.valueOf(g),String.valueOf(s)});
            //db.execSQL("DROP TABLE IF EXISTS + TABLE_NAME);
                db.setVersion(dbVersion);
            }
        }catch (Throwable throwable){
            String message = throwable.getMessage();
            Throwable _case =throwable.getCause();
        }finally {
            db.close();
        }
    }

    public static void TestData(SQLiteDatabase db){
//        if (!checkDefaultGroup(db)) {
//            //create default group 'All'
//            Formatter f = new Formatter();
//            db.execSQL(f.format(insertLearnGroupTableQueryFormat, new Date(), learnGroupsName_All).toString());
//            checkDefaultGroup(db);
//        }
//        db.execSQL("INSERT INTO tb_languages (name) VALUES ('russian');");
//        db.execSQL("INSERT INTO tb_languages (name) VALUES ('english');");
//        db.execSQL("INSERT INTO tb_sidetickets (id, created, learningText, language, correctCount, incorrectCount) VALUES ('www-www-www', '2014-10-10', 'fast', 1, 0, 0);" );
//        db.execSQL("INSERT INTO tb_sidetickets (id, created, learningText, language, correctCount, incorrectCount) VALUES ('qqq-qqq-qqq', '2014-10-10', 'быстро', 2, 0, 0);");
//        db.execSQL("INSERT INTO tb_learntickets (id, created, idSideFirst, idSideSecond, learningSide, idGroup, background) VALUES ('eee-eee-eee', '2014-10-10', 'www-www-www', 'qqq-qqq-qqq', 0, 1,2);");
//        Cursor c = db.rawQuery("select * from tb_learntickets", null);
//        String id = null;
//        if(c.getCount() > 0){
//            id = c.getString(c.getColumnIndex("id"));
//        }
    }

    public static void export(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "tickets.language.svp.languagetickets" +"/databases/"+dbName;
        String backupDBPath = dbName;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();


//            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void dropAllTables(SQLiteDatabase db) {
        String[] tables = new String[]{
            dbLearnTicketsTableName,
            dbSideTicketsTableName,
            dbDictionariesTableName,
            dbTicketDictionariesTableName,
            dbTicketsLearnedTableName,
            dbLanguagesTableName
        };
        for (int i = 0; i < tables.length ; ++i){
            db.execSQL("DROP TABLE IF EXISTS " + tables[i]);
        }
    }


/*
    private static boolean checkDefaultGroup(SQLiteDatabase db){
        Cursor c = db.rawQuery("select * from " + dbLearnGroupsTableName +
                " where title = ?", new String[]{learnGroupsName_All});
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                learnGroupsId_All = c.getInt(c.getColumnIndex("id"));
                return true;
            }
            return false;
        }finally {
            c.close();
        }
    }
*/

    protected Formatter getFormatter(){
        return new Formatter();
    }

    protected Format getDateFormatter(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    protected static final String insertSideTicketTableQueryFormat =
            "INSERT INTO " + dbSideTicketsTableName +
                    " (id, created, learningText, language, correctCount, incorrectCount) VALUES ('%s', '%s', '%s', %d, %d, %d);";
//    protected static final String insertLearnGroupTableQueryFormat =
//            "INSERT INTO " + dbLearnGroupsTableName + " (created, title) VALUES ('%s', '%s');";


    //region dbLearnTicketsTableName
    private static final String insertLearnTicketsTableQueryFormat =
            "INSERT INTO " + dbLearnTicketsTableName +
                    " (id, created, idSideFirst, idSideSecond, learningSide, background) VALUES ('%s', '%s', '%s', '%s', %d, %d)";
    protected String getInsertLearnTicketsTableQuery(String id, Date date, String idSideFirst, String idSideSecond,
                                                     int learningSide, int background){
        return getFormatter().format(insertLearnTicketsTableQueryFormat,
                id, getDateFormatter().format(date), idSideFirst, idSideSecond,learningSide, background
        ).toString();
    }

    private static final String removeLearnTicketsTableQueryByIdFormat =
            "DELETE FROM " + dbLearnTicketsTableName + " WHERE id = '%s'";
    protected String getRemoveLearnTicketsTableQueryById(String id){
        return getFormatter().format(removeLearnTicketsTableQueryByIdFormat,id).toString();
    }

    private static final String updateLearnTicketsTableQueryFormat =
            "UPDATE " + dbLearnTicketsTableName +
                    " SET idSideFirst='%s', idSideSecond='%s', learningSide=%d, background=%d  WHERE id='%s';";
    protected String getUpdateLearnTicketsTableQuery(String id, String idSideFirst,String idSideSecond, int learningSide,
        int background){
        return getFormatter().format(updateLearnTicketsTableQueryFormat,
                idSideFirst, idSideSecond, learningSide, background, id)
                .toString();
    }

    //endregion

    //region dbSideTicketsTableName

    private static final String updateSideTicketTableQueryFormat =
            "UPDATE " + dbSideTicketsTableName +
                    " SET learningText='%s', language=%d, correctCount=%d, incorrectCount=%d WHERE id='%s';";
    protected String getUpdateSideTicketTableQuery(String id,String learningText, int language, int correctCount, int incorrectCount){
        return getFormatter().format(updateSideTicketTableQueryFormat,
                learningText, language, correctCount, incorrectCount, id
        ).toString();
    }
    private static final String deleteSideTicketTableQueryFormat =
            "DELETE FROM  " + dbSideTicketsTableName + " WHERE id='%s';";
    protected String getDeleteSideTicketTableQuery(String id){
        return getFormatter().format(deleteSideTicketTableQueryFormat, id).toString();
    }

    //endregion

    //region dictionary
    protected static String getSelectDictionariesTableQuery(String where){
        String select = "SELECT tb_dictionaries.id AS id,title,sys, Count(*) AS length FROM " //,COUNT(tb_ticket_dictionaries.id) AS length
                + dbDictionariesTableName
                + " LEFT JOIN " + dbTicketDictionariesTableName
                + " ON tb_ticket_dictionaries.idDictionary = tb_dictionaries.id";
        if(where != null){
            select += " WHERE " + where;
        }
        select += " GROUP BY tb_dictionaries.id";
        return select;
    }

    private static final String insertDictionariesTableQueryFormat = "INSERT INTO "
            + dbDictionariesTableName + " (created, title) VALUES ('%s', '%s');";
    protected String getInsertDictionariesTableQuery(String title, Date created){
        return getFormatter().format(insertDictionariesTableQueryFormat,
                getDateFormatter().format(created), title
        ).toString();
    }
    private static final String insertSysDictionariesTableQueryFormat = "INSERT INTO "
            + dbDictionariesTableName + " (created, title, sys) VALUES ('%s', '%s', 1);";
    protected String getInsertSysDictionariesTableQuery(String title, Date created){
        return getFormatter().format(insertSysDictionariesTableQueryFormat,
                getDateFormatter().format(created), title
        ).toString();
    }
    private static final String deleteDictionariesTableQueryFormat =
            "DELETE FROM " + dbDictionariesTableName + " WHERE id = %d";
    protected String getDeleteDictionariesTableQuery(int id){
        return getFormatter().format(deleteDictionariesTableQueryFormat, id).toString();
    }
    private static final String updateDictionariesTableQueryFormat =
            "UPDATE " + dbDictionariesTableName + " SET title='%s' WHERE id = %d";
    protected String getUpdateDictionariesTableQuery(int id, String title){
        return getFormatter().format(updateDictionariesTableQueryFormat, title, id).toString();
    }
    //endregion

    //region tb_ticket_dictionaries
    private static final String insertTicketDictionariesTableQueryFormat = "INSERT INTO "
            + dbTicketDictionariesTableName + " (idDictionary, idTicket) VALUES (%d, '%s');";
    protected String getInsertTicketDictionariesTableQuery(int idDictionary, String idTicket){
        return getFormatter().format(insertTicketDictionariesTableQueryFormat,
                idDictionary, idTicket
        ).toString();
    }
    private static final String updateTicketDictionariesTableQueryFormat = "UPDATE "
            + dbTicketDictionariesTableName + " SET idDictionary=%d WHERE idTicket='%s';";
    protected String getUpdateTicketDictionariesTableQuery(int idDictionary, String idTicket){
        return getFormatter().format(updateTicketDictionariesTableQueryFormat,
                idDictionary, idTicket
        ).toString();
    }
    private static final String deleteTicketDictionariesTableQueryFormat = "DELETE FROM "
            + dbTicketDictionariesTableName + " WHERE idTicket='%s';";
    protected String getDeleteTicketDictionariesTableQuery(String idTicket){
        return getFormatter().format(deleteTicketDictionariesTableQueryFormat, idTicket
        ).toString();
    }
    private static final String deleteTicketDictionariesTableByDicIdQueryFormat = "DELETE FROM "
            + dbTicketDictionariesTableName + " WHERE idDictionary=%d;";
    protected String getDeleteTicketDictionariesTableByDicIdQuery(int idDic){
        return getFormatter().format(deleteTicketDictionariesTableByDicIdQueryFormat, idDic
        ).toString();
    }

    //endregion

    //region learned dictionary

    private static final String insertTicketToLearnedTableQueryFormat = "INSERT INTO "
            + dbTicketsLearnedTableName + " (idDictionary, idTicket, added) VALUES (%d, '%s', '%s');";
    protected String getInsertTicketToLearnedTableQuery(int idDictionary, String idTicket, Date added){
        return getFormatter().format(insertTicketToLearnedTableQueryFormat,
                idDictionary, idTicket, getDateFormatter().format(added))
                .toString();
    }

    private static final String deleteLearnedTicketTableQueryFormat =
            "DELETE FROM " + dbTicketsLearnedTableName + " WHERE idTicket = '%s'";
    protected String getDeleteLearnedTicketTableQuery(String idTicket){
        return getFormatter().format(deleteLearnedTicketTableQueryFormat, idTicket).toString();
    }

    //endregion
}
