package model.objects.items.powerups;

import model.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.UserStateModel;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.PowerUps.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

import java.util.ArrayList;
import java.util.Random;

public class PowerUpsManagerModel {

    private static PowerUpsManagerModel instance;

    private ArrayList<PowerUpModel> powerups;
    private PlayerModel playerModel;

    private int potionLightningTick = 0, potionLightningTimer = 900;

    private Random random;

    private boolean bombExploding;

    public static PowerUpsManagerModel getInstance() {
        if(instance == null) {
            instance = new PowerUpsManagerModel();
        }
        return instance;
    }

    private PowerUpsManagerModel() {
        playerModel = PlayerModel.getInstance();
        initPowerUps();
        random = new Random();
    }

    private void initPowerUps(){
        powerups = new ArrayList<>();
    }

    public void update(){
        checkPowerUpsSpawningConditions();
        updatePowerUps();
    }

    private void updatePowerUps() {
        for(PowerUpModel powerUp : powerups) {
            if (powerUp.isActive()) {
                powerUp.update();
                if (playerModel.getHitbox().intersects(powerUp.getHitbox()) && !powerUp.isPickedUp()) {
                    UserStateModel.getInstance().getCurrentUserModel().incrementTempScore(powerUp.getScore());
                    powerUp.applyEffect();
                }
            }
        }
    }

    private void checkPowerUpsSpawningConditions() {
        checkCandySpawningConditions();
        checkUmbrellaSpawningConditions();
        checkRingSpawningConditions();
        checkOtherSpawningConditions();
    }

    private void checkCandySpawningConditions() {
        if(playerModel.getBlowedBubbles() == 35) {
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_PINK));
            playerModel.setBlowedBubbles(0);
        }
        if(playerModel.getPoppedBubbles() == 35) {
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_BLUE));
            playerModel.setPoppedBubbles(0);
        }
        if(playerModel.getJumpedTimes() == 35) {
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_YELLOW));
            playerModel.setJumpedTimes(0);
        }
    }

    private void checkUmbrellaSpawningConditions() {
        if (playerModel.getPoppedWaterBubbles() % 3 == 0) {
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_ORANGE));
            playerModel.incrementPoppedWaterBubbles();
        }
        if (playerModel.getPoppedWaterBubbles() % 21 == 0) {
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_RED));
            playerModel.incrementPoppedWaterBubbles();
        }
        if (playerModel.getPoppedWaterBubbles() % 26 == 0){
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_PINK));
            playerModel.incrementPoppedWaterBubbles();
        }
    }

    private void checkRingSpawningConditions() {
        if (playerModel.getEatenPinkCandies() == 3) {
            powerups.add(new RingModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (9 * SCALE), (int) (14 * SCALE), RING_PINK));
            playerModel.setEatenPinkCandies(0);
        }
        if (playerModel.getEatenYellowCandies() == 3) {
            powerups.add(new RingModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (9 * SCALE), (int) (14 * SCALE), RING_RED));
            playerModel.setEatenYellowCandies(0);
        }
    }

    private void checkOtherSpawningConditions() {
        if (playerModel.getRunDistanceAmount() >= (GAME_WIDTH - TILES_SIZE * 2) * 15) {
            powerups.add(new SneakerModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (9 * SCALE)));
            playerModel.setRunDistanceAmount(0);
        }

        if (playerModel.getPoppedLightingBubbles() == 12) {
            powerups.add(new ClockModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13f * SCALE), (int) (14f * SCALE)));
            playerModel.setPoppedLightingBubbles(0);
        }

        if (canBombSpawn()) {
            powerups.add(new BombModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (14f * SCALE), (int) (12f * SCALE)));
            playerModel.incrementPoppedFireBubbles();
        }

        if (canPotionLightingSpawn())
            powerups.add(new PotionLightningModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (16f * SCALE)));
    }

    private int[] generateRandomCoordinates() {
        int randomTileX = random.nextInt(TILES_IN_WIDTH - 2) + 1;
        int randomTileY = random.nextInt(TILES_IN_HEIGHT - 2) + 1;

        while(IsTileSolid(randomTileX, randomTileY, getLvlData())) {
            randomTileX = random.nextInt(TILES_IN_WIDTH - 2) + 1;
            randomTileY = random.nextInt(TILES_IN_HEIGHT - 2) + 1;
        }

        return new int[]{randomTileX * TILES_SIZE, randomTileY * TILES_SIZE};
    }

    public ArrayList<PowerUpModel> getPowerups() {
        return powerups;
    }

    private boolean canBombSpawn(){
        int poppedFireBubbles = playerModel.getPoppedFireBubbles();
        return (poppedFireBubbles % 11 == 0 || poppedFireBubbles % 14 == 0
                || poppedFireBubbles % 17 == 0 || poppedFireBubbles % 20 == 0);
    }

    private boolean canPotionLightingSpawn(){
        if (LevelManagerModel.getInstance().getLvlIndex() == LevelManagerModel.getInstance().getLevels().size()){
            potionLightningTick++;
            if (potionLightningTimer == potionLightningTick){
                potionLightningTick = 0;
                return true;
            }
        }
        return false;
    }

    public void setBombExploding(boolean bombExploding) {
        this.bombExploding = bombExploding;
    }

    public boolean getBombExploding() {
        return bombExploding;
    }
}