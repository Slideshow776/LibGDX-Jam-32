package no.sandramoen.libgdx32.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameUtils {

    public static void saveGameState() {
        BaseGame.preferences.putBoolean("loadPersonalParameters", true);
        BaseGame.preferences.putFloat("musicVolume", BaseGame.musicVolume);
        BaseGame.preferences.putFloat("soundVolume", BaseGame.soundVolume);
        BaseGame.preferences.putFloat("voiceVolume", BaseGame.voiceVolume);
        BaseGame.preferences.flush();
    }

    public static void loadGameState() {
        BaseGame.preferences = Gdx.app.getPreferences("GGJ2023GameState");
        BaseGame.loadPersonalParameters = BaseGame.preferences.getBoolean("loadPersonalParameters");
        BaseGame.musicVolume = BaseGame.preferences.getFloat("musicVolume");
        BaseGame.soundVolume = BaseGame.preferences.getFloat("soundVolume");
        BaseGame.voiceVolume = BaseGame.preferences.getFloat("voiceVolume");
    }

    public static void setWidgetHoverColor(final Widget widget) {
        widget.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                widget.setColor(Color.FIREBRICK);
                AssetLoader.hoverOverEnterSound.play(BaseGame.soundVolume);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                widget.setColor(Color.WHITE);
            }
        });
    }

    public static void playLoopingMusic(Music music) {
        music.setVolume(BaseGame.musicVolume);
        music.setLooping(true);
        music.play();
    }

    public static void playLoopingMusic(Music music, float volume) {
        music.setVolume(volume);
        music.setLooping(true);
        music.play();
    }

    public static void setMusicVolume(float volume) {
        if (volume > 1f || volume < 0f)
            logError("Volume needs to be within [0-1]. Volume is: " + volume);
        BaseGame.musicVolume = volume;
        setAllMusicVolumes(volume);
    }

    private static void setAllMusicVolumes(float volume) {
        for (Music music : AssetLoader.music)
            music.setVolume(volume);
    }

    public static void stopAllMusic() {
        for (Music music : AssetLoader.music)
            music.stop();
    }

    public static void addWidgetEnterExitEffect(Widget widget, Color enterColor, Color exitColor) {
        widget.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                widget.addAction(Actions.color(enterColor, 0.125f));
                AssetLoader.hoverOverEnterSound.play(BaseGame.soundVolume);
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                widget.addAction(Actions.color(exitColor, 0.125f));
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }



    public static float normalizeValue(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    public static boolean isTouchDownEvent(Event event) {
        return event instanceof InputEvent && ((InputEvent) event).getType() == InputEvent.Type.touchDown;
    }

    public static ShaderProgram initShaderProgram(String vertexShader, String fragmentShader) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled())
            logError("Error: Couldn't compile shader => " + shaderProgram.getLog());
        return shaderProgram;
    }

    public static Color randomLightColor() {
        float r = 0f;
        float g = 0f;
        float b = 0f;
        while (r <= 0 && g <= 0 && b <= 0) {
            r = MathUtils.random(0f, 1f);
            g = MathUtils.random(0f, 1f);
            b = MathUtils.random(0f, 1f);
        }

        return new Color(r, g, b, 1);
    }

    public static Object getNextItemOfLoopingArray(Array array, int index) {
        if (index >= array.size)
            return array.get(index % array.size);
        else if (index < 0)
            return array.get(index % array.size + array.size);
        else
            return array.get(index);
    }

    public static void printLoadingTime(String tag, String message, long startTime) {
        long endTime = System.currentTimeMillis();
        Gdx.app.log(tag, message + " took " + (endTime - startTime) + " ms to load.");
    }

    public static boolean isDistanceBigEnough(Vector2 k1, Vector2 k2, float distance) {
        return distanceBetween(k1, k2) >= distance;
    }

    public static boolean isWithinDistance(Vector2 k1, Vector2 k2, float distance) {
        return distanceBetween(k1, k2) <= distance;
    }

    public static float distanceBetween(Vector2 k1, Vector2 k2) {
        return (float) Math.sqrt(Math.pow(Math.abs(k2.x - k1.x), 2) + Math.pow(Math.abs(k2.y - k1.y), 2));
    }


    private static void logError(String message) {
        Gdx.app.error(GameUtils.class.getSimpleName(), message);
    }
}
