package tickets.language.svp.languagetickets.domain;

import tickets.language.svp.languagetickets.domain.model.DictionaryDto;
import tickets.language.svp.languagetickets.ui.viewModel.DictionaryViewModel;

/**
 * Created by Pasha on 7/15/2015.
 */
public final class Consts {
    public final static class Dictionary {
        private static final String dictionaryName_All = "All";
        protected static final String dictionaryName_Learned = "Learned by heart";
        public static final DictionaryViewModel All = new DictionaryViewModel(new DictionaryDto(dictionaryName_All,1));
        public static final DictionaryViewModel Learned = new DictionaryViewModel(new DictionaryDto(dictionaryName_Learned,1));
    }
}
