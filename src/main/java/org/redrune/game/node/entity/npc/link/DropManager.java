package org.redrune.game.node.entity.npc.link;

import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Drop;
import org.redrune.utility.tool.RandomFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public final class DropManager {
	
	/**
	 * Generates a list of drops for the npc
	 *
	 * @param killer
	 * 		The killer of the npc
	 * @param npc
	 * 		The npc
	 * @param drops
	 * 		The drops
	 */
	public static List<Drop> generateDrops(Player killer, NPC npc, List<Drop> drops) {
		List<Drop> dropList = generateDrops(killer, drops, npc.getDefinitions().getName());
		if (PvPLocation.isAtWild(killer.getLocation())) {
			// dj khaled code
			boolean anotherOne = false;
			for (Drop drop : dropList) {
				if (drop.getItemId() == 536) {
					anotherOne = true;
					break;
				}
			}
			if (anotherOne) {
				dropList.add(new Drop(536, 100, 1, 1));
			}
		}
		return dropList;
	}
	
	/**
	 * Generates a list of drops
	 *
	 * @param killer
	 * 		The killer
	 * @param drops
	 * 		The drops list
	 * @param name
	 * 		The name of the npc
	 */
	// TODO: ring of wealth
	private static List<Drop> generateDrops(Player killer, List<Drop> drops, String name) {
		List<Drop> possibleDrops = new ArrayList<>();
		//boolean equippingROW = killer.getEquipment().getIdInSlot(EquipConstants.SLOT_RING) == 2572;
		int possibleDropsCount = 0;
		for (Drop drop : drops) {
			if (drop.getItemId() == 995) {
				continue;
			}
			if (drop.getRate() == 100) {
				possibleDrops.add(drop);
			} else {
				double chance = 100;
				final boolean revenant = name != null && name.toLowerCase().contains("revenant");
				if (name != null && revenant) {
					chance = 1000;
				}
				//chance = chance * (equippingROW && killer.getFacade().getRowCharges() > 0 ? 0.95 : 1);
				
				// the rolledChance number
				double rolledChance = RandomFunction.random(0, (int) chance);
				
				// re-roll for rev drops
				if (rolledChance < 40 && RandomFunction.random(0, 100) >= 30 && revenant) {
					rolledChance = RandomFunction.random(0, (int) chance);
				}
				if (rolledChance < drop.getRate()) {
					possibleDropsCount++;
					possibleDrops.add(drop);
				}
			}
		}
		if (possibleDropsCount > 0) {
			Collections.shuffle(possibleDrops);
			Drop random = possibleDrops.get(0);
			possibleDrops = possibleDrops.stream().filter(p -> p.getRate() == 100).collect(Collectors.toList());
			possibleDrops.add(random);
		}
		possibleDrops.sort((o1, o2) -> Boolean.compare(o1.isBone(), o2.isBone()));
		return possibleDrops;
	}
}
