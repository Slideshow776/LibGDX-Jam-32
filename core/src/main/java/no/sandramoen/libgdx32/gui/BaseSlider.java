package no.sandramoen.libgdx32.gui;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.GameUtils;


public class BaseSlider extends Table {
    private final TypingLabel label;
    private final Slider slider;

    public BaseSlider(String type, String labelText) {
        label = labelInit(labelText);
        slider = initializeSlider(type);/**/

        float containerWidth = Gdx.graphics.getWidth() * 0.07f;
        float containerHeight = Gdx.graphics.getHeight() * 0.02f;
        float containerScaleX = 4f;
        float containerScaleY = 4.5f;

        Container<Slider> container = new Container<>(slider);
        container.setTransform(true);
        container.setWidth(containerWidth * containerScaleX);
        container.setHeight(containerHeight * containerScaleY);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setScale(containerScaleX, containerScaleY);

        setContainerHoverColor(container, label);

        add(container).width(container.getWidth()).height(container.getHeight());
        add(label).width(Gdx.graphics.getWidth() * 0.1f).padLeft(Gdx.graphics.getWidth() * 0.06f).padBottom(Gdx.graphics.getHeight() * 0.015f);
    }

    private Slider initializeSlider(String type) {
        Slider slider = new Slider(0f, 1f, 0.1f, false, AssetLoader.mySkin);
        initializeSliderValue(slider, type);
        addSliderListener(slider, type);
        return slider;
    }

    private void addSliderListener(Slider slider, String type) {
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setChangedValue(type);
                AssetLoader.click1Sound.play(BaseGame.musicVolume);
                GameUtils.saveGameState();
            }
        });
    }

    private void initializeSliderValue(Slider slider, String type) {
        switch (type) {
            case "sound":
                slider.setValue(getSquareValue(BaseGame.soundVolume));
                break;
            case "music":
                slider.setValue(getSquareValue(BaseGame.musicVolume));
                break;
            case "voice":
                slider.setValue(getSquareValue(BaseGame.voiceVolume));
                break;
            case "vibration":
                slider.setValue(BaseGame.vibrationStrength);
                break;
            default:
                Gdx.app.error(getClass().getSimpleName(), "Error, could not set value for type: " + type);
        }
    }

    private void setChangedValue(String type) {
        BaseGame.musicVolume = slider.getValue();
        switch (type) {
            case "sound":
                BaseGame.soundVolume = getExponentialValue(slider.getValue());
                break;
            case "music":
                GameUtils.setMusicVolume(getExponentialValue(slider.getValue()));
                break;
            case "voice":
                BaseGame.voiceVolume = getExponentialValue(slider.getValue());
                break;
            case "vibration":
                BaseGame.vibrationStrength = getExponentialValue(slider.getValue());
                //GameUtils.vibrateController(1, 100);
                break;
            default:
                Gdx.app.error(getClass().getSimpleName(), "Error, could not save value for type: " + type);
        }
    }

    private TypingLabel labelInit(String labelText) {
        TypingLabel label = new TypingLabel(labelText, AssetLoader.getLabelStyle("Play-Bold20white"));
        GameUtils.addWidgetEnterExitEffect(label, Color.LIGHT_GRAY, Color.WHITE);
        return label;
    }

    private void setContainerHoverColor(Container<Slider> container, TypingLabel label) {
        container.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                label.setColor(Color.GRAY);
                AssetLoader.hoverOverEnterSound.play(BaseGame.soundVolume);
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                label.setColor(Color.WHITE);
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }

    private float getExponentialValue(float value) {
        return (float) (pow(value * 10, 2) / 100);
    }

    private float getSquareValue(float result) {
        return (float) (sqrt(result * 100) / 10);
    }
}
