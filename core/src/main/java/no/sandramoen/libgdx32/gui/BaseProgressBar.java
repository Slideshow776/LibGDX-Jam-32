package no.sandramoen.libgdx32.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.Styles;
import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.libgdx32.utils.AssetLoader;
import no.sandramoen.libgdx32.utils.BaseActor;


public class BaseProgressBar extends BaseActor {

    public int level = 0; // Store level as an integer from 0 to 100
    public float animationDuration = 0.25f;

    private BaseActor progress;
    private TypingLabel label;

    public BaseProgressBar(float x, float y, Stage stage) {
        super(0f, 0f, stage);

        loadImage("whitePixel");
        setColor(new Color(0.035f, 0.039f, 0.078f, 1f));
        setSize(Gdx.graphics.getWidth() * 0.9f, Gdx.graphics.getHeight() * 0.05f);
        setPosition(x, y - getHeight());
        setOrigin(Align.center);

        progress = new BaseActor(0f, 0f, stage);
        progress.loadImage("whitePixel");
        progress.setColor(new Color(0.875f, 0.518f, 0.647f, 1f)); // light pink
        progress.setSize(0.0f, getHeight());
        progress.setOrigin(Align.center);
        addActor(progress);

        Styles.LabelStyle levelStyle = AssetLoader.getLabelStyle("Play-Bold59white");
        levelStyle.font.scale(0.5f);
        label = new TypingLabel(level + " / 100", levelStyle);
        label.setWidth(getWidth());
        label.setAlignment(Align.center);
        label.skipToTheEnd(true, true);
        label.setColor(new Color(0.922f, 0.929f, 0.914f, 1f)); // white
       /* label.setPosition(
            getWidth() * 0.5f - label.getFont().calculateSize(label.getWorkingLayout()) * 0.5f,
            -getHeight() * 2.1f);*/
        label.setVisible(false);
        addActor(label);

        // Just shows where the exact center of the bar is.
//        BaseActor guideline = new BaseActor(getWidth() * 0.5f, 0f, stage);
//        guideline.loadImage("whitePixel");
//        guideline.setColor(new Color(0.2f, 0.8f, 0.5f, 1f)); // mid aqua
//        guideline.setSize(2f, getHeight());
//        addActor(guideline);

    }


    public void setProgress(int percentage) {
        level = Math.min(level + percentage, 100);
        float newWidth = (float) level / 100 * getWidth();
        progress.setSize(newWidth, getHeight());
    }


    // Increment the progress bar by a certain percentage (in integer values)
    public void incrementPercentage(int percentage) {
        if (percentage == 0)
            return;

        // Make sure the percentage is within the valid range [0, 100]
        level = Math.min(level + percentage, 100); // Clamp to 100%

        // Calculate the new width based on the level percentage
        float newWidth = (float) level / 100 * getWidth();
        progress.addAction(Actions.sizeTo(newWidth, getHeight(), animationDuration));

        // Update the label with the new level
        label.setText(level + " / 100", true);
        label.setPosition(
            getWidth() * 0.5f - label.getFont().calculateSize(label.getWorkingLayout()) * 0.5f,
            getHeight() * 0.55f);


        Action action = Actions.sequence(
            Actions.delay(0.2f),
            Actions.scaleTo(1.0f, 1.1f, 0.2f, Interpolation.elasticOut),
            Actions.scaleTo(1.0f, 0.9f, 0.2f, Interpolation.elasticOut),
            Actions.scaleTo(1.0f, 1.0f, 1.25f, Interpolation.elasticOut)
        );
        addAction(action);
        progress.addAction(action);
    }

    // Decrement the progress bar by a certain percentage (in integer values)
    public void decrementPercentage(int percentage) {
        // Make sure the percentage is within the valid range [0, 100]
        level = Math.max(level - percentage, 0); // Clamp to 0%

        // Calculate the new width based on the level percentage
        float newWidth = (float) level / 100 * getWidth();
        progress.addAction(Actions.sizeTo(newWidth, getHeight(), animationDuration));

        Action action = Actions.sequence(
            Actions.delay(0.2f),
            Actions.scaleTo(1.0f, 0.9f, 0.3f, Interpolation.bounceOut),
            Actions.scaleTo(1.0f, 1.1f, 0.3f, Interpolation.bounceOut),
            Actions.scaleTo(1.0f, 1.0f, 2.5f, Interpolation.bounceOut)
        );
        addAction(action);
        progress.addAction(action);

        // Update the label with the new level
        label.setText(level + " / 100", true);
        label.setPosition(
            getWidth() * 0.5f - label.getFont().calculateSize(label.getWorkingLayout()) * 0.5f,
            getHeight() * 0.55f);
    }

    // Set the color of the progress bar
    public void setProgressBarColor(Color color) {
        if (progress != null) {
            progress.setColor(color);
        }
    }
}
