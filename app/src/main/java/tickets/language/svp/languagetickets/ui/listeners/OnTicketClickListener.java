package tickets.language.svp.languagetickets.ui.listeners;

import tickets.language.svp.languagetickets.ui.ShowTicketPopup;
import tickets.language.svp.languagetickets.ui.controllers.RootActivityController;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 6/24/2015.
 */
public class OnTicketClickListener {
    private final ShowTicketPopup popup;
    private final RootActivityController controller;
    public OnTicketClickListener(ShowTicketPopup popup, RootActivityController controller) {
        this.popup = popup;
        this.controller = controller;
    }

    public void onClick(TicketViewModel ticket) {
        controller.markTicketAsSelected(ticket);
        popup.show(ticket);
    }
}

