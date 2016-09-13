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
import core.Finder;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamal on 9/4/16.
 */
public class FindingMovieUI extends JFrame {

    private DefaultTableModel model;
    private Footer footer;
    private JButton browseBtn;
    private JButton exportBtn;
    private Map<File, Map<String, String>> result;
    private String[] movieListHeaders = new String[]{"IMDB Rating","Metascore" ,"IMDB Votes" , "Title", "Year", "Rated", "Released", "Runtime", "Genre", "Location", "Result", "File"};
    private InfoView infoView;
    public static final Color BTN_ICON_CLR = new Color(209, 205, 205);


    public void init(){
        setTitle("Finding Movie");
        // Register the IconFont
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        setIconImage(IconFontSwing.buildImage(GoogleMaterialDesignIcons.FAVORITE, 16, BTN_ICON_CLR));
        //table
        model = new DefaultTableModel(movieListHeaders, 0);

        final JTable table = new JTable(model)
        {
            //  Returning the Class of each column will allow different
            //  renderer to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(String.class, defaultTableCellRenderer);
        //sorter
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowHeight(table.getRowHeight() + 15);
        table.removeColumn(table.getColumnModel().getColumn(10));
        table.removeColumn(table.getColumnModel().getColumn(10));
        //Main Tool Bar
        JToolBar toolBar = new JToolBar();
        browseBtn = new JButton("Browse  ");
        browseBtn.setToolTipText("Browse and select directory containing movie files");
        browseBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FOLDER_OPEN, 24, BTN_ICON_CLR));
        toolBar.add(browseBtn);
        //export
        exportBtn = new JButton("Export  ");
        exportBtn.setToolTipText("Export movie list to file");
        exportBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PUBLISH, 24, BTN_ICON_CLR));
        toolBar.add(exportBtn);
        //Footer
        footer = new Footer();
        //open window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(toolBar, BorderLayout.PAGE_START);
        add(footer, BorderLayout.SOUTH);
        //split pane
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setLeftComponent(new JScrollPane(table));
        //view info
        infoView = new InfoView();
        jSplitPane.setRightComponent(infoView);
        jSplitPane.setDividerLocation(720);
        add(jSplitPane, BorderLayout.CENTER);
        pack();
        setSize(1200, 768);
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
                    startFinding(folder.getAbsolutePath(), table);
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
                        int colCount = model.getColumnCount();
                        String[] header = new String[colCount];
                        for(int j=0; j < colCount; j++) {
                            //Create Header
                            header[j] = model.getColumnName(j);
                        }
                        writer.writeNext(header);
                        for(int i=0; i< model.getRowCount(); i++) {
                            String[] record = new String[colCount];
                            int convertedIndex = table.convertRowIndexToModel(i);
                            for(int j=0; j < colCount; j++) {
                                //Create record
                                record[j] = model.getValueAt(convertedIndex, j).toString();
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

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int column = 10;
                int fcolumn = 11;
                int row = table.getSelectedRow();
                if(row >= 0) {
                    int convertedIndex = table.convertRowIndexToModel(row);
                    Map<String, String> infoMapCurrent = (Map<String, String>) table.getModel().getValueAt(convertedIndex, column);
                    File currentFile = (File) table.getModel().getValueAt(convertedIndex, fcolumn);
                    infoView.updateContent(infoMapCurrent, currentFile);
                }
            }
        });
    }

    private void startFinding(final String filePath, final JTable table){
        //find
        class FindThread extends SwingWorker {

            @Override
            protected Object doInBackground() throws Exception {
                getModel().setRowCount(0);
                footer.getCancelBtn().setEnabled(Boolean.TRUE);
                footer.getCancelBtn().removeAll();
                footer.updateSuccessStatus(0);
                footer.updateFailedStatus(0);
                final Finder finder = new Finder(new Finder.ProgressNotifier() {

                    public void notifyProgress(File file, final Map<String, String> infoMap, final float progress, int successCount) {
                        if(infoMap != null && infoMap.get("Response").equals("True")) {
                            model.addRow(new Object[]{
                                    infoMap.get("imdbRating"),
                                    infoMap.get("Metascore"),
                                    infoMap.get("imdbVotes"),
                                    infoMap.get("Title"),
                                    infoMap.get("Year"),
                                    infoMap.get("Rated"),
                                    infoMap.get("Released"),
                                    infoMap.get("Runtime"),
                                    infoMap.get("Genre"),
                                    file.getAbsolutePath(),
                                    infoMap,
                                    file
                            });
                            resizeColumns(table);
                        }
                        footer.updateFooter("Processed : " + file.getAbsolutePath(), (int) progress);
                        footer.updateSuccessStatus(successCount);
                    }

                    @Override
                    public void notifyErrors(File file, int failedCount) {
                        footer.updateFailedStatus(failedCount);
                    }
                });
                footer.getCancelBtn().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finder.cancelFind();
                    }
                });
                result = finder.getMovieInfo(filePath);
                browseBtn.setEnabled(Boolean.TRUE);
                footer.getCancelBtn().setEnabled(Boolean.FALSE);
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

    private void resizeColumns(JTable table){
        for (int column = 0; column < table.getColumnCount(); column++)
        {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getHeaderValue().toString().length() * 6;
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++)
            {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth)
                {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth( preferredWidth);
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }

    DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() {

        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
            return this;
        }

    };


}
