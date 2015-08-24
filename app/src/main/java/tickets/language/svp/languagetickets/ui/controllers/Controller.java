package tickets.language.svp.languagetickets.ui.controllers;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;

import tickets.language.svp.languagetickets.RootActivity;
import tickets.language.svp.languagetickets.domain.Consts;
import tickets.language.svp.languagetickets.domain.IQueryObject;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.domain.db.DatabaseStorage;
import tickets.language.svp.languagetickets.domain.db.DbActivateSettings;
import tickets.language.svp.languagetickets.queries.DictionaryQueries;
import tickets.language.svp.languagetickets.queries.LanguageQueries;
import tickets.language.svp.languagetickets.queries.TicketQueries;
import tickets.language.svp.languagetickets.ui.BaseActivity;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 7/5/2015.
 */
public class Controller{
    private static DictionaryViewModel selectedDictionary;

    protected static Repository repository;

    public Controller(){
        //this.repository = repository;
//        if(repository == null){
//            File sd = Environment.getExternalStorageDirectory();
//            File path = new File(sd + "/Download");
//            DbActivateSettings as = new DbActivateSettings(DatabaseStorage.LocalStore, path);
//
//            repository = new Repository(as);
//        }
//        if(selectedDictionary == null){
//            selectedDictionary = Consts.Dictionary.All;
//        }
    }

    protected void init(DbActivateSettings activateSettings){
        if(repository == null){
            repository = new Repository(activateSettings);
        }
        if(selectedDictionary == null){
            selectedDictionary = Consts.Dictionary.All;
        }
    }

    //region dictionary

    public void addNewDictionary(String newtext) {
        repository.add(DictionaryQueries.addNew(newtext));
    }
    public ArrayList<DictionaryViewModel> GetDictionaries() {
        ArrayList<DictionaryViewModel> all = new ArrayList<DictionaryViewModel>();
        DictionaryViewModel[] fromDB = repository.get(DictionaryQueries.getAll());
        for (DictionaryViewModel dir: fromDB){
            all.add(dir);
        }
        all.add(Consts.Dictionary.All);
        all.add(Consts.Dictionary.Learned);
        return all;//.toArray(new DictionaryViewModel[all.size()]);
    }
    public void removeDictionary(DictionaryViewModel selected) {
        //TODO: ask about tickets in this dictionary and what do we do remove/move
        repository.remove(DictionaryQueries.remove(selected));
    }
    public DictionaryViewModel getSelectedDictionary(){
        return selectedDictionary;
    }
    public void setCurrentDictionary(DictionaryViewModel dictionary) { selectedDictionary = dictionary; }

    //endregion

    public TicketViewModel[] getTickets(DictionaryViewModel dictionary){
        TicketViewModel[] tt = repository.get(TicketQueries.getByDictionary(dictionary));
        if(tt == null){
            tt = new TicketViewModel[0];
        }
        dictionary.dto.length = tt.length;
        return tt;
    }
    public void addNewTicket(TicketViewModel ticket) {
        if(ticket.isNew()) {
            repository.add(TicketQueries.addNew(ticket));
        }else{
            updateTicket(ticket);
        }
    }
    public void removeTicket(TicketViewModel ticket) {
        repository.remove(TicketQueries.remove(ticket));
        ticket.markAsRemoved();
    }
    public void updateTicket(TicketViewModel ticket){
        repository.update(TicketQueries.update(ticket));
    }

    public LanguageViewModel[] getLanguages()    {
        return repository.get(LanguageQueries.GetAll());
    }
/*
    private void tryInitSystemDictionaries(){
            IQueryObject<DictionaryViewModel> query = DictionaryQueries.getSystemDictionaries();
            DictionaryViewModel[] dics = repository.get(query);
            for (int i=0; i < dics.length;++i){
                String title = dics[i].dto.title;
                if(title == null){
                    continue;
                }
                if(title.equals(Consts.Dictionary.All.getTitle())){
                    repository.remove(DictionaryQueries.remove(dics[i]));
                }else if(title.equals(Consts.Dictionary.Learned.getTitle())){
                    repository.remove(DictionaryQueries.remove(dics[i]));
                }
            }
//            boolean retry = false;
//            if(dictionary_All == null) {
//                repository.add(DictionaryQueries.addSystem(dictionaryName_All));
//                retry = true;
//            }
//            if(dictionary_Learned == null){
//                repository.add(DictionaryQueries.addSystem(dictionaryName_Learned));
//                retry = true;
//            }
//            if(retry){
//                tryInitSystemDictionaries();
//            }

    }
    */

}
