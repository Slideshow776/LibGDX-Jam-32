package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.libgdx32.actors.Background;
import no.sandramoen.libgdx32.actors.Enemy;
import no.sandramoen.libgdx32.actors.Player;
import no.sandramoen.libgdx32.actors.Projectile;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Player player;
    Enemy enemy;

    private boolean is_able_to_shoot = true;
    private float shoot_frequency = Enemy.MAX_MOVE_DURATION;
    private float shoot_counter = shoot_frequency;


    public LevelScreen() {
        initializeActors();
        initializeGUI();

        //Gdx.input.setCursorCatched(true);
    }


    @Override
    public void initialize() {}


    @Override
    public void update(float delta) { handle_shoot_cooldown_timer(delta); }


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

        player = new Player(BaseGame.WORLD_WIDTH / 2, 1, mainStage);

        enemy = new Enemy(BaseGame.WORLD_WIDTH / 2, 13f, mainStage);
        enemy.addListener(onEnemyTouched());
    }


    private EventListener onEnemyTouched() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (is_able_to_shoot == false)
                    return false;

                enemy.setHealth(enemy.health - 1);
                return true;
            }
        };
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (is_able_to_shoot == false) {
            // TODO: play dud sound, unable to shoot
            return super.touchDown(screenX, screenY, pointer, button);
        }

        fire_projectile(screenX, screenY);
        player.shoot();
        shoot_counter = 0f;
        return super.touchDown(screenX, screenY, pointer, button);
    }


    private void fire_projectile(int x, int y) {
        Projectile projectile = new Projectile(
            player.getX() + player.getWidth() / 2,
            player.getY() + player.getHeight() / 2,
            mainStage
        );

        Vector2 world_coordinates = mainStage.getViewport().unproject(new Vector2(x, y));
        projectile.move_to(world_coordinates.x, world_coordinates.y, enemy.getScaleX());
    }


    private void initializeGUI() {
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
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
