package no.sandramoen.libgdx32.actors.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.actors.Enemy;
import no.sandramoen.libgdx32.actors.utils.Wobble;
import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;


public class Player extends BaseActor {

    public static final float SHOOT_COOL_DOWN = 0.75f;

    public Sprite arm;
    public Shield shield;
    public int health = 33;
    public boolean is_able_to_shoot = true;
    public boolean is_using_shield = false;
    public float shoot_frequency = SHOOT_COOL_DOWN;

    private float shoot_counter = shoot_frequency;
    private static final String ARM_NAME = "player/arm_test";
    private Vector3 temp = new Vector3();
    private float elapsedTime = 0;

    public Player(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("player/player");

        // arm
        arm = AssetLoader.textureAtlas.createSprite(ARM_NAME);
        if (arm == null)
            Gdx.app.error(getClass().getSimpleName(), "Error: arm is null. Are you sure the image '" + ARM_NAME + "' exists?");

        arm.setSize(4, 4);
        arm.setOrigin(0.6f, 0f);
        arm.setOriginBasedPosition(x-0.1f, y);

        // body
        setSize(4, 8);
        centerAtPosition(x, y);
        setOrigin(Align.center);

        shield = new Shield(-2f, 8f, getStage());
        addActor(shield);

        addListener(onEnterExit());

        //setDebug(true);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        float_around_animation(delta);
        if (is_using_shield == false)
            handle_shoot_cooldown_timer(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        getStage().getCamera().unproject(temp);
        arm.setRotation(MathUtils.atan2Deg360(temp.y - arm.getY() - 1, temp.x - arm.getX() - 1) - 90);
        arm.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }


    private EventListener onEnterExit() {
        return new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                is_using_shield = true;
                addAction(Actions.scaleTo(1.2f, 0.8f, Shield.FADE_IN_DURATION));
                shield.activate();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                is_using_shield = false;
                shoot_counter = 0f;
                addAction(Actions.scaleTo(1.0f, 1.0f, Shield.FADE_IN_DURATION));
                shield.deactivate();
            }
        };
    }


    public void shoot() {
        shoot_counter = 0f;

        // animation
        addAction(Actions.sequence(
            Actions.scaleTo(0.95f, 1.05f, SHOOT_COOL_DOWN * (1f / 5f)),
            Actions.scaleTo(1.0f, 1.0f, SHOOT_COOL_DOWN * (4f / 5f), Interpolation.bounceOut)
        ));
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


    private void die() {
        arm.setColor(0f, 0f, 0f, 0f);
        addAction(Actions.sequence(
            Actions.fadeOut(0.6f),
            Actions.removeActor()
        ));
    }


    private void take_damage() {
        addAction(Wobble.shakeCamera(0.6f, Interpolation.linear, getStage().getCamera(), 9f, 0.5f));
    }


    private void heal() {}


    @Override
    protected void positionChanged() {
        super.positionChanged();
        if(arm != null) // can be null in constructor, before creating arm
            arm.setOriginBasedPosition(getX(Align.center)-0.1f, getY(Align.center));
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
        moveBy(-offset, offset); // Moves left/right and up/down
        setRotation(250 * offset);
    }


    private void handle_shoot_cooldown_timer(float delta) {
        if (shoot_counter < shoot_frequency) {
            shoot_counter += delta;
            is_able_to_shoot = false;
        } else {
            is_able_to_shoot = true;
        }
    }
}
