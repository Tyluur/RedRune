package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2StringBuilder;
import org.redrune.network.world.packet.outgoing.impl.InterfaceChangeBuilder;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.constant.BonusConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class BonusesInterfaceInteractionModule implements InterfaceInteractionModule {
	
	/**
	 * The data used to write bonuses on the equipment bonuses interface.
	 */
	private static final Object[][] BONUSES_INTERFACE_DATA = new Object[][] {
			// ATTACK
			{ 31, BonusConstants.STAB_ATTACK, "Stab" }, { 32, BonusConstants.SLASH_ATTACK, "Slash" }, { 33, BonusConstants.CRUSH_ATTACK, "Crush" }, { 34, BonusConstants.MAGIC_ATTACK, "Magic" }, { 35, BonusConstants.RANGE_ATTACK, "Range" },
			// DEFENCE
			{ 36, BonusConstants.STAB_DEFENCE, "Stab" }, { 37, BonusConstants.SLASH_DEFENCE, "Slash" }, { 38, BonusConstants.CRUSH_DEFENCE, "Crush" }, { 39, BonusConstants.MAGIC_DEFENCE, "Magic" }, { 40, BonusConstants.RANGE_DEFENCE, "Range" }, { 41, BonusConstants.SUMMONING_DEFENCE, "Summoning" },
			// ABSORB
			{ 42, BonusConstants.ABSORB_MELEE_BONUS, "Absorb Melee" }, { 43, BonusConstants.ABSORB_MAGE_BONUS, "Absorb Magic" }, { 44, BonusConstants.ABSORB_RANGE_BONUS, "Absorb Range" },
			// STRENGTHS
			{ 45, BonusConstants.STRENGTH_BONUS, "Strength" }, { 46, BonusConstants.RANGED_STRENGTH_BONUS, "Ranged Strength" }, { 47, BonusConstants.PRAYER_BONUS, "Prayer" }, { 48, BonusConstants.MAGIC_DAMAGE_BONUS, "Magic Damage" } };
	
	/**
	 * The id of the interface
	 */
	private static final int INTERFACE_ID = 667;
	
	/**
	 * The id of the inventory interface
	 */
	private static final int INVENTORY_INTERFACE_ID = 670;
	
	/**
	 * The bonus labels
	 */
	private static final String[] BONUS_LABELS = new String[] { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Ranged", "Strength", "Ranged Str", "Prayer", "Magic Damage" };
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(INTERFACE_ID, INVENTORY_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == INTERFACE_ID) {
			if (componentId == 7) {
				Item item = player.getEquipment().getItems().lookup(itemId);
				if (item == null || item.getId() != itemId) {
					return true;
				}
				if (!player.getInventory().getItems().hasSpaceFor(item)) {
					player.getTransmitter().sendMessage("You do not have enough space in your inventory.");
					return true;
				}
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					player.getEquipment().getItems().remove(item);
					player.getEquipment().refresh(slotId);
					player.getInventory().addItem(item.getId(), item.getAmount());
					player.getUpdateMasks().register(new AppearanceUpdate(player));
					refresh(player);
					return true;
				} else if (packetId == NetworkConstants.SEVENTH_PACKET_ID) {
					showStats(player, item);
					return true;
				} else if (packetId == NetworkConstants.EXAMINE_PACKET_ID) {
					ItemEvent.handleItemExamining(player, item);
					return true;
				}
			} else if (componentId == 65) {
				// close button
				return true;
			}
		} else {
			if (componentId == 0) {
				Item item = player.getInventory().getItems().get(slotId);
				if (item == null || item.getId() != itemId) {
					return true;
				}
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					ItemEvent.handleItemEquipping(player, item, slotId);
					refresh(player);
					return true;
				} else if (packetId == NetworkConstants.SEVENTH_PACKET_ID) {
					showStats(player, item);
					return true;
				} else if (packetId == NetworkConstants.EXAMINE_PACKET_ID) {
					ItemEvent.handleItemExamining(player, item);
					return true;
				}
			}
		}
		System.out.println("player = [" + player + "], interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
	
	/**
	 * Refreshes the interface
	 *
	 * @param player
	 * 		The player
	 */
	private static void refresh(Player player) {
		for (Object[] element : BONUSES_INTERFACE_DATA) {
			int bonus = player.getEquipment().getBonus((int) element[1]);
			String sign = bonus > 0 ? "+" : "";
			player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, (int) element[0], element[2] + ": " + sign + bonus);
		}
		player.getTransmitter().send(new CS2ConfigBuilder(779, player.getEquipment().getWeaponRenderEmote()).build(player));
	}
	
	/**
	 * Shows the stats of the item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 */
	private static void showStats(Player player, Item item) {
		int[] bonuses = ItemRepository.getBonuses(item.getId());
		if (bonuses == null) {
			bonuses = new int[18];
		}
		
		StringBuilder titles = new StringBuilder();
		StringBuilder names = new StringBuilder();
		StringBuilder stats = new StringBuilder();
		
		String namesArray[] = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Summoning", "Absorb Melee", "Absorb Magic", "Absorb Range", "Strength", "Ranged Str", "Prayer", "Magic Damage" };
		int count = 0;
		boolean title1Done = false;
		boolean title2Done = false;
		boolean title3Done = false;
		boolean namesIndentDone = false;
		
		for (int i = 0; i < bonuses.length; i++) {
			if (bonuses[i] != 0) {
				if (i <= 4 && !title1Done) {
					title1Done = true;
					titles.append("Attack Bonus");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				} else if (i >= 5 && i <= 13 && !title2Done) {
					for (int j = 0; j <= count; j++) {
						if (title1Done) {
							titles.append("<br>");
						}
					}
					count = 0;
					title2Done = true;
					titles.append("Defence Bonus");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				} else if (i >= 14 && !title3Done) {
					for (int j = 0; j <= count; j++) {
						titles.append("<br>");
					}
					count = 0;
					title3Done = true;
					titles.append("Other");
					titles.append("                 ");
					stats.append("<br>");
					names.append("<br>");
				}
				names.append(namesArray[i]).append(":");
				if (!namesIndentDone) {
					namesIndentDone = true;
					names.append("                       ");
				}
				names.append("<br>");
				stats.append(bonuses[i] > 0 ? "+" : "").append(bonuses[i]);
				stats.append("<br>");
				count++;
			}
		}
		
		player.getTransmitter().send(new CS2StringBuilder(321, item.getName()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(324, stats.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(323, names.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(322, titles.toString()).build(player));
		
		
		/*StringBuilder attack = new StringBuilder();
		attack.append("Attack Bonuses<br><br>");
		StringBuilder defence = new StringBuilder();
		defence.append("Defence Bonuses<br><br>");
		StringBuilder other = new StringBuilder();
		other.append("Other Bonuses<br><br>");
		for (int i = 0; i < bonuses.length; i++) {
			double bonus = bonuses[i];
			String label = BONUS_LABELS[i];
			StringBuilder bldr = (i <= 4 ? attack : i <= 13 ? defence : other);
			String sign = bonus > 0 ? "+" : "";
			
			bldr.append(label).append(": ").append(sign).append(bonus).append(label.contains("Absorb") ? "%" : "").append(i == bonuses.length - 1 ? "" : "<br>");
		}
		other.append("<br>Weight: ").append(ItemRepository.getWeight(item.getId(), true));
		player.getTransmitter().send(new CS2StringBuilder(321, "Stats for " + item.getName()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(323, attack.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(324, defence.toString()).build(player));
		player.getTransmitter().send(new CS2StringBuilder(325, other.toString()).build(player));*/
	}
	
	/**
	 * Shows the interface to the player
	 *
	 * @param player
	 * 		The player
	 */
	public static void show(Player player) {
		// sent twice because of the bank glitch
		for (int i = 0; i < 2; i++) {
			player.stop(true, true, true, true);
			player.getTransmitter().send(new AccessMaskBuilder(INTERFACE_ID, 7, 0, 15, 1538).build(player));
			player.getTransmitter().send(new AccessMaskBuilder(INVENTORY_INTERFACE_ID, 0, 0, 28, 1538).build(player));
			player.getTransmitter().send(new InterfaceChangeBuilder(INTERFACE_ID, 49, true).build(player));
			player.getManager().getInterfaces().sendInterface(INTERFACE_ID, true).sendInventoryInterface(INVENTORY_INTERFACE_ID);
			refresh(player);
		}
	}
	
}
