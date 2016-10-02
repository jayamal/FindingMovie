/*
 * Copyright - Copyright FindingMovie
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/5/16 8:07 AM
 * Last Modified Date: 9/4/16 7:43 PM
 * File: uis.FindingMovieUI
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

package uis;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import core.Finder;
import domain.IgnoredItem;
import domain.MediaItem;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import org.apache.commons.io.FilenameUtils;
import uis.ignored.IgnoredView;
import uis.results.ResultView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by jayamal on 9/4/16.
 */
public class FindingMovieUI extends JFrame {

    private ProgressArea progressArea;
    private JTabbedPane tabbedPane;
    private Footer footer;
    private JButton browseBtn;
    private JButton saveBtn;
    private JButton openBtn;
    private JButton exportBtn;
    private JButton aboutBtn;
    private ResultView resultView;
    private IgnoredView ignoredView;

    private Map<File, Map<String, String>> result;

    public static final Color BTN_ICON_CLR = new Color(209, 205, 205);
    public static final Color GOOD_CLR = new Color(218, 148, 50);
    public static final Color BAD_CLR = new Color(221, 99, 51);


    public void init(){
        setTitle("Finding Movie");
        // Register the IconFont
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        try {
            java.net.URL imgUrl = getClass().getResource("/finding-movie-icon.png");
            BufferedImage picture = ImageIO.read(imgUrl);
            ImageIcon icon = new ImageIcon(picture.getScaledInstance(36, 36, Image.SCALE_SMOOTH));
            setIconImage(icon.getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Main Tool Bar
        JToolBar toolBar = new JToolBar();
        browseBtn = new JButton("Search  ");
        browseBtn.setToolTipText("Browse and select directory containing movie files for search");
        browseBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEARCH, 24, BTN_ICON_CLR));
        toolBar.add(browseBtn);
        //Save
        saveBtn = new JButton("Save  ");
        saveBtn.setToolTipText("Save search result");
        saveBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SAVE, 24, BTN_ICON_CLR));
        toolBar.add(saveBtn);
        //Open
        openBtn = new JButton("Open  ");
        openBtn.setToolTipText("Open saved search results (.fm file)");
        openBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.OPEN_IN_BROWSER, 24, BTN_ICON_CLR));
        toolBar.add(openBtn);
        //export
        exportBtn = new JButton("Export  ");
        exportBtn.setToolTipText("Export movie list to file");
        exportBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PUBLISH, 24, BTN_ICON_CLR));
        toolBar.add(exportBtn);
        //about
        aboutBtn = new JButton("About  ");
        aboutBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.INFO, 24, BTN_ICON_CLR));
        toolBar.add(aboutBtn);
        //Footer
        progressArea = new ProgressArea();
        //open window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(toolBar, BorderLayout.PAGE_START);
        //result view
        resultView = new ResultView();
        //ignored view
        ignoredView = new IgnoredView();
        //tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Movie Results", IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK_BOX, 24, GOOD_CLR), resultView);
        tabbedPane.addTab("Ignored", IconFontSwing.buildIcon(GoogleMaterialDesignIcons.INDETERMINATE_CHECK_BOX, 24, BAD_CLR), ignoredView);
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(progressArea, BorderLayout.PAGE_START);
        progressArea.setVisible(Boolean.FALSE);
        mainArea.add(tabbedPane, BorderLayout.CENTER);
        footer = new Footer();
        mainArea.add(footer, BorderLayout.SOUTH);
        add(mainArea, BorderLayout.CENTER);
        pack();
        //set size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screenSize.width*0.85), (int) (screenSize.height*0.85));
        setLocationRelativeTo(null);
        setVisible(true);

        browseBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showDialog(FindingMovieUI.this, "Select Folder");
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File folder = fc.getSelectedFile();
                    startFinding(folder.getAbsolutePath(), resultView);
                    browseBtn.setEnabled(Boolean.FALSE);
                }
            }
        });

        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showSaveDialog(FindingMovieUI.this) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        // save to file
                        if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("csv")) {
                            file = new File(file.toString() + ".csv");
                        }
                        String csv = file.getAbsolutePath();
                        CSVWriter writer = new CSVWriter(new FileWriter(csv));
                        DefaultTableModel model = resultView.getModel();
                        int colCount = model.getColumnCount();
                        String[] header = new String[colCount];
                        for(int j=0; j < colCount; j++) {
                            //Create Header
                            if(j != 1) {
                                header[j] = model.getColumnName(j);
                            }
                        }
                        writer.writeNext(header);
                        for(int i=0; i< model.getRowCount(); i++) {
                            String[] record = new String[colCount];
                            int convertedIndex = resultView.getTable().convertRowIndexToModel(i);
                            for(int j=0; j < colCount; j++) {
                                //Create record
                                if(j != 1) {
                                    record[j] = model.getValueAt(convertedIndex, j).toString();
                                }
                            }
                            //Write the record to file
                            writer.writeNext(record);
                        }
                        //close the writer
                        writer.close();
                        Desktop.getDesktop().open(file);
                        JOptionPane.showMessageDialog(FindingMovieUI.this, "Exported : " + file.getAbsolutePath());
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(FindingMovieUI.this, "Error occurred while exporting");
                    ex.printStackTrace();
                }
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                String userDir = System.getProperty("user.home");
                fileChooser.setCurrentDirectory(new java.io.File(userDir));
                if (fileChooser.showSaveDialog(FindingMovieUI.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // save to file
                    if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("fm")) {
                        file = new File(file.toString() + ".fm");
                    }
                    java.util.List<MediaItem> mediaItemList = resultView.getAllItemsAsList();
                    try {
                        Writer writer = new FileWriter(file.getAbsolutePath());
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonStr = gson.toJson(mediaItemList);
                        try {
                            file.createNewFile();
                            FileOutputStream fOut = new FileOutputStream(file);
                            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                            myOutWriter.append(jsonStr);
                            myOutWriter.close();
                            fOut.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(FindingMovieUI.this, "Saved : " + file.getAbsolutePath());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(FindingMovieUI.this, "Error occurred while saving the file");
                        e1.printStackTrace();
                    }
                }

            }
        });

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                String userDir = System.getProperty("user.home");
                fc.setCurrentDirectory(new java.io.File(userDir)); // start at application current directory
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("SAVED SEARCHES", "fm", "text");
                fc.setFileFilter(filter);
                int returnVal = fc.showDialog(FindingMovieUI.this, "Select Saved Result");
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        JsonReader reader = new JsonReader(new FileReader(file.getAbsolutePath()));
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        java.util.List<MediaItem> mediaItemListTemp = gson.fromJson(reader, new TypeToken<List<MediaItem>>() {
                        }.getType());
                        resultView.clearResultView();
                        ignoredView.clearResultView();
                        if (mediaItemListTemp != null) {
                            for (MediaItem mediaItem : mediaItemListTemp) {
                                try {
                                    mediaItem.setFile(new File(mediaItem.getFileLocation()));
                                    resultView.addMediaItem(mediaItem);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(FindingMovieUI.this, "Error occurred while opening the file");
                        ex.printStackTrace();
                    }
                }

            }
        });

        aboutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog aboutDialog = new AboutDialog(FindingMovieUI.this, "About");
            }
        });

    }

    private void startFinding(final String filePath, final ResultView resultView){
        //find
        class FindThread extends SwingWorker {

            @Override
            protected Object doInBackground() throws Exception {
                resultView.clearResultView();
                ignoredView.clearResultView();
                progressArea.setVisible(Boolean.TRUE);
                progressArea.getCancelBtn().setEnabled(Boolean.TRUE);
                progressArea.getCancelBtn().removeAll();
                tabbedPane.setTitleAt(0, "Movie Results");
                final Finder finder = new Finder(new Finder.ProgressNotifier() {

                    public void notifyProgress(File file, final Map<String, String> infoMap, final float progress, int successCount) {
                        if(infoMap != null && infoMap.get("Response").equals("True")) {
                            resultView.addMediaItem(new MediaItem(infoMap, file));
                        }
                        progressArea.updateFooter("Processed : " + file.getAbsolutePath(), (int) progress);
                        tabbedPane.setTitleAt(0, "Movie Results [" + successCount + "]");
                    }

                    @Override
                    public void notifyErrors(File file, int failedCount, String reason) {
                        System.out.println(file.getName() + " : " + reason);
                        ignoredView.addIgnoredItem(new IgnoredItem(file, reason));
                        tabbedPane.setTitleAt(1, "Ignored [" + failedCount + "]");
                    }
                });
                progressArea.getCancelBtn().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finder.cancelFind();
                    }
                });
                result = finder.getMovieInfo(filePath);
                browseBtn.setEnabled(Boolean.TRUE);
                progressArea.getCancelBtn().setEnabled(Boolean.FALSE);
                progressArea.setVisible(Boolean.FALSE);
                return null;
            }

        }
        try {
            FindThread thread = new FindThread();
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
