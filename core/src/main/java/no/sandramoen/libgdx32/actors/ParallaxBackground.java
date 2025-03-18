package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;


public class ParallaxBackground extends BaseActor {

    Image image1;
    Image image2;
    float speed;


    public ParallaxBackground(float x, float y, Stage s, String image_path, float speed) {
        super(x, y, s);
        this.speed = speed;
        setPosition(x, y);
        setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);

        image1 = new Image(AssetLoader.textureAtlas.findRegion(image_path));
        image1.setSize(getWidth(), getHeight());
        image1.setOrigin(Align.center);
        image1.setScaleX(1.1f);
        addActor(image1);

        image2 = new Image(AssetLoader.textureAtlas.findRegion(image_path));
        image2.setSize(getWidth(), getHeight());
        image2.setPosition(getWidth(), getY());
        image2.setOrigin(Align.center);
        image2.setScaleX(1.1f);
        addActor(image2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image1.setPosition(image1.getX() + speed * delta, getY());
        image2.setPosition(image2.getX() + speed * delta, getY());

        if (image1.getX() + image1.getWidth() < 0)
            image1.setPosition(getWidth(), getY());

        if (image2.getX() + image2.getWidth() < 0)
            image2.setPosition(getWidth(), getY());
    }
}
