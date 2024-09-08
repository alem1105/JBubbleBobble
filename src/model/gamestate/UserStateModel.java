package model.gamestate;

import model.UserModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
        getAllUsers();
    }

    public void getAllUsers() {
        userModels = new ArrayList<>();
        
        final File folder = new File("res/users");
        File[] files = folder.listFiles();

        if(files == null)
            return;

        files = Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".bb"))
                .toArray(File[]::new);

        for (File f : files) {
            userModels.add(UserModel.read(f.getPath()));
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
