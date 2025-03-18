package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.actors.particles.HitEffect;
import no.sandramoen.libgdx32.actors.utils.Wobble;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;


public class Enemy extends BaseActor {

    public static final float MIN_MOVE_DURATION = 0.25f;
    public static final float MAX_MOVE_DURATION = 0.75f;

    public int health = 3;
    public float move_duration = 0f;

    private float elapsedTime = 0;


    public Enemy(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("enemy/enemy");

        setSize(1, 2);
        centerAtPosition(x, y);
        setOrigin(Align.center);
        //setDebug(true);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        float_around_animation(delta);
    }


    public void setHealth(int new_health) {
        int temp = health;
        health = new_health;

        if (temp > health) {
            move_duration = MathUtils.random(MIN_MOVE_DURATION, MAX_MOVE_DURATION);
            addAction(Actions.sequence(
                Actions.run(() -> take_damage()),
                Actions.delay(move_duration),
                Actions.run(() -> move())
            ));
        } else if (temp < health) {
            heal();
        }

        if (health <= 0)
            die();
    }


    private void die() {
        addAction(Actions.sequence(
            Actions.fadeOut(0.6f),
            Actions.removeActor()
        ));
    }


    private void take_damage() {
        // particle effect
        HitEffect effect = new HitEffect();
        effect.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
        effect.setScale(0.002f * getScaleX());
        getStage().addActor(effect);
        effect.start();

        // movement animation
        addAction(Actions.sequence(
            Actions.scaleTo(1.1f, 0.9f, move_duration * (1 / 5f)),
            //Wobble.shakeCamera(0.75f, Interpolation.linear, getStage().getCamera(), 9f, 0.5f),
            Actions.scaleTo(1.0f, 1.0f, move_duration * (4 / 5f), Interpolation.bounceOut)
        ));
    }


    private void move() { // made by chatgpt, tweaked by me
        float centerX = BaseGame.WORLD_WIDTH / 2 - getWidth() / 2;
        float centerY = 12.5f;

        float random_angle = MathUtils.random(0f, MathUtils.PI2);
        float random_radius = MathUtils.random(0.9f, 1.4f); // Random radius for movement
        float stretchFactorX = 2.7f; // Stretched along the x-axis (e.g., 1.5 means the ellipse is 1.5 times wider than the circle)

        // Convert polar to Cartesian coordinates
        float x = centerX + random_radius * stretchFactorX * MathUtils.cos(random_angle); // Apply stretch on x
        float y = centerY + random_radius * MathUtils.sin(random_angle); // Keep y as is, based on angle

        // Add some randomness to the final position
        float random_x = MathUtils.random(-0.5f, 0.5f);
        float random_y = MathUtils.random(-0.5f, 0.5f);

        // Apply random offsets
        x += random_x;
        y += random_y;

        // Random scale
        float scale = MathUtils.random(0.75f, 1.25f);

        // Random move duration
        float move_duration = MathUtils.random(0.2f, 1.0f);

        // Execute the action with the new position
        addAction(Actions.parallel(
            Actions.moveTo(x, y, move_duration, Interpolation.circleOut),
            Actions.scaleTo(scale, scale, move_duration, Interpolation.circleOut)
        ));
    }


    private void float_around_animation(float delta) {
        // Update elapsed time
        elapsedTime += delta;

        // Floating effect parameters
        float amplitude = 0.005f; // Maximum movement amount
        float speed = 1f; // Oscillation speed

        // Calculate sine-based movement
        float offset = MathUtils.sin(elapsedTime * speed) * amplitude;

        // Apply movement and rotation
        moveBy(offset, -offset); // Moves left/right and up/down
        setRotation(250 * -offset); // WARNING: overrides other set rotations
    }


    private void heal() {}
}
