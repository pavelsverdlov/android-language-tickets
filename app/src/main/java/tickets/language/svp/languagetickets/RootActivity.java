package tickets.language.svp.languagetickets;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;

import tickets.language.svp.languagetickets.ui.AddDictionaryPopup;
import tickets.language.svp.languagetickets.ui.BaseActivity;
import tickets.language.svp.languagetickets.ui.ShowTicketPopup;
import tickets.language.svp.languagetickets.ui.ViewExtensions;
import tickets.language.svp.languagetickets.ui.controllers.RootActivityController;
import tickets.language.svp.languagetickets.ui.listeners.*;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.EditTicket;

public class RootActivity extends BaseActivity<RootActivity,RootActivityController>{
    private PlaceholderFragment fragment;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initAsRoot(this);
        controller = new RootActivityController(this);
        super.onCreate(savedInstanceState);
        savedInstanceState = null;
        setContentView(R.layout.activity_root);
        updateTitle();
        drawer = new Drawer(new DrawerItemClickListener(){
            @Override
            public void onDictionarySelected(DictionaryViewModel selected){
                controller.setCurrentDictionary(selected);
                restartActivity();
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            fragment = new PlaceholderFragment();
            fragment.setController(controller);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    private void updateTitle(){
        DictionaryViewModel d = controller.getSelectedDictionary();
        setTitle(d.getTitle() + " (" + d.getLength() + ")");
    }

    public int getScrollPosition(){
        return PlaceholderFragment.getScrollView(fragment.getView()).getScrollY();
    }
    /** ========================== */
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        controller.restartActivityWithSaveStorage();
        // Pass any configuration change to the drawer toggles
        //drawer.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_root, menu);

        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView) menu.findItem(R.id.root_action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint(getString(android.s));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragment.search(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    private final int[] menukeys = new int[]{
        R.id.root_action_search,
        R.id.actionbar_top_btn_add_new,
        R.id.action_settings,
        R.id.action_startgamelearning
    };
    private final int[] drawermenukeys = new int[]{
            R.id.actionbar_top_btn_add_new_dictionary,
            R.id.actionbar_top_btn_edit_dictionary,
            R.id.actionbar_top_btn_remove_dictionary
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawer.isDrawerOpen();
        for (int menukey : menukeys) {
            menu.findItem(menukey).setVisible(!drawerOpen);
        }
        for (int drawermenukey : drawermenukeys) {
            menu.findItem(drawermenukey).setVisible(drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawer.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
//            case R.id.root_action_search:
//                ProductsResulstActivity.this.finish();
//                break;
            case R.id.actionbar_top_btn_add_new:
                controller.goToAddTicketActivity();
                return true;
            case R.id.action_startgamelearning:
                controller.goToGameLearningActivity();
                return true;
            case R.id.action_settings:
                controller.goToAppSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(RESULT_CANCELED == resultCode){
            return;
        }
        if (requestCode == EditTicket.toInt() && data != null) {
                storage = new Storage(data);
                TicketViewModel tvm = storage.getSelectedTicket();
                if(tvm.isChanged()) {
                    controller.addNewTicket(tvm);
                }
        }
        if(resultCode == RESULT_OK){
            restartActivity(controller.activity.storage);
//            finish();
//            startActivity(getIntent());
        }
    }


    public static class PlaceholderFragment extends Fragment {
        private RootActivityController controller;
        private TicketViewModel[] tickets;
        LayoutInflater inflater;
        ViewGroup container;
        OnTicketClickListener listener;
        LinearLayout col1;
        LinearLayout col2;
        public PlaceholderFragment() {}

        public static ScrollView getScrollView(View rootView){
            return ViewExtensions.findViewById(rootView,R.id.root_scrollview);
        }

        public void setController(RootActivityController controller){
            this.controller = controller;
        }
        //onViewCreated
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            this.inflater = inflater;
            this.container = container;
            View rootView = inflater.inflate(R.layout.fragment_root, container, false);
            //set content of page
            tickets = controller.getTicketsBySelectedDictionary();

            Point size = new Point();
            controller.activity.getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int halfScreenWidth = (int)(screenWidth * 0.5);

            GridLayout root = ViewExtensions.findViewById(rootView, R.id.rootLayout);
            col1 = new LinearLayout(inflater.getContext());
            col1.setOrientation(LinearLayout.VERTICAL);
            col1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            col2 = new LinearLayout(inflater.getContext());
            col2.setOrientation(LinearLayout.VERTICAL);
            col2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            GridLayout.LayoutParams param1 = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(0));
            param1.width = halfScreenWidth;
            root.addView(col1, param1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(1));
            param2.width = halfScreenWidth;
            root.addView(col2, param2);

            listener = new OnTicketClickListener(new ShowTicketPopup(controller, rootView),controller);
            search(null);
            final ScrollView sv = getScrollView(rootView);
            sv.post(new Runnable() {
                @Override
                public void run() {
                    int pos = controller.activity.storage.getScrollPosition();
                    sv.scrollTo(0,pos);
                }
            });
            controller.activity.updateTitle();
            return rootView;
        }

        public void search(String query){
            col2.removeAllViews();
            col1.removeAllViews();
            int index = 0;
            for (int i=0; i < tickets.length ; ++i) {
                TicketViewModel ticket = tickets[i];
                String text1 =ticket.getText(TicketViewModel.SideTypes.Back);
                String text2 =ticket.getText(TicketViewModel.SideTypes.Front);
                if(query != null && !text1.contains(query) && !text2.contains(query)){
                    continue;
                }
                index++;
                View item = inflater.inflate( R.layout.ticket_item_template , container, false);
                item.findViewById(R.id.ticket_item_container)
                        .setBackgroundResource(BaseActivity.getDrawableBackgroundId(ticket.getBackground()));
                item.setOnClickListener(new OnClickListener(ticket, listener));
                //adapter for list of words for one ticket
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                        R.layout.list_item_word_template,
                        ticket.getDisplayLearningText());
                ListView list = ViewExtensions.findViewById(item, R.id.learning_listOfFirst);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new OnItemClickListener(ticket, listener));
                BaseActivity.setHeightListView(list, adapter);
                //separate tickets by 2 columns
                if(index % 2 == 0){
                    col1.addView(item);
                }else {
                    col2.addView(item);
                }
            }
        }


    }

    public abstract class DrawerItemClickListener {
        public abstract void onDictionarySelected(DictionaryViewModel selected);
    }
    public class Drawer{
        private DictionaryViewModel selected;
        private ActionBarDrawerToggle mDrawerToggle;
        private DrawerLayout mDrawerLayout;
        private ListView mDrawerList;
        private String title;

        public Drawer(final DrawerItemClickListener listener) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);

            mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            });
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DictionaryViewModel select = (DictionaryViewModel)parent.getAdapter().getItem(position);
                    if(select == selected){
                        listener.onDictionarySelected(selected);
                    }
                    selected = select;
                }
            });

            mDrawerToggle = new ActionBarDrawerToggle(
                    RootActivity.this, /* host Activity */
                    mDrawerLayout, /* DrawerLayout object */
                    R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                    R.string.drawer_open, /* "open drawer" description */
                    R.string.drawer_close /* "close drawer" description */
            ) {
                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getActionBar().setTitle(title);
                    invalidateOptionsMenu();
                }
                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    title = getActionBar().getTitle().toString();
                    invalidateOptionsMenu();
                    UpdateDraverList();
                    String title = getString(R.string.drawer_title_dictionaries) +
                            " (" + mDrawerList.getAdapter().getCount() + ")";
                    getActionBar().setTitle(title);
                }
            };
            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            switch (item.getItemId()) {
                case R.id.actionbar_top_btn_add_new_dictionary:
                    AddDictionaryPopup addDictionaryPopup = new AddDictionaryPopup(RootActivity.this,fragment.getView());
                    addDictionaryPopup.setAddClickListener(new AddDictionaryPopup.OnAddListener() {
                        @Override
                        public void onAddClick(String newtext) {
                            controller.addNewDictionary(controller.userSettings.getDbActivateSettings(),newtext);
                            UpdateDraverList();
                        }
                    });
                    addDictionaryPopup.show();
                    return true;
                case R.id.actionbar_top_btn_remove_dictionary:
                    if(selected != null){
                        controller.removeDictionary(controller.userSettings.getDbActivateSettings(),selected);
                        UpdateDraverList();
                    }
                    return true;
            }
            return false;
        }

        public void UpdateDraverList(){
            mDrawerList.setAdapter(new DisplayDictionaryDrawerAdapter(
                    RootActivity.this, controller.GetDictionaries(), true));
        }

        public void syncState() {
            mDrawerToggle.syncState();
        }

        public void onConfigurationChanged(Configuration newConfig) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

        public boolean isDrawerOpen() {
            return mDrawerLayout.isDrawerOpen(mDrawerList);
        }
    }


}
