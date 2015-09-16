package tickets.language.svp.languagetickets.ui.viewModel;

import java.io.Serializable;

import tickets.language.svp.languagetickets.domain.Consts;
import tickets.language.svp.languagetickets.domain.model.DictionaryDto;

/**
 * Created by Pasha on 3/15/2015.
 */
public class DictionaryViewModel implements Serializable {
    public final DictionaryDto dto;

    public DictionaryViewModel(DictionaryDto dto) {
        this.dto = dto;
    }

    public String getTitle(){
        return dto.title;
    }

    public static DictionaryViewModel createNew(String newtext) {
        DictionaryDto dto = new DictionaryDto();
        dto.id = -1;
        dto.title = newtext;
        return new DictionaryViewModel(dto);
    }

    public int getLength() {
        return dto.length;
    }

    public boolean isSys() {
        return this == Consts.Dictionary.Learned || this == Consts.Dictionary.All;
    }

    public boolean isLearned() {
        return Consts.Dictionary.Learned == this;
    }
}
