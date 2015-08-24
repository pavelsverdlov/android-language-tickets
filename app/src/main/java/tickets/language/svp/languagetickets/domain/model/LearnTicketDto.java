package tickets.language.svp.languagetickets.domain.model;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Pasha on 2/1/2015.
 * LearnTicket is the ticket union in group for some list of learning
 * example:
 * Group for learning by 10/10/2015
 * - LearnTicket1
 * - LearnTicket2
 * ...
 */
public final class LearnTicketDto extends UUIDBaseDto implements Serializable {
    /**
     * 0 - first
     * 1 - second
     */
    public int learningSide;

    public SideTicketDto first;
    public SideTicketDto second;
    public Date created;

    public int background;
    public DictionaryDto dictionary;
    public boolean noDictionary;

    public LearnTicketDto(){
        created = new Date();
    }
}

