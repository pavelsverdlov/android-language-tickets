package tickets.language.svp.languagetickets.domain.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Pasha on 1/4/2015.
 */
public final class SideTicketDto extends UUIDBaseDto implements Serializable {
    public String learningText;
    public Date created;

    public int language;
    public int correctCount;
    public int incorrectCount;

    public LanguageDto lang;

    public SideTicketDto(){
        created = new Date();
        learningText = "";
//        learningText = id.substring(0,5);
    }
}
