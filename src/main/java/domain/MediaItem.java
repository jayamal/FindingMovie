package domain;

import javax.xml.bind.annotation.XmlTransient;
import java.beans.Transient;
import java.io.File;
import java.util.Map;

/**
 * Created by jayamalk on 9/21/2016.
 */
public class MediaItem {

    private Map<String, String> infoMap;
    private String imdbRating;
    private String metascore;
    private String imdbVotes;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String fileSize;
    private String language;
    private String fileLocation;
    @XmlTransient
    private File file;
    private String actors;
    private String director;
    private String writer;
    private String country;

    public MediaItem(Map<String, String> infoMap, File file){
        this.infoMap = infoMap;
        this.file   = file;
        this.fileLocation = file.getAbsolutePath();
        this.imdbRating  = infoMap.get("imdbRating");
        this.metascore   = infoMap.get("Metascore");
        this.imdbVotes   = infoMap.get("imdbVotes");
        this.title       = infoMap.get("Title");
        this.year        = infoMap.get("Year");
        this.rated       = infoMap.get("Rated");
        this.released    = infoMap.get("Released");
        this.runtime     = infoMap.get("Runtime");
        this.genre       = infoMap.get("Genre");
        this.fileSize    = infoMap.get("Size");
        this.language    = infoMap.get("Language");
        this.actors      = infoMap.get("Actors");
        this.director    = infoMap.get("Director");
        this.writer      = infoMap.get("Writer");
        this.country     = infoMap.get("Country");
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCountry() {
        return country;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
