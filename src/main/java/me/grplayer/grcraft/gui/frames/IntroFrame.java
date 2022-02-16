package me.grplayer.grcraft.gui.frames;

import me.grplayer.grcraft.gui.GuiManager;
import me.grplayer.grcraft.gui.panels.IntroPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroFrame extends JFrame implements ActionListener {

    private final GuiManager guiManager;

    public IntroFrame(GuiManager guiManager) {
        super("G_RCraft Installer");
        this.guiManager = guiManager;
        setSize(500, 510);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new IntroPanel(this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed");
        System.out.println(e);
    }

    public void install() {
        this.hide();
        this.guiManager.startInstallation();
    }
}
