package uis.ignored;

import domain.IgnoredItem;
import domain.MediaItem;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import uis.FindingMovieUI;
import uis.results.InfoView;
import utils.FileUtils;
import utils.TableUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jayamalk on 9/21/2016.
 */
public class IgnoredView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private String[] tableHeaders = new String[]{"Actions", "Parent Folder", "File", "Reason", "Size", "File Path"};

    public IgnoredView() {
        setLayout(new GridLayout(0, 1));
        //table
        model = new DefaultTableModel(tableHeaders, 0);
        table = new JTable(model)
        {
            //  Returning the Class of each column will allow different
            //  renderer to be used based on Class
            public Class getColumnClass(int column)
            {
                return String.class;
            }

            public boolean isCellEditable(int row, int column) {
                if(column == 0) {
                    return true;
                }else{
                    return false;
                }
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(String.class, defaultTableCellRenderer);
        //sorter
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowHeight(table.getRowHeight() + 15);
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        add(new JScrollPane(table));
    }

    public void clearResultView(){
        model.setRowCount(0);
    }

    public void addIgnoredItem(IgnoredItem ignoredItem){
        model.addRow(new Object[]{
                null,
                ignoredItem.getFile().getParentFile().getName(),
                ignoredItem.getFile().getName(),
                ignoredItem.getReason(),
                FileUtils.getFileSizeInMB(ignoredItem.getFile()) + " MB",
                ignoredItem.getFile().getAbsolutePath()
        });
        TableUtils.resizeColumns(table);
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

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText("Open");
            setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FOLDER_OPEN, 24, FindingMovieUI.BTN_ICON_CLR));
            return this;
        }
    }

    /**
     * @version 1.0 11/09/98
     */

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Open");
            button.setOpaque(true);
            button.setToolTipText("Open File Location");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int convertedIndex = IgnoredView.this.table.convertRowIndexToModel(row);
                        String path = (String) IgnoredView.this.table.getModel().getValueAt(convertedIndex, 5);
                        Desktop.getDesktop().open(new File(path).getParentFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, final int row, int column) {
            this.row  = row;
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = "Open";
            button.setText(label);
            button.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FOLDER_OPEN, 24, FindingMovieUI.BTN_ICON_CLR));

            return button;
        }

        public Object getCellEditorValue() {
            return new String(label);
        }

    }

}
