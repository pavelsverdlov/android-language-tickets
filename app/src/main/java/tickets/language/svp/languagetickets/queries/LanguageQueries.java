package tickets.language.svp.languagetickets.queries;

import android.database.Cursor;

import java.util.ArrayList;

import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.domain.model.LanguageDto;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;

/**
 * Created by Pasha on 2/8/2015.
 */
public class LanguageQueries{
    public static IQueryObject<LanguageViewModel> GetAll(DbActivateSettings set){
        return new GetAll(set);
    }

    public static final class GetAll extends AQueryObject<LanguageViewModel> {
        public GetAll(DbActivateSettings set){
            super(set);
            query.append("SELECT id,name FROM " + dbLanguagesTableName);
        }
        @Override
        public String[] getQuery() {
            return new String[] {query.toString()};
        }

        @Override
        public LanguageViewModel[] parse(Cursor cursor) {
            ArrayList<LanguageViewModel> list = new ArrayList<LanguageViewModel>();
            if (cursor.getCount() > 0){
                try {
                    cursor.moveToFirst();
                    do {
                        LanguageDto dto = new LanguageDto();
                        dto.id = cursor.getInt(cursor.getColumnIndex("id"));
                        dto.name = cursor.getString(cursor.getColumnIndex("name"));
                        list.add(new LanguageViewModel(dto));
                    } while (cursor.moveToNext());
                }finally {
                    cursor.close();
                }
            }
            return list.toArray(new LanguageViewModel[list.size()]);
        }
    }
}
