package model.gamestate;

import model.UserModel;

import java.io.File;
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
//        UserModel user = new UserModel("Utente 1", 500, 200, 200, 200, 200, LoadSave.AVATAR_1);
//        user.serialize("res/users/user.txt");
        getAllUsers();
    }

    public void getAllUsers() {
        userModels = new ArrayList<>();
        
        final File folder = new File("res/users");
        File[] files = folder.listFiles();

        if (files != null) {
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
