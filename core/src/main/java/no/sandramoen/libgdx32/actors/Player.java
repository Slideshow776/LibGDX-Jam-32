package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import no.sandramoen.libgdx32.utils.BaseActor;


public class Player extends BaseActor {

    public Player(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("player_test");

        setSize(8, 8);
        centerAtPosition(x, y);
    }
}
