package hu.uni.miskolc.iit.engine.sound;

import org.lwjgl.openal.AL10;

public class Source {
    private int sourceId;
    private int buffer;

    public Source() {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
    }

    public void play() {
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlay();
    }

    public void init(final int buffer){
        this.buffer=buffer;
    }

    public void pause() {
        AL10.alSourcePause(sourceId);
    }

    public void continuePlay() {
        AL10.alSourcePlay(sourceId);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void delete() {
        stop();
        AL10.alDeleteSources(sourceId);
    }

}