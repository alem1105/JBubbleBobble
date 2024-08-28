package model.ui.buttons;

import model.UserModel;

public class UserButtonModel extends CustomButtonModel{
    private UserModel userModel;

    public UserButtonModel(int x, int y, int width, int height, UserModel userModel) {
        super(x, y, width, height);
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
