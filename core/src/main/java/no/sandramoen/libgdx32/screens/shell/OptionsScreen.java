package no.sandramoen.libgdx32.screens.shell;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.libgdx32.gui.BaseSlider;
import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.BaseScreen;
import no.sandramoen.libgdx32.utils.GameUtils;


public class OptionsScreen extends BaseScreen {

    @Override
    public void initialize() {
        TypingLabel mainLabel = new TypingLabel("Options", AssetLoader.mySkin);
        mainLabel.getFont().scale(.8f, .8f);
        uiTable.add(mainLabel)
                .growY()
                .padBottom(-Gdx.graphics.getHeight() * .15f)
                .row();

        uiTable.add(optionsTable())
                .growY()
                .row();

        uiTable.add(initializeBackButton())
                .expandY()
                .width(Gdx.graphics.getWidth() * .125f)
                .height(Gdx.graphics.getHeight() * .075f);

        /*uiTable.setDebug(true);*/
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.Q)
            BaseGame.setActiveScreen(new MenuScreen());
        return super.keyDown(keycode);
    }

    private Table optionsTable() {
        Table table = new Table();

        BaseSlider soundSlider = new BaseSlider("Sound", "Sound");
        BaseSlider musicSlider = new BaseSlider("Music", "Music");
        BaseSlider voiceSlider = new BaseSlider("Voice", "Voice");

        table.defaults().spaceTop(Gdx.graphics.getHeight() * .05f).width(Gdx.graphics.getWidth() * .6f);
        table.add(soundSlider).row();
        table.add(musicSlider).row();
        table.add(voiceSlider).row();

        /*table.setDebug(true);*/
        return table;
    }

    private TextButton initializeBackButton() {
        TextButton backButton = new TextButton("Back", AssetLoader.mySkin);
        backButton.addListener(
                (Event event) -> {
                    if (GameUtils.isTouchDownEvent(event))
                        BaseGame.setActiveScreen(new MenuScreen());
                    return false;
                }
        );
        return backButton;
    }
}
