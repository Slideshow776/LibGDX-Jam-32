package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import no.sandramoen.libgdx32.actors.ParallaxBackground;
import no.sandramoen.libgdx32.actors.Enemy;
import no.sandramoen.libgdx32.actors.Player;
import no.sandramoen.libgdx32.actors.Projectile;
import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Player player;
    Enemy enemy;

    private final boolean IS_MUSIC_ENABLED = true;

    private boolean is_game_over = false;

    private boolean is_player_able_to_shoot = true;
    private float player_shoot_frequency = Enemy.MAX_MOVE_DURATION;
    private float player_shoot_counter = player_shoot_frequency;

    private boolean is_enemy_able_to_shoot = true;
    private float enemy_shoot_frequency = 2.0f;
    private float enemy_shoot_counter = 0f;


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
        player_shoot_frequency = enemy.move_duration;
        handle_player_shoot_cooldown_timer(delta);

        handle_enemy_shoot_cooldown_timer(delta);
        handle_enemy_shooting();

        handle_music_volume(delta);
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

        enemy = new Enemy(BaseGame.WORLD_WIDTH / 2, 13f, mainStage);
        enemy.addListener(onEnemyTouched());
    }


    private EventListener onEnemyTouched() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (is_player_able_to_shoot == false)
                    return false;

                enemy.setHealth(enemy.health - 1);
                if (enemy.health <= 0)
                    set_game_over();
                return true;
            }
        };
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (is_player_able_to_shoot == false) {
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
        player_shoot_counter = 0f;
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
        enemy.die();
        is_game_over = true;
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
    }


    private void handle_player_shoot_cooldown_timer(float delta) {
        if (player_shoot_counter < player_shoot_frequency) {
            player_shoot_counter += delta;
            is_player_able_to_shoot = false;
        } else {
            is_player_able_to_shoot = true;
        }
    }


    private void handle_enemy_shoot_cooldown_timer(float delta) {
        if (enemy_shoot_counter < enemy_shoot_frequency) {
            enemy_shoot_counter += delta;
            is_enemy_able_to_shoot = false;
        } else {
            enemy_shoot_counter = 0f;
            is_enemy_able_to_shoot = true;
        }
    }


    private void handle_enemy_shooting() {
        if (is_enemy_able_to_shoot == true){
            fire_projectile( // at the player
                new Vector2(
                    enemy.getX() + enemy.getWidth() / 2,
                    enemy.getY() + enemy.getHeight() / 2
                ),
                new Vector2(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2),
                false
            );
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
