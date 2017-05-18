package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by jayamalk on 10/4/2016.
 */
public class PreferenceUtil {

    private static final String DEFAULT_PROFILE = "FINDING_MOVIE_PREFERENCE";
    private static final Preferences prefs = Preferences.userRoot().node("FINDING_MOVIE_PREFERENCE");

    public enum PreferenceKey{

        RECENT_FILES("RECENT_FILES", "List"),
        RECENT_SEARCH_LOCATIONS("RECENT_SEARCH_LOCATIONS", "List"),
        LAST_OPENED_FILE_LOCATION("LAST_OPENED_FILE_LOCATION"),
        LAST_SAVED_LOCATION("LAST_SAVED_LOCATION"),
        LAST_SEARCHED_LOCATION("LAST_SEARCHED_LOCATION");

        private String key;
        private String type;

        PreferenceKey(String key) {
            this.key = key;
            this.type = "String";
        }

        PreferenceKey(String key, String type) {
            this.key = key;
            this.type = type;
        }

    }

    public static void updatePreference(PreferenceKey preferenceKey, String value){
        prefs.put(preferenceKey.key, value);
    }

    public static String getPreference(PreferenceKey preferenceKey, String defaultValue){
        return prefs.get(preferenceKey.key, defaultValue);
    }

    public static List<String> getPreferenceAsStrList(PreferenceKey preferenceKey){
        String listStr = prefs.get(preferenceKey.key, null);
        List<String> stringList = new ArrayList<String>();
        if(listStr != null) {
            Gson gson = new Gson();
            stringList = gson.fromJson(listStr, new TypeToken<List<String>>() {
            }.getType());
        }
        return stringList;
    }

    public static void addPreferenceListItem(PreferenceKey preferenceKey, String item){
        List<String> stringList = getPreferenceAsStrList(preferenceKey);
        if(stringList == null){
            stringList = new ArrayList<String>();
        }
        if(!stringList.contains(item)) {
            stringList.add(item);
        }
        Gson gson = new Gson();
        prefs.put(preferenceKey.key, gson.toJson(stringList));
    }
}
