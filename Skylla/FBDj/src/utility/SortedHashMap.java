package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SortedHashMap {

    public static ArrayList<String> getSortedStringKeys(@SuppressWarnings("rawtypes") HashMap hmap) {

        @SuppressWarnings("unchecked")
        ArrayList<String> mapKeys = new ArrayList<String>(hmap.keySet());
        // copy arraylist and sort descending
        Collections.sort(mapKeys);

        return mapKeys;
    }

}
