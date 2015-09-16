package tickets.language.svp.languagetickets;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import tickets.language.svp.languagetickets.domain.Repository;
import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.domain.game.GameLearning;
import tickets.language.svp.languagetickets.domain.game.ILearningQuestion;
import tickets.language.svp.languagetickets.ui.BaseActivity;
import tickets.language.svp.languagetickets.ui.TicketColors;
import tickets.language.svp.languagetickets.ui.ViewExtensions;
import tickets.language.svp.languagetickets.ui.controllers.ActivityController;
import tickets.language.svp.languagetickets.ui.listeners.OnClickListener;
import tickets.language.svp.languagetickets.ui.listeners.OnItemClickListener;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.GameLearningActivity.PlaceholderFragment.*;


public class GameLearningActivity extends BaseActivity<GameLearningActivity,GameLearningActivity.GameLearningActivityController> {
    public static class GameLearningActivityController extends ActivityController<GameLearningActivity> {
        private static GameLearning game;
        private final GameLearning.OnGameListener listener;
        public GameLearningActivityController( GameLearningActivity activity, GameLearning.OnGameListener listener) {
            super(activity);
            this.listener = listener;
        }

        public void start(){
            if(game == null) {
                game = new GameLearning(getTickets(getSelectedDictionary()));
                game.setOnRiseLearningTicket(listener);
                moveNextQuestion();
            }
        }

        public void moveNextQuestion() {
            game.moveNext();
        }
    }

    public final class ActivityStorage {
        private final Bundle bundle;
        public ActivityStorage(Intent intent){
//        this.intent = intent;
            Bundle b = intent.getExtras();
//        Object d = null;
//        if(b != null) {
//            d = b.getSerializable(selectedTicketKey);
//        }
            this.bundle = b == null ? new Bundle() : b;
            if(b == null){
                //putTo(intent);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        controller = new GameLearningActivityController(this,
                new GameLearning.OnGameListener(){
                    @Override
                    public void onMovedNext(ILearningQuestion question) {
                        updatePlaceholder(question);
                    }
                }
        );
        super.onCreate(savedInstanceState);
        updatePlaceholder(null);
        controller.start();
    }
    private PlaceholderFragment updatePlaceholder(ILearningQuestion question){
        setContentView(R.layout.activity_game_learning);
        final PlaceholderFragment container = new PlaceholderFragment();
        container.setController(controller);
        container.setQuestion(question,
                new PlaceholderClickListener() {
                    public void onSelected(ILearningQuestion question, int position) {
                        //TODO менять кнопки нижнего меню, переключить в prev | next

                    }
                    @Override
                    public void onForward() {
                        controller.moveNextQuestion();
                    }
                }
        );
        getFragmentManager().beginTransaction()
                .add(R.id.container, container)
                .commit();
        return container;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_learning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private GameLearningActivityController controller;

        public static abstract class PlaceholderClickListener {
            public abstract void onSelected(ILearningQuestion question, int position);
            public abstract void onForward();
        }

        private ILearningQuestion question;
        private PlaceholderClickListener listener;
        private ImageButton forward;

        public void setController(GameLearningActivityController controller){
            this.controller = controller;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_game_learning, container, false);

            if(question != null){
                TicketViewModel ticket = question.getLearningTicket();
                TicketColors color = ticket.getBackground();
                view.findViewById(R.id.game_learning_frame_base)
                        .setBackground(BaseActivity.getDrawableSecondBackground(color));

                int background = BaseActivity.getDrawableBackgroundId(color);

                view.findViewById(R.id.game_learning_ask_ticket).setBackgroundResource(background);
                view.findViewById(R.id.game_learning_frame_choice_btns).setBackgroundResource(background);

                TextView text = (TextView)view.findViewById(R.id.game_learning_text);
                text.setText(question.getHeader());



                String[] cc = question.getChoices();
                ChoiceAdapter.ChoiceItem[] choices = new ChoiceAdapter.ChoiceItem[cc.length];
                for (int i=0; i < cc.length;++i){
                    choices[i] = new ChoiceAdapter.ChoiceItem(StringHelper.splitByLineSeparator(cc[i]));
                }

                int halfScreenWidth = (int)(controller.getScreenWidth() * 0.5);

                GridLayout root = ViewExtensions.findViewById(view, R.id.game_grid_choices);
                LinearLayout col1 = new LinearLayout(inflater.getContext());
                col1.setOrientation(LinearLayout.VERTICAL);
                col1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                LinearLayout col2 = new LinearLayout(inflater.getContext());
                col2.setOrientation(LinearLayout.VERTICAL);
                col2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                GridLayout.LayoutParams param1 = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(0));
                param1.width = halfScreenWidth;
                root.addView(col1, param1);
                GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(1));
                param2.width = halfScreenWidth;
                root.addView(col2, param2);

                for (int i=0; i < choices.length ; ++i) {
                    View item = inflater.inflate( R.layout.ticket_item_template , container, false);
                    item.findViewById(R.id.ticket_item_container).setBackgroundResource(background);
                 //   item.setOnClickListener(new OnClickListener(ticket, listener));
                    //adapter for list of words for one ticket
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                            R.layout.list_item_word_template,
                            ticket.getDisplayLearningText());
                    ListView list = ViewExtensions.findViewById(item, R.id.learning_listOfFirst);
                    list.setAdapter(adapter);
                   // list.setOnItemClickListener(new OnItemClickListener(ticket, listener));
                    BaseActivity.setHeightListView(list, adapter);
                    //separate tickets by 2 columns
                    if(i % 2 == 0){
                        //col1.getChildCount();
                        col1.addView(item);
                    }else {
                        col2.addView(item);
                    }
                }
                /*
//                ChoiceAdapter adapter = new ChoiceAdapter(inflater,choices ,background);
                //final GridView grid = (GridView)view.findViewById(R.id.game_grid_choices);
                grid.setAdapter(adapter);

                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, grid);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = grid.getLayoutParams();
                params.height = totalHeight + (10 * (adapter.getCount() - 1));
                grid.setLayoutParams(params);
                grid.requestLayout();

                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int correct = question.getCorrectChoice();
                        ChoiceAdapter adapter = (ChoiceAdapter)parent.getAdapter();
                        adapter.setChosenItem(view, parent, position, correct);

                        listener.onSelected(question, position);
                        //unsubscribe events to discard selecting another choice
                        grid.setOnItemClickListener(null);
                        // subscribe/visible to forward button
                        forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onForward();
                            }
                        });
                        forward.setVisibility(View.VISIBLE);
                    }
                });
                */
                forward = (ImageButton)view.findViewById(R.id.game_bottom_btn_forward);
                forward.setVisibility(View.GONE);
            }

            return view;
        }

        public void setQuestion(ILearningQuestion question, PlaceholderClickListener bfListener) {
            this.question = question;
            this.listener = bfListener;
        }

        public static class ChoiceAdapter extends BaseAdapter {

            public static class ChoiceItem{
                public final String[] text;
                public boolean isCorrect;
                public boolean isIncorrect;
                public ChoiceItem(String[] text){
                    this.text = text;
                    isCorrect = false;
                    isIncorrect = false;
                }
                public void markAsCorrectChoice(){
                    isCorrect = true;
                    isIncorrect = false;
                }
                public void markAsIncorrectChoice(){
                    isCorrect = false;
                    isIncorrect = true;
                }
            }

            private final ChoiceItem[] choices;
            private final int backgroundResource;
            private final LayoutInflater inflater;

            public ChoiceAdapter(LayoutInflater inflater, ChoiceItem[] choices, int backgroundResource) {
                this.choices = choices;
                this.backgroundResource = backgroundResource;
                this.inflater = inflater;
            }

            @Override
            public int getCount() {
                return choices.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public void setChosenItem(View view, AdapterView<?>  parent, int position, int correct){
                ChoiceItem item = choices[position];
                if(position == correct){
                    item.markAsCorrectChoice();
                } else {
                    item.markAsIncorrectChoice();
                    choices[correct].markAsCorrectChoice();

                    //int visiblePosition = parent.getFirstVisiblePosition();
                    View view1 = parent.getChildAt(correct);

                    setChosenView(view1,choices[correct]);
                }
                setChosenView(view, item);
            }

            @Override
            public View getView(int position, View itemTemplate, ViewGroup parent) {
                if (itemTemplate == null) {
                    itemTemplate = inflater.inflate(R.layout.game_learning_choise_item_template, parent, false);
                }
                LinearLayout container = (LinearLayout) itemTemplate.findViewById(R.id.ticket_item_container);
                container.setBackgroundResource(backgroundResource);

                TextView textView = (TextView) itemTemplate.findViewById(R.id.learning_text);

                ChoiceItem item = choices[position];

//                ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(inflater.getContext(),
//                        android.R.layout.simple_list_item_1,item.text);
//                ListView listView =(ListView)itemTemplate.findViewById(R.id.learning_text);
//                listView.setAdapter(firstAdapter);
//                setHeightListView(listView, firstAdapter);
//                listView.setOnItemClickListener(listener);

                String text =StringHelper.joinByLineSeparator(item.text);
                textView.setText(text);

                setChosenView(itemTemplate, item);

                return itemTemplate;
            }

            private static void setChosenView(View view, ChoiceItem item){
//                FontFitTextView text = (FontFitTextView) view.findViewById(R.id.learning_text);
                TextView text =(TextView)view.findViewById(R.id.learning_text);
                ImageView image;
                if(item.isCorrect){
                    //  text.setTextColor(Color.parseColor("#0099CC"));
                    image = (ImageView)view.findViewById(R.id.game_learning_choice_item_status_correct);
                    item.markAsCorrectChoice();
                    image.setVisibility(View.VISIBLE);
                }
                if(item.isIncorrect){
                    //text.setTextColor(Color.parseColor("#CC0000"));
                    image = (ImageView) view.findViewById(R.id.game_learning_choice_item_status_incorrect);
                    item.markAsIncorrectChoice();
                    image.setVisibility(View.VISIBLE);
                }
                text.setLeft(40);
            }
        }
    }
}

