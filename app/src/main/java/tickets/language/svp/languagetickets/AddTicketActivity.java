package tickets.language.svp.languagetickets;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import tickets.language.svp.languagetickets.domain.CollectionHelper;
import tickets.language.svp.languagetickets.domain.Consts;
import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.ui.BaseActivity;
import tickets.language.svp.languagetickets.ui.LanguageNavigationAdapter;
import tickets.language.svp.languagetickets.ui.TicketColors;
import tickets.language.svp.languagetickets.ui.ViewExtensions;
import tickets.language.svp.languagetickets.ui.adapters.WordsEditAdapter;
import tickets.language.svp.languagetickets.ui.controllers.AddActivityController;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;
//http://stackoverflow.com/questions/8312344/how-to-add-a-dropdown-item-on-the-action-bar
public class AddTicketActivity extends BaseActivity<AddTicketActivity,AddActivityController> {
    private PlaceholderFragment container;
    private TicketViewModel editing;
    private ActionBarController actionBar;
    private LanguageViewModel[] languages;
    private ArrayList<DictionaryViewModel> dictionaries;
    private SparseIntArray languageMap;//Map<Integer,Integer>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        controller = new AddActivityController(this);
        super.onCreate(savedInstanceState);

        editing = storage.getSelectedTicket();
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        languages = controller.getLanguages();
        languageMap = new SparseIntArray();// new HashMap<Integer, Integer>();
        for (int i =0; i < languages.length;++i){
            languageMap.put(languages[i].dto.id,i);
        }

        actionBar = new ActionBarController(this,getActionBar());
        dictionaries = controller.GetDictionaries();

        //remove "Learned"/"All" dic
        this.dictionaries.remove(Consts.Dictionary.Learned);
        this.dictionaries.remove(Consts.Dictionary.All);

        if(editing.isNew()) {
            editing.setLanguage(TicketViewModel.SideTypes.Back, languages[0]);
            editing.setLanguage(TicketViewModel.SideTypes.Front, languages[1]);
            editing.setDictionary(dictionaries.get(0));
        }
        if (savedInstanceState == null) {
            updatePlaceholder();
        }
    }


    private void updatePlaceholder(){
       // updateEditingTextsData();

        setContentView(R.layout.activity_add_ticket);
        container = new PlaceholderFragment();
        container.setEditingTicket(editing, languages, dictionaries);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, container)
                .commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void updateMenuLangs(){
        int frontLang = languageMap.get(editing.getLanguage(editing.getCurrentSideType()).dto.id);
        int backLang = languageMap.get(editing.getLanguage(editing.getInvertOfCurrentSideType()).dto.id);
        actionBar.updateLanguages(languages,frontLang, backLang);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_ticket, menu);
        actionBar.init(menu);

        updateMenuLangs();

        actionBar.setFrontDropdownSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editing.setLanguage(editing.getCurrentSideType(), actionBar.getFrontLangSelected());
                updateEditingTextsData();
                updatePlaceholder();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        actionBar.setBackDropdownSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editing.setLanguage(editing.getInvertOfCurrentSideType(), actionBar.getBackLangSelected());
                updateEditingTextsData();
                updatePlaceholder();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        actionBar.setBackground(BaseActivity.getDrawableSecondBackground(editing.getBackground()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_ticket_dictionaries_menu_markAsLearned:
                controller.markAsLearned(editing);
                this.setResult(RESULT_OK);
                super.onBackPressed();
                break;
            case R.id.add_ticket_dictionaries_menu_delete:
                controller.removeTicket(editing);
                onBackPressed();
                break;
            case R.id.add_ticket_dictionaries_menu_selectcolor:
                SelectColorPopup selectColorsPopup = new SelectColorPopup(container.getView());
                selectColorsPopup.show();
                break;
            case R.id.add_ticket_dictionaries_menu_turnover:
                updateEditingTextsData();
                editing.turnOver();
                updateMenuLangs();
                updatePlaceholder();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(!editing.isRemoved()) {
            boolean f = trySetNewLearnText(container.getFirstText(), editing.getCurrentSideType());
            boolean b = trySetNewLearnText(container.getSecondText(), editing.getInvertOfCurrentSideType());
            if (editing.isChanged()) {// && (f || b)
                this.setResult(RESULT_OK, storage.getNewIntent());
            }
        }else{
            this.setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
    private void updateEditingTextsData(){
        if(container == null ) {
            return;
        }
        trySetNewLearnText(container.getFirstText(), editing.getCurrentSideType());
        trySetNewLearnText(container.getSecondText(), editing.getInvertOfCurrentSideType());
    }
    private boolean trySetNewLearnText(String[] newtxt, TicketViewModel.SideTypes side){
        String[] oldtxt = editing.getLearningText(side);
        if(!Arrays.equals(newtxt, oldtxt)){
            editing.setLearningText(side, newtxt);
            return true;
        }
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private class ListElement{
            public final ArrayList<String> array;
            public final WordsEditAdapter adapter;
            public final ListView view;
            public ListElement(ListView view, ArrayList<String> array, WordsEditAdapter adapter){
                this.view = view;
                this.array = array;
                this.adapter = adapter;
            }
            public void updateHeight(){
                setHeightListView(view ,adapter);
            }
        }

        private TicketViewModel editing;
        private LanguageViewModel[] languages;
        private ArrayList<DictionaryViewModel> dictionaries;
        private ArrayList<String> firstText;
        private ArrayList<String> secondText;
        private TicketViewModel.SideTypes first;
        private TicketViewModel.SideTypes second;

        public void setEditingTicket(TicketViewModel editing,
                                     LanguageViewModel[] languages,
                                     ArrayList<DictionaryViewModel> dictionaries) {
            this.dictionaries = dictionaries;
            this.editing = editing;
            this.languages = languages;

            first = editing.getCurrentSideType();
            second = editing.getInvertOfCurrentSideType();

            firstText = new ArrayList<String>();
            secondText = new ArrayList<String>();
            CollectionHelper.AddRange(firstText,editing.getLearningText(first));
            CollectionHelper.AddRange(secondText,editing.getLearningText(second));
        }


        public String[] getFirstText(){
            return firstText.toArray(new String[firstText.size()]);
        }
        public String[] getSecondText(){
            return secondText.toArray(new String[secondText.size()]);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_add_ticket, container, false);
            //TODO: refactor
            WordsEditAdapter firstAdapter = new WordsEditAdapter(inflater, firstText);
            ListView firstList = ViewExtensions.findViewById(view, R.id.edit_learning_listOfFirst);
            firstList.setAdapter(firstAdapter);
            final ListElement firstElement = new ListElement(firstList ,firstText, firstAdapter);
            firstAdapter.setOnClickListener(new WordsEditAdapter.OnWordEditClickListener() {
                @Override
                public void onClick(int position) {
                    firstElement.array.remove(position);
                    firstElement.updateHeight();
                }
            });
            setHeightListView(firstList, firstAdapter);

            WordsEditAdapter secondAdapter = new WordsEditAdapter(inflater, secondText);
            ListView secondList = ViewExtensions.findViewById(view, R.id.edit_learning_listOfSecond);
            secondList.setAdapter(secondAdapter);
            final ListElement secondElement = new ListElement(secondList ,secondText, secondAdapter);
            secondAdapter.setOnClickListener(new WordsEditAdapter.OnWordEditClickListener() {
                @Override
                public void onClick(int position) {
                    secondElement.array.remove(position);
                    secondElement.updateHeight();
                }
            });
            setHeightListView(secondList, secondAdapter);

            ViewExtensions.<EditText>findViewById(view,R.id.edit_learning_text_first)
                    .addTextChangedListener(new OnWordAddedListener(firstElement));
            ViewExtensions.<EditText>findViewById(view,R.id.edit_learning_text_second)
                    .addTextChangedListener(new OnWordAddedListener(secondElement));

            TextView lang1 = ViewExtensions.findViewById(view, R.id.edit_lang_first);
            TextView lang2 = ViewExtensions.findViewById(view,R.id.edit_lang_second);
            String frLang = editing.getLanguage(first).getTitle().toUpperCase();
            String baLang = editing.getLanguage(second).getTitle().toUpperCase();
            lang1.setText(frLang);
            //lang1.setTextColor();
            lang2.setText(baLang);

            TicketColors color = editing.getBackground();
            updateBackground(view, BaseActivity.getDrawableBackgroundId(color));
            view.findViewById(R.id.add_ticket_frame_base)
                .setBackground(BaseActivity.getDrawableSecondBackground(color));

            Spinner dics = ViewExtensions.findViewById(view, R.id.add_ticket_dictionaries);
            dics.setAdapter(new DisplayDictionaryDrawerAdapter(this.getActivity(), dictionaries, false));
            dics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DictionaryViewModel selected = dictionaries.get(position);
                    editing.setDictionary(selected);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            int i = 0;
            int curId = editing.getDictionary().dto.id;
            if(curId != 0) {
                for (; i < dictionaries.size(); ++i) {
                    if (dictionaries.get(i).dto.id == curId) {
                        break;
                    }
                }
            }
            dics.setSelection(i);
            return view;
        }


        public static void updateBackground(View view, int resourceId){
            LinearLayout f1 = (LinearLayout)view.findViewById(R.id.add_ticket_frame_first);
            FrameLayout f2 = (FrameLayout)view.findViewById(R.id.add_ticket_frame_second);
            f1.setBackgroundResource(resourceId);
            f2.setBackgroundResource(resourceId);
            view.findViewById(R.id.add_ticket_frame_dictionary).setBackgroundResource(resourceId);
        }

        @Override
        public void onPause(){
            super.onPause();
        }

        private class OnWordAddedListener implements TextWatcher {
//            private final ArrayList<String> array;
//            private final ArrayAdapter<String> adapter;
            private final ListElement element;
            //ArrayList<String> array, ArrayAdapter<String> adapter
            public OnWordAddedListener(ListElement element){
                this.element = element;
//                this.array = array;
//                this.adapter = adapter;
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if(StringHelper.endsWithLineSeparator(s)){
                    String word = s.toString().trim();
                    if(!word.isEmpty()) {
                        element.array.add(word);
                        element.adapter.notifyDataSetChanged();
                        element.updateHeight();
                        s.clear();
                    }
                }
            }
        }
    }

    /**
     * Action bur controller
     */
    public static class ActionBarController{
        private final Activity activity;
        private final ActionBar actionBar;
        private Spinner spinnerLeft;
        private Spinner spinnerRight;

        public ActionBarController(Activity activity,ActionBar actionBar) {
            this.activity = activity;
            this.actionBar = actionBar;
        }

        public void init(Menu menu){
           // s.setOnItemSelectedListener(onItemSelectedListener); // set the listener, to perform actions based on item selection
            spinnerLeft =(Spinner) MenuItemCompat.getActionView(menu.findItem(R.id.actionbar_languagefirst));
            spinnerRight =(Spinner) MenuItemCompat.getActionView(menu.findItem(R.id.actionbar_languagesecond));
//
//            turnoverBtn = menu.findItem(R.id.add_ticket_dictionaries_menu_turnover);
//            deleteBtn = menu.findItem(R.id.add_ticket_dictionaries_menu_delete);
//            selectColorsBtn = menu.findItem(R.id.add_ticket_dictionaries_menu_selectcolor);

//            turnoverBtn = (ImageButton)MenuItemCompat.getActionView(menu.findItem(R.id.add_ticket_dictionaries_menu_turnover));
//            deleteBtn = (ImageButton)MenuItemCompat.getActionView(menu.findItem(R.id.add_ticket_dictionaries_menu_delete));
//            selectColorsBtn = (ImageButton)MenuItemCompat.getActionView(menu.findItem(R.id.add_ticket_dictionaries_menu_selectcolor));
//            spinnerLeft =(Spinner) view.findViewById(R.id.menu_language_selector1);
//            spinnerRight =(Spinner) view.findViewById(R.id.menu_language_selector2);
        }
        public void updateLanguages(LanguageViewModel[] languages,int frontInd, int backInd){
            initLanguagesDropdown(spinnerLeft, languages, frontInd);
            initLanguagesDropdown(spinnerRight, languages, backInd);
        }

        private void initLanguagesDropdown(Spinner spinner, LanguageViewModel[] languages, int selectIndex){
            spinner.setAdapter(new LanguageNavigationAdapter(activity,languages));
            spinner.setSelection(selectIndex);
        }

        public LanguageViewModel getFrontLangSelected(){
            return (LanguageViewModel)spinnerLeft.getSelectedItem();
        }
        public LanguageViewModel getBackLangSelected(){
            return (LanguageViewModel)spinnerRight.getSelectedItem();
        }

        public void setFrontDropdownSelectedListener(AdapterView.OnItemSelectedListener click) {
            spinnerLeft.setOnItemSelectedListener(click);
        }
        public void setBackDropdownSelectedListener(AdapterView.OnItemSelectedListener click) {
            spinnerRight.setOnItemSelectedListener(click);
        }

        public void setBackground(Drawable drawable){
//             actionBar.setSplitBackgroundDrawable(drawable);
//             actionBar.setStackedBackgroundDrawable(drawable);
             actionBar.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Select color popup
     */
    public class SelectColorPopup {
        AlertDialog.Builder builder;
        AlertDialog dialog;
        View view;
        TicketColors[] colors;

        public SelectColorPopup(View view) {
            colors = TicketColors.values();
//                    new int[]{
//                R.drawable.ticket_background_blue,
//                R.drawable.ticket_background_green,
//                R.drawable.ticket_background_red,
//                R.drawable.ticket_background_violet,
//                R.drawable.ticket_background_yellow,
//                R.drawable.ticket_background_white
//            };
            builder = new AlertDialog.Builder(view.getContext());
            this.view = AddTicketActivity.this.getLayoutInflater().inflate(R.layout.select_colors_template, null);
            builder.setView(this.view);
            dialog = builder.create();
            init();
        }

        private void init() {
            GridView grid = (GridView)view.findViewById(R.id.grid_colors);

            grid.setAdapter(new DisplayColorAdapter());
            //
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TicketColors color = colors[position];
                    editing.setBackground(color);
                    dialog.cancel();
                    updateEditingTextsData();
                    updatePlaceholder();
                }
            } );
        }

        public void show() {
            dialog.show();
        }

        public class DisplayColorAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return colors.length;
            }

            @Override
            public Object getItem(int position) {
                return colors[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = AddTicketActivity.this.getLayoutInflater();
                View itemTemplate = inflater.inflate( R.layout.select_color_item_template , parent, false);
                FrameLayout container = (FrameLayout)itemTemplate.findViewById(R.id.select_color_frame);
                container.setBackgroundResource(BaseActivity.getDrawableBackgroundId(colors[position]));
                return itemTemplate;
            }
        }
    }
}
