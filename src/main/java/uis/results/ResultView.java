package uis.results;

import domain.MediaItem;
import utils.TableUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamalk on 9/21/2016.
 */
public class ResultView extends JPanel {

    private InfoView infoView;
    private JTable table;
    private DefaultTableModel model;
    private String[] movieListHeaders = new String[]{
            "Ref",
            "Result",
            "File",
            "IMDB Rating",
            "Metascore" ,
            "IMDB Votes" ,
            "Title",
            "Year",
            "Rated",
            "Released",
            "Runtime",
            "Size",
            "Genre",
            "Language",
            "Actors",
            "Director",
            "Writer",
            "Country",
            "Location"
    };

    public ResultView() {
        setLayout(new GridLayout(0, 1));
        //table
        model = new DefaultTableModel(movieListHeaders, 0);
        table = new JTable(model)
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
        table.setDefaultRenderer(String.class, TableUtils.getDefaultCellRenderer());
        //sorter
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(25);
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowHeight(table.getRowHeight() + 15);
        table.removeColumn(table.getColumnModel().getColumn(0));
        table.removeColumn(table.getColumnModel().getColumn(0));
        table.removeColumn(table.getColumnModel().getColumn(0));
        //split pane
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setLeftComponent(new JScrollPane(table));
        //view info
        infoView = new InfoView();
        jSplitPane.setRightComponent(infoView);
        jSplitPane.setResizeWeight(1);
        add(jSplitPane);
        //listeners
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int column = 1;
                int fcolumn = 2;
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

    public void clearResultView(){
        model.setRowCount(0);
    }

    public void addMediaItem(MediaItem mediaItem){
        model.addRow(new Object[]{
                mediaItem,
                mediaItem.getInfoMap(),
                mediaItem.getFile(),
                mediaItem.getImdbRating(),
                mediaItem.getMetascore(),
                mediaItem.getImdbVotes(),
                mediaItem.getTitle(),
                mediaItem.getYear(),
                mediaItem.getRated(),
                mediaItem.getReleased(),
                mediaItem.getRuntime(),
                mediaItem.getFileSize(),
                mediaItem.getGenre(),
                mediaItem.getLanguage(),
                mediaItem.getActors(),
                mediaItem.getDirector(),
                mediaItem.getWriter(),
                mediaItem.getCountry(),
                mediaItem.getFile().getAbsolutePath()
        });
        TableUtils.resizeColumns(table);
    }

    public List<MediaItem> getAllItemsAsList(){
        List<MediaItem> mediaItems = new ArrayList<MediaItem>();
        for(int i=0; i< model.getRowCount(); i++) {
            int convertedIndex = getTable().convertRowIndexToModel(i);
            MediaItem mediaItem = (MediaItem) model.getValueAt(convertedIndex, 0);
            mediaItems.add(mediaItem);
        }
        return mediaItems;
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

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
