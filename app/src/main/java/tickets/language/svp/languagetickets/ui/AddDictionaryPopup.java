package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.ui.controllers.RootActivityController;

/**
 * Created by Pasha on 3/21/2015.
 */

public class AddDictionaryPopup {
    public interface OnAddListener {
        void onAddClick(String newtext);
    }

    private final AlertDialog dialog;
    private final View view;
    public AddDictionaryPopup(Activity activity, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        view = activity.getLayoutInflater().inflate(R.layout.fragment_add_dictionary_popup, null);
        builder.setView(view);
        dialog = builder.create();

        ImageButton no = (ImageButton)view.findViewById(R.id.actionbar_bottom_btn_no);
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public String getInputText(){
        EditText editText = (EditText)view.findViewById(R.id.popup_add_dictionary_text);
        return editText.getText().toString();
    }

    public void setAddClickListener(final AddDictionaryPopup.OnAddListener listener){
        ImageButton ok = (ImageButton)view.findViewById(R.id.actionbar_bottom_btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddClick(getInputText());
                dialog.cancel();
            }
        });
    }

    public void show(){
        dialog.show();
    }

}
