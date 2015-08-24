package tickets.language.svp.languagetickets.ui.listeners;

import android.view.View;

import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 6/24/2015.
 */
public class OnClickListener implements View.OnClickListener {
    private final TicketViewModel ticket;
    private final OnTicketClickListener listener;
    public OnClickListener(TicketViewModel ticket, OnTicketClickListener listener) {
        this.ticket = ticket;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(ticket);
    }
}
