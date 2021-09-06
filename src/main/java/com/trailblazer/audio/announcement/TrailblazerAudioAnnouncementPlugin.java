package com.trailblazer.audio.announcement;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Trailblazer Audio Announcement",
	description = "Plays the Leagues 2: Trailblazer jingle when completing specific tasks",
	tags = {"trailblazer", "leagues", "sound", "combat achievements", "collection log"}
)
public class TrailblazerAudioAnnouncementPlugin extends Plugin
{
	@Inject
	private Client client;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private TrailblazerAudioAnnouncementConfig config;

	private static final Pattern COLLECTION_LOG_ITEM_REGEX = Pattern.compile("New item added to your collection log:.*");
	private static final Pattern COMBAT_TASK_REGEX = Pattern.compile("Congratulations, you've completed an? (?:\\w+) combat task:.*");

	private int ticksSinceLogin = 0;
	private boolean resetTicks = false;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Trailblazer Audio Announcement started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Trailblazer Audio Announcement stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		switch(event.getGameState())
		{
			case LOGIN_SCREEN:
			case HOPPING:
			case LOGGING_IN:
			case LOGIN_SCREEN_AUTHENTICATOR:
			case CONNECTION_LOST:
				resetTicks = true;
				// set to 0 here in-case of race condition with varbits changing before this handler is called
				// when game state becomes LOGGED_IN
				ticksSinceLogin = 0;
				break;
			case LOGGED_IN:
				if (resetTicks) {
					resetTicks = false;
					ticksSinceLogin = 0;
				}
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		ticksSinceLogin++;
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM) {
			return;
		}

		if (config.playCollectionLog() && COLLECTION_LOG_ITEM_REGEX.matcher(chatMessage.getMessage()).matches()) {
			soundEngine.playClip(Sound.JINGLE);
		} else if (config.playCombatAchievement() && COMBAT_TASK_REGEX.matcher(chatMessage.getMessage()).matches()) {
			soundEngine.playClip(Sound.JINGLE);
		}
	}

	@Provides
	TrailblazerAudioAnnouncementConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrailblazerAudioAnnouncementConfig.class);
	}
}
