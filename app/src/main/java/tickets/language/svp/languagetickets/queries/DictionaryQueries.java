package tickets.language.svp.languagetickets.queries;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

import tickets.language.svp.languagetickets.domain.Consts;
import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;

public class DictionaryQueries {
    public static IQueryObject<DictionaryViewModel> getAll(DbActivateSettings sett){
        return new GetAllDictionaries(sett);
    }

//    public static IQueryObject<DictionaryViewModel> getSystemDictionaries(){
//        return new GetSystem();
//    }
//    public static IQueryObject addSystem(String newtext) {
//        return new AddSystem(newtext);
//    }

    public static IQueryObject addNew(DbActivateSettings sett,String newtext) {
        return new Add(sett, newtext);
    }

    public static IQueryObject remove(DbActivateSettings sett, DictionaryViewModel dic) {
        return new Remove(sett,dic);
    }

    public static IQueryObject<DictionaryViewModel> getLearned(DbActivateSettings sett) {
        return new GetLearned(sett);
    }

    public static final class Remove extends AQueryObject<DictionaryViewModel> {
        private final int id;
        public Remove(DbActivateSettings sett, DictionaryViewModel dic){
            super(sett);
            id = dic.dto.id;
            query.append(getDeleteDictionariesTableQuery(id));
        }

        @Override
        public String[] getQuery() {
            return new String[]{
                query.toString(),
                //remove all tickets from this dictionary
                getDeleteTicketDictionariesTableByDicIdQuery(id)
            };
        }

        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            throw new InternalError();
        }
    }

    public static final class Add extends AQueryObject<DictionaryViewModel> {
        public Add(DbActivateSettings sett, String title){
            super(sett);
            query.append(getInsertDictionariesTableQuery(title, new Date()));
        }

        @Override
        public String[] getQuery() {
            return new String[]{ query.toString() };
        }

        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            throw new InternalError();
        }
    }

    public static final class GetAllDictionaries extends AQueryObject<DictionaryViewModel> {
        public GetAllDictionaries(DbActivateSettings sett){
            super(sett);
            query.append(getSelectDictionariesTableQuery(null));
        }
        @Override
        public String[] getQuery() {
            return new String[] {query.toString()};
        }
        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            ArrayList<DictionaryViewModel> list = new ArrayList<DictionaryViewModel>();
            if (cursor.getCount() > 0){
                try {
                    cursor.moveToFirst();
                    do {
                        DictionaryDto dto = new DictionaryDto();
                        dto.id = cursor.getInt(cursor.getColumnIndex("id"));
                        dto.title = cursor.getString(cursor.getColumnIndex("title"));
                        dto.sys = cursor.getInt(cursor.getColumnIndex("sys"));
                        dto.length = cursor.getInt(cursor.getColumnIndex("length"));
                        list.add(new DictionaryViewModel(dto));
                    } while (cursor.moveToNext());
                }finally {
                    cursor.close();
                }
            }
            return list.toArray(new DictionaryViewModel[list.size()]);
        }
    }

    public static final class GetLearned extends AQueryObject<DictionaryViewModel> {
        public GetLearned(DbActivateSettings sett) {
            super(sett);
        }


        @Override
        public String[] getQuery() {
            return new String[]{
                getCountOfLearnedTickets()
            };
        }

        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            DictionaryViewModel dic = Consts.Dictionary.Learned;
            if (cursor.getCount() > 0){
                try {
                    cursor.moveToFirst();
                    do {
                        dic.dto.length = cursor.getInt(cursor.getColumnIndex("count"));
                    } while (cursor.moveToNext());
                }finally {
                    cursor.close();
                }
            }
            return new DictionaryViewModel[] { dic };
        }
    }

    public static final class GetSystem extends AQueryObject<DictionaryViewModel> {
        public GetSystem(DbActivateSettings sett) {
            super(sett);
            query.append(getSelectDictionariesTableQuery("sys=1"));
        }
        @Override
        public String[] getQuery() {
            return new String[] {query.toString()};
        }
        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            ArrayList<DictionaryViewModel> list = new ArrayList<DictionaryViewModel>();
            if (cursor.getCount() > 0){
                try {
                    cursor.moveToFirst();
                    do {
                        DictionaryDto dto = new DictionaryDto();
                        dto.id = cursor.getInt(cursor.getColumnIndex("id"));
                        dto.title = cursor.getString(cursor.getColumnIndex("title"));
                        dto.sys = cursor.getInt(cursor.getColumnIndex("sys"));
                        dto.length = cursor.getInt(cursor.getColumnIndex("length"));
                        list.add(new DictionaryViewModel(dto));
                    } while (cursor.moveToNext());
                }finally {
                    cursor.close();
                }
            }
            return list.toArray(new DictionaryViewModel[list.size()]);
        }
    }

    public static final class AddSystem extends AQueryObject<DictionaryViewModel> {
        public AddSystem(DbActivateSettings sett,String title){
            super(sett);
            query.append(getInsertSysDictionariesTableQuery(title, new Date()));
        }

        @Override
        public String[] getQuery() {
            return new String[]{ query.toString() };
        }

        @Override
        public DictionaryViewModel[] parse(Cursor cursor) {
            throw new InternalError();
        }
    }
}
