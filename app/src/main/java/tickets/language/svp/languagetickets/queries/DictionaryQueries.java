package tickets.language.svp.languagetickets.queries;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;

public class DictionaryQueries {
    public static IQueryObject<DictionaryViewModel> getAll(){
        return new GetAllDictionaries();
    }

//    public static IQueryObject<DictionaryViewModel> getSystemDictionaries(){
//        return new GetSystem();
//    }
//    public static IQueryObject addSystem(String newtext) {
//        return new AddSystem(newtext);
//    }

    public static IQueryObject addNew(String newtext) {
        return new Add(newtext);
    }

    public static IQueryObject remove(DictionaryViewModel dic) {
        return new Remove(dic);
    }

    public static final class Remove extends AQueryObject<DictionaryViewModel> {
        public Remove(DictionaryViewModel dic){
            query.append(getDeleteDictionariesTableQuery(dic.dto.id));
            //TODO: remove all tickets with this dictionary
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

    public static final class Add extends AQueryObject<DictionaryViewModel> {
        public Add(String title){
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
        public GetAllDictionaries(){
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

    public static final class GetSystem extends AQueryObject<DictionaryViewModel> {
        public GetSystem() {
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
        public AddSystem(String title){
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
