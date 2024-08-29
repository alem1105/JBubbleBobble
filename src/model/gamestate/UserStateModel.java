package model.gamestate;

import model.UserModel;
import view.utilz.LoadSave;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class UserStateModel {

    private static UserStateModel instance;

    private UserModel currentUserModel;
    private ArrayList<UserModel> userModels;

    public static UserStateModel getInstance() {
        if (instance == null) {
            instance = new UserStateModel();
        }
        return instance;
    }

    private UserStateModel() {
        // TODO TOGLIERE
        UserModel user = new UserModel("Utente 1", 500, 200, 200, 200, 200, LoadSave.HIDEGONS_FIREBALL);
        user.serialize("res/users/user.txt");
        getAllUsers();
    }

    private void getAllUsers() {
        userModels = new ArrayList<>();
        URL url = UserStateModel.class.getResource("/users");
        File file = null;

        if (url != null) {
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File[] files = file.listFiles();

            for (File f : files) {
                userModels.add(UserModel.read(f.getPath()));
            }
        }

    }

    public void setCurrentUserModel(UserModel currentUserModel) {
        this.currentUserModel = currentUserModel;
    }

    public UserModel getCurrentUserModel() {
        return currentUserModel;
    }

    public ArrayList<UserModel> getUserModels() {
        return userModels;
    }
}
