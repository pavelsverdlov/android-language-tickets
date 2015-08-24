package tickets.language.svp.languagetickets.ui.tab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import tickets.language.svp.languagetickets.MainActivity;
import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.ui.DisplayTicketsOnGridAdapter;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(MainActivity activity, int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.activity = activity;
        fragment.sectionNumber = sectionNumber;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivity activity;
    private int sectionNumber;

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //set content of page
        GridView grid = (GridView)rootView.findViewById(R.id.grid_tab);

        //grid.setAdapter(new DisplayTicketsOnGridAdapter(activity.controller));
        //
        grid.setOnItemLongClickListener(new TicketLongClickListener());
//        grid.setOnItemClickListener(new TicketClickListener(new ActionBarBottomController(activity.controller,rootView)));
        return rootView;
    }

    private class TicketClickListener implements AdapterView.OnItemClickListener{
//        private final ActionBarBottomController actionBar;
//        public TicketClickListener(ActionBarBottomController actionBar) {
//            this.actionBar = actionBar;
//        }
        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
            DisplayTicketsOnGridAdapter adapter = (DisplayTicketsOnGridAdapter)arg0.getAdapter();
            TicketViewModel ticket = adapter.getTickets()[position];
            activity.MarkTicketAsSelected(ticket);
            activity.updateTicketView(ticket, v);
            //Toast.makeText(getApplicationContext(),"Selected Item: "+selectedItem, Toast.LENGTH_SHORT).show();
//            actionBar.show();
        }
    }
    private class TicketLongClickListener implements AdapterView.OnItemLongClickListener{
        public TicketLongClickListener(){

        }
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            return true;
        }
    }
}
