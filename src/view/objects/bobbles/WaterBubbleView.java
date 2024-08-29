package view.objects.bobbles;

import model.objects.bobbles.WaterBubbleModel;
import view.utilz.LoadSave;

import java.awt.image.BufferedImage;

public class WaterBubbleView extends BubbleView<WaterBubbleModel>{

    public WaterBubbleView(WaterBubbleModel model) {
        super(model);
        this.sprite = LoadSave.GetSpriteAtlas(LoadSave.WATER_BUBBLE_SPRITE);
    }
}
