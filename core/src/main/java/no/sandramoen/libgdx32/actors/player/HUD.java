package no.sandramoen.libgdx32.actors.player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;

public class HUD extends BaseActor {

    public HUD(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("player/hud/0");
        setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);
        setTouchable(Touchable.disabled);
        setOrigin(Align.center);
        setOpacity(0f);
    }


    public void fade_in_and_out() {
        if (MathUtils.randomBoolean())
            flip();

        addAction(Actions.sequence(
            Actions.fadeIn(0.5f),
            Actions.delay(0.1f),
            Actions.fadeOut(01f)
        ));
    }
}
