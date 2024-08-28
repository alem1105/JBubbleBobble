package view.objects;

import model.objects.CustomObjectModel;
import model.objects.ProjectileModel;

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
        super.draw(g);
    }
}
