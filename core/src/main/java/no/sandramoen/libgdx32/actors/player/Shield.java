package no.sandramoen.libgdx32.actors.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;

public class Shield extends BaseActor {

    public static final float FADE_IN_DURATION = 0.5f;

    public boolean is_active = false;

    private Vector2 original_position;


    public Shield(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("player/shield");
        setSize(8, 3);
        setOrigin(Align.center);
        setOpacity(0);
        //setDebug(true);

        setTouchable(Touchable.disabled);
        original_position = new Vector2(getX(), getY());
    }


    public void activate() {
        AssetLoader.shield_up_sound.play(BaseGame.soundVolume);
        is_active = true;
        addAction(Actions.fadeIn(FADE_IN_DURATION, Interpolation.elastic));
    }


    public void deactivate() {
        AssetLoader.shield_down_sound.play(BaseGame.soundVolume);
        is_active = false;
        addAction(Actions.fadeOut(FADE_IN_DURATION, Interpolation.elastic));
    }


    public void endure() {
        addAction(Actions.sequence(
            Actions.delay(0.1f),
            Actions.run(() -> AssetLoader.shield_hit_sound.play(BaseGame.soundVolume))
        ));

        float hit_duration = MathUtils.random(0.25f, 0.5f);
        float restore_duration = MathUtils.random(0.25f, 0.5f);
        addAction(Actions.parallel(
            Actions.sequence(
              Actions.moveTo(original_position.x, original_position.y -1f, hit_duration, Interpolation.bounceOut),
              Actions.moveTo(original_position.x, original_position.y, restore_duration, Interpolation.bounceOut)
            ),
            Actions.sequence(
                Actions.scaleTo(1.1f, 0.9f, hit_duration, Interpolation.circleOut),
                Actions.scaleTo(1.0f, 1.0f, restore_duration, Interpolation.circleOut)
            ),
            Actions.sequence(
                Actions.color(new Color(1f, 1f, 1f, MathUtils.random(0f, 0.5f)), hit_duration, Interpolation.elastic),
                Actions.color(new Color(1f, 1f, 1f, 1f), restore_duration, Interpolation.elastic)
            )
        ));
    }
}
