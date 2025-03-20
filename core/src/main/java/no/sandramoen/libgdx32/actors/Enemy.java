package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.actors.particles.HitEffect;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;


public class Enemy extends BaseActor {

    public int health = 33;
    public float move_duration = 0f;
    public boolean is_able_to_shoot = true;

    public enum Magic {LIGHTNING, FIRE, DEATH}
    public Magic current_magic = Magic.FIRE;

    private final float MOVE_NORMAL = 0.5f;
    private final float MOVE_FAST = 0.25f;
    private final int DAMAGE_LESS = 1;
    private final int DAMAGE_NORMAL = 2;
    private final int DAMAGE_EXTRA = 4;
    private final float SHOOT_NORMAL = 2.0f;
    private final float SHOOT_FAST = 0.5f;
    private final float SHOOT_SLOW = 4.0f;

    public int damage_modifier = DAMAGE_NORMAL;

    private float shoot_frequency = SHOOT_NORMAL;
    private float shoot_counter = 0f;
    private float elapsedTime = 0;
    private SequenceAction shoot_animation;


    public Enemy(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("enemy/enemy");

        setSize(1, 2);
        centerAtPosition(x, y);
        setOrigin(Align.center);
        //setDebug(true);

        addAction(charge_shot_animation());

        move_duration = MOVE_NORMAL;
        addAction(Actions.forever(Actions.sequence(
            Actions.delay(2f),
            Actions.delay(move_duration),
            Actions.run(() -> move())
        )));
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        float_around_animation(delta);
        handle_shoot_cooldown_timer(delta);
    }


    public void setHealth(int new_health) {
        int temp = health;
        health = new_health;

        if (temp > health) {
            take_damage();
        } else if (temp < health) {
            heal();
        }

        if (health <= 0)
            die();
    }


    public void shoot_animation() {
        shoot_animation = Actions.sequence(
            Actions.scaleTo(0.8f, 1.2f, shoot_frequency * 0.1f, Interpolation.circleOut), // Stretch forward
            Actions.delay(0.1f), // Slight delay before shooting

            Actions.scaleTo(1f, 1f, shoot_frequency * 0.2f, Interpolation.bounceOut), // Snap back after shooting

            charge_shot_animation()
        );

        addAction(shoot_animation);
    }


    public void phase_damage_and_speed() {
        // moves around quickly, takes extra damage, attacks fast, lightning magic
        move_duration = MOVE_FAST;
        damage_modifier = DAMAGE_EXTRA;
        shoot_frequency = SHOOT_FAST;
        current_magic = Magic.LIGHTNING;
    }


    public void phase_speed_and_tough(float phase_duration) {
        // moves around quickly, takes less damage, doesn't attack, death magic
        move_duration = MOVE_FAST;
        damage_modifier = DAMAGE_LESS;
        shoot_frequency = phase_duration;
        current_magic = Magic.DEATH;
    }


    public void phase_damage_and_tough(float phase_duration) {
        // moves nothing, takes less damage, attacks slow, fire magic
        move_duration = phase_duration;
        damage_modifier = DAMAGE_NORMAL;
        shoot_frequency = SHOOT_SLOW;
        current_magic = Magic.FIRE;
    }


    public String get_projectile_image_path_for(Enemy.Magic magic) {
        switch (magic) {
            case FIRE:
                return "enemy/fire_projectile";
            case LIGHTNING:
                return "enemy/lightning_projectile";
            case DEATH:
                return "enemy/death_projectile";
            default:
                Gdx.app.error("Projectile.java", "Error: Unknown magic type " + magic);
                return "player/projectile";
        }
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

        // take damage animation
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
        //removeAction(shoot_animation);
        addAction(Actions.parallel(
            Actions.moveTo(x, y, move_duration, Interpolation.circleOut)/*,
            Actions.scaleTo(scale, scale, move_duration, Interpolation.circleOut)*/
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


    private void handle_shoot_cooldown_timer(float delta) {
        if (shoot_counter < shoot_frequency) {
            shoot_counter += delta;
            is_able_to_shoot = false;
        } else {
            shoot_counter = 0f;
            is_able_to_shoot = true;
        }
    }


    private SequenceAction charge_shot_animation() {
        return Actions.sequence(
            Actions.scaleTo(1.4f, 0.6f, shoot_frequency * 0.5f, Interpolation.smooth), // Squash in anticipation
            Actions.delay(0.2f) // Hold charge
        );
    }
}
