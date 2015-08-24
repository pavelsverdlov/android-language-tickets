package tickets.language.svp.languagetickets.ui.listeners;

import android.view.View;
import android.widget.AdapterView;

import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 6/24/2015.
 */
public class OnItemClickListener implements AdapterView.OnItemClickListener{
    private final TicketViewModel ticket;
    private final OnTicketClickListener listener;
    public OnItemClickListener(TicketViewModel ticket,OnTicketClickListener listener ) {
        this.ticket = ticket;
        this.listener = listener;
    }
    @Override
    public void onItemClick(AdapterView<?> p, View view, int pos, long id) {
        listener.onClick(ticket);
    }
}

