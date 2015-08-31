package tickets.language.svp.languagetickets.ui.controllers;

import tickets.language.svp.languagetickets.RootActivity;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 2/8/2015.
 */
public class RootActivityController extends ActivityController<RootActivity> {
    public enum CounterTypes {
        Correct,
        Incorrect
    }

    public RootActivityController(RootActivity activity) {
        super(activity);
    }

    private TicketViewModel[] displayedTickets;
    private TicketViewModel selectedTicket;

    public TicketViewModel markTicketAsSelected(TicketViewModel ticket){
        //ticket.TurnOver();
        selectedTicket = ticket;
        activity.storage.setSelectedTicket(ticket);
        return ticket;
    }

    public TicketViewModel[] getTicketsBySelectedDictionary(){
        displayedTickets = getTickets(userSettings.getDbActivateSettings(),getSelectedDictionary());
        return displayedTickets;
    }

    public void updateTicketCounters(TicketViewModel ticket, CounterTypes type, TicketViewModel.SideTypes side) {
        switch (type){
            case Correct:
                ticket.correctTranslated(side);
                break;
            case Incorrect:
                ticket.incorrectTranslated(side);
                break;
        }
        updateTicket(userSettings.getDbActivateSettings(),ticket);
    }
}
