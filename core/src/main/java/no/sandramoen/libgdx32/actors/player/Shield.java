package no.sandramoen.libgdx32.actors.player;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;

public class Shield extends BaseActor {
    public Shield(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("player/shield");
        setSize(8, 4);
        setOrigin(Align.center);
        setDebug(true);
    }
}
