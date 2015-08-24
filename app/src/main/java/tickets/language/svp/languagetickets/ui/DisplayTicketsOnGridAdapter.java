package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.ui.controllers.RootActivityController;
import tickets.language.svp.languagetickets.ui.controls.AutoResizeTextView;
import tickets.language.svp.languagetickets.ui.controls.FontFitTextView;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

@Deprecated
public class DisplayTicketsOnGridAdapter extends BaseAdapter {
    @Deprecated
    public interface OnTicketClickListener{
        public abstract void onClick(TicketViewModel ticket);
    }
    private final TicketViewModel[] tickets;
    private final LayoutInflater inflater;
    private final OnTicketClickListener listener;

    public DisplayTicketsOnGridAdapter(LayoutInflater inflater, TicketViewModel[] tickets, OnTicketClickListener listener){
        this.inflater = inflater;
        this.tickets = tickets;
        this.listener = listener;
    }

//    public DisplayTicketsOnGridAdapter(MainActivity activity, TicketViewModel[] tickets){
//        this.tickets = tickets;
//        this.activity = activity;
//    }

    public  TicketViewModel[] getTickets(){
        return tickets;
    }

    @Override
    public int getCount() { return tickets.length; }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemTemplate, final ViewGroup parent) {
        if(itemTemplate == null){
            // get layout from ticket_item_template.xml
            itemTemplate = inflater.inflate( R.layout.ticket_item_template , parent, false);
        }
        FrameLayout container = (FrameLayout)itemTemplate.findViewById(R.id.ticket_item_container);
        container.setBackgroundResource(BaseActivity.getDrawableBackgroundId(tickets[position].getBackground()));
        // set values
        //FontFitTextView textView = (FontFitTextView) itemTemplate.findViewById(R.id.learning_text);
        //textView.setText(StringHelper.joinByLineSeparator());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                R.layout.list_item_word_template,
                tickets[position].getDisplayLearningText());
        ListView list = (ListView) itemTemplate.findViewById(R.id.learning_listOfFirst);
        list.setOnItemClickListener(new ItemClickListener(tickets[position], listener));
        list.setAdapter(adapter);

        BaseActivity.setHeightListView(list, adapter);

        FrameLayout frame = (FrameLayout)itemTemplate.findViewById(R.id.ticket_item_container);
        frame.setOnClickListener(new ClickListener(tickets[position], listener));

//        textView.resizeText();
        //controller.updateTicketView(tickets[position], itemTemplate);
        //
        return itemTemplate;
    }
    @Deprecated
    public static class ClickListener implements View.OnClickListener {
        private final TicketViewModel ticket;
        private final OnTicketClickListener listener;
        public ClickListener(TicketViewModel ticket, OnTicketClickListener listener) {
            this.ticket = ticket;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(ticket);
        }
    }
    @Deprecated
    public static class ItemClickListener implements AdapterView.OnItemClickListener{
        private final TicketViewModel ticket;
        private final OnTicketClickListener listener;
        public ItemClickListener(TicketViewModel ticket,OnTicketClickListener listener ) {
            this.ticket = ticket;
            this.listener = listener;
        }
        @Override
        public void onItemClick(AdapterView<?> p, View view, int pos, long id) {
            listener.onClick(ticket);
        }
    }
}

