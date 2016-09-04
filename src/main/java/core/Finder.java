package core;

import namecleaner.NameCleaner;
import utils.FileUtils;
import utils.RestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamal on 9/4/16.
 */
public class Finder {

    private ProgressNotifier progressNotifier;

    public interface ProgressNotifier{
        public void notifyProgress(File file, Map<String, String> infoMap, float progress);
    }

    public Finder(ProgressNotifier progressNotifier) {
        this.progressNotifier = progressNotifier;
    }

    public Map<File, Map<String, String>> getMovieInfo(String baseDir){
        Map<File, Map<String, String>> movieInfoMap = new HashMap<File, Map<String, String>>();
        //list all files
        File[] files = new File(baseDir).listFiles();
        List<File> collectedMediaFiles = new ArrayList<File>();
        int index = 1;
        FileUtils.collectMediaFiles(files, collectedMediaFiles);
        int size = collectedMediaFiles.size();
        for(File mediaFile : collectedMediaFiles){
            String fileName = mediaFile.getName();
            String cleanedName = NameCleaner.extractName(fileName, Boolean.FALSE);
            Integer cleanedYear = NameCleaner.extractYear(fileName);
            if(cleanedName != null) {
                String response = RestUtils.makeRestCall("http://www.omdbapi.com/?t="
                        + cleanedName.replace(" ", "+") + (cleanedYear != null ? "&y=" + cleanedYear : "") + "&plot=short&r=json");
                Map<String, String> infoMap = RestUtils.convertResponseToMap(response);
                movieInfoMap.put(mediaFile, infoMap);
                if (this.progressNotifier != null) {
                    this.progressNotifier.notifyProgress(mediaFile, infoMap, ((float) index / size) * 100);
                }
            }
            index++;
        }
        return movieInfoMap;
    }

}
