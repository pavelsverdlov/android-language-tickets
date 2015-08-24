package tickets.language.svp.languagetickets.ui.viewModel;

import tickets.language.svp.languagetickets.domain.model.LanguageDto;

/**
 * Created by Pasha on 2/8/2015.
 */
public class LanguageViewModel {
    public final LanguageDto dto;
    private boolean isSelected;

    public LanguageViewModel(LanguageDto dto){
        this.dto = dto;
    }

    public void markAsSelected(){
        isSelected = true;
    }

    public String getTitle() {
        return dto.name;
    }
}
