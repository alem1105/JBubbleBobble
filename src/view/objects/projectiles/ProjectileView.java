package view.objects.projectiles;

import model.objects.projectiles.ProjectileModel;
import view.objects.CustomObjectView;

import java.awt.*;

public abstract class ProjectileView<T extends ProjectileModel> extends CustomObjectView<T> {

    public ProjectileView(T objectModel) {
        super(objectModel);
    }

    protected void update(){
        updateAnimationTick();
    }

    public boolean conditionToDraw(){
        return objectModel.isActive();
    }

    public void updateAndDraw(Graphics g){
        update();
        draw(g);
    }
}
