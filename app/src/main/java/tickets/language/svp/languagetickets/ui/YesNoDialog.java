package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import tickets.language.svp.languagetickets.R;

/**
 * Created by Pasha on 9/13/2015.
 */
public class YesNoDialog {
    public static abstract class OnYesNoClickListener{
        public void onYesClick(){}
        public void onNoClick(){}
    }

    public static YesNoDialog Create(Context context,LayoutInflater inflater){
        return new YesNoDialog(context,inflater);
    }
    public static YesNoDialog Create(Activity activity){
        return new YesNoDialog(activity,activity.getLayoutInflater());
    }

    private final View view;
    AlertDialog dialog;

    public YesNoDialog(Context context,LayoutInflater inflater){
        view = inflater.inflate(R.layout.yes_no_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        dialog = builder.create();
    }

    public YesNoDialog setOnYesNoClickListener( final OnYesNoClickListener listener){
        ViewExtensions.<ImageButton>findViewById(view,R.id.yesnodialog_bottom_btn_no)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener.onNoClick();
                        dialog.cancel();
                    }
                });
        ViewExtensions.<ImageButton>findViewById(view,R.id.yesnodialog_bottom_btn_ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onYesClick();
                        dialog.cancel();
                    }
                });
        return this;
    }

    public void show(String title, String text){
        ViewExtensions.<TextView>findViewById(view, R.id.yesnodialog_title)
                .setText(title);
        ViewExtensions.<TextView>findViewById(view, R.id.yesnodialog_text)
                .setText(text);

        dialog.show();
    }
}
