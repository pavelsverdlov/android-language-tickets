package tickets.language.svp.languagetickets.domain.db;

/**
 * Created by Pasha on 8/23/2015.
 */
public enum DatabaseStorage {
    Application(0),
    LocalStore(1);
    private final int code;

    private DatabaseStorage(int code) {
        this.code = code;
    }
    public int toInt() {
        return code;
    }
    public String toString() {
        return String.valueOf(code);
    }
}
