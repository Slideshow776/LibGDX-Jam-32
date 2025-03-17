package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

import no.sandramoen.libgdx32.actors.Background;
import no.sandramoen.libgdx32.actors.Player;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    public LevelScreen() {
        initializeActors();
        initializeGUI();
    }


    @Override
    public void initialize() {}


    @Override
    public void update(float delta) {}


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q)
            Gdx.app.exit();
        else if (keycode == Keys.R)
            restart();
        else if (keycode == Keys.NUMPAD_0) {
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom += .1f;
        }

        return super.keyDown(keycode);
    }


    private void initializeActors() {
        new Background(0, 0, mainStage);
        new Player(2, 6, mainStage);
    }


    private void initializeGUI() {
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
    }
}
