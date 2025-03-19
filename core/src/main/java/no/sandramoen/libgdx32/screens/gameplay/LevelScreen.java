package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import no.sandramoen.libgdx32.actors.player.HUD;
import no.sandramoen.libgdx32.actors.ParallaxBackground;
import no.sandramoen.libgdx32.actors.Enemy;
import no.sandramoen.libgdx32.actors.player.Player;
import no.sandramoen.libgdx32.actors.Projectile;
import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Player player;
    Enemy enemy;
    HUD hud;

    private final boolean IS_MUSIC_ENABLED = false;

    private boolean is_game_over = false;



    public LevelScreen() {
        initializeActors();
        initializeGUI();

        // music
        AssetLoader.levelMusic.setLooping(true);
        AssetLoader.levelMusic.setVolume(0f);
        if (IS_MUSIC_ENABLED) AssetLoader.levelMusic.play();
        AssetLoader.ambientMusic.setLooping(true);
        if (IS_MUSIC_ENABLED) AssetLoader.ambientMusic.play();

        //Gdx.input.setCursorCatched(true);
    }


    @Override
    public void initialize() {}


    @Override
    public void update(float delta) {
        handle_music_volume(delta);

        if (is_game_over)
            return;

        player.shoot_frequency = enemy.move_duration;

        handle_enemy_shooting();
    }


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
        // background
        BaseActor sky_background = new BaseActor(0f, 0f, mainStage);
        sky_background.loadImage("parallax_backgrounds/-1");
        sky_background.setSize(BaseGame.WORLD_WIDTH, BaseGame.WORLD_HEIGHT);

        for (int i = 0; i <= 4; i++)
            new ParallaxBackground(0, 0, mainStage, "parallax_backgrounds/" + i, (i + 1) * -0.75f * (i + 0.05f));

        // characters
        player = new Player(BaseGame.WORLD_WIDTH / 2, 1, mainStage);
        player.shield.activate(); // TODO: remove this, only for debugging

        enemy = new Enemy(BaseGame.WORLD_WIDTH / 2, 13f, mainStage);
        enemy.addListener(onEnemyTouched());

        hud = new HUD(0f, 0f, mainStage);
    }


    private EventListener onEnemyTouched() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (player.is_able_to_shoot == false || is_game_over == true)
                    return false;

                enemy.setHealth(enemy.health - 1);
                if (enemy.health <= 0)
                    set_game_over();
                return true;
            }
        };
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { // 0 for left, 1 for right
        if (is_game_over)
            return super.touchDown(screenX, screenY, pointer, button);

        if (player.is_able_to_shoot == false) {
            // TODO: play dud sound, unable to shoot
            return super.touchDown(screenX, screenY, pointer, button);
        }

        Vector2 world_coordinates = mainStage.getViewport().unproject(new Vector2(screenX, screenY));
        fire_projectile( // at the enemy
            new Vector2(
                player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2
            ),
            new Vector2(world_coordinates),
            true
        );
        player.shoot();
        return super.touchDown(screenX, screenY, pointer, button);
    }


    private void fire_projectile(Vector2 start_position, Vector2 end_position, boolean is_near_to_far) {
        Projectile projectile = new Projectile(start_position.x, start_position.y, mainStage);
        if (is_near_to_far)
            projectile.move_near_to_far(end_position.x, end_position.y, enemy.getScaleX());
        else
            projectile.move_far_to_near(end_position.x, end_position.y, enemy.getScaleX());
    }


    private void set_game_over() {
        is_game_over = true;
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
    }


    private void handle_enemy_shooting() {
        if (enemy.is_able_to_shoot == true){

            Vector2 source = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
            Vector2 target = new Vector2(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

            if (player.shield.is_active)
                target = new Vector2(player.shield.getX() + MathUtils.random(4f, 9f), player.shield.getY() + MathUtils.random(-1f, 1f));

            fire_projectile( // at the player
                source,
                target,
                false
            );


            if (player.shield.is_active == false) {
                player.setHealth(player.health - 1);
                hud.fade_in_and_out();
                if (player.health <= 0)
                    set_game_over();
            }
        }
    }


    private void handle_music_volume(float delta) {
        if (is_game_over) { // turn music down, ambient up
            if (AssetLoader.ambientMusic.getVolume() <= 1.0f)
                AssetLoader.ambientMusic.setVolume(MathUtils.clamp(AssetLoader.ambientMusic.getVolume() + delta * 0.1f, 0f, 1f));
            if (AssetLoader.levelMusic.getVolume() > 0.0f)
                AssetLoader.levelMusic.setVolume(MathUtils.clamp(AssetLoader.levelMusic.getVolume() - delta * 0.1f, 0f, 1f));
        } else { // turn music up, ambient down
            if (AssetLoader.ambientMusic.getVolume() > 0.4f)
                AssetLoader.ambientMusic.setVolume(MathUtils.clamp(AssetLoader.ambientMusic.getVolume() - delta * 0.1f, 0f, 1f));
            if (AssetLoader.levelMusic.getVolume() < 0.75f)
                AssetLoader.levelMusic.setVolume(MathUtils.clamp(AssetLoader.levelMusic.getVolume() + delta * 0.05f, 0f, 1f));
        }
    }


    private void initializeGUI() {
    }
}
