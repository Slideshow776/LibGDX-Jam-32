package no.sandramoen.libgdx32.actors;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdx32.utils.BaseActor;
import no.sandramoen.libgdx32.utils.GameUtils;


public class Enemy extends BaseActor {

    public int health = 3;


    public Enemy(float x, float y, Stage s) {
        super(x, y, s);
        loadImage("player_test");

        setSize(2, 2);
        centerAtPosition(x, y);
        setOrigin(Align.center);
    }

    public void setHealth(int new_health) {
        int temp = health;
        health = new_health;

        if (temp > health) {
            take_damage();
        } else if (temp < health) {
            heal();
        }

        if (health <= 0)
            die();
    }


    private void take_damage() {
        addAction(Actions.sequence(
            Actions.rotateTo(10, 0.1f),
            Actions.rotateTo(-10, 0.2f),
            Actions.rotateTo(0, 0.1f)
        ));
    }


    private void heal() {}


    private void die() {
        setOpacity(0);
    }
}
