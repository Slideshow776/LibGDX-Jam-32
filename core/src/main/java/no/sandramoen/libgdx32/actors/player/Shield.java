package no.sandramoen.libgdx32.actors.player;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;

public class Shield extends BaseActor {

    public static final float FADE_IN_DURATION = 0.5f;

    public boolean is_active = false;


    public Shield(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("player/shield");
        setSize(8, 3);
        setOrigin(Align.center);
        setOpacity(0);
        //setDebug(true);
    }


    public void activate() {
        is_active = true;
        addAction(Actions.fadeIn(FADE_IN_DURATION, Interpolation.elastic));
    }


    public void deactivate() {
        is_active = false;
        addAction(Actions.fadeOut(FADE_IN_DURATION, Interpolation.elastic));
    }
}
