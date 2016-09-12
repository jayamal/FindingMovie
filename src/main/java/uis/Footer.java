package uis;

import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jayamalk on 9/8/2016.
 */
public class Footer extends JPanel {

    private DefaultBoundedRangeModel progressBarModel;
    private JButton cancelBtn;
    private JLabel progressLbl;
    private JProgressBar progressBar;

    public Footer(){
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        progressBarModel = new DefaultBoundedRangeModel();
        progressBar = new JProgressBar(progressBarModel);
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
        cancelBtn = new JButton();
        cancelBtn.setEnabled(Boolean.FALSE);
        progressLbl = new JLabel("0 %");
        add(progressBar, c1);
        add(progressLbl, c);
        add(cancelBtn, c);
        cancelBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 16, new Color(231, 76, 60)));
    }

    public void updateFooter(String progressMsg, int progress){
        progressBarModel.setValue(progress);
        progressLbl.setText(String.valueOf((int)progress) + " %");
        progressBar.setToolTipText(progressMsg);
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

}
