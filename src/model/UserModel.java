package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class UserModel implements Serializable, Comparable<UserModel> {
    private String nickname, avatarPath;
    private int maxScore, level, levelScore, wins, losses, matches;
    transient int tempScore; // non lo serializza

    transient private BufferedImage avatar; // non lo serializza

    public UserModel(String nickname, int maxScore, int level, int levelScore, int wins, int losses, int matches, String avatarPath) {
        this.nickname = nickname;
        this.maxScore = maxScore;
        this.level = level;
        this.levelScore = levelScore;
        this.wins = wins;
        this.losses = losses;
        this.matches = matches;
        this.avatarPath = avatarPath;
        this.avatar = GetSpriteAtlas(this.avatarPath);
    }

    public void serialize(String path) {
        try {

            File dir = new File("res/users/");
            if (!dir.exists()) {
                dir.mkdir();
            }

            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(nickname);
            os.writeObject(maxScore);
            os.writeObject(level);
            os.writeObject(levelScore);
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
            Object o8 = is.readObject();

            String nickname = (String) o1;
            int score = (Integer) o2;
            int level = (Integer) o3;
            int levelScore = (Integer) o4;
            int wins = (Integer) o5;
            int losses = (Integer) o6;
            int matches = (Integer) o7;
            String avatarPath = (String) o8;

            UserModel userModel =  new UserModel(nickname, score, level, levelScore, wins, losses, matches, avatarPath);

            is.close();
            fis.close();

            return userModel;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = UserModel.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
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

    public int getMaxScore() {
        return maxScore;
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

    public int getTempScore() {
        return tempScore;
    }

    public void setAvatarPath(String path) {
        this.avatarPath = path;
        this.avatar = GetSpriteAtlas(avatarPath);
    }

    public void incrementTempScore(int value) {
        this.tempScore += value;
    }

    public void updateLevelScore() {
        this.levelScore += tempScore;
        level = levelScore / 5000;
    }

    public void setMaxScore() {
        this.maxScore = Math.max(maxScore, tempScore);
    }

    @Override
    public int compareTo(UserModel o) {
        return Integer.compare(o.maxScore, this.maxScore);
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public void incrementMatches() {
        this.matches++;
    }

    public void setTempScore(int tempScore) {
        this.tempScore = tempScore;
    }

}