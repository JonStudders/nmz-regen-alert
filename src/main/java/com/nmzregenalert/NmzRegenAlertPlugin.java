package com.nmzregenalert;

import com.google.inject.Provides;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.WorldView;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "NMZ Regen Alert",
	description = "Flashes the screen and notifies you just before your hitpoints regenerate",
	tags = {"nmz", "nightmare", "zone", "regen", "rapid", "heal", "flick", "absorption"}
)
public class NmzRegenAlertPlugin extends Plugin
{
	// Hitpoints regenerate every 100 ticks, halved while Rapid Heal is active
	private static final int NORMAL_HP_REGEN_TICKS = 100;
	// Nightmare Zone dreams are an instance of this region, above ground level
	private static final int[] NMZ_MAP_REGION = {9033};
	private static final double TICK_MILLIS = 600.0;

	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private RegenFlashOverlay overlay;

	@Inject
	private NmzRegenAlertConfig config;

	// Starts at -2 to match the core Regeneration Meter's login/hop offset
	private int ticksSinceHpRegen = -2;
	private boolean notified;

	@Getter(AccessLevel.PACKAGE)
	private boolean warning;

	@Provides
	NmzRegenAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NmzRegenAlertConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		warning = false;
		notified = false;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		final GameState state = event.getGameState();
		if (state == GameState.HOPPING || state == GameState.LOGIN_SCREEN)
		{
			ticksSinceHpRegen = -2;
			warning = false;
			notified = false;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// Toggling Rapid Heal (the "flick") resets the hitpoints regeneration timer
		if (event.getVarbitId() == VarbitID.PRAYER_RAPIDHEAL)
		{
			ticksSinceHpRegen = 0;
			warning = false;
			notified = false;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		int ticksPerHpRegen = NORMAL_HP_REGEN_TICKS;
		if (client.getVarbitValue(VarbitID.PRAYER_RAPIDHEAL) == 1)
		{
			ticksPerHpRegen /= 2;
		}

		ticksSinceHpRegen = (ticksSinceHpRegen + 1) % ticksPerHpRegen;
		final int ticksUntilRegen = ticksPerHpRegen - ticksSinceHpRegen;
		final int warningTicks = (int) Math.ceil(config.warningSeconds() * 1000 / TICK_MILLIS);

		// Hitpoints only regenerate upwards while below their real level
		final boolean belowMax = client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS);
		final boolean eligible = belowMax && (!config.onlyInNmz() || isInNightmareZone());

		warning = eligible && ticksUntilRegen <= warningTicks;
		if (!warning)
		{
			notified = false;
			return;
		}

		if (!notified)
		{
			notified = true;
			log.debug("HP regen in {} ticks, warning", ticksUntilRegen);
			notifier.notify(config.notification(), "Your hitpoints are about to regenerate - flick Rapid Heal!");
		}
	}

	private boolean isInNightmareZone()
	{
		final WorldView worldView = client.getTopLevelWorldView();
		return worldView != null
			&& worldView.getPlane() > 0
			&& Arrays.equals(worldView.getMapRegions(), NMZ_MAP_REGION);
	}
}
