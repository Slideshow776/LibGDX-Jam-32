package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import no.sandramoen.libgdx32.utils.BaseActor;


public class Player extends BaseActor {

    public Player(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("player");

        setPosition(x, y);
        setSize(4, 4);

    }
}
