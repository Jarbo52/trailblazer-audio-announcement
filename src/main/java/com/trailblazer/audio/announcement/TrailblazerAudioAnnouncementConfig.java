package com.trailblazer.audio.announcement;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(TrailblazerAudioAnnouncementConfig.GROUP)
public interface TrailblazerAudioAnnouncementConfig extends Config
{
	String GROUP = "trailblazeraudioannouncement";

	@ConfigItem(
		keyName = "playCombatAchievement",
		name = "Combat Achievements",
		description = "Should the jingle play when you complete a combat achievement?",
		position = 0
	)
	default boolean playCombatAchievement() { return true; }

	@ConfigItem(
			keyName = "playCollectionLog",
			name = "Collection Log Slot",
			description = "Should the jingle play when you fill in a collection log slot?",
			position = 1
	)
	default boolean playCollectionLog() { return true; }

}
