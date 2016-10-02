package uis;

import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by jayamalk on 9/8/2016.
 */
public class Footer extends JPanel {

    private JLabel copyrightLabel;

    public Footer(){
        copyrightLabel = new JLabel("Copyright © 2016 by Jayamal Kulathunge");
        copyrightLabel.setForeground(new Color(147, 147, 147));
        add(copyrightLabel);
    }

}
