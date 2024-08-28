package view.ui.buttons;

import model.UserModel;
import model.ui.buttons.CustomButtonModel;
import model.ui.buttons.UserButtonModel;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.GameConstants.GAME_WIDTH;
import static model.utilz.Constants.GameConstants.SCALE;

public class UserButtonView extends CustomButtonView<UserButtonModel> {

    private BufferedImage avatar;
    private String nickname;

    public UserButtonView(UserButtonModel model) {
        super(model);
    }

    @Override
    protected void loadSprites() {
         avatar = buttonModel.getUserModel().getAvatar();
         nickname = buttonModel.getUserModel().getNickname();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(avatar, (int) (buttonModel.getX() - avatar.getWidth() - (5 * SCALE)), buttonModel.getY(), null);
        g.drawRoundRect(buttonModel.getX(), buttonModel.getY(), buttonModel.getWidth(), buttonModel.getHeight(), 5, 5);
        g.setFont(LoadSave.JEQO_FONT);
        if (buttonModel.isHover())
            drawHover(g);
        else if (buttonModel.isPressed())
            drawPressed(g);
        else
            drawNormal(g);
    }

    private void drawPressed(Graphics g) {
        g.setColor(new Color(254, 238, 31));
        FontMetrics misure = g.getFontMetrics(LoadSave.JEQO_FONT);
        g.drawString(nickname, buttonModel.getWidth() / 2 - misure.stringWidth(nickname)/2, buttonModel.getY()  + (int)(1 * SCALE));
    }

    private void drawHover(Graphics g) {
        FontMetrics misure = g.getFontMetrics(LoadSave.JEQO_FONT);
        g.setColor(new Color(88, 83, 13)); // Ombra
        g.drawString(nickname, buttonModel.getWidth() / 2 - misure.stringWidth(nickname)/2, buttonModel.getY() + (int)(1 * SCALE));
        g.drawRoundRect(buttonModel.getX(), buttonModel.getY() + (int)(1 * SCALE), buttonModel.getWidth(), buttonModel.getHeight(), 5, 5);
        g.setColor(new Color(254, 238, 31));
        g.drawString(nickname, buttonModel.getWidth() / 2 - misure.stringWidth(nickname)/2, buttonModel.getY() );
    }

    public void drawNormal(Graphics g){
        FontMetrics misure = g.getFontMetrics(LoadSave.JEQO_FONT);
        g.setColor(new Color(87, 33, 59)); // Ombra
        g.drawRoundRect(buttonModel.getX(), buttonModel.getY() + (int)(1 * SCALE), buttonModel.getWidth(), buttonModel.getHeight(), 5, 5);
        //drawRectangle(g, buttonModel.getX(), buttonModel.getY() + (int)(1 * SCALE), buttonModel.getWidth(), buttonModel.getHeight(), 3);
        g.drawString(nickname, buttonModel.getWidth() / 2 - misure.stringWidth(nickname)/2, buttonModel.getY() + (int)(1 * SCALE));
        g.setColor(new Color(242, 70, 152));
        g.drawString(nickname, buttonModel.getWidth() / 2 + misure.stringWidth(nickname), buttonModel.getY() + misure.getHeight() - (int)(2 * SCALE));
    }

    public static void drawRectangle(Graphics g, int x, int y, int width, int height, int thickness) {
        g.drawRoundRect(x, y, width, height, 5, 5);
        if (thickness > 1) {
            drawRectangle(g, x + 1, y + 1, width - 2, height - 2, thickness - 1);
        }
    }
}
