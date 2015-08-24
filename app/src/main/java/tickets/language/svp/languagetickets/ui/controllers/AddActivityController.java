package tickets.language.svp.languagetickets.ui.controllers;

import tickets.language.svp.languagetickets.AddTicketActivity;
import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.queries.TicketQueries;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 2/8/2015.
 */
public class AddActivityController extends ActivityController<AddTicketActivity> {
    public AddActivityController( AddTicketActivity activity) {
        super(activity);
    }

    public void markAsLearned(TicketViewModel ticket) {
        repository.add(TicketQueries.addAsLearned(ticket));
    }
}
