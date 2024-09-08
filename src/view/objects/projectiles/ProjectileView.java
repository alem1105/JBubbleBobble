package view.objects.projectiles;

import view.objects.CustomObjectView;
import model.objects.projectiles.ProjectileModel;

import java.awt.*;

/**
 * Classe astratta utilizzata per indicare i proiettili in generale
 * @param <T>
 */
public abstract class ProjectileView<T extends ProjectileModel> extends CustomObjectView<T> {

    public ProjectileView(T objectModel) {
        super(objectModel);
    }

    /**
     * Aggiorna il proiettile graficamente
     */
    protected void update(){
        updateAnimationTick();
    }

    /**
     * @return Booleano che indica se il proiettile deve essere disegnato o meno, di base se attivo o non
     */
    public boolean conditionToDraw(){
        return objectModel.isActive();
    }

    public void updateAndDraw(Graphics g){
        update();
        draw(g);
    }
}
