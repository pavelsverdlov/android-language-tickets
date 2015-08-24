package tickets.language.svp.languagetickets.domain.model;

import java.io.Serializable;

/**
 * Created by Pasha on 1/11/2015.
 */
public class UUIDBaseDto implements Serializable {
    public String id;

    public UUIDBaseDto(){}
    public void generateId(){
        id = java.util.UUID.randomUUID().toString();
    }
}
