package no.sandramoen.libgdx32.screens.gameplay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.libgdx32.actors.particles.HitEffect;
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

    private Player player;
    private Enemy enemy;
    private HUD hud;
    private Array<ParallaxBackground> parallax_backgrounds;

    private final boolean IS_MUSIC_ENABLED = true;
    private final boolean IS_SOUNDS_ENABLED = true;
    private final float PHASE_SHIFT_DURATION = 20f;

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

        // sounds
        if (!IS_SOUNDS_ENABLED)
            BaseGame.soundVolume = 0f;

        // Gdx.input.setCursorCatched(true);

        phase_damage_and_tough();
        Actor phase_timer = new Actor();
        phase_timer.addAction(Actions.forever(Actions.sequence(
            Actions.delay(PHASE_SHIFT_DURATION),
            Actions.run(() -> phase_damage_and_speed()),
            Actions.delay(PHASE_SHIFT_DURATION),
            Actions.run(() -> phase_speed_and_tough()),
            Actions.delay(PHASE_SHIFT_DURATION),
            Actions.run(() -> phase_damage_and_tough())
        )));
        mainStage.addActor(phase_timer);
    }


    @Override
    public void initialize() {}


    @Override
    public void update(float delta) {
        handle_music_volume(delta);

        if (is_game_over)
            return;

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

        parallax_backgrounds = new Array();
        for (int i = 0; i <= 4; i++)
            parallax_backgrounds.add(new ParallaxBackground(0, 0, mainStage, "parallax_backgrounds/" + i, (i + 1) * -0.75f * (i + 0.05f)));

        // characters
        player = new Player(BaseGame.WORLD_WIDTH / 2, 1, mainStage);

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

                enemy.setHealth(enemy.health - enemy.damage_modifier);
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

        if (player.is_able_to_shoot == false || player.is_using_shield) {
            // TODO: play dud sound, unable to shoot
            return super.touchDown(screenX, screenY, pointer, button);
        }

        Vector2 player_coordinates = new Vector2(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
        Vector2 mouse_position_in_world_coordinates = mainStage.getViewport().unproject(new Vector2(screenX, screenY));
        fire_projectile( // at the enemy
            player_coordinates,
            mouse_position_in_world_coordinates,
            true,
            "player/projectile"
        );
        AssetLoader.player_shoot_0_sound.play(BaseGame.soundVolume);
        player.shoot();
        return super.touchDown(screenX, screenY, pointer, button);
    }


    private void fire_projectile(Vector2 start_position, Vector2 end_position, boolean is_near_to_far, String image_path) {
        Projectile projectile = new Projectile(start_position, mainStage, image_path);
        if (is_near_to_far)
            projectile.move_near_to_far(end_position, enemy.getScaleX());
        else
            projectile.move_far_to_near(end_position, enemy.getScaleX());
    }


    private void set_game_over() {
        is_game_over = true;
        for (ParallaxBackground background : parallax_backgrounds)
            background.stop();
    }


    private void restart() {
        BaseGame.setActiveScreen(new LevelScreen());
    }


    private void handle_enemy_shooting() {
        if (enemy.is_able_to_shoot == true){
            Vector2 source = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
            Vector2 target = new Vector2(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

            if (player.shield.is_active) {
                target = new Vector2(player.shield.getX() + MathUtils.random(4f, 9f), player.shield.getY() + MathUtils.random(-1f, 1f));

                HitEffect effect = new HitEffect();
                effect.setPosition(target.x, target.y - MathUtils.random(1.5f, 2.0f));
                effect.setScale(0.005f);
                mainStage.addActor(effect);
                effect.start();
            }

            fire_projectile( // at the player
                source,
                target,
                false,
                enemy.get_projectile_image_path_for(enemy.current_magic)
            );

            AssetLoader.enemy_shoot_0_sound.play(BaseGame.soundVolume);
            enemy.shoot_animation();


            if (player.shield.is_active == false) {
                AssetLoader.player_heart_beat_sound.play(1f);
                player.setHealth(player.health - 1);
                hud.fade_in_and_out();
                if (player.health <= 0)
                    set_game_over();
            } else {
                player.shield.endure();
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


    private void phase_damage_and_speed() {
        // moves around quickly, takes extra damage, attacks fast, lightning magic
        enemy.phase_damage_and_speed();
        for (ParallaxBackground background : parallax_backgrounds) {
            background.speed_up();
            background.reverse();
        }
    }


    private void phase_speed_and_tough() {
        // moves around quickly, takes less damage, doesn't attack, death magic
        enemy.phase_speed_and_tough(PHASE_SHIFT_DURATION);
        for (ParallaxBackground background : parallax_backgrounds) {
            background.normal_speed();
            background.reverse();
        }
    }


    private void phase_damage_and_tough() {
        // moves nothing, takes less damage, attacks slow, fire magic
        enemy.phase_damage_and_tough(PHASE_SHIFT_DURATION);
        for (ParallaxBackground background : parallax_backgrounds) {
            background.normal_speed();
            background.reverse();
        }

    }


    private void initializeGUI() {
    }
}
