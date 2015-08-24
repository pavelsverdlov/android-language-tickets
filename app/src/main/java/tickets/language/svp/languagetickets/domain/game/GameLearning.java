package tickets.language.svp.languagetickets.domain.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import tickets.language.svp.languagetickets.domain.StringHelper;
import tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel;

import static tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel.SideTypes.Back;
import static tickets.language.svp.languagetickets.ui.viewModel.TicketViewModel.SideTypes.Front;

/**
 * Created by Pasha on 4/25/2015.
 */
public class GameLearning {
    public static abstract class OnGameListener {
        public abstract void onMovedNext(ILearningQuestion question);
    }
    public final class LearningQuestion implements ILearningQuestion {
        @Override
        public String getHeader() {
            return StringHelper.joinByLineSeparator(gameTicketSide.ticket.getLearningText(gameTicketSide.side));
        }
        @Override
        public String[] getChoices() {
            return choices;
        }
        public TicketViewModel getLearningTicket(){
            return gameTicketSide.ticket;
        }

        private final String[] choices;
        private final TicketViewModel.SideTypes side;
        private final GameTicketSide gameTicketSide;
        protected LearningQuestion(GameTicketSide gameTicketSide, TicketViewModel.SideTypes side,String[] choices){
            this.gameTicketSide = gameTicketSide;
            this.side = side;
            this.choices = choices;
        }

        public int getCorrectChoice(){
            String correct = StringHelper.joinByLineSeparator(gameTicketSide.ticket.getLearningText(side));
            for (int i=0;i < choices.length; ++i){
                if(choices[i].equals(correct)){
                    return i;
                }
            }
            throw new InternalError("Not found correct text in choices array.");
        }
    }
    static class GameTicketSide implements Comparable<GameTicketSide>{
        public final TicketViewModel ticket;
        public final TicketViewModel.SideTypes side;
        public final int index;
        public GameTicketSide(TicketViewModel ticket, TicketViewModel.SideTypes side, int index){
            this.ticket = ticket;
            this.side = side;
            this.index = index;
        }

        public TicketViewModel.SideTypes getInvertOfCurrentSideType() {
            switch (side){
                case Back:
                    return Front;
                case Front:
                    return Back;
            }
            throw new InternalError("Incorrect side type.");
        }

        @Override
        public String toString() {
            return "[" + index + ";" + ticket.getLearningText(side) + "]";
        }

        @Override
        public int compareTo(GameTicketSide tmp2) {
            GameTicketSide tmp1 = this;
            int incorrect1 = tmp1.ticket.getIncorrectTranslated(tmp1.side);
            int incorrect2 = tmp2.ticket.getIncorrectTranslated(tmp2.side);
            if(incorrect1 < incorrect2) {
                /* текущее меньше полученного */
                return -1;
            } else if(incorrect1 > incorrect2) {
                /* текущее больше полученного */
                return 1;
            }
                /* текущее равно полученному */
            return 0;
        }

        public static class GameTicketSideComparator implements Comparator<GameTicketSide> {
            private final Map<Integer, Integer> played;
            public GameTicketSideComparator(Map<Integer, Integer> played){
                this.played = played;
            }
            @Override
            public int compare(GameTicketSide tmp1, GameTicketSide tmp2) {
                int incorrect1 = tmp1.ticket.getIncorrectTranslated(tmp1.side);
                int incorrect2 = tmp2.ticket.getIncorrectTranslated(tmp2.side);

//                if(played.containsKey(tmp1.index)){
//                    ++incorrect2;
//                    incorrect2 *= played.get(tmp1.index);
//                }
//                if(played.containsKey(tmp2.index)){
//                    ++incorrect1;
//                    incorrect1 *= played.get(tmp2.index);
//                }

                if(incorrect1 < incorrect2) {
                /* текущее меньше полученного */
                    return -1;
                } else if(incorrect1 > incorrect2) {
                /* текущее больше полученного */
                    return 1;
                }
                /* текущее равно полученному */
                return 0;
            }
        }
    }

    private final int waitTime = 3;
//    private final Queue<GameTicketSide> gameTicketsQueue;
    private final ArrayList<GameTicketSide> gameTicketsQueue;
    private final TicketViewModel[] tickets;
    private final ArrayList<GameTicketSide> correct;
    private final ArrayList<GameTicketSide> incorrect;
    private final Timer timer;
    private final Random rand;
    private final Map<Integer, Integer> played;
    private OnGameListener listener;
    private LearningQuestion currentQuestion;

    public GameLearning(TicketViewModel[] tickets){
        played = new HashMap<Integer, Integer>();
        this.tickets = tickets;
        timer = new Timer();
//        this.gameTicketsQueue = new PriorityQueue<GameTicketSide>(tickets.length,
//                new GameTicketSide.GameTicketSideComparator(played));
        this.gameTicketsQueue = new ArrayList<GameTicketSide>();
        for(int i =0; i < tickets.length; ++i){
            TicketViewModel t = tickets[i];
            this.gameTicketsQueue.add(new GameTicketSide(t, t.getCurrentSideType(), i));
        }
        Collections.sort(this.gameTicketsQueue);

        correct = new ArrayList<GameTicketSide>();
        incorrect = new ArrayList<GameTicketSide>();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, waitTime);

        rand = new Random();
    }

    public void setOnRiseLearningTicket(OnGameListener listener){
        this.listener = listener;
    }

    public void moveNext(){
        if (listener == null){
            throw new NullPointerException("listener should be initialized!");
        }
        currentQuestion = getCurrentQuestion();
        listener.onMovedNext(currentQuestion);
    }

    private LearningQuestion getCurrentQuestion(){
        GameTicketSide gts = gameTicketsQueue.get(0);
        gameTicketsQueue.remove(0);
        //
        TicketViewModel ticket = gts.ticket;
        int lang = ticket.getLanguage(gts.side).dto.id;
        ArrayList<String> choices = new ArrayList<String>();
        TicketViewModel.SideTypes invertSide = gts.getInvertOfCurrentSideType();
        choices.add(StringHelper.joinByLineSeparator(ticket.getLearningText(invertSide)));
        //try {
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            int count = tickets.length > 5 ? 5 : tickets.length;
            while (choices.size() != count) {
                int indexAnswer = getRandomIndex(gts.index);
                if (indexes.contains(indexAnswer)) {
                    continue;
                }
                indexes.add(indexAnswer);

                TicketViewModel answer = tickets[indexAnswer];
                int id = answer.getLanguage(Front).dto.id;
                TicketViewModel.SideTypes side;
                if (id != lang) {
                    side = Front;
                } else {
                    side = Back;
                }

                choices.add(StringHelper.joinByLineSeparator(answer.getLearningText(side)));
            }
            //mix choices
            Collections.shuffle(choices);

            int playcount = 0;
            if (!played.containsKey(gts.index)) {
                played.put(gts.index, playcount);
            } else {
                playcount = played.get(gts.index);
            }
            ++playcount;
            //skip GameTicketSide if both sides were played
            if (playcount < 2) {
                GameTicketSide otherside = new GameTicketSide(ticket, invertSide, gts.index);
                int index = rand.nextInt(tickets.length);
                if(index >= gameTicketsQueue.size()){

                }
                gameTicketsQueue.add(index, otherside);
                played.put(gts.index, playcount);
            }
       // }catch (Exception ex){

//            int line = ex.getStackTrace()[0].getLineNumber();
//            String method = ex.getStackTrace()[0].getMethodName();
            //Toast.makeText(.this, str, Toast.LENGTH_LONG).show();
        //}
        return new LearningQuestion(gts, invertSide, choices.toArray(new String[choices.size()]));
    }

    private int getRandomIndex(int skip){
        int i;
        while (true){
            i = rand.nextInt(tickets.length);
            if(i != skip && i < gameTicketsQueue.size()){
                break;
            }
        }
        return i;
    }
}
