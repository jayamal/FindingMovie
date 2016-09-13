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

    private DefaultBoundedRangeModel progressBarModel;
    private JButton cancelBtn;
    private JLabel progressLbl;
    private JProgressBar progressBar;
    private JLabel successStatus;
    private JLabel failedStatus;


    public Footer(){
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        progressBarModel = new DefaultBoundedRangeModel();
        progressBar = new JProgressBar(progressBarModel);
        successStatus = new JLabel();
        failedStatus = new JLabel();
        successStatus.setOpaque(true);
        failedStatus.setOpaque(true);
        Border border = successStatus.getBorder();
        Border margin = new EmptyBorder(4,10,4,10);
        successStatus.setBorder(new CompoundBorder(border, margin));
        successStatus.setBackground(new Color(27,200,120));
        Border border1 = failedStatus.getBorder();
        failedStatus.setBorder(new CompoundBorder(border1, margin));
        failedStatus.setBackground(new Color(244,67,5));
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
        add(successStatus, c);
        add(failedStatus, c);
        add(progressBar, c1);
        add(progressLbl, c);
        add(cancelBtn, c);
        cancelBtn.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 16, new Color(244,67,5)));
        updateSuccessStatus(0);
        updateFailedStatus(0);
    }

    public void updateFooter(String progressMsg, int progress){
        progressBarModel.setValue(progress);
        progressLbl.setText(String.valueOf((int)progress) + " %");
        progressBar.setToolTipText(progressMsg);
    }

    public void updateSuccessStatus(int count){
        if(count > 0){
            successStatus.setVisible(Boolean.TRUE);
        }else{
            successStatus.setVisible(Boolean.FALSE);
        }
        successStatus.setText("Found [" + count + "]");
    }

    public void updateFailedStatus(int count){
        if(count > 0){
            failedStatus.setVisible(Boolean.TRUE);
        }else{
            failedStatus.setVisible(Boolean.FALSE);
        }
        failedStatus.setText("Ignored [" + count + "]");
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

}
