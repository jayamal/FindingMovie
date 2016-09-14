package uis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jayamalk on 9/13/2016.
 */
public class AboutDialog extends JDialog implements ActionListener {

    public AboutDialog(JFrame parent, String title) {
        super(parent, title, true);
        GridBagLayout content = new GridBagLayout();
        JPanel messagePane = new JPanel(content);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLbl = new JLabel("Finding Movie");
        titleLbl.setFont(new Font(titleLbl.getName(), Font.BOLD, 16));
        messagePane.add(titleLbl, gbc);
        gbc.gridy = 1;
        messagePane.add(new JLabel("Version : 2.0"), gbc);
        gbc.gridy = 2;
        messagePane.add(new JLabel("Creator : Jayamal Kulathunge"), gbc);
        gbc.gridy = 3;
        messagePane.add(new JLabel("Email : kulathunge@gmail.com"), gbc);
        //logo
        JLabel logoLbl = new JLabel();
        try {
            java.net.URL imgUrl = getClass().getResource("/finding-movie-icon.png");
            BufferedImage picture = ImageIO.read(imgUrl);
            ImageIcon icon = new ImageIcon(picture.getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            logoLbl.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        messagePane.add(logoLbl, gbc);
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}