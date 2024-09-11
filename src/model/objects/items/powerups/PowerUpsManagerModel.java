package model.objects.items.powerups;

import model.level.LevelManagerModel;
import model.entities.PlayerModel;
import model.gamestate.UserStateModel;

import static model.utilz.Constants.GameConstants.*;
import static model.utilz.Constants.PowerUps.*;
import static model.utilz.Gravity.*;
import static model.utilz.UtilityMethods.getLvlData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Questa classe gestisce la creazione e l'aggiornamento dei powerup nel gioco.
 * Controlla le condizioni di spawning e fa applicare gli effetti dei powerup quando il giocatore li raccoglie.
 */
public class PowerUpsManagerModel {

    private static PowerUpsManagerModel instance;

    private ArrayList<PowerUpModel> powerups;
    private PlayerModel playerModel;

    private int potionLightningTick = 0, potionLightningTimer = 120, numberOfPotions;

    private Random random;

    private boolean bombExploding;
    int[][] lightningPotionSpawnPoints = {{6 * TILES_SIZE, 3 * TILES_SIZE}, {13 * TILES_SIZE, 3 * TILES_SIZE}};

    /**
     * Restituisce l'istanza singleton della classe {@code PowerUpsManagerModel}.
     *
     * @return l'istanza singleton di {@code PowerUpsManagerModel}
     */
    public static PowerUpsManagerModel getInstance() {
        if(instance == null) {
            instance = new PowerUpsManagerModel();
        }
        return instance;
    }

    /**
     * Costruttore privato della classe {@code PowerUpsManagerModel}.
     * Inizializza il modello del giocatore e la lista di potenziamenti, e crea un'istanza di {@code Random}.
     */
    private PowerUpsManagerModel() {
        playerModel = PlayerModel.getInstance();
        initPowerUps();
        random = new Random();
    }

    /**
     * Inizializza la lista dei powerup.
     */
    private void initPowerUps(){
        powerups = new ArrayList<>();
    }

    /**
     * Aggiorna lo stato dei powerup e verifica le condizioni di spawning.
     */
    public void update(){
        checkPowerUpsSpawningConditions();
        updatePowerUps();
    }

    /**
     * Aggiorna tutti i powerup attivi.
     * Verifica se il giocatore ha interagito con un oggetto di potenziamento e fa applicare gli effetti se necessario.
     */
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

    /**
     * Controlla le condizioni di spawning per tutti i tipi di potenziamenti.
     */
    private void checkPowerUpsSpawningConditions() {
        checkCandySpawningConditions();
        checkUmbrellaSpawningConditions();
        checkRingSpawningConditions();
        checkOtherSpawningConditions();
    }

    /**
     * Controlla le condizioni di spawning per i potenziamenti di tipo Candy.
     */
    private void checkCandySpawningConditions() {
        if(playerModel.getBlowedBubbles() == 35) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new CandyModel(randomCoordinates[0], randomCoordinates[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_PINK));
            playerModel.setBlowedBubbles(0);
        }
        if(playerModel.getPoppedBubbles() == 35) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new CandyModel(randomCoordinates[0], randomCoordinates[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_BLUE));
            playerModel.setPoppedBubbles(0);
        }
        if(playerModel.getJumpedTimes() == 35) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new CandyModel(randomCoordinates[0], randomCoordinates[1], (int) (16f * SCALE), (int) (10f * SCALE), CANDY_YELLOW));
            playerModel.setJumpedTimes(0);
        }
    }

    /**
     * Controlla le condizioni di spawning per i potenziamenti di tipo Umbrella.
     */
    private void checkUmbrellaSpawningConditions() {
        if (playerModel.getPoppedWaterBubbles() % 3 == 0) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new UmbrellaModel(randomCoordinates[0], randomCoordinates[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_ORANGE));
            playerModel.incrementPoppedWaterBubbles();
        }
        if (playerModel.getPoppedWaterBubbles() % 21 == 0) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new UmbrellaModel(randomCoordinates[0], randomCoordinates[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_RED));
            playerModel.incrementPoppedWaterBubbles();
        }
        if (playerModel.getPoppedWaterBubbles() % 26 == 0){
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new UmbrellaModel(randomCoordinates[0], randomCoordinates[1], (int) (13 * SCALE), (int) (16 * SCALE), UMBRELLA_PINK));
            playerModel.incrementPoppedWaterBubbles();
        }
    }

    /**
     * Controlla le condizioni di spawning per i potenziamenti di tipo Ring.
     */
    private void checkRingSpawningConditions() {
        if (playerModel.getEatenPinkCandies() == 3) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new RingModel(randomCoordinates[0], randomCoordinates[1], (int) (9 * SCALE), (int) (14 * SCALE), RING_PINK));
            playerModel.setEatenPinkCandies(0);
        }
        if (playerModel.getEatenYellowCandies() == 3) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new RingModel(randomCoordinates[0], randomCoordinates[1], (int) (9 * SCALE), (int) (14 * SCALE), RING_RED));
            playerModel.setEatenYellowCandies(0);
        }
    }

    /**
     * Controlla le condizioni di spawning per altri tipi di potenziamenti.
     */
    private void checkOtherSpawningConditions() {
        if (playerModel.getRunDistanceAmount() >= (GAME_WIDTH - TILES_SIZE * 2) * 15) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new SneakerModel(randomCoordinates[0], randomCoordinates[1], (int) (13 * SCALE), (int) (9 * SCALE)));
            playerModel.setRunDistanceAmount(0);
        }

        if (playerModel.getPoppedLightingBubbles() == 12) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new ClockModel(randomCoordinates[0], randomCoordinates[1], (int) (13f * SCALE), (int) (14f * SCALE)));
            playerModel.setPoppedLightingBubbles(0);
        }

        if (canBombSpawn()) {
            int[] randomCoordinates = generateRandomCoordinates();
            powerups.add(new BombModel(randomCoordinates[0], randomCoordinates[1], (int) (14f * SCALE), (int) (12f * SCALE)));
            playerModel.incrementPoppedFireBubbles();
        }

        if (canPotionLightingSpawn()) {
            int[] randomCoordinates = lightningPotionSpawnPoints[random.nextInt(lightningPotionSpawnPoints.length)];
            powerups.add(new PotionLightningModel(randomCoordinates[0], randomCoordinates[1], (int) (16f * SCALE), (int) (16f * SCALE)));
        }
    }

    /**
     * Genera coordinate casuali per il posizionamento di un power-up.
     * Le coordinate vengono generate assicurandosi che il blocco (tile) non sia solido.
     *
     * @return un array contenente le coordinate casuali x e y
     */
    private int[] generateRandomCoordinates() {
        int randomTileX = random.nextInt(TILES_IN_WIDTH - 2) + 1;
        int randomTileY = random.nextInt(TILES_IN_HEIGHT - 2) + 1;

        while(isTileSolid(randomTileX, randomTileY, getLvlData())) {
            randomTileX = random.nextInt(TILES_IN_WIDTH - 2) + 1;
            randomTileY = random.nextInt(TILES_IN_HEIGHT - 2) + 1;
        }

        return new int[]{randomTileX * TILES_SIZE, randomTileY * TILES_SIZE};
    }

    /**
     * Controlla se una bomba può essere generata in base al numero di bolle di fuoco scoppiate dal giocatore.
     * Le bombe possono essere generate quando il numero di bolle di fuoco scoppiate è divisibile per 11, 14, 17 o 20.
     *
     * @return true se una bomba può essere generata, false altrimenti
     */
    private boolean canBombSpawn(){
        int poppedFireBubbles = playerModel.getPoppedFireBubbles();
        return (poppedFireBubbles % 11 == 0 || poppedFireBubbles % 14 == 0
                || poppedFireBubbles % 17 == 0 || poppedFireBubbles % 20 == 0);
    }

    /**
     * Controlla se un powerup di tipo Potion Lightning può essere generato.
     * Un powerup di tipo Potion Lightning può essere generato solo se il livello attuale è l'ultimo del gioco
     * e solo dopo un intervallo di tempo specificato.
     *
     * @return true se un potenziamento di tipo Potion Lightning può essere generato, false altrimenti
     */
    private boolean canPotionLightingSpawn(){
        if (LevelManagerModel.getInstance().getLvlIndex() + 1 == LevelManagerModel.getInstance().getLevels().size()){
            potionLightningTick++;
            if (potionLightningTimer == potionLightningTick){
                potionLightningTick = 0;
                return true;
            }
        }
        return false;
    }

    public ArrayList<PowerUpModel> getPowerups() {
        return powerups;
    }

    public void setBombExploding(boolean bombExploding) {
        this.bombExploding = bombExploding;
    }

    public boolean getBombExploding() {
        return bombExploding;
    }
}