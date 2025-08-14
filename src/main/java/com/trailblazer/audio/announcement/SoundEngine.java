package com.trailblazer.audio.announcement;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Singleton
@Slf4j
public class SoundEngine {

    @Inject
    private TrailblazerAudioAnnouncementConfig config;

    @Inject
    private AudioPlayer audioPlayer;

    public void playClip(Sound sound) {
        float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);

        try {
            audioPlayer.play(SoundEngine.class.getResourceAsStream(sound.getResourceName()), gain);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            log.warn("Failed to load jingle " + sound, e);
        }
    }
}