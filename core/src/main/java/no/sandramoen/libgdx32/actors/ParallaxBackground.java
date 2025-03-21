package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;


public class ParallaxBackground extends BaseActor {

    private Image image1;
    private Image image2;
    private float speed;
    private float multiplier = 1;
    private float y_offset = 0.75f;

    public ParallaxBackground(float x, float y, Stage s, String image_path, float speed) {
        super(x, y, s);
        this.speed = speed;
        setPosition(x, y);
        setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);
        setTouchable(Touchable.disabled);

        image1 = new Image(AssetLoader.textureAtlas.findRegion(image_path));
        image1.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);
        image1.setOrigin(Align.center);
        image1.setScale(1.1f);
        addActor(image1);

        image2 = new Image(AssetLoader.textureAtlas.findRegion(image_path));
        image2.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);
        image2.setPosition(BaseGame.WORLD_WIDTH, getY());
        image2.setOrigin(Align.center);
        image2.setScale(1.1f);
        addActor(image2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image1.setPosition(image1.getX() + speed * multiplier * delta, getY() - y_offset);
        image2.setPosition(image2.getX() + speed * multiplier * delta, getY() - y_offset);

        if (image1.getX() + BaseGame.WORLD_WIDTH < 0)
            image1.setPosition(image2.getX() + BaseGame.WORLD_WIDTH, getY() - y_offset);
        else if (image1.getX() > BaseGame.WORLD_WIDTH)
            image1.setPosition(image2.getX() - BaseGame.WORLD_WIDTH, getY() - y_offset);

        if (image2.getX() + BaseGame.WORLD_WIDTH < 0)
            image2.setPosition(image1.getX() + BaseGame.WORLD_WIDTH, getY() - y_offset);
        else if (image2.getX() > BaseGame.WORLD_WIDTH)
            image2.setPosition(image1.getX() - BaseGame.WORLD_WIDTH, getY() - y_offset);
    }


    public void reverse() { speed *= -1; }
    public void speed_up() { multiplier = 2; }
    public void normal_speed() { multiplier = 1; }
    public void stop() { speed = 0; }
}
