package tickets.language.svp.languagetickets.queries;

import android.database.Cursor;

import java.util.ArrayList;

import tickets.language.svp.languagetickets.domain.Consts;
import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.domain.model.LanguageDto;
import tickets.language.svp.languagetickets.domain.model.LearnTicketDto;
import tickets.language.svp.languagetickets.domain.model.SideTicketDto;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 2/1/2015.
 */
public class TicketQueries {
    public static IQueryObject<TicketViewModel> addNew(TicketViewModel ticket){
        return new TicketViewModel.AddTicketQuery(ticket);
    }
    public static IQueryObject<TicketViewModel> getByDictionary(DictionaryViewModel dictionary){
        return new GetByDictionary(dictionary);
    }

    public static IQueryObject<TicketViewModel> remove(TicketViewModel ticket) {
        return new TicketViewModel.RemoveQuery(ticket);
    }

    public static IQueryObject<TicketViewModel> update(TicketViewModel ticket) {
        return new TicketViewModel.UpdateQuery(ticket);
    }

    public static IQueryObject addAsLearned(TicketViewModel ticket) {
        return new TicketViewModel.AddAsLearned(ticket);
    }

    public static class GetByDictionary extends AQueryObject<TicketViewModel> {
        public GetByDictionary(DictionaryViewModel dictionary) {
            boolean isLearned = dictionary == Consts.Dictionary.Learned;
            boolean isAll = dictionary == Consts.Dictionary.All;
            query.append(
                  "SELECT L1.id AS 'L1id', L1.learningSide AS 'L1learningSide', L1.created AS 'L1created', L1.background AS 'L1background'"
                      + ",S1.id AS 'S1id', S1.learningText AS 'S1learningText', S1.language AS 'S1language', S1.correctCount AS 'S1correctCount', S1.incorrectCount AS 'S1incorrectCount'"
                      + ",LG1.id AS 'LG1id', LG1.name AS 'LG1name'"
                      + ",S2.id AS 'S2id', S2.learningText AS 'S2learningText', S2.language AS 'S2language', S2.correctCount AS 'S2correctCount', S2.incorrectCount AS 'S2incorrectCount'"
                      + ",LG2.id AS 'LG2id', LG2.name AS 'LG2name'"
                      + ",D1.id AS 'D1id', D1.title AS 'D1title', D1.created AS 'D1created' "
            );
            query.append(
                    "FROM tb_learntickets L1 "
                    + "JOIN tb_sidetickets S1 ON S1.id = L1.idSideFirst "
                    + "JOIN tb_languages LG1 ON S1.language = LG1.id "
                    + "JOIN tb_sidetickets S2 ON S2.id = L1.idSideSecond "
                    + "JOIN tb_languages LG2 ON S2.language = LG2.id "

            );
            if(isLearned){
                query.append(
                    "JOIN " + dbTicketsLearnedTableName + " TD1 ON TD1.idTicket = L1.id "
                    + "LEFT JOIN tb_dictionaries D1 ON D1.id = TD1.idDictionary "
                );
            } else{
               query.append(
                    "LEFT JOIN tb_ticket_dictionaries TD1 ON TD1.idTicket = L1.id "
                    + "LEFT JOIN tb_dictionaries D1 ON D1.id = TD1.idDictionary "
               );
            }
            if(isAll) {
                query.append("GROUP BY L1.id");
            }


            if(!isAll && !isLearned){
                query.append("WHERE TD1.idDictionary=" + dictionary.dto.id);
            }
        }
        @Override
        public String[] getQuery() {
            return new String[]{ query.toString() };
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            ArrayList<TicketViewModel> list = new ArrayList<TicketViewModel>();
            if (cursor.getCount() > 0){
                try {
                    cursor.moveToFirst();
                    do {
                        LearnTicketDto dto = new LearnTicketDto();
                        dto.id = cursor.getString(cursor.getColumnIndex("L1id"));
                        dto.learningSide = cursor.getInt(cursor.getColumnIndex("L1learningSide"));
                        dto.background = cursor.getInt(cursor.getColumnIndex("L1background"));
                        dto.first = parseSide(cursor, "1");
                        dto.second = parseSide(cursor, "2");
                        DictionaryDto ddto = new DictionaryDto();
                        ddto.id = cursor.getInt(cursor.getColumnIndex("D1id"));
                        if(ddto.id == 0){
                            dto.noDictionary = true;
                        }
                        ddto.title = cursor.getString(cursor.getColumnIndex("D1title"));
                        dto.dictionary = ddto;
                        list.add(new TicketViewModel(dto));
                    } while (cursor.moveToNext());
                }finally {
                    cursor.close();
                }
            }
            return list.toArray(new TicketViewModel[list.size()]);
        }

        private static SideTicketDto parseSide(Cursor cursor, String tableKey){
            SideTicketDto side = new SideTicketDto();
            side.id = cursor.getString(cursor.getColumnIndex("S" + tableKey + "id"));
            side.learningText = cursor.getString(cursor.getColumnIndex("S" +tableKey + "learningText"));
            side.language = cursor.getInt(cursor.getColumnIndex("S" +tableKey + "language"));
            side.correctCount = cursor.getInt(cursor.getColumnIndex("S" +tableKey + "correctCount"));
            side.incorrectCount = cursor.getInt(cursor.getColumnIndex("S" +tableKey + "incorrectCount"));
            side.lang = new LanguageDto();
            side.lang.id = cursor.getInt(cursor.getColumnIndex("LG" + tableKey + "id"));
            side.lang.name = cursor.getString(cursor.getColumnIndex("LG" + tableKey + "name"));
            return side;
        }

        /*
        String id = cursor.getString(cursor.getColumnIndex("idTicket"));
                        if(dto == null || !dto.id.equals(id)) {
                            dto = new LearnTicket();
                            dto.id = id;
                            dto.learningSide = cursor.getInt(cursor.getColumnIndex("learningSide"));
                            dto.background = cursor.getInt(cursor.getColumnIndex("background"));

                            DictionaryDto ddto = new DictionaryDto();
                            DictionaryViewModel dvm = new DictionaryViewModel(ddto);

                            list.add(new TicketViewModel(dto));
                        }
                        if(dto.first == null){
                            dto.first = parseSide(cursor);
                        }else if(dto.second == null){
                            dto.second = parseSide(cursor);
                        }
        * */


    }
}
