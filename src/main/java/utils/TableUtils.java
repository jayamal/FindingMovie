package utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created by jayamalk on 9/21/2016.
 */
public class TableUtils {

    public static void resizeColumns(JTable table){
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

    public static DefaultTableCellRenderer getDefaultCellRenderer(){
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
        return defaultTableCellRenderer;
    }
}
