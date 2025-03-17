package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import no.sandramoen.libgdx32.utils.BaseActor;


public class Background extends BaseActor {

    public Background(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("coralPixel");

        setPosition(x, y);
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }
}
