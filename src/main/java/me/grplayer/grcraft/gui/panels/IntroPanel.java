package me.grplayer.grcraft.gui.panels;

import me.grplayer.grcraft.gui.TextureLoader;
import me.grplayer.grcraft.gui.frames.IntroFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroPanel extends JPanel {

    private final IntroFrame frame;

    public IntroPanel(IntroFrame frame) {
        super();
        this.frame = frame;
        setSize(500, 510);
        setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2));

        JButton installButton = new JButton("Install now");
        installButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.install();
            }
        });
        installButton.setSize(100, 50);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        cancelButton.setSize(100, 50);

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(installButton);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(TextureLoader.getTexture("welcome")).getImage(), 0, 0, this);
    }
}
