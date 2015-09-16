package tickets.language.svp.languagetickets.ui.viewModel;

import android.database.Cursor;

import java.io.Serializable;
import java.util.Date;

import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.domain.model.LanguageDto;
import tickets.language.svp.languagetickets.domain.model.LearnTicketDto;
import tickets.language.svp.languagetickets.domain.model.SideTicketDto;
import tickets.language.svp.languagetickets.queries.AQueryObject;
import tickets.language.svp.languagetickets.ui.TicketColors;

import static tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel.SideTypes.Back;
import static tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel.SideTypes.Front;

/**
 * Created by Pasha on 1/4/2015.
 */
public class TicketViewModel implements Serializable {
    public enum SideTypes{
        Front,
        Back
    }

    private SideTypes sideType;
    private boolean isChanged;
    private boolean isNew;
    private boolean isRemoved;
    private boolean isLearned;

    private final LearnTicketDto dto;

    public static TicketViewModel createNew(){
        LearnTicketDto t = new LearnTicketDto();
        t.generateId();
        t.first = new SideTicketDto();
        t.first.generateId();
        t.second = new SideTicketDto();
        t.second.generateId();
        t.learningSide = 0;
        t.dictionary = new DictionaryDto();
        t.dictionary.id = -1;
        TicketViewModel tvm = new TicketViewModel(t);
        tvm.isNew = true;
        return tvm;
    }

    public TicketViewModel(LearnTicketDto ticket){
        this.dto = ticket;
        sideType = ticket.learningSide == 0 ? SideTypes.Front : SideTypes.Back;
        isChanged = false;
    }

    public boolean isChanged() {
        return isChanged;
    }
    public boolean isNew() { return isNew;}
    public void markAsRemoved() {  isRemoved = true; }
    public boolean isRemoved() {  return isRemoved; }

    public String[] getDisplayLearningText() {
        return StringHelper.splitByLineSeparator(getCurrent(sideType).learningText);
    }
    public String[] getLearningText(SideTypes type) {
        return StringHelper.splitByLineSeparator(getCurrent(type).learningText);
    }
    public String getText(SideTypes type) {
        return getCurrent(type).learningText;
    }

    public void turnOver(){
        switch (sideType){
            case Back:
                sideType = Front;
                dto.learningSide = 0;
                break;
            case Front:
                sideType = Back;
                dto.learningSide = 1;
                break;
        }
        isChanged = true;
    }
    public SideTypes getCurrentSideType(){
        return sideType;
    }
    public SideTypes getInvertOfCurrentSideType(){
        switch (sideType){
            case Back: return Front;
            case Front: return Back;
        }
        return Front;
    }

    private SideTicketDto getCurrent(SideTypes type){
        switch (type){
            case Back:
                return dto.second;
            case Front:
                return dto.first;
        }
        throw new InternalError("sideType is not correct");
    }
    public void correctTranslated(SideTypes type){
        this.getCurrent(type).correctCount++;
    }
    public void incorrectTranslated(SideTypes type) {
        this.getCurrent(type).incorrectCount++;
    }
    public int getCorrectTranslated(SideTypes type){
        return this.getCurrent(type).correctCount;
    }
    public int getIncorrectTranslated(SideTypes type) {
        return this.getCurrent(type).incorrectCount;
    }
    public String getId() {
        return dto.id;
    }

    public void setDictionary(DictionaryViewModel dictionary){
        dto.dictionary = dictionary.dto;
        isChanged = true;
    }
    public DictionaryViewModel getDictionary(){
        return new DictionaryViewModel(dto.dictionary);
    }

    public void setLanguage(SideTypes type, LanguageViewModel lang) {
        if(lang.dto.id == getCurrent(type).language) { return;}
        getCurrent(type).language = lang.dto.id;
        getCurrent(type).lang = lang.dto;
        isChanged = true;
    }
    public LanguageViewModel getLanguage(SideTypes type) {
        LanguageDto lang = getCurrent(type).lang;
        return new LanguageViewModel(lang);
    }

    public void setLearningText(SideTypes type, String[] text) {
        getCurrent(type).learningText = StringHelper.joinByLineSeparator(text);
        isChanged = true;
    }

    public void setBackground(TicketColors background){
        dto.background = background.toInt();
        isChanged = true;
    }
    public TicketColors getBackground(){
        return TicketColors.fromInt(dto.background);
    }

    @Override
    public String toString(){

        return "[ id: "+ dto.id+ " dictionary: "+ dto.dictionary + "]";
    }

    public static class AddTicketQuery extends AQueryObject<TicketViewModel> {
        private final LearnTicketDto ticket;
        private final TicketViewModel tvm;

        public AddTicketQuery(DbActivateSettings sett,TicketViewModel vm){
            super(sett);
            tvm = vm;
            ticket = vm.dto;
        }

        @Override
        public String[] getQuery() {
            tvm.isNew = false;
            return new String[]{
                    insertSide(ticket.first),
                    insertSide(ticket.second),
                    getInsertLearnTicketsTableQuery(
                            ticket.id,
                            ticket.created,
                            ticket.first.id,
                            ticket.second.id,
                            ticket.learningSide,
                            ticket.background
                    ),
                    //is not -1
                    getInsertTicketDictionariesTableQuery(ticket.dictionary.id,ticket.id),
            };
        }

        private String insertSide(SideTicketDto dto){
            return getFormatter().format(insertSideTicketTableQueryFormat,
                    dto.id,
                    getDateFormatter().format(dto.created),
                    dto.learningText,
                    dto.language,
                    dto.correctCount,
                    dto.incorrectCount).toString();
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            return new TicketViewModel[0];
        }
    }
    public static class RemoveQuery extends AQueryObject<TicketViewModel>{
        private final LearnTicketDto ticket;

        public RemoveQuery(DbActivateSettings sett,TicketViewModel vm) {
            super(sett);
            ticket = vm.dto;
        }

        @Override
        public String[] getQuery() {
            return new String[]{
                    getDeleteSideTicketTableQuery(ticket.first.id),
                    getDeleteSideTicketTableQuery(ticket.second.id),
                    getDeleteTicketDictionariesTableQuery(ticket.id),
                    getRemoveLearnTicketsTableQueryById(ticket.id)
            };
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            throw new InternalError("Parser for Remove class is not implemented.");
        }
    }
    public static class UpdateQuery extends AQueryObject<TicketViewModel>{
        private final LearnTicketDto ticket;

        public UpdateQuery(DbActivateSettings sett,TicketViewModel vm) {
            super(sett);
            ticket = vm.dto;
        }

        @Override
        public String[] getQuery() {
            SideTicketDto first = ticket.first;
            SideTicketDto second = ticket.second;

            String dicQuery = getUpdateTicketDictionariesTableQuery(ticket.dictionary.id, ticket.id);
            if(ticket.noDictionary){
                ticket.noDictionary = false;
                dicQuery = getInsertTicketDictionariesTableQuery(ticket.dictionary.id, ticket.id);
            }

            return new String[]{
                getUpdateSideTicketTableQuery(first.id,first.learningText,first.language,first.correctCount,first.incorrectCount),
                getUpdateSideTicketTableQuery(second.id,second.learningText,second.language,second.correctCount,second.incorrectCount),
                getUpdateLearnTicketsTableQuery(ticket.id,
                    first.id, second.id, ticket.learningSide, ticket.background),
                //is not -1
                dicQuery,
            };
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            throw new InternalError("Parser for Remove class is not implemented.");
        }
    }
    public static class AddAsLearned extends AQueryObject<TicketViewModel>{
        private final LearnTicketDto dto;

        public AddAsLearned(DbActivateSettings sett, TicketViewModel vm){
            super(sett);
            dto = vm.dto;
        }
        @Override
        public String[] getQuery() {
            return new String[]{
                getInsertTicketToLearnedTableQuery(dto.dictionary.id,dto.id,new Date()),
                getDeleteTicketDictionariesTableQuery(dto.id)
            };
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            return new TicketViewModel[0];
        }
    }
    public static class RevertToLearning extends AQueryObject<TicketViewModel>{
        private final LearnTicketDto ticket;
        public RevertToLearning(DbActivateSettings settings,TicketViewModel vm) {
            super(settings);
            ticket = vm.dto;
        }

        @Override
        public String[] getQuery() {
            return new String[]{
                getInsertTicketDictionariesTableQuery(ticket.dictionary.id,ticket.id),
                getDeleteLearnedTicketTableQuery(ticket.id),
            };
        }

        @Override
        public TicketViewModel[] parse(Cursor cursor) {
            return new TicketViewModel[0];
        }
    }

}
