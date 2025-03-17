package no.sandramoen.libgdx32.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseGame;
import no.sandramoen.libgdx32.utils.GameUtils;


public class BaseCheckbox extends Table {

    public CheckBox checkBox;
    private TypingLabel label;
    private boolean isJustClicked;

    public BaseCheckbox(String text) {
        label = new TypingLabel(text, AssetLoader.getLabelStyle("Play-Bold20white"));
        label.setColor(Color.WHITE);
        GameUtils.setWidgetHoverColor(label);

        checkBox = new CheckBox("", AssetLoader.mySkin);

        setContainerHoverColor(checkBox, label);

        add(label).padRight(Gdx.graphics.getWidth() * .212f);
        add(checkBox).expandX().left();

        /*setDebug(true);*/
    }

    private void setContainerHoverColor(CheckBox checkBox, TypingLabel label) {
        checkBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                isJustClicked = true;
                label.setColor(Color.FIREBRICK);
                /*if (checkBox.isChecked())
                    BaseGame.isHeadBobbing = true;
                else if (!checkBox.isChecked())
                    BaseGame.isHeadBobbing = false;*/
                GameUtils.saveGameState();
                return;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                label.setColor(Color.FIREBRICK);
                AssetLoader.hoverOverEnterSound.play(BaseGame.soundVolume);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (isJustClicked)
                    label.setColor(Color.FIREBRICK);
                else
                    label.setColor(Color.WHITE);
                isJustClicked = false;
            }
        });
    }
}
