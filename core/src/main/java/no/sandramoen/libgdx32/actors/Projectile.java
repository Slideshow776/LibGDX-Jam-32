package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;

public class Projectile extends BaseActor {

    public Projectile(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("projectile");

        setSize(2, 2);
        centerAtPosition(x, y);
        setOrigin(Align.center);
    }


    public void move_to(float x, float y, float enemy_scale) {
        float final_scale = 0.1f * enemy_scale * 0.1f;
        final_scale = MathUtils.clamp(final_scale,0.1f,0.2f);

        float duration = 0.15f + (1 - enemy_scale);
        duration = MathUtils.clamp(duration, 0.1f, Enemy.MIN_MOVE_DURATION);

        addAction(Actions.parallel(
            Actions.scaleTo(final_scale, final_scale, duration * 1.1f),
            Actions.moveTo(
                x - getWidth() / 2,
                y - getHeight() / 2,
                duration
            )
        ));
        addAction(Actions.after(Actions.removeActor()));
    }
}
