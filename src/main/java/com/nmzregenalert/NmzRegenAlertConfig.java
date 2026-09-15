package com.nmzregenalert;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(NmzRegenAlertConfig.GROUP)
public interface NmzRegenAlertConfig extends Config
{
	String GROUP = "nmzregenalert";

	@ConfigItem(
		keyName = "onlyInNmz",
		name = "Only in Nightmare Zone",
		description = "Only warn while inside a Nightmare Zone dream",
		position = 0
	)
	default boolean onlyInNmz()
	{
		return true;
	}

	@Range(min = 1, max = 30)
	@Units(Units.SECONDS)
	@ConfigItem(
		keyName = "warningSeconds",
		name = "Warning time",
		description = "How long before your hitpoints regenerate to start warning you",
		position = 1
	)
	default int warningSeconds()
	{
		return 5;
	}

	@ConfigItem(
		keyName = "flashScreen",
		name = "Flash screen",
		description = "Flash the game view until you flick Rapid Heal or your hitpoints regenerate",
		position = 2
	)
	default boolean flashScreen()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "flashColor",
		name = "Flash colour",
		description = "Colour of the screen flash",
		position = 3
	)
	default Color flashColor()
	{
		return new Color(255, 0, 0, 70);
	}

	@ConfigItem(
		keyName = "notification",
		name = "Notification",
		description = "Send a notification (sound, tray message, etc.) when the warning starts",
		position = 4
	)
	default Notification notification()
	{
		return Notification.ON;
	}
}
