package view.ui;

import model.LevelManagerModel;
import model.LevelModel;
import view.entities.PlayerView;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.Color.*;
import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.GameConstants.SCALE;
import static view.utilz.LoadSave.BUB_LEVEL_TRANSITION;
import static view.utilz.LoadSave.loadAnimations;

public class NextLevelScreenView {

    private static NextLevelScreenView instance;
    private LevelManagerModel levelManager;
    private BufferedImage[] lvlSprites;
    private int time = 0, aniIndex, aniTick;

    // per il livello
    private int[][] prevLevelData;
    private int [][] nextLevelData;
    private int prevY = 0, nextY = GAME_HEIGHT - TILES_SIZE;

    // per bub
    private PlayerView playerView;
    private BufferedImage[][] bubTransitionImages;
    private Point nextPlayerSpawn, curPlayerPos;

    public static NextLevelScreenView getInstance() {
        if (instance == null) {
            instance = new NextLevelScreenView();
        }
        return instance;
    }

    private NextLevelScreenView(){
        playerView = PlayerView.getInstance();
        levelManager = LevelManagerModel.getInstance();
        lvlSprites = LoadSave.importSprites();
        bubTransitionImages = loadAnimations(BUB_LEVEL_TRANSITION, 2, 2, 30, 34);
    }

    public void update(){
        if (nextY <= 0 && nextPlayerSpawn.equals(curPlayerPos)){
            resetNextScreenView();
            return;
        }
        if (time == 0)
            getData();
        else {
            updateLvlPos();
            updateBubPosition();
        }
        time++;
    }

    private void getData() {
        prevLevelData = levelManager.getLevels().get(levelManager.getLvlIndex() - 1).getLvlData();
        nextLevelData = levelManager.getLevels().get(levelManager.getLvlIndex()).getLvlData();
        curPlayerPos = playerView.getCurPlayerPos();
        nextPlayerSpawn = levelManager.getLevels().get(levelManager.getLvlIndex()).getPlayerSpawn();
    }

    private void updateLvlPos(){
        if (!(nextY <= 0)){
            nextY -= (int) (1 * SCALE);
            prevY -= (int) (1 * SCALE);
        }
    }

    private void updateBubPosition(){
        if(curPlayerPos.x != nextPlayerSpawn.x) {
            if (curPlayerPos.x < nextPlayerSpawn.x)
                curPlayerPos.x ++;
            else
                curPlayerPos.x --;
        }
        if(curPlayerPos.y != nextPlayerSpawn.y){
            if (curPlayerPos.y < nextPlayerSpawn.y)
                curPlayerPos.y ++;
            else
                curPlayerPos.y --;
                //curPlayerPos.y -= (int) (1 * SCALE);
        }
    }

    public void resetNextScreenView(){
        time = 0;
        aniTick = 0;
        aniIndex = 0;
        prevY = 0;
        nextY = GAME_HEIGHT - TILES_SIZE;
        levelManager.setNextLevel(false);
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        renderData(g, prevLevelData, prevY);
        renderData(g, nextLevelData, nextY);
        if(time < 80)
            renderBub(g, 0, 0);
        else{
            updateAni();
            renderBub(g, 1, aniIndex);
        }
    }

    private void updateAni() {
        aniTick ++;
        if (aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
        }
        if (aniIndex > 1)
            aniIndex = 0;
    }

    private void renderData(Graphics g, int[][] lvlData, int lvlY){
        for (int y = 0; y < TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < lvlData[0].length; x++) {
                int index = lvlData[y][x];
                if (index == 0 || index == 255) continue;
                g.drawImage(lvlSprites[index - 1], x * TILES_SIZE, (y * TILES_SIZE) + lvlY , TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    private void renderBub(Graphics g, int stateIndex, int aniIndex){
        g.drawImage(bubTransitionImages[stateIndex][aniIndex],
                (curPlayerPos.x - (int)(8 * SCALE)), (curPlayerPos.y - (int) (9*SCALE)),
                (int) (SCALE * 30), (int) (SCALE * 34) , null);
    }
}
