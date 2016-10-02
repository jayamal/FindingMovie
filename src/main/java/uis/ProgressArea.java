package uis;

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
public class ProgressArea extends JPanel {

    private DefaultBoundedRangeModel progressBarModel;
    private JButton cancelBtn;
    private JLabel progressLbl;
    private JProgressBar progressBar;
    private JLabel statusMsg;
    private Color PROGRESS_BG_CLR = new Color(55, 55, 55);

    public ProgressArea(){
        GridBagLayout gb = new GridBagLayout();
        JPanel progressContainer = new JPanel(gb);
        progressBarModel = new DefaultBoundedRangeModel();
        progressBar = new JProgressBar(progressBarModel);
        statusMsg = new JLabel();
        statusMsg.setOpaque(true);
        Border border = statusMsg.getBorder();
        Border margin = new EmptyBorder(4,6,4,10);
        statusMsg.setBorder(new CompoundBorder(border, margin));
        statusMsg.setBackground(PROGRESS_BG_CLR);
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
        progressContainer.add(progressBar, c1);
        progressContainer.add(progressLbl, c);
        progressContainer.add(cancelBtn, c);
        cancelBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 16, new Color(244,67,5)));
        setBorder(new EmptyBorder(10,10,10,10));
        statusMsg.setVisible(Boolean.FALSE);
        //add(statusMsg, BorderLayout.PAGE_START);
        GridBagLayout gbMain = new GridBagLayout();
        setLayout(gbMain);
        GridBagConstraints cMain = new GridBagConstraints();
        cMain.gridx = 0;
        cMain.gridy = 0;
        cMain.weighty = 0;
        cMain.weightx = 1;
        cMain.fill = 1;
        cMain.insets = new Insets(3,0,1,3);
        add(statusMsg, cMain);
        cMain.gridy = 1;
        add(progressContainer, cMain);
        setBackground(PROGRESS_BG_CLR);
        progressContainer.setBackground(PROGRESS_BG_CLR);
    }

    public void updateFooter(String progressMsg, int progress){
        progressBarModel.setValue(progress);
        progressLbl.setText(String.valueOf((int)progress) + " %");
        progressBar.setToolTipText(progressMsg);
        statusMsg.setVisible(Boolean.TRUE);
        statusMsg.setText(progressMsg);
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if(aFlag){
            statusMsg.setText(null);
        }
    }
}
