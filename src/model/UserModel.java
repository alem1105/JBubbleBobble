package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Rappresenta un modello utente nel gioco.
 * Contiene informazioni sul punteggio, il livello, le vittorie, le sconfitte,
 * e il percorso dell'avatar dell'utente. Implementa Serializable per la
 * serializzazione e Comparable per il confronto tra utenti basato sul punteggio massimo.
 */
public class UserModel implements Serializable, Comparable<UserModel> {
    private String nickname, avatarPath;
    private int maxScore, level, levelScore, wins, losses, matches;
    transient int tempScore; // non serializza

    transient private BufferedImage avatar; // non serializza

    /**
     * Costruttore della classe UserModel.
     *
     * @param nickname Il nickname dell'utente.
     * @param maxScore Il punteggio massimo dell'utente.
     * @param level Il livello attuale dell'utente.
     * @param levelScore I progressi del livello attuale.
     * @param wins Il numero di vittorie dell'utente.
     * @param losses Il numero di sconfitte dell'utente.
     * @param matches Il numero totale di partite giocate.
     * @param avatarPath Il percorso dell'avatar dell'utente.
     */
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

    /**
     * Serializza l'oggetto UserModel nel percorso specificato.
     *
     * @param path Il percorso in cui serializzare l'oggetto.
     */
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

            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Legge un oggetto UserModel da un file nel percorso specificato.
     *
     * @param path Il percorso da cui leggere l'oggetto.
     * @return L'oggetto UserModel letto dal file, o null se si verifica un errore.
     */
    public static UserModel read(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream is = new ObjectInputStream(fis);

            String nickname = (String) is.readObject();
            int score = (Integer) is.readObject();
            int level = (Integer) is.readObject();
            int levelScore = (Integer) is.readObject();
            int wins = (Integer) is.readObject();
            int losses = (Integer) is.readObject();
            int matches = (Integer) is.readObject();
            String avatarPath = (String) is.readObject();

            UserModel userModel = new UserModel(nickname, score, level, levelScore, wins, losses, matches, avatarPath);

            is.close();
            fis.close();

            return userModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ottiene l'immagine dell'avatar dall'URL specificato.
     *
     * @param fileName Il nome del file dell'avatar.
     * @return L'immagine dell'avatar come BufferedImage.
     */
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = UserModel.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    /**
     * Confronta questo oggetto UserModel con un altro in base al punteggio massimo.
     *
     * @param o L'altro UserModel da confrontare.
     * @return Un valore negativo, zero o positivo se questo UserModel è
     *         rispettivamente minore, uguale o maggiore dell'altro UserModel.
     */
    @Override
    public int compareTo(UserModel o) {
        return Integer.compare(o.maxScore, this.maxScore);
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