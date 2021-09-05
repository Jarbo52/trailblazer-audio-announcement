package com.trailblazer.audio.announcement;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TrailblazerAudioAnnouncementPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TrailblazerAudioAnnouncementPlugin.class);
		RuneLite.main(args);
	}
}