package tickets.language.svp.languagetickets.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.ui.viewModel.LanguageViewModel;

/**
 * Created by Pasha on 2/8/2015.
 */
public class LanguageNavigationAdapter extends BaseAdapter {

    //private ImageView imgIcon;
    private TextView txtTitle;
    private LanguageViewModel[] spinnerNavItem;
    private Context context;

    public LanguageNavigationAdapter(Context context, LanguageViewModel[] spinnerNavItem) {
        this.spinnerNavItem = spinnerNavItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return spinnerNavItem.length;
    }

    @Override
    public Object getItem(int index) {
        return spinnerNavItem[index];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(
                    R.layout.list_item_word_template,
                    //android.R.layout.simple_spinner_dropdown_item,//
                    null);
        }
//        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(android.R.id.text1);
//        imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
       // imgIcon.setVisibility(View.GONE);
        txtTitle.setText(spinnerNavItem[position].getTitle());
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item,//R.layout.list_item_title_navigation,
                    null);
        }

//        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(android.R.id.text1);

//        imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
        txtTitle.setText(spinnerNavItem[position].getTitle());
        return convertView;
    }

}