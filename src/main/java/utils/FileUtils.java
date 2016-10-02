/*
 * Copyright - Copyright FindingMovie
 * Original Work - Copyright (C) 2012 Nicolas Magré
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/5/16 8:07 AM
 * Last Modified Date: 9/4/16 1:22 PM
 * File: utils.FileUtils
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
package utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import namecleaner.NameCleaner;
import org.w3c.dom.Document;

import java.io.FilenameFilter;

/**
 * Class FileUtils
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 * @author Jayamal Kulathunge
 */
public final class FileUtils {

  /**
   * Pattern used for matching file extensions.
   */
  private static final Pattern EXTENSION = Pattern.compile("(?<=.[.])\\p{Alnum}+$");

  public static String getExtension(File file) {
    if (file.isDirectory()) {
      return null;
    }

    return getExtension(file.getName());
  }

  public static String getExtension(String name) {
    Matcher matcher = EXTENSION.matcher(name);

    if (matcher.find()) {
      // extension without '.'
      return matcher.group();
    }

    // no extension
    return null;
  }

  public static boolean hasExtension(File file, String... extensions) {
    return hasExtension(file.getName(), extensions) && !file.isDirectory();
  }

  public static boolean hasExtension(String filename, String... extensions) {
    String extension = getExtension(filename);

    if (extensions == null || extensions.length == 0) {
      return true;
    }

    for (String value : extensions) {
      if ((extension == null && value == null) || (extension != null && extension.equalsIgnoreCase(value))) {
        return true;
      }
    }

    return false;
  }

  public static String getNameWithoutExtension(String name) {
    Matcher matcher = EXTENSION.matcher(name);

    if (matcher.find()) {
      return name.substring(0, matcher.start() - 1);
    }

    // no extension, return given name
    return name;
  }

  public static String getName(File file) {
    if (file.getName().isEmpty()) {
      return getFolderName(file);
    }

    return getNameWithoutExtension(file.getName());
  }

  public static String getFolderName(File file) {
    String name = file.getName();

    if (!name.isEmpty()) {
      return name;
    }

    return replacePathSeparators(file.toString(), "");
  }

  private static String replacePathSeparators(CharSequence path, String replacement) {
    return Pattern.compile("\\s*[\\\\/]+\\s*").matcher(path).replaceAll(replacement);
  }

  /**
   * Check if file have a good extension
   *
   * @param file
   * @return True if file extension is in array
   */
  public static boolean checkFileExt(File file) {
    return checkFileExt(file, NameCleaner.getCleanerProperty("file.extension").split("\\|"));// FIXME use setting
  }

  /**
   * Check if file have a good extension
   *
   * @param file
   * @param extensions Array of extensions
   * @return True if file extension is in array
   */
  public static boolean checkFileExt(File file, String[] extensions) {
    if (file.isHidden()) {
      return false;
    }
    return checkFileExt(file.getName(), extensions);
  }

  /**
   * Check if file have a good extension
   *
   * @param fileName File to check extension
   * @param extensions Array of extensions
   * @return True if file extension is in array
   */
  public static boolean checkFileExt(String fileName, String[] extensions) {
    if (extensions == null | extensions.length == 0) {
      return false;
    }

    if (!fileName.contains(StringUtils.DOT)) {
      return false;
    }

    String ext = fileName.substring(fileName.lastIndexOf(StringUtils.DOT) + 1);
    for (int i = 0; i < extensions.length; i++) {
      if (ext.equalsIgnoreCase(extensions[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if dir is a root directory
   *
   * @param dir Directory
   * @return True if it is a directory
   */
  public static boolean isRootDir(File dir) {
    if (!dir.isDirectory()) {
      return false;
    }

    File[] roots = File.listRoots();
    for (File root : roots) {
      if (root.equals(dir)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method writes a DOM document to a file
   */
  public static void writeXmlFile(Document doc, File file) {
    try {
      // Prepare the DOM document for writing
      Source source = new DOMSource(doc);

      // Prepare the output file
      Result result = new StreamResult(file.toURI().getPath());

      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, result);
    } catch (TransformerConfigurationException e) {
    } catch (TransformerException e) {
    }
  }

  public static File move(File currentFile, String dest) {
    try {
      File destFile = new File(currentFile.getParentFile(), dest);
      if (destFile.toURI().getScheme().equals("file")) {
        if (currentFile.renameTo(destFile)) {
          return destFile;
        }
      }
    } catch (Exception ex) {
    }
    return currentFile;
  }

  public static class FolderFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
      return new File(dir.getAbsolutePath() + File.separator + name).isDirectory();
    }
  }

  public static class FileAndFolderFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
      if (file.isHidden()) {
        return false;
      }

      if (file.isFile() && !file.getName().contains(StringUtils.DOT)) {
        return false;
      }

      return true;
    }
  }

  public static class ExtensionFileFilter implements FileFilter {

    private final String[] extensions;
    private final boolean acceptDir;

    public ExtensionFileFilter(String... extensions) {
      this(false, extensions);
    }

    public ExtensionFileFilter(boolean acceptDir, String... extensions) {
      this.extensions = extensions;
      this.acceptDir = acceptDir;
    }

    public ExtensionFileFilter(Collection<String> extensions) {
      this(false, extensions);
    }

    public ExtensionFileFilter(boolean acceptDir, Collection<String> extensions) {
      this.extensions = extensions.toArray(new String[0]);
      this.acceptDir = acceptDir;
    }

    @Override
    public boolean accept(File file) {
      return (acceptDir && file.isDirectory()) || checkFileExt(file, extensions);
    }

    public boolean acceptExtension(String extension) {
      for (String other : extensions) {
        if (other.equalsIgnoreCase(extension)) {
          return true;
        }
      }

      return false;
    }

    public String extension() {
      return extensions[0];
    }

    public String[] extensions() {
      return extensions.clone();
    }
  }

  public static void collectMediaFiles(File[] files, List<File> collectedMediaFiles) {
    for (File file : files) {
      if (file.isDirectory() && !file.isHidden()) {
        System.out.println("Directory: " + file.getName());
        collectMediaFiles(file.listFiles(), collectedMediaFiles); // Calls same method again.
      } else if(!file.isHidden() && checkFileExt(file)){
          collectedMediaFiles.add(file);
      }
    }
  }

  private FileUtils() {
    throw new UnsupportedOperationException();
  }

  public static double getFileSizeInMB(File file){
    return file.length()/(1024*1024);
  }

}
