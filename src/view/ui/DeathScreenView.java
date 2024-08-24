package view.ui;

import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static model.utilz.Constants.GameConstants.GAME_HEIGHT;
import static model.utilz.Constants.GameConstants.GAME_WIDTH;

public class DeathScreenView {
    int x, y, width, height;
    private BufferedImage deathScreen;

    public DeathScreenView() {
        this.x = 100;
        this.y = 500;
        this.width = 300;
        this.height = 500;
    }

    public void render(Graphics g) {
        Color myColour = new Color(0, 0, 0, 230);
        g.setColor(myColour);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        //g.drawImage(deathScreen, x, y, width, height, null);
        g.setColor(WHITE);
        g.setFont(LoadSave.CUSTOM_FONT);
        g.drawString("hai perso popi popi ", x, y);
    }
}
