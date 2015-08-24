package tickets.language.svp.languagetickets.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import tickets.language.svp.languagetickets.R;
import tickets.language.svp.languagetickets.ui.ViewExtensions;

/**
 * Created by Pasha on 7/14/2015.
 */
public class WordsEditAdapter extends BaseAdapter {
    public interface OnWordEditClickListener {
        void onClick(int position);
    }

    private final ArrayList<String> words;
    private final LayoutInflater inflater;
    private OnWordEditClickListener listener;

    public WordsEditAdapter(LayoutInflater inflater,ArrayList<String> words){
        this.words = words;
        this.inflater = inflater;

    }
    public void setOnClickListener(OnWordEditClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return words.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if(view == null){
            view = inflater.inflate( R.layout.list_edit_item_word_template, parent, false);
        }
        TextView text = ViewExtensions.findViewById(view, R.id.word);
        text.setText(words.get(position));
        ImageButton remove = ViewExtensions.findViewById(view, R.id.remove_word_btn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
        return view;
    }
}
