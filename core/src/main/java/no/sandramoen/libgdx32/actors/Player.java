package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;


public class Player extends BaseActor {

    public static final float SHOOT_COOL_DOWN = Enemy.MAX_MOVE_DURATION;

    public Player(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("player_test");

        setSize(4, 8);
        centerAtPosition(x, y);
        setOrigin(Align.center);
    }


    public void shoot() {
        // animation
        addAction(Actions.sequence(
            Actions.scaleTo(0.95f, 1.05f, SHOOT_COOL_DOWN * (1f / 5f)),
            Actions.scaleTo(1.0f, 1.0f, SHOOT_COOL_DOWN * (4f / 5f), Interpolation.bounceOut)
        ));
    }
}
