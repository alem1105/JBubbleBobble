package view.ui;

import model.ui.EditButtonModel;
import view.utilz.LoadSave;

import java.awt.*;

public class EditButtonView extends CustomButtonView<EditButtonModel> {
    private Font font;

    public EditButtonView(EditButtonModel model) {
        super(model);
        font = LoadSave.CUSTOM_FONT;
    }

//    @Override
//    public void draw(Graphics g) {
//        g.setColor(Color.WHITE);
//        g.drawRect(getButtonModel().getX(), getButtonModel().getY(), getButtonModel().getWidth(), getButtonModel().getHeight());
//        g.setFont(font);
//        g.setColor(Color.GREEN);
//        g.drawString("EDIT", getButtonModel().getX() + getButtonModel().getWidth() / 4, (int)(getButtonModel().getY() + getButtonModel().getHeight() / 1.25));
//    }
    @Override
    protected void loadSprites() {
        sprites = LoadSave.loadAnimations(LoadSave.EDIT_BUTTON, 1, 3);
    }
}
