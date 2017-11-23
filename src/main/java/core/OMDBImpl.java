package core;

import core.omdb.Movie;
import core.tmdb.ResultSet;
import utils.RestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jayamalk on 5/23/2017.
 */
public class OMDBImpl implements MovieDataAPI {
    @Override
    public Map<String, String> fetchMovieInfo(String cleanedName, Integer cleanedYear) {
        Map<String, String> infoMap = new HashMap<String, String>();
        if (cleanedName != null && !cleanedName.isEmpty()) {
            String response = RestUtils.makeRestCall("http://www.omdbapi.com/?t="
                    + cleanedName.replace(" ", "+") + (cleanedYear != null ? "&y=" + cleanedYear : "") + "&plot=short&r=json&apikey=" + System.getenv("fm.api_key"));
            Movie movie = (Movie) RestUtils.convertResponseToObject(response, Movie.class);
            infoMap.put("Title",movie.getTitle());
            infoMap.put("Year",movie.getYear());
            infoMap.put("Rated",movie.getRated());
            infoMap.put("Released",movie.getReleased());
            infoMap.put("Runtime", movie.getRuntime());
            infoMap.put("Genre", movie.getGenre());
            infoMap.put("Director", movie.getDirector());
            infoMap.put("Writer", movie.getWriter());
            infoMap.put("Actors", movie.getActors());
            infoMap.put("Plot", movie.getPlot());
            infoMap.put("Language", movie.getLanguage());
            infoMap.put("Country", movie.getCountry());
            infoMap.put("Awards", movie.getAwards());
            infoMap.put("Poster", movie.getPoster());
            infoMap.put("Metascore", movie.getMetascore());
            infoMap.put("imdbRating", movie.getImdbRating());
            infoMap.put("imdbVotes", movie.getImdbVotes());
            infoMap.put("imdbID", movie.getImdbID());
            infoMap.put("Type", movie.getType());
            infoMap.put("DVD", movie.getDVD());
            infoMap.put("BoxOffice", movie.getBoxOffice());
            infoMap.put("Production", movie.getProduction());
            infoMap.put("Website", movie.getWebsite());
            infoMap.put("Response", "True");
        }else {
            infoMap.put("Response", "False");
            infoMap.put("Reason", "Couldn't extract movie name from file");
        }
        return infoMap;
    }
}
