package tickets.language.svp.languagetickets.domain.game;

import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 4/26/2015.
 */
public interface ILearningQuestion {
    String getHeader();
    String[] getChoices();
    TicketViewModel getLearningTicket();
    int getCorrectChoice();
}
