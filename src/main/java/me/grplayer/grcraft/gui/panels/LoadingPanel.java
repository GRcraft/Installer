package me.grplayer.grcraft.gui.panels;

import javax.swing.*;

public class LoadingPanel extends JPanel {

    public LoadingPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar);
    }

}
