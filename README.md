## Finding Movie

It is difficult find a good movie when your friend give you gigabytes of his movie collection to you. Movie names are also usually given like **Money.Monster.2016.BRRip.XviD-ETRG** and are difficult process and find the actual name. What Finding Movie does is it provides API to recursively search through your entire movie library and clean the names and find IMDB infor using that cleaned name. Finding Movie either can be use as a API or standalone appliation using provided UI.

## Using API
```java
final FindingMovieUI findingMovieUI = new FindingMovieUI();
findingMovieUI.init();
//find
Finder finder = new Finder(new Finder.ProgressNotifier() {
  public void notifyProgress(File file, Map<String, String> infoMap, float progress) {
      System.out.println("Progress : " + progress + " : " + infoMap.get("Title") + " : " + infoMap.get("imdbRating"));
      if(infoMap.get("Response").equals("True")) {
          findingMovieUI.getModel().addRow(new Object[]{
                  infoMap.get("imdbRating"),
                  //RestUtils.getImageIcon(infoMap.get("Poster")),
                  infoMap.get("Title"),
                  infoMap.get("Year"),
                  infoMap.get("Rated"),
                  infoMap.get("Released"),
                  infoMap.get("Runtime"),
                  infoMap.get("Genre")
          });
      }
      findingMovieUI.getProgressBar().setValue((int)progress);
  }
});
Map<File, Map<String, String>> result = finder.getMovieInfo("/Volumes/JayamalHD/Films/");
for(Map.Entry<File, Map<String, String>> movieEntry : result.entrySet()){
      System.out.println(movieEntry.getKey().getName() + " : " + movieEntry.getValue().get("Title") + " : " + movieEntry.getValue().get("imdbRating"));
}
```
## Installation

Execute bellow command to compile and package content in to exetable JAR file.
```
mvn clean compile assembly:single
```
## License
