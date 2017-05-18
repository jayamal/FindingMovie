## Finding Movie

It is difficult find a good movie when your friend give you gigabytes of his movie collection to you. Movie names are also usually given like **Test.Movie.2016.BRRip.XviD** and are difficult process and find the actual name. What Finding Movie does is it provides API to recursively search through your entire movie library and clean the names and find IMDB infor using that cleaned name. Finding Movie either can be use as an API or standalone appliation using provided UI.

## Using API
```java
final Finder finder = new Finder(new Finder.ProgressNotifier() {

    public void notifyProgress(File file, final Map<String, String> infoMap, final float progress, int successCount) {
        if(infoMap != null && infoMap.get("Response").equals("True")) {
            //use infoMap data here
			System.out.println(infoMap.get("imdbRating"));
        }
    }

    @Override
    public void notifyErrors(File file, int failedCount, String reason) {
        System.out.println(file.getName() + " : " + reason);
    }
});
```
## Installation

Execute bellow command to compile and package content in to executable JAR file.
```
mvn clean compile assembly:single
```
## License
GNU AGPLv3
