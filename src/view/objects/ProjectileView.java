package view.objects;

import model.objects.CustomObjectModel;

public class ProjectileView extends CustomObjectView {


    public ProjectileView(CustomObjectModel objectModel) {
        super(objectModel);
    }

    @Override
    protected int getSpriteAmount() {
        return 0;
    }

}
