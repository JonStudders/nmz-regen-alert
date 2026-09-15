package com.nmzregenalert;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class RegenFlashOverlay extends Overlay
{
	// Length of each on/off half of the flash
	private static final long FLASH_PERIOD_MILLIS = 300;

	private final Client client;
	private final NmzRegenAlertPlugin plugin;
	private final NmzRegenAlertConfig config;

	@Inject
	RegenFlashOverlay(Client client, NmzRegenAlertPlugin plugin, NmzRegenAlertConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		// Drawn under interfaces so the prayer orb and inventory stay fully visible
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isWarning() || !config.flashScreen())
		{
			return null;
		}

		if ((System.currentTimeMillis() / FLASH_PERIOD_MILLIS) % 2 == 0)
		{
			graphics.setColor(config.flashColor());
			graphics.fillRect(client.getViewportXOffset(), client.getViewportYOffset(),
				client.getViewportWidth(), client.getViewportHeight());
		}

		return null;
	}
}
