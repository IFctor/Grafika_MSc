package hu.uni.miskolc.iit.engine.sound;

import hu.uni.miskolc.iit.config.AppConfig;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.ALC10.*;

public class AudioMaster {

    private static final String SYS_EXTENSION_SEPARATOR = ".";
    private static final String SOUNDS_LOCATION = AppConfig.appConfig().getSounds().getLocation() + File.separator;
    private static final String SOUNDS_EXTENSION = SYS_EXTENSION_SEPARATOR + AppConfig.appConfig().getSounds().getExtension();
    public static int GAME_MUSIC;
    public static int HOHOHO_SOUND;
    public static int DEATH_SOUND;
    public static int HIT_SOUND;
    public static int DRINK_SOUND;
    public static int EAT_SOUND;
    private static List<Integer> buffers = new ArrayList<>();
    private static long device;
    private static long context;

    public static void init() {
        final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);

        AL.createCapabilities(ALC.createCapabilities(device));

        initDefault();
    }

    private static void initDefault() {
        GAME_MUSIC = loadSound(SOUNDS_LOCATION + "JingleBells" + SOUNDS_EXTENSION);
        DEATH_SOUND = loadSound(SOUNDS_LOCATION + "Dead" + SOUNDS_EXTENSION);
        HOHOHO_SOUND = loadSound(SOUNDS_LOCATION + "Ho_ho_ho" + SOUNDS_EXTENSION);
        HIT_SOUND = loadSound(SOUNDS_LOCATION + "Pain" + SOUNDS_EXTENSION);
        DRINK_SOUND = loadSound(SOUNDS_LOCATION + "Slurping" + SOUNDS_EXTENSION);
        EAT_SOUND = loadSound(SOUNDS_LOCATION + "Eat" + SOUNDS_EXTENSION);
    }

    public static void setListenerData() {
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    public static int loadSound(final String file) {
        final int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData wavFile = WaveData.create(file);
        AL10.alBufferData(buffer, wavFile.format, wavFile.data, wavFile.samplerate);
        wavFile.dispose();
        return buffer;
    }

    public static void cleanUp() {
        for (final int buffer : buffers) {
            alDeleteBuffers(buffer);
        }
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

}