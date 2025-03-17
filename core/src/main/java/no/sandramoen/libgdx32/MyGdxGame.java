package no.sandramoen.libgdx32;


import no.sandramoen.libgdx32.screens.gameplay.LevelScreen;
import no.sandramoen.libgdx32.utils.BaseGame;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends BaseGame {

    @Override
    public void create() {
        super.create();
        //setActiveScreen(new SplashScreen());
        //setActiveScreen(new MenuScreen());

        //setActiveScreen(new LevelSelectScreen());
        setActiveScreen(new LevelScreen());
    }
}
