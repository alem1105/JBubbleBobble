package view;

import view.utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

/**
 * Classe che indica la finestra dove viene disegnato il GamePanel
 * Imposta i paramentri necessari
 */
public class GameWindow {

    private JFrame jFrame;

    private BufferedImage icon = LoadSave.getSpriteAtlas(LoadSave.ICON);

    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("JBubbleBobble");
        jFrame.setIconImage(icon);
        Taskbar.getTaskbar().setIconImage(icon);
        jFrame.add(gamePanel);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });
    }

}
