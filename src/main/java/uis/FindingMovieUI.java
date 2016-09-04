package uis;

import core.Finder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jayamal on 9/4/16.
 */
public class FindingMovieUI extends JFrame {

    private DefaultTableModel model;
    private DefaultBoundedRangeModel progressBarModel;
    private JButton browseBtn;
    private JLabel progressLbl;

    public void init(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());;
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                   // break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        //table
        model = new DefaultTableModel(new String[]{"IMDB Rating" , "Title", "Year", "Rated", "Released", "Runtime", "Genre"}, 0);

        JTable table = new JTable(model)
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowHeight(table.getRowHeight() + 15);

        //open window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gb = new GridBagLayout();
        JPanel topPanel = new JPanel(gb);
        progressBarModel = new DefaultBoundedRangeModel();
        final JProgressBar progressBar = new JProgressBar(progressBarModel);
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 0;
        c.insets = new Insets(3,3,3,3);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.weighty = 1;
        c1.weightx = 1;
        c1.fill = 1;
        c1.insets = new Insets(3,3,3,3);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.weighty = 1;
        c2.weightx = 0;
        c2.insets = new Insets(3,3,3,10);
        browseBtn = new JButton("Browse");
        progressLbl = new JLabel("0 %");
        topPanel.add(browseBtn, c);
        topPanel.add(progressBar, c1);
        topPanel.add(progressLbl, c2);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        pack();
        setSize(1024, 600);
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
                    browseBtn.setToolTipText(folder.getAbsolutePath());
                    progressBar.setToolTipText(folder.getAbsolutePath());
                    browseBtn.setEnabled(Boolean.FALSE);
                    startFinding(folder.getAbsolutePath());
                }
            }
        });
    }

    private void startFinding(final String filePath){
        //find
        class FindThread extends SwingWorker {
            @Override
            protected Object doInBackground() throws Exception {
                Finder finder = new Finder(new Finder.ProgressNotifier() {
                    public void notifyProgress(File file, Map<String, String> infoMap, final float progress) {
                        System.out.println("Progress : " + progress + " : " + infoMap.get("Title") + " : " + infoMap.get("imdbRating"));
                        if(infoMap.get("Response").equals("True")) {
                            model.addRow(new Object[]{
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
                        progressBarModel.setValue((int)progress);
                        progressLbl.setText(String.valueOf((int)progress) + " %");
                    }
                });
                Map<File, Map<String, String>> result = finder.getMovieInfo(filePath);
                browseBtn.setEnabled(Boolean.TRUE);
                return null;
            }

        }
        FindThread thread = new FindThread();
        try {
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }

}
