/*
 * Copyright - Copyright FindingMovie
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/5/16 8:07 AM
 * Last Modified Date: 9/4/16 7:30 PM
 * File: core.Finder
 *
 * This file is part of FindingMovie.
 *
 * FindingMovie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FindingMovie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    private boolean stopFlag;

    public interface ProgressNotifier{

        public void notifyProgress(File file, Map<String, String> infoMap, float progress, int successCount);

        public void notifyErrors(File file, int failedCount);
    }

    public Finder(ProgressNotifier progressNotifier) {
        this.progressNotifier = progressNotifier;
    }

    public Map<File, Map<String, String>> getMovieInfo(String baseDir){
        stopFlag = Boolean.FALSE;
        Map<File, Map<String, String>> movieInfoMap = new HashMap<File, Map<String, String>>();
        //list all files
        File[] files = new File(baseDir).listFiles();
        List<File> collectedMediaFiles = new ArrayList<File>();
        int index = 1;
        FileUtils.collectMediaFiles(files, collectedMediaFiles);
        int size = collectedMediaFiles.size();
        int successCount = 0;
        int failedCount = 0;
        for(File mediaFile : collectedMediaFiles){
            if(!stopFlag) {
                String fileName = mediaFile.getName();
                String cleanedName = NameCleaner.extractName(fileName, Boolean.FALSE);
                Integer cleanedYear = NameCleaner.extractYear(fileName);
                Map<String, String> infoMap = null;
                if (cleanedName != null) {
                    String response = RestUtils.makeRestCall("http://www.omdbapi.com/?t="
                            + cleanedName.replace(" ", "+") + (cleanedYear != null ? "&y=" + cleanedYear : "") + "&plot=short&r=json");
                    infoMap = RestUtils.convertResponseToMap(response);
                    infoMap.put("Location", mediaFile.getAbsolutePath());
                    movieInfoMap.put(mediaFile, infoMap);
                    if (this.progressNotifier != null) {
                        if(!infoMap.get("Response").equals("True")) {
                            failedCount++;
                            this.progressNotifier.notifyErrors(mediaFile, failedCount);
                        }else {
                            successCount++;
                        }
                    }
                }else {
                    if (this.progressNotifier != null) {
                        failedCount++;
                        this.progressNotifier.notifyErrors(mediaFile, failedCount);
                    }
                }
                this.progressNotifier.notifyProgress(mediaFile, infoMap, ((float) index / size) * 100, successCount);
                index++;
            }
        }
        return movieInfoMap;
    }

    public void cancelFind(){
        stopFlag = Boolean.TRUE;
    }

}
