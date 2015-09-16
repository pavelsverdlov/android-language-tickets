package tickets.language.svp.languagetickets.ui;

/**
 * Created by Pasha on 1/18/2015.
 */
public enum ActivityOperationResult {
    Undefined(0),
    Root(1),
    EditTicket(2),
    AddNewTicket(3),
    GameLearning(4),
    AppSettings(5),
    GoogleDrive(6),
    Crash(7);


    private final int code;

    private ActivityOperationResult(int code) {
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public String toString() {
        return String.valueOf(code);
    }
}
