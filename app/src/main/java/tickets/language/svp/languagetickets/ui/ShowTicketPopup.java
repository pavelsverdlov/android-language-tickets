package tickets.language.svp.languagetickets.ui;

import android.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.ui.controllers.RootActivityController;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

/**
 * Created by Pasha on 2/10/2015.
 */
public class ShowTicketPopup {
    private TicketViewModel ticket;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    View view;
    TicketViewModel.SideTypes type;
    public final RootActivityController controller;
    public ShowTicketPopup(RootActivityController controller, View v) {
        type = TicketViewModel.SideTypes.Back;
        this.controller = controller;
        builder = new AlertDialog.Builder(v.getContext());
        view = controller.activity.getLayoutInflater().inflate(R.layout.fragment_ticket_clicked_popup, null);
        builder.setView(view);
        dialog = builder.create();
        init();
    }

    private void init(){
        ImageButton ok = (ImageButton)view.findViewById(R.id.actionbar_bottom_btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.updateTicketCounters(ticket, RootActivityController.CounterTypes.Correct, type);
                dialog.cancel();
            }
        });
        ImageButton no = (ImageButton)view.findViewById(R.id.actionbar_bottom_btn_no);
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                controller.updateTicketCounters(ticket, RootActivityController.CounterTypes.Incorrect, type);
                dialog.cancel();
            }
        });

        ImageButton edit = (ImageButton)view.findViewById(R.id.actionbar_bottom_btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
                controller.goToEditTicketActivity();
            }
        });
    }

    public void show(TicketViewModel ticket){
        this.ticket = ticket;
        type =  ticket.getInvertOfCurrentSideType();
        TextView txt = (TextView)view.findViewById(R.id.popup_show_learning_text);
        txt.setText(StringHelper.joinByLineSeparator(ticket.getLearningText(type)));

        //TextView data = (TextView)view.findViewById(R.id.popup_show_data);
       // data.setText(ticket.getData(type));

        TextView lang = (TextView)view.findViewById(R.id.popup_show_language);
        lang.setText(ticket.getLanguage(type).getTitle().toUpperCase());

        int id = BaseActivity.getDrawableBackgroundId(ticket.getBackground());
        view.findViewById(R.id.frame_ticket_clicked_top).setBackgroundResource(id);
        view.findViewById(R.id.actionbar_bottom_btn_ok).setBackgroundResource(id);
        view.findViewById(R.id.actionbar_bottom_btn_no).setBackgroundResource(id);
        view.findViewById(R.id.frame_ticket_clicked_base)
            .setBackground(BaseActivity.getDrawableSecondBackground(ticket.getBackground()));

        if(controller.getSelectedDictionary().isLearned()) {
            view.findViewById(R.id.actionbar_bottom_btn_edit).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.actionbar_bottom_btn_ok).setVisibility(View.GONE);
            view.findViewById(R.id.actionbar_bottom_btn_no).setVisibility(View.GONE);
        }

        dialog.show();
    }
}
