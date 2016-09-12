/*
 * Copyright - Copyright FindingMovie
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/10/16 7:13 AM
 * Last Modified Date: 9/10/16 7:13 AM
 * File: uis.InfoView
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

import utils.ImageLoader;
import utils.RestUtils;
import utils.SpringUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Created by jayamal on 9/10/16.
 */
public class InfoView extends JPanel implements ImageLoader.ImageConsumer{

    private InfoTable infoTable;
    private JLabel imageLbl;
    private JButton openBtn;
    private JButton imdbBtn;
    private JLabel movieTitleLbl;
    private JLabel movieRatingLbl;
    private JLabel movieYearRuntimeLbl;
    private JLabel movieGenreLbl;
    private Map<String, String> infoMapCurrent;
    private File currentFile;
    private ImageLoader imageLoader;

    public InfoView(){
        setLayout(new BorderLayout());
        DefaultTableModel viewInfoTableModel = new DefaultTableModel(new String[]{"", ""}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            public void setValueAt(Object value, int row, int col) {
                super.setValueAt(value, row, col);
                fireTableCellUpdated(row, col);
            }
        };;
        infoTable = new InfoTable(viewInfoTableModel);
        JScrollPane viewInfoHolder = new JScrollPane(infoTable);
        imageLbl = new JLabel("");
        //highlighted info
        JPanel highInfoPanel = new JPanel(new BorderLayout());
        highInfoPanel.add(imageLbl, BorderLayout.WEST);

        // Define the panel to hold the components
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        movieTitleLbl           = new JLabel("Title (Year)");
        movieTitleLbl.setForeground(Color.white);
        movieTitleLbl.setFont(new Font(movieTitleLbl.getName(), Font.BOLD, 16));
        movieRatingLbl          = new JLabel("Rating / 10 (Votes)");
        movieRatingLbl.setForeground(Color.white);
        movieYearRuntimeLbl     = new JLabel("Release Date | Runtime");
        movieYearRuntimeLbl.setForeground(Color.white);
        movieGenreLbl           = new JLabel("Genre");
        movieGenreLbl.setForeground(Color.white);
        openBtn                 = new JButton("Open");
        imdbBtn                 = new JButton("Open in IMDB");
        // Put constraints on different buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(movieTitleLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(movieRatingLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(movieYearRuntimeLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(movieGenreLbl, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(5,0,5,0);
        panel.add(imdbBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(openBtn, gbc);

        highInfoPanel.add(panel, BorderLayout.CENTER);
        add(highInfoPanel, BorderLayout.NORTH);
        add(viewInfoHolder, BorderLayout.CENTER);

        openBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(infoMapCurrent.get("Location")));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        imdbBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String url = "http://www.imdb.com/title/" + infoMapCurrent.get("imdbID");
                    try {
                        URI originalRequestPath = new URI(url);
                        Desktop.getDesktop().browse(originalRequestPath);
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void updateContent(Map<String, String> infoMapCurrent){
        if(imageLoader != null){
            imageLoader.cancel(Boolean.TRUE);
        }
        this.infoMapCurrent = infoMapCurrent;
        infoTable.getModel().setRowCount(0);
        for(Map.Entry<String, String> entry : infoMapCurrent.entrySet()){
            if(!entry.getKey().equals("Poster")) {
                infoTable.getModel().addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        }
        movieTitleLbl.setText(infoMapCurrent.get("Title") + " (" + infoMapCurrent.get("Year") + ")");
        movieRatingLbl.setText(infoMapCurrent.get("imdbRating") + "/10" + " (" + infoMapCurrent.get("imdbVotes") + ")");
        movieYearRuntimeLbl.setText(infoMapCurrent.get("Released") + " | " + infoMapCurrent.get("Runtime"));
        movieGenreLbl.setText(infoMapCurrent.get("Genre"));
        imageLbl.setIcon(null);
        imageLoader = new ImageLoader(this, infoMapCurrent.get("Poster"));
        imageLoader.execute();
    }

    @Override
    public void imageLoaded(Image image, String url) {
        if(image != null && url.equals(infoMapCurrent.get("Poster"))) {
            try {
                ImageIcon imageIcon = null;
                imageIcon = new ImageIcon(image.getScaledInstance(150, 222, Image.SCALE_SMOOTH));
                imageLbl.setIcon(imageIcon);
            } catch (Exception e) {
                System.out.println("Error fetching image");
            }
        }
    }
}
