package model;

import view.utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.io.*;

public class UserModel implements Serializable {
    private String nickname, avatarPath;
    private int score, level, wins, losses, matches;

    transient private BufferedImage avatar;

    public UserModel(String nickname, int score, int level, int wins, int losses, int matches, String avatarPath) {
        this.nickname = nickname;
        this.score = score;
        this.level = level;
        this.wins = wins;
        this.losses = losses;
        this.matches = matches;
        this.avatarPath = avatarPath;
        this.avatar = LoadSave.GetSpriteAtlas(this.avatarPath);
    }

    public void serialize(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(nickname);
            os.writeObject(score);
            os.writeObject(level);
            os.writeObject(wins);
            os.writeObject(losses);
            os.writeObject(matches);
            os.writeObject(avatarPath);

            //os.flush();
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserModel read(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream is = new ObjectInputStream(fis);

            Object o1 = is.readObject();
            Object o2 = is.readObject();
            Object o3 = is.readObject();
            Object o4 = is.readObject();
            Object o5 = is.readObject();
            Object o6 = is.readObject();
            Object o7 = is.readObject();

            String nickname = (String) o1;
            int score = (Integer) o2;
            int level = (Integer) o3;
            int wins = (Integer) o4;
            int losses = (Integer) o5;
            int matches = (Integer) o6;
            String avatarPath = (String) o7;

            UserModel userModel =  new UserModel(nickname, score, level, wins, losses, matches, avatarPath);

            is.close();
            fis.close();

            return userModel;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public int getLevel() {
        return level;
    }

    public int getMatches() {
        return matches;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}