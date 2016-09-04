Finding Movie - Version 1.0

Making Executable

Execute bellow command to compile and package content in to exetable JAR file.

mvn clean compile assembly:single

Using JAR File as a Library

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
