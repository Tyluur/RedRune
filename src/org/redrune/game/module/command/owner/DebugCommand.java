package org.redrune.game.module.command.owner;

import org.redrune.game.content.play.AppearanceModification;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "You never know what this will do!")
public class DebugCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		/*
		  player.getPackets().sendItems(91, storeItems);
            player.getPackets().sendUnlockIComponentOptionSlots(860, 23, 0, count, 0,
        1, 2, 3, 4,5,6);
      player.getPackets().sendInterSetItemsOptionsScript(860, 23, 91, 8, 150,
                    "Take", "Take 10", "Take 1000", "", "");
		 */
		AppearanceModification.openCharacterStyling(player);
		
		
		/*List<Entity> entityList = new ArrayList<>();
		entityList.addAll(player.getRegion().getNpcs());
		for (Player o : player.getRegion().getPlayers()) {
			if (o == null || o == player) {
				continue;
			}
			entityList.add(o);
		}
		if (entityList.isEmpty()) {
			System.out.println("Empty");
			return;
		}
		entityList.sort(Comparator.comparingInt(o -> o.getLocation().getDistance(player.getLocation())));
		Entity e = entityList.get(0);
		System.out.println("E = " + e);
		Hit hit = new Hit(player, 0, HitSplat.MELEE_DAMAGE);
		
		
		Player source = player;
		// the instance of the sources prayer
		PrayerManager sourcePrayer = source.getManager().getPrayers();
		
		// we only want to find the drain prayers
		List<Prayer> drainers = sourcePrayer.getActivePrayers().stream().filter(Prayer::isDrainer).collect(Collectors.toList());
		
		// the message to sent
		String message = null;
		
		// loops through all the drain prayers
		for (Prayer prayer : drainers) {
			// the optional drain instance
			Optional<DrainPrayer> optional = PrayerEffectRepository.getDrainPrayer(prayer);
			if (!optional.isPresent()) {
				System.out.println("Unable to find drain for " + prayer);
				continue;
			}
			// the drain prayer
			DrainPrayer drain = optional.get();
			// if the source has maxed its drain and the receiver has reached its least bonuses
			if (sourcePrayer.maxed(null, prayer, drain.raiseSource())) {
				message = ("Your opponent has been weakened so much that your " + (prayer.isSap() ? "sap" : "leech") + " curse has no effect.");
			} else {
				source.sendAnimation(drain.startAnimationId());
				if (drain.startGraphicsId() > 0) {
					source.sendGraphics(drain.startGraphicsId());
				}
				sourcePrayer.modify(null, sourcePrayer, drain.prayerSlots(), drain.amounts(), drain.drainCap(), drain.raiseCap(), drain.raiseSource());
				sourcePrayer.visualizeLeech(source, e, drain.projectileId(), drain.landingGraphicsId());
			}
		}
		if (message != null) {
			source.getTransmitter().sendMessage(message, false);
		}
		*/
		
		//		player.getUpdateMasks().register(new FaceLocationUpdate(player, Location.create(intParam(args, 1), intParam(args, 2), player.getLocation().getPlane())));
		//		player.getSkills().addExperienceNoMultiplier((short) intParam(args, 1), intParam(args, 2));
		//		player.getManager().getInterfaces().sendChatboxInterface(intParamOrDefault(args, 1, 740));
		//		player.getTransmitter().send(new FriendsListBuilder(args[1], "", intParam(args, 2), 0, true, boolParam(args, 3), boolParam(args, 4)).build(player));
		/*if (boolParam(args, 1)) {
			player.getManager().getInterfaces().sendPrimaryOverlay(intParam(args, 2));
		} else {
			player.getManager().getInterfaces().closePrimaryOverlay();
		}*/
/*		Entity target = null;
		for (int i = 0; i <= World.get().getPlayers().size(); i++) {
			Player p = World.get().getPlayers().get(i);
			if (p == null) {
				continue;
			}
			if (p.getIndex() == player.getIndex()) {
				continue;
			}
			target = p;
		}
		if (target == null) {
			System.out.println("Notargfound");
			return;
		}
		boolean found = EntityMovement.findBasicRoute(player, target, target.getLocation(), 25);
		System.out.println("targ=" + target + ", found=" + found);*/
		
		
	/*	int stat = 30;
		int value = (stat + intParamOrDefault(args, 1, 0))
				            | ((stat + intParamOrDefault(args, 2, 0)) << 6)
				            | ((stat + intParamOrDefault(args, 3, 0)) << 12)
				            | ((stat + intParamOrDefault(args, 4, 0)) << 18)
				            | ((stat + intParamOrDefault(args, 5, 0)) << 24);
		player.getTransmitter().send(new ConfigPacketBuilder(1583, value).build(player));*/
		//		ShopRepository.open(player, intParam(args, 1));
		//		player.getManager().getPrayers().setBook(PrayerBook.valueOf(args[1].toUpperCase()));
		//player.getNetworkSession().getChannel().close();
		//		System.out.println(player.getRegion().getPlayers());
		//			player.getManager().getPrayers().setIcon(PrayerIcon.valueOf(args[1].toUpperCase()));
		//			player.getManager().getDialogues().startDialogue(new BankerNPCDialogue(), 45);
	/*		Animation MODERN_ANIM = new Animation(8939, 0, false, Priority.HIGHEST);
			Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);
			
			player.getUpdateMasks().register(MODERN_ANIM);
			player.getUpdateMasks().register(MODERN_GRAPHIC);
			
			SystemManager.getScheduler().schedule(new ScheduledTask(3, 1, false, () -> {
				player.getUpdateMasks().register(new Animation(-1));
				player.getUpdateMasks().register(new Graphic(-1));
				player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 0));
			}));
			*/
	}
}