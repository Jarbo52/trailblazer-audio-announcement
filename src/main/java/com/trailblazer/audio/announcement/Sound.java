package com.trailblazer.audio.announcement;

public enum Sound {
    JINGLE("/trailblazer_league_task_completion_jingle.wav");

    private final String resourceName;

    Sound(String resourceName) {
        this.resourceName = resourceName;
    }

    String getResourceName() {
        return this.resourceName;
    }
}
