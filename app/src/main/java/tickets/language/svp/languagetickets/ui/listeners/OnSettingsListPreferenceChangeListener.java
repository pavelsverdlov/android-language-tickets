package tickets.language.svp.languagetickets.ui.listeners;

import android.preference.ListPreference;
import android.preference.Preference;

/**
 * Created by Pasha on 8/18/2015.
 */
public abstract class OnSettingsListPreferenceChangeListener<T> implements Preference.OnPreferenceChangeListener {

    private final OnTClickListener<T> listener;
    public OnSettingsListPreferenceChangeListener(OnTClickListener<T> listener){
        this.listener = listener;
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        ListPreference listPreference = (ListPreference) preference;
        int index = listPreference.findIndexOfValue(stringValue);

        // Set the summary to reflect the new value.
        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        listener.onClick(parseResult(value));

        return true;
    }
    protected abstract T parseResult(Object value);
}
