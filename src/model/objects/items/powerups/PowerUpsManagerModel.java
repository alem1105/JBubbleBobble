package model.objects.items.powerups;

import model.LevelManagerModel;
import model.entities.PlayerModel;

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
        for(PowerUpModel powerUp : powerups)
            if(playerModel.getHitbox().intersects(powerUp.getHitbox()) && powerUp.isActive())
                powerUp.update();
    }

    private void checkPowerUpsSpawningConditions() {
        checkCandySpawningConditions();
        checkUmbrellaSpawningConditions();
        checkRingSpawningConditions();
        checkOtherSpawningConditions();
    }

    private void checkCandySpawningConditions() {
        if(playerModel.getBlowedBubbles() % 2 == 0 && playerModel.getBlowedBubbles() != 0)
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (13f * SCALE), CANDY_PINK));
        if(playerModel.getPoppedBubbles() % 2 == 0 && playerModel.getPoppedBubbles() != 0)
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (13f * SCALE), CANDY_PINK));
        if(playerModel.getJumpedTimes() % 2 == 0 && playerModel.getPoppedBubbles() != 0)
            powerups.add(new CandyModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (16f * SCALE), (int) (13f * SCALE), CANDY_PINK));
    }

    private void checkUmbrellaSpawningConditions() {
        if (playerModel.getPoppedWaterBubbles() % 15 == 0 && playerModel.getPoppedWaterBubbles() != 0)
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0],generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_ORANGE));
        if (playerModel.getPoppedWaterBubbles() % 20 == 0 && playerModel.getPoppedWaterBubbles() != 0)
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_RED));
        if (playerModel.getPoppedWaterBubbles() % 25 == 0 && playerModel.getPoppedWaterBubbles() != 0)
            powerups.add(new UmbrellaModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_PINK));
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
        if (playerModel.getPoppedLightingBubbles() % 12 == 0 && playerModel.getPoppedLightingBubbles() != 0)
            powerups.add(new ClockModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (13f * SCALE), (int) (14f * SCALE)));
        if (canBombSpawn())
            powerups.add(new BombModel(generateRandomCoordinates()[0], generateRandomCoordinates()[1], (int) (14f * SCALE), (int) (12f * SCALE)));
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
        return (poppedFireBubbles % 10 == 0 || poppedFireBubbles % 13 == 0
                || poppedFireBubbles % 16 == 0 || poppedFireBubbles % 19 == 0) && poppedFireBubbles != 0 ;
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
}