package model.gamestate;

import model.UserModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * La classe {@code UserStateModel} è responsabile della gestione dello stato degli utenti
 * nel gioco. Utilizza il pattern Singleton per garantire che ci sia un'unica istanza
 * della classe che gestisce i dati relativi agli utenti.
 *
 * Questa classe carica i dati degli utenti da una cartella specifica e tiene traccia
 * dell'utente attualmente selezionato.
 */
public class UserStateModel {

    private static UserStateModel instance;

    /** L'utente attualmente selezionato nel gioco. */
    private UserModel currentUserModel;

    /** Lista di tutti gli utenti caricati dal file system. */
    private ArrayList<UserModel> userModels;

    /**
     * Restituisce l'istanza Singleton di {@code UserStateModel}. Se l'istanza non esiste,
     * ne viene creata una nuova.
     *
     * @return l'istanza corrente di {@code UserStateModel}.
     */
    public static UserStateModel getInstance() {
        if (instance == null) {
            instance = new UserStateModel();
        }
        return instance;
    }

    /**
     * Costruttore privato della classe {@code UserStateModel}.
     * Inizializza la lista di utenti caricando i dati da file.
     */
    private UserStateModel() {
        getAllUsers();
    }

    /**
     * Carica tutti i modelli utente dalla cartella "res/users". Filtra i file con
     * estensione ".bb" e li aggiunge alla lista {@code userModels}.
     * Se non vengono trovati file, la lista rimane vuota.
     */
    public void getAllUsers() {
        userModels = new ArrayList<>();

        final File folder = new File("res/users");
        File[] files = folder.listFiles();

        // Se non ci sono file nella cartella, non eseguire nessuna operazione
        if(files == null)
            return;

        // Filtra i file che terminano con ".bb" e li converte in array
        files = Arrays.stream(files)
                .filter(f -> f.getName().endsWith(".bb"))
                .toArray(File[]::new);

        // Aggiunge i modelli utente alla lista
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
