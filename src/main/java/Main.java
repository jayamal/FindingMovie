import core.Finder;
import namecleaner.NameCleaner;
import uis.FindingMovieUI;
import utils.FileUtils;
import utils.RestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamal on 9/4/16.
 */
public class Main {

    public static void main(String[] args){
        final FindingMovieUI findingMovieUI = new FindingMovieUI();
        findingMovieUI.init();
    }
}
