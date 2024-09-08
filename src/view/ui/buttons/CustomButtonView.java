package view.ui.buttons;

import model.ui.buttons.CustomButtonModel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La classe CustomButtonView rappresenta una vista generica per un pulsante personalizzato.
 * È responsabile del rendering degli sprite del pulsante in base al suo stato
 * (normale, hover, premuto) e dell'aggiornamento dell'aspetto del pulsante di conseguenza.
 *
 * @param <T> il tipo di modello del pulsante, che deve estendere CustomButtonModel.
 */
public class CustomButtonView<T extends CustomButtonModel> {

    /**
     * Un array bidimensionale di sprite del pulsante, dove ogni riga rappresenta uno stato diverso
     * del pulsante (ad esempio, normale, hover, premuto).
     */
    protected BufferedImage[][] sprites;

    /**
     * Il modello del pulsante
     */
    protected T buttonModel;

    /**
     * L'indice corrente dello sprite da visualizzare in base allo stato del pulsante.
     */
    protected int spriteIndex;

    /**
     * Costruttore della classe CustomButtonView.
     * Inizializza il modello del pulsante e carica gli sprite associati.
     *
     * @param buttonModel Il modello del pulsante da associare a questa vista.
     */
    public CustomButtonView(T buttonModel) {
        this.buttonModel = buttonModel;
        loadSprites();
    }

    /**
     * Metodo protetto per caricare gli sprite del pulsante.
     * Questo metodo verra' sovrascritto dalle classi figlie.
     */
    protected void loadSprites() {}

    /**
     * Disegna il pulsante sulla schermata.
     * Visualizza lo sprite corrispondente allo stato attuale del pulsante.
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(sprites[0][spriteIndex],
                buttonModel.getX(), buttonModel.getY(),
                buttonModel.getWidth(), buttonModel.getHeight(), null);
    }

    /**
     * Disegna un rettangolo verde intorno al pulsante per indicare che è selezionato.
     *
     * @param g
     */
    protected void drawSelectedBox(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(buttonModel.getX() - 1, buttonModel.getY() - 1,
                buttonModel.getWidth() + 1, buttonModel.getHeight() + 1);
    }

    /**
     * Aggiorna lo stato visivo del pulsante in base alle interazioni dell'utente.
     * Cambia l'indice dello sprite se il pulsante è in stato di hover o premuto.
     */
    public void update() {
        if (buttonModel.isHover()) spriteIndex = 1;
        else spriteIndex = 0;
        if (buttonModel.isPressed()) spriteIndex = 2;
    }

    public T getButtonModel() {
        return buttonModel;
    }

}
