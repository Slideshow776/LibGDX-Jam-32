package no.sandramoen.libgdx32.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.FWSkin;
import com.github.tommyettinger.textra.FWSkinLoader;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.Styles;

public class AssetLoader implements AssetErrorListener {

    public static TextureAtlas textureAtlas;
    public static FWSkin mySkin;

    public static String defaultShader;
    public static String shockwaveShader;
    public static String backgroundShader;

    public static Sound player_shoot_0_sound;
    public static Sound player_heart_beat_sound;
    public static Sound shield_up_sound;
    public static Sound shield_down_sound;
    public static Sound shield_hit_sound;
    public static Sound enemy_shoot_0_sound;

    public static Array<Music> music;
    public static Music levelMusic;
    public static Music ambientMusic;

    static {
        long time = System.currentTimeMillis();
        BaseGame.assetManager = new AssetManager();
        BaseGame.assetManager. setLoader(Skin. class, new FWSkinLoader(BaseGame.assetManager. getFileHandleResolver()));
        BaseGame.assetManager.setErrorListener(new AssetLoader());

        loadAssets();
        BaseGame.assetManager.finishLoading();
        assignAssets();

        Gdx.app.log(AssetLoader.class.getSimpleName(), "Asset manager took " + (System.currentTimeMillis() - time) + " ms to load all game assets.");
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(AssetLoader.class.getSimpleName(), "Could not load asset: " + asset.fileName, throwable);
    }

    public static Styles.LabelStyle getLabelStyle(String fontName) {
        return new Styles.LabelStyle(
            new Font(
                AssetLoader.mySkin.get(fontName, Font.class)
            ), Color.WHITE);
    }

    private static void loadAssets() {
        // images
        BaseGame.assetManager.setLoader(Text.class, new TextLoader(new InternalFileHandleResolver()));
        BaseGame.assetManager.load("images/included/packed/images.pack.atlas", TextureAtlas.class);

        // music
        BaseGame.assetManager.load("audio/music/361424__furbyguy__synth-metal-rock-loop.wav", Music.class);
        BaseGame.assetManager.load("audio/music/ambient_wind.wav", Music.class);

        // sounds
        BaseGame.assetManager.load("audio/sounds/player/Laser_Shoot48.wav", Sound.class);
        BaseGame.assetManager.load("audio/sounds/player/heart_beat.wav", Sound.class);
        BaseGame.assetManager.load("audio/sounds/player/shield_down.wav", Sound.class);
        BaseGame.assetManager.load("audio/sounds/player/shield_up.wav", Sound.class);
        BaseGame.assetManager.load("audio/sounds/player/shield_hit.wav", Sound.class);
        BaseGame.assetManager.load("audio/sounds/enemy/laser_0.wav", Sound.class);

        // i18n

        // shaders
        BaseGame.assetManager.load(new AssetDescriptor("shaders/default.vs", Text.class, new TextLoader.TextParameter()));
        BaseGame.assetManager.load(new AssetDescriptor("shaders/shockwave.fs", Text.class, new TextLoader.TextParameter()));
        BaseGame.assetManager.load(new AssetDescriptor("shaders/voronoi.fs", Text.class, new TextLoader.TextParameter()));

        // skins

        // fonts

        // tiled maps
        //BaseGame.assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        //BaseGame.assetManager.load("maps/test.tmx", TiledMap.class);

        // other
        // BaseGame.assetManager.load(AssetDescriptor("other/jentenavn.csv", Text::class.java, TextLoader.TextParameter()))
    }

    private static void assignAssets() {
        // images
        textureAtlas = BaseGame.assetManager.get("images/included/packed/images.pack.atlas");

        // music
        music = new Array();
        levelMusic = BaseGame.assetManager.get("audio/music/361424__furbyguy__synth-metal-rock-loop.wav", Music.class);
        ambientMusic = BaseGame.assetManager.get("audio/music/ambient_wind.wav", Music.class);
        music.add(levelMusic);

        // sounds
        player_shoot_0_sound = BaseGame.assetManager.get("audio/sounds/player/Laser_Shoot48.wav", Sound.class);
        player_heart_beat_sound = BaseGame.assetManager.get("audio/sounds/player/heart_beat.wav", Sound.class);
        shield_up_sound = BaseGame.assetManager.get("audio/sounds/player/shield_up.wav", Sound.class);
        shield_down_sound = BaseGame.assetManager.get("audio/sounds/player/shield_down.wav", Sound.class);
        shield_hit_sound = BaseGame.assetManager.get("audio/sounds/player/shield_hit.wav", Sound.class);
        enemy_shoot_0_sound = BaseGame.assetManager.get("audio/sounds/enemy/laser_0.wav", Sound.class);

        // i18n

        // shaders
        defaultShader = BaseGame.assetManager.get("shaders/default.vs", Text.class).getString();
        shockwaveShader = BaseGame.assetManager.get("shaders/shockwave.fs", Text.class).getString();
        backgroundShader = BaseGame.assetManager.get("shaders/voronoi.fs", Text.class).getString();

        // skins
        mySkin = new FWSkin(Gdx.files.internal("skins/mySkin/mySkin.json"));

        // fonts
        loadFonts();

        // tiled maps
        //loadTiledMap();

        // other
    }

    private static void loadFonts() {
        float scale = Gdx.graphics.getWidth() * .05f; // magic number ensures scale ~= 1, based on screen width
        scale *= 1.01f; // make x percent bigger, bigger = more fuzzy

        mySkin.get("Play-Bold20white", Font.class).scale(scale);
        mySkin.get("Play-Bold40white", Font.class).scale(scale);
        mySkin.get("Play-Bold59white", Font.class).scale(scale);
    }

    private static void loadTiledMap() {
        /*testMap = BaseGame.assetManager.get("maps/test.tmx", TiledMap.class);
        level1 = BaseGame.assetManager.get("maps/level1.tmx", TiledMap.class);
        level2 = BaseGame.assetManager.get("maps/level2.tmx", TiledMap.class);

        maps = new Array();
        maps.add(testMap);
        maps.add(level1);
        maps.add(level2);*/
    }
}
