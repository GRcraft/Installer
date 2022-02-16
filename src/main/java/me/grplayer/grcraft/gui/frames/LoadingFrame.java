package me.grplayer.grcraft.gui.frames;

import javax.swing.*;

public class LoadingFrame extends JFrame {

    public LoadingFrame() {
        this.setTitle("Loading...");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        this.add(progressBar);
        this.setLocationRelativeTo(null);
        this.setSize(200, 50);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

}
