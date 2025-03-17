package no.sandramoen.libgdx32.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.Dimension;
import java.util.Locale;

import no.sandramoen.libgdx32.MyGdxGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MyGdxGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("libGDX jam #31");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        //configuration.setWindowedMode(640, 480);
        //// You can change these files; they are in lwjgl3/src/main/resources/.

        configuration.useVsync(true);
        configuration.setWindowIcon("images/excluded/icon_16x16.png", "images/excluded/icon_32x32.png", "images/excluded/icon_64x64.png");

        boolean isFullscreen = false;
        if (isFullscreen)
            configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        else
            setWindowedMode(.25f, configuration);
        return configuration;
    }

    private static String getCountryCode() {
        String countryCode = Locale.getDefault().getCountry().toLowerCase(Locale.ROOT);
        System.out.println("[DesktopLauncher] Locale => Country code: " + countryCode);
        return countryCode;
    }

    private static void setWindowedMode(float percentOfScreenSize, Lwjgl3ApplicationConfiguration config) {
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (dimension.width * percentOfScreenSize);

        float aspectRatio = 9 / 16f;
        int height = (int) (width / aspectRatio);

        System.out.println("[DesktopLauncher] Window dimensions => width: " + width + ", height: " + height);
        config.setWindowedMode(width, height);
    }
}
