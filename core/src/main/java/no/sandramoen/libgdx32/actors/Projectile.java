package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;

public class Projectile extends BaseActor {

    private final float MOVE_DURATION = 0.25f;


    public Projectile(Vector2 position, Stage stage) {
        super(position.x, position.y, stage);
        loadImage("projectile");

        setSize(2, 2);
        centerAtPosition(position.x, position.y);
        setOrigin(Align.center);
    }


    public void move_near_to_far(Vector2 position, float enemy_scale) {
        float final_scale = 0.1f * enemy_scale * 0.1f;
        final_scale = MathUtils.clamp(final_scale,0.1f,0.2f);

        float duration = 0.15f + (1 - enemy_scale);
        duration = MathUtils.clamp(duration, 0.1f, MOVE_DURATION);

        addAction(Actions.parallel(
            Actions.scaleTo(final_scale, final_scale, duration * 1.1f),
            move_to_action(position, duration)
        ));
        addAction(Actions.after(Actions.removeActor()));
    }


    public void move_far_to_near(Vector2 position, float enemy_scale) {
        setScale(0.1f * enemy_scale * 0.1f);
        rotateBy(180);

        float final_scale = 1f;

        float duration = 0.3f + (1 - enemy_scale);
        duration = MathUtils.clamp(duration, 0.1f, MOVE_DURATION);

        addAction(Actions.parallel(
            Actions.scaleTo(final_scale, final_scale, duration * 1.1f),
            move_to_action(position, duration)
        ));
        addAction(Actions.after(Actions.removeActor()));
    }


    private MoveToAction move_to_action(Vector2 position, float duration) {
        return Actions.moveTo(
            position.x - getWidth() / 2,
            position.y - getHeight() / 2,
            duration
        );
    }
}
