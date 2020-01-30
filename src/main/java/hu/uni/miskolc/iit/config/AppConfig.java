package hu.uni.miskolc.iit.config;

import hu.uni.miskolc.iit.game.objects.Coal;
import hu.uni.miskolc.iit.game.objects.FallenObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConfig {

    @Getter(AccessLevel.PRIVATE)
    private static AppConfig INSTANCE;
    @Getter(AccessLevel.PRIVATE)
    private static String PROPERTY_FILE_NAME = "config.properties";

    private Textures textures;
    private Sounds sounds;
    private Application application;
    private Window window;
    private Sprites sprites;
    private Scenes scenes;
    private Resources resources;
    private Shaders shaders;
    private Players players;

    public static AppConfig appConfig() {

        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
            INSTANCE.loadConfig();
        }
        return INSTANCE;
    }

    private void loadConfig() {
        Properties prop = new Properties();
        String propFileName = PROPERTY_FILE_NAME;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            sounds = new Sounds(prop.getProperty("sounds.location"), prop.getProperty("sounds.extension"));
            textures = new Textures(prop.getProperty("textures.location"), prop.getProperty("textures.extension"));
            application = new Application(prop.getProperty("application.name"));
            window = new Window(Integer.parseInt(prop.getProperty("window.width")), Integer.parseInt(prop.getProperty("window.height")), Boolean.parseBoolean(prop.getProperty("window.vsync")));
            sprites = new Sprites(prop.getProperty("sprites.location"));
            scenes = new Scenes(new Scenes.Background(prop.getProperty("scenes.background.location")));
            resources = new Resources(prop.getProperty("resources.location"));
            shaders = new Shaders(prop.getProperty("shaders.location"),
                    new Shaders.Vertex(prop.getProperty("shaders.vertex.vertex.name"), prop.getProperty("shaders.vertex.extension")),
                    new Shaders.Vertex(prop.getProperty("shaders.vertex.line.name"), prop.getProperty("shaders.vertex.extension")),
                    new Shaders.Fragment(prop.getProperty("shaders.fragment.fragment.name"), prop.getProperty("shaders.fragment.extension")),
                    new Shaders.Fragment(prop.getProperty("shaders.fragment.line.name"), prop.getProperty("shaders.vertex.extension")),
                    new Shaders.Uniforms(
                            prop.getProperty("shaders.uniforms.projectionMatrix"),
                            prop.getProperty("shaders.uniforms.worldMatrix"),
                            prop.getProperty("shaders.uniforms.modelMatrix"),
                            prop.getProperty("shaders.uniforms.texture_sampler"),
                            prop.getProperty("shaders.uniforms.texture_color"),
                            prop.getProperty("shaders.uniforms.line_color")
                    )
            );
            players = new Players(
                    new Players.FallenObjects(
                            new Players.FallenObjects.Coal(
                                    Integer.parseInt(prop.getProperty("players.fallenobjects.coals.count")),
                                    prop.getProperty("players.fallenobjects.coals.location")
                            ),

                            new Players.FallenObjects.Cookie(
                                    Integer.parseInt(prop.getProperty("players.fallenobjects.cookies.count")),
                                    prop.getProperty("players.fallenobjects.cookies.location")
                            ),

                            new Players.FallenObjects.Milk(
                                    Integer.parseInt(prop.getProperty("players.fallenobjects.milks.count")),
                                    prop.getProperty("players.fallenobjects.milks.location")
                            ),
                            new Players.FallenObjects.Present(
                                    Integer.parseInt(prop.getProperty("players.fallenobjects.presents.count")),
                                    prop.getProperty("players.fallenobjects.presents.location")
                            )
                    ),
                    new Players.Score(Integer.parseInt(prop.getProperty("players.score.maxDigit")),
                            prop.getProperty("players.score.location")),
                    new Players.LifeBar(prop.getProperty("players.life_bar.location")),
                    new Players.Santa(prop.getProperty("players.santa.location"))

            );
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Application {
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Window {
        private final int width;
        private final int height;
        private final boolean vSync;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Textures {
        private final String location;
        private final String extension;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Sounds {
        private final String location;
        private final String extension;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Sprites {
        private final String location;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Scenes {
        private final Background background;

        @Getter
        @RequiredArgsConstructor
        public static final class Background {
            private final String location;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Resources {
        private final String location;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Players {
        private final FallenObjects fallenObject;
        private final Score score;
        private final LifeBar lifeBar;
        private final Santa santa;

        @Getter
        @RequiredArgsConstructor
        public static final class Score {
            private final int maxDigit;
            private final String location;
        }
        @Getter
        @RequiredArgsConstructor
        public static final class LifeBar {
            private final String location;
        }
        @Getter
        @RequiredArgsConstructor
        public static final class Santa {
            private final String location;
        }

        @Getter
        @RequiredArgsConstructor
        public static final class FallenObjects {

            private final Coal coal;
            private final Cookie cookie;
            private final Milk milk;
            private final Present present;

            @Getter
            @RequiredArgsConstructor
            public static final class Coal {
                private final int count;
                private final String location;
            }

            @Getter
            @RequiredArgsConstructor
            public static final class Cookie {
                private final int count;
                private final String location;
            }

            @Getter
            @RequiredArgsConstructor
            public static final class Milk {
                private final int count;
                private final String location;
            }

            @Getter
            @RequiredArgsConstructor
            public static final class Present {
                private final int count;
                private final String location;
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static final class Shaders {
        private final String location;

        private final Vertex vertex;
        private final Vertex lineVertex;
        private final Fragment fragment;
        private final Fragment lineFragment;
        private final Uniforms uniforms;

        @Getter
        @RequiredArgsConstructor
        public static final class Vertex {
            private final String name;
            private final String extension;
        }

        @Getter
        @RequiredArgsConstructor
        public static final class Fragment {
            private final String name;
            private final String extension;
        }

        @Getter
        @RequiredArgsConstructor
        public static final class Uniforms {
            private final String projectionMatrix;
            private final String worldMatrix;
            private final String modelMatrix;
            private final String texture_sampler;
            private final String texture_color;
            private final String line_color;

        }
    }

}
