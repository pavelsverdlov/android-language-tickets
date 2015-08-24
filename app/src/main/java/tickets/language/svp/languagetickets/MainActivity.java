package tickets.language.svp.languagetickets;

import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.ui.ActivityOperationResult;
import tickets.language.svp.languagetickets.ui.tab.SectionsPagerAdapter;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.ui.ActivityOperationResult.EditTicket;

public class MainActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    //public Controller controller;
    private Storage storage;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = new Storage(this.getIntent());
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                        //Do your own error handling here
//                        if (oldHandler != null)
//                            oldHandler.uncaughtException(
//                                    paramThread,
//                                    paramThrowable
//                            ); //Delegates to Android's error handling
//                        else
                        Throwable couse = paramThrowable.getCause();

                        System.exit(2); //Prevents the service/app from freezing
                    }
                });

//        this.controller = new Controller(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //display < at the right of the program icon
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            ActionBar.Tab tab = actionBar.newTab()
                .setText(mSectionsPagerAdapter.getPageTitle(i))
                .setTabListener(this);

            actionBar.addTab(tab);
        }
    }
    private boolean isRestarted;

    @Override
    protected void onRestart(){
        isRestarted = true;
        super.onRestart();
    }
    @Override
    protected void onResume(){
        super.onResume();
//
//        if(!isRestarted){ return; }
//        isRestarted = false;
//        TicketViewModel tv = storage.getSelectedTicket();
//        storage = new Storage(this.getIntent());
//        tv = storage.getSelectedTicket();
//
//        String txt = null;
//        if(selectedTicket.isChanged()){
//            txt =selectedTicket.getDisplayLearningText();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(RESULT_CANCELED == resultCode){
            return;
        }
        if (requestCode == EditTicket.toInt()) {
            if (resultCode == RESULT_OK) {
                storage = new Storage(data);
                TicketViewModel tv = storage.getSelectedTicket();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_top_btn_add_new:
                goToAddTicketActivity();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * invokes when select any tab item for example "SECTION 1".. etc
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    //grid items events
    private TicketViewModel[] displayedTickets;
    private TicketViewModel selectedTicket;
    private int selectedTab;

    public TicketViewModel MarkTicketAsSelected(TicketViewModel ticket){
        //ticket.TurnOver();
        selectedTicket = ticket;
        storage.setSelectedTicket(selectedTicket);
        return ticket;
    }
    public void updateTicketView(TicketViewModel ticket, View view) {
        // set values
        TextView textView = (TextView) view.findViewById(R.id.learning_text);
        textView.setText(StringHelper.joinByLineSeparator(ticket.getDisplayLearningText()));
    }
    public TicketViewModel[] getTicketsBySectionId(int sectionNumber){
        if(selectedTab != sectionNumber) {
            selectedTab = sectionNumber;
        //    displayedTickets = controller.getTickets();
        }
        return displayedTickets;
    }
    public void goToAddTicketActivity(){
        Intent intent = new Intent(MainActivity.this, AddTicketActivity.class);//new Intent(this, AddTicketActivity.class);
        //storage.putTo(intent);
        startActivityForResult(intent,EditTicket.toInt());
    }

    public void goToActivity(ActivityOperationResult type){
        goToAddTicketActivity();
    }
    public void saveSelectedTicket(TicketViewModel ticket){

    }
}


