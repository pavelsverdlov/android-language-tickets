package tickets.language.svp.languagetickets.domain;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/11/2015.
 */
public class CollectionHelper {
    public static  <T> void AddRange(ArrayList<T> to, T[] from){
        for (int i =0;i < from.length; ++i){
            to.add(from[i]);
        }
    }
}
