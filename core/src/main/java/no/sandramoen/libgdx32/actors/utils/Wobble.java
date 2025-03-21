package no.sandramoen.libgdx32.actors.utils;

import static no.sandramoen.libgdx32.utils.BaseGame.WORLD_HEIGHT;
import static no.sandramoen.libgdx32.utils.BaseGame.WORLD_WIDTH;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.TimeUtils;

import static no.sandramoen.libgdx32.utils.BaseGame.WORLD_HEIGHT;
import static no.sandramoen.libgdx32.utils.BaseGame.WORLD_WIDTH;

public class Wobble {
    /**
     * Sway smoothly using bicubic interpolation between 4 points (the two integers before t and the two after).
     * This pretty much never produces steep changes between peaks and valleys; this may make it more useful for things
     * like generating terrain that can be walked across in a side-scrolling game.
     *
     * @param t    a distance traveled; should change by less than 1 between calls, and should be less than about 10000
     * @param seed any long
     * @return a smoothly-interpolated swaying value between -1 and 1, both exclusive
     */
    public static float bicubicWobble(float t, long seed)
    {
        final long floor = (long) Math.floor(t);
        // what we add here ensures that at the very least, the upper half will have some non-zero bits.
        long s = ((seed & 0xFFFFFFFFL) ^ (seed >>> 32)) + 0x9E3779B97F4A7C15L;
        // fancy XOR-rotate-rotate is a way to mix bits both up and down without multiplication.
        s = (s ^ (s << 21 | s >>> 43) ^ (s << 50 | s >>> 14)) + floor;
        // we use a different technique here, relative to other wobble methods.
        // to avoid frequent multiplication and replace it with addition by constants, we track 3 variables, each of
        // which updates with a different large, negative long increment. when we want to get a result, we just XOR
        // m, n, and o, and use only the upper bits (by multiplying by a tiny fraction).
        final long m = s * 0xD1B54A32D192ED03L;
        final long n = s * 0xABC98388FB8FAC03L;
        final long o = s * 0x8CB92BA72F3D8DD7L;

        final float a = (m ^ n ^ o);
        final float b = (m + 0xD1B54A32D192ED03L ^ n + 0xABC98388FB8FAC03L ^ o + 0x8CB92BA72F3D8DD7L);
        final float c = (m + 0xA36A9465A325DA06L ^ n + 0x57930711F71F5806L ^ o + 0x1972574E5E7B1BAEL);
        final float d = (m + 0x751FDE9874B8C709L ^ n + 0x035C8A9AF2AF0409L ^ o + 0xA62B82F58DB8A985L);

        // get the fractional part of t.
        t -= floor;
        // this is bicubic interpolation, inlined
        final float p = (d - c) - (a - b);
        // 7.7.228014483236334E-20 , or 0x1.5555555555428p-64 , is just inside {@code -2f/3f/Long.MIN_VALUE} .
        // it gets us about as close as we can go to 1.0 .
        return (t * (t * t * p + t * (a - b - p) + c - a) + b) * 7.228014E-20f;
    }

    public static TemporalAction shakeCamera(float duration, Interpolation interpolation,
                                             Camera cam, final float speed, final float distance) {
        return new TemporalAction(duration, interpolation) {
            float xMove = 0f, yMove = 0f, startTime = 0f, camX = 0f, camY = 0f;
            @Override
            protected void begin() {
                startTime = (TimeUtils.millis() & 0xFFFFFL);
                camX = WORLD_WIDTH / 2f - 0.5f;
                camY = WORLD_HEIGHT / 2f - 0.5f;
//                System.out.println("at time " + (startTime * 0.001f) + ", cam position is " + cam.position);
                super.begin();
            }

            @Override
            protected void update(float percent) {
                xMove = bicubicWobble(percent * speed + startTime, 0x1234567890ABCDEFL) * distance * (1f - getTime());
                yMove = bicubicWobble(percent * speed + startTime + 0.618f, 0xFEDCBA0987654321L) * distance * (1f - getTime());
                cam.position.set(camX + xMove, camY + yMove, 0f);
            }

            @Override
            protected void end() {
                cam.position.set(camX, camY, 0f);
                super.end();
            }
        };
    }

}
