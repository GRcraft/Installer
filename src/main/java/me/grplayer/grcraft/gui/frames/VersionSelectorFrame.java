package me.grplayer.grcraft.gui.frames;

import me.grplayer.grcraft.Release;
import me.grplayer.grcraft.gui.GuiManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class VersionSelectorFrame extends JFrame {

    public VersionSelectorFrame(GuiManager guiManager, Release @NotNull [] releases) {
        super("Select the version to install");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] releasesNames = new String[releases.length];
        for (int i = 0; i < releases.length; i++) {
            releasesNames[i] = releases[i].getVersion();
        }

        JList versionList = new JList(releasesNames);
        versionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        versionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        versionList.setVisibleRowCount(-1);
        versionList.setFixedCellWidth(getWidth());
        versionList.addListSelectionListener(e -> {
            guiManager.submitRelease(releases[versionList.getSelectedIndex()]);
        });

        JScrollPane versionListScroller = new JScrollPane(versionList);
        versionList.setPreferredSize(new Dimension(getWidth(), getHeight()));

        add(versionListScroller, BorderLayout.CENTER);
        setVisible(true);
    }

}
