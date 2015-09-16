package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tickets.language.svp.languagetickets.AddTicketActivity;
import tickets.language.svp.languagetickets.AppSettingsActivity;
import tickets.language.svp.languagetickets.EditTicketActivity;
import tickets.language.svp.languagetickets.GameLearningActivity;
import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.RootActivity;
import tickets.language.svp.languagetickets.Storage;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.ui.controllers.*;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.AddNewTicket;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.AppSettings;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.EditTicket;
import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.GameLearning;

/**
 * Created by Pasha on 2/8/2015.
 */
public abstract class BaseActivity<TA extends BaseActivity,TC extends ActivityController<TA>> extends Activity {
    public Storage storage;
    protected TC controller;// should be static but because of has ref to  TA Activity it cannot :(
    //protected static Repository repository;

    protected void initAsRoot(RootActivity activity){
        Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler(activity));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            this.getIntent().putExtras(savedInstanceState);
        }
        storage = new Storage(this.getIntent());
        super.onCreate(null);
    }

    @Override
    protected void onPause() {

        // TODO close database

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

        // TODO open database
    }
    //remove this method
    public void goToActivity(ActivityOperationResult type){
        switch (type){
            case Root:
                break;
            case EditTicket:
                Intent edit = new Intent(this.getBaseContext(), AddTicketActivity.class);
                storage.putTo(edit);
                startActivityForResult(edit,EditTicket.toInt());
                break;
            case AddNewTicket:
                Intent add = new Intent(this.getBaseContext(), AddTicketActivity.class);
                storage.setSelectedTicket(TicketViewModel.createNew());
                storage.putTo(add);
                startActivityForResult(add, EditTicket.toInt());
                break;
            case GameLearning:
                Intent game = new Intent(this.getBaseContext(), GameLearningActivity.class);
                startActivityForResult(game, GameLearning.toInt());
                break;
            case AppSettings:
                Intent appsett = new Intent(this.getBaseContext(), AppSettingsActivity.class);
                startActivityForResult(appsett, AppSettings.toInt());
                break;
        }
    }

    protected static Drawable getDrawableSecondBackground(TicketColors color){
        return new ColorDrawable(getColorIntSecondBackground(color));
    }
    protected static int getColorIntSecondBackground(TicketColors color){
        int icolor;
        switch (color){
            case Blue:
                icolor = Color.parseColor("#2196F3");
                break;
            case Yellow:
                icolor = Color.parseColor("#FF9800");
                break;
            case Green:
                icolor = Color.parseColor("#4CAF50");
                break;
            case Red:
                icolor = Color.parseColor("#F44336");
                break;
            case Violet:
                icolor = Color.parseColor("#9C27B0");
                break;
            case White:
                icolor = Color.parseColor("#CCCCCC");
                break;
            default:
                icolor = Color.parseColor("#CCCCCC");
                break;
        }
        return icolor;
    }

    protected static int getDrawableBackgroundId(TicketColors color){
        switch (color){
            case Blue:
                return R.drawable.ticket_background_blue;
            case Yellow:
                return R.drawable.ticket_background_yellow;
            case Green:
                return R.drawable.ticket_background_green;
            case Red:
                return R.drawable.ticket_background_red;
            case Violet:
                return R.drawable.ticket_background_violet;
            case White:
                return R.drawable.ticket_background_white;
            case Pink:
                return R.drawable.ticket_background_pink;
            case DeepPurple:
                return R.drawable.ticket_background_deeppurple;
            case Indigo:
                return R.drawable.ticket_background_indigo;
            case Cyan:
                return R.drawable.ticket_background_cyan;
            case Lime:
                return R.drawable.ticket_background_lime;
            case DeepOrange:
                return R.drawable.ticket_background_deeporange;
            case Brown:
                return R.drawable.ticket_background_brown;
            case Grey:
                return R.drawable.ticket_background_gray;
        }
        return R.drawable.ticket_background_white;
    }
    /**
     * calculate height by Adapter items amd set it to ListView height params
     */
    public static void setHeightListView(ListView listView, Adapter listAdapter){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void restartActivity(Storage storage){
        finish();
        Intent intent = getIntent();
        storage.putTo(intent);
        startActivity(intent);
    }
    protected void restartActivity(){
        finish();
        startActivity(getIntent());
    }

    public static class DisplayDictionaryDrawerAdapter extends BaseAdapter {
        private final ArrayList<DictionaryViewModel> dictionaries;
        private final Activity activity;
        private final boolean selectSystem;

        private final static Comparator<DictionaryViewModel> dictionaryComparator = new Comparator<DictionaryViewModel>() {
            public int compare(DictionaryViewModel obj1,DictionaryViewModel obj2) {
                if(obj1.dto.sys == 1 && obj2.dto.sys!=1){
                    return -1;
                }
                if(obj2.dto.sys == 1 && obj1.dto.sys!=1){
                    return 1;
                }

                return ((Integer)obj2.dto.length).compareTo(obj1.dto.length);
            }
        };

        public DisplayDictionaryDrawerAdapter(Activity activity, ArrayList<DictionaryViewModel> dictionaries, boolean selectSystem) {
            this.activity = activity;
            Collections.sort(dictionaries,dictionaryComparator);
            this.dictionaries = dictionaries;
            this.selectSystem = selectSystem;
        }

//        public DictionaryViewModel[] getTickets() {
//            return dictionaries;
//        }

        @Override
        public int getCount() {
            return dictionaries.size();
        }

        @Override
        public Object getItem(int position) {
            return dictionaries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View itemTemplate, ViewGroup parent) {
            if (itemTemplate == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                itemTemplate = inflater.inflate(R.layout.drawer_list_item, parent, false);
            }

            DictionaryViewModel dic = dictionaries.get(position);

            TextView textView = ViewExtensions.findViewById(itemTemplate, R.id.dic_title);
            textView.setText(dic.getTitle());
            //
            TextView tvl = ViewExtensions.findViewById(itemTemplate, R.id.dic_length);
            tvl.setText("("+Integer.toString(dic.getLength())+")");

            if(dic.isSys() && selectSystem){
//                itemTemplate.setBackgroundResource(getDrawableBackgroundId(TicketColors.Blue));
                tvl.setTextColor(getColorIntSecondBackground(TicketColors.Blue));
                textView.setTextColor(getColorIntSecondBackground(TicketColors.Blue));
            }
            return itemTemplate;
        }
    }

    //storage


}