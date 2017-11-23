package core;

import java.util.Map;

/**
 * Created by jayamalk on 5/23/2017.
 */
public interface MovieDataAPI {

    public Map<String, String> fetchMovieInfo(String cleanedName, Integer cleanedYear);

}
