package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import no.sandramoen.libgdx32.actors.Background;
import no.sandramoen.libgdx32.actors.Enemy;
import no.sandramoen.libgdx32.actors.Player;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Enemy enemy;


    public LevelScreen() {
        initializeActors();
        initializeGUI();

        //Gdx.input.setCursorCatched(true);
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
        new Player(BaseGame.WORLD_WIDTH / 2, 1, mainStage);

        enemy = new Enemy(BaseGame.WORLD_WIDTH / 2, 13f, mainStage);
        enemy.addListener(onTouched());
    }


    private EventListener onTouched() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                enemy.setHealth(enemy.health - 1);
                return true;
            }
        };
    }


    private void initializeGUI() {
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
    }
}
