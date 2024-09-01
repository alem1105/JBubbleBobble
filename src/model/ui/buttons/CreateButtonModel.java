package model.ui.buttons;

import model.UserModel;
import model.gamestate.UserStateModel;
import view.ui.buttons.CustomButtonView;
import view.utilz.LoadSave;

public class CreateButtonModel extends CustomButtonModel{

    public CreateButtonModel(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void saveUser(UserModel user) {
        user.serialize("res/users/" + user.getNickname() + ".bubblebobble");
        UserStateModel.getInstance().getAllUsers();
    }
}
