package tickets.language.svp.languagetickets.ui;

/**
 * Created by Pasha on 2/14/2015.
 */
public enum TicketColors {
    //Undefined(0),
    Blue(1),
    Yellow(2),
    Green(3),
    Red(4),
    Violet(5),
    White(6),
    Pink(7),
    DeepPurple(8),
    Indigo(9),
    Cyan(10),
    Lime(11),
    DeepOrange(12),
    Brown(13),
    Grey(14);

    private final int code;

    private TicketColors(int code) {
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public static TicketColors fromInt(int i) {
        switch (i){
            //case 0: return Undefined;
            case 1: return Blue;
            case 2: return Yellow;
            case 3: return Green;
            case 4: return Red;
            case 5: return Violet;
            case 6: return White;
            case 7: return Pink;
            case 8: return DeepPurple;
            case 9: return Indigo;
            case 10: return Cyan;
            case 11: return Lime;
            case 12: return DeepOrange;
            case 13: return Brown;
            case 14: return Grey;
        }
        return White;
    }

    public String toString() {
        return String.valueOf(code);
    }
}
