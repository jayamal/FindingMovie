package core;

import com.google.gson.reflect.TypeToken;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.enumeration.SearchType;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import core.tmdb.Result;
import core.tmdb.ResultSet;
import utils.RestUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamalk on 5/23/2017.
 */
public class TMDBImpl implements MovieDataAPI {
    @Override
    public Map<String, String> fetchMovieInfo(String cleanedName, Integer cleanedYear) {
        Map<String, String> infoMap = new HashMap<String, String>();
        if (cleanedName != null && !cleanedName.isEmpty()) {
            String response = RestUtils.makeRestCall("http://api.themoviedb.org/3/search/movie?api_key=cb2b503835b95a280b447ce38ff33694&query="
                    + cleanedName.replace(" ", "+") + (cleanedYear != null ? "&year=" + cleanedYear : ""));

            Type type = new TypeToken<ResultSet>(){}.getType();
            ResultSet resultSet = (ResultSet) RestUtils.convertResponseToObject(response, type);
            List<Result> results = resultSet.getResults();
            if(results != null && !results.isEmpty()){
                for(Result result : results){
                    infoMap.put("imdbRating", "12");
                    infoMap.put("Metascore",  "12");
                    infoMap.put("imdbVotes", String.valueOf(result.getVoteCount()));
                    infoMap.put("Title",  result.getTitle());
                    infoMap.put("Year", "2014");
                    infoMap.put("Rated", "12");
                    infoMap.put("Released", String.valueOf(result.getReleaseDate()));
                    infoMap.put("Runtime", "12");
                    infoMap.put("Genre", "12");
                    infoMap.put("Size", "12");
                    infoMap.put("Language", "12");
                    infoMap.put("Actors", "12");
                    infoMap.put("Director", "12");
                    infoMap.put("Writer", "12");
                    infoMap.put("Country", "12");
                    infoMap.put("Poster", "http://ia.media-imdb.com/images/M/" + String.valueOf(result.getPosterPath()));
                    infoMap.put("Response", "True");
                    break;
                }
            }
        }else {
            infoMap.put("Response", "False");
            infoMap.put("Reason", "Couldn't extract movie name from file");
        }
        return infoMap;
    }
}
