package com.nmzregenalert;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NmzRegenAlertPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NmzRegenAlertPlugin.class);
		RuneLite.main(args);
	}
}
