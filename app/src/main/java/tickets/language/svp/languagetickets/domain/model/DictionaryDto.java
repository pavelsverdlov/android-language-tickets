package tickets.language.svp.languagetickets.domain.model;

import java.io.Serializable;

/**
 * Created by Pasha on 3/15/2015.
 */
public final class DictionaryDto extends BaseDto implements Serializable {
    public String title;
    public int sys;
    public int length;

    public DictionaryDto(){}
    public DictionaryDto(String title,int sys){
        this.title = title;
        this.sys = sys;
    }
}
