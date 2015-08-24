package tickets.language.svp.languagetickets.ui;

import android.view.View;
import android.widget.LinearLayout;

import tickets.language.svp.languagetickets.R;

/**
 * Created by Pasha on 6/1/2015.
 */
public class ViewExtensions {
    public static <T extends View> T findViewById(View view,int id) {
        return (T)view.findViewById(id);
    }
}
