package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;


public class Player extends BaseActor {

    public static final float SHOOT_COOL_DOWN = Enemy.MAX_MOVE_DURATION;

    public Sprite arm;
    private static final String ARM_NAME = "player/arm_test";
    private Vector3 temp = new Vector3();

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
    }


    public void shoot() {
        // animation
        addAction(Actions.sequence(
            Actions.scaleTo(0.95f, 1.05f, SHOOT_COOL_DOWN * (1f / 5f)),
            Actions.scaleTo(1.0f, 1.0f, SHOOT_COOL_DOWN * (4f / 5f), Interpolation.bounceOut)
        ));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        getStage().getCamera().unproject(temp);
        arm.setRotation(MathUtils.atan2Deg360(temp.y - arm.getY() - 1, temp.x - arm.getX() - 1) - 90);
        arm.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if(arm != null) // can be null in constructor, before creating arm
            arm.setOriginBasedPosition(getX(Align.center)-0.1f, getY(Align.center));
    }
}
