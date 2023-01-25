package org.redrune.game.node.entity.player.render.flag.impl;

import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerAppearance;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2ConfigBuilder;

/**
 * Represents a player's appearance update flag.
 *
 * @author Emperor
 */
public class AppearanceUpdate extends UpdateFlag {
	
	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The player's appearance.
	 */
	private final PlayerAppearance appearance;
	
	/**
	 * Constructs a new {@code AppearanceUpdate} {@code Object}.
	 *
	 * @param player
	 * 		The player.
	 */
	public AppearanceUpdate(Player player) {
		this.player = player;
		this.appearance = player.getDetails().getAppearance();
		// updating the combat level on the styles tab
		player.getTransmitter().send(new CS2ConfigBuilder(1000, player.getSkills().getCombatLevelWithSummoning()).build(player));
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		PacketBuilder playerUpdate = new PacketBuilder();
		final int npcId = appearance.getNpcId();
		int bitSet = 0;
		//bitSet |= 0x4; we are in a pvp area
		if (!appearance.isMale()) {
			bitSet |= 0x1;
		}
		final NPCDefinition definition = npcId >= 0 ? NPCDefinitionParser.forId(npcId) : null;
		if (npcId != -1 && definition != null) {
			bitSet |= 0x2;
		}
		playerUpdate.writeByte(bitSet);
		playerUpdate.writeByte(0); // title
		playerUpdate.writeByte(player.getVariables().getSkullIcon().getId()); //skull icon
		playerUpdate.writeByte(player.getManager().getPrayers().getIcon().getId());
		playerUpdate.writeByte(0);
		if (definition == null || npcId == -1) {
			for (int i = 0; i < BodyDataParser.getBodyData().length; i++) {
				if (BodyDataParser.getBodyData()[i] != 1) {
					int d = appearance.getBodyPart(i);
					if (d == 0) {
						playerUpdate.writeByte(0);
					} else {
						playerUpdate.writeShort((short) d);
					}
				}
			}
			bitSet = 0;
			int part = 0;
			int slotHash = 0;
			for (int i = 0; i < BodyDataParser.getBodyData().length; i++) {
				if (BodyDataParser.getBodyData()[i] != 1) {
					/*int itemId = player.getEquipment().get(i) == null ? -1 : player.getEquipment().get(i).getId();
					if (i == 1) {
						if ((itemId == 20767 || itemId == 20769 || itemId == 20771) && player.getCapeRecolouring().isRecolourable(ItemDefinition.getItemDefinition(itemId))) {
							bitSet |= 1 << part;
							slotHash |= 0x1;
						}
					} */
				}
				part++;
			}
			playerUpdate.writeShort(bitSet);
			/*if ((slotHash & 0x1) != 0) { //Only with recolored cape.
				playerUpdate.writeByte(0x4);
				int[] colors = player.getCapeRecolouring().getColours();
				if (colors == null) {
					colors = ItemDefinitionParser.forId(player.getEquipment().getSlotById(Equipment.SLOT_CAPE)).originalModelColors;
				}
				int[] data = { 12816, colors[1], colors[0], colors[3], colors[2]};
				for (int i = 0; i < data.length; i++) {
					playerUpdate.putShort(data[i]);
				}
			}*/
			if (appearance.getBodyPart(14) > 0) { //Only with aura.
				playerUpdate.writeByte(0x1);
				playerUpdate.writeIntSmart(8719);
				playerUpdate.writeIntSmart(8719);
			}
		} else {
			playerUpdate.writeShort(-1);
			playerUpdate.writeShort(npcId);
			playerUpdate.writeByte(0);
		}
		for (byte i = 0; i < 10; i++) {
			playerUpdate.writeByte(appearance.getColor(i));
		}
		playerUpdate.writeShort(appearance.getRenderEmote());
		playerUpdate.writeRS2String(player.getDetails().getDisplayName());
		
		boolean pvpArea = World.get().isPvpArea(player.getLocation());
		
		// this also updates combat lvl in tab
		playerUpdate.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		playerUpdate.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
		
		playerUpdate.writeByte(-1);
		
		// npc morph
		playerUpdate.writeByte(npcId >= 0 ? 1 : 0);
		if (npcId >= 0 && definition != null) {
			playerUpdate.writeShort(definition.getAnInt876());
			playerUpdate.writeShort(definition.getAnInt842());
			playerUpdate.writeShort(definition.getAnInt884());
			playerUpdate.writeShort(definition.getAnInt875());
			playerUpdate.writeByte(definition.getAnInt875());
		}
		bldr.writeByte(playerUpdate.getBuffer().writerIndex());
		bldr.writeBytesA(playerUpdate.getBuffer().array(), 0, playerUpdate.getBuffer().writerIndex());
	}
	
	@Override
	public int getOrdinal() {
		return 6;
	}
	
	@Override
	public int getMaskData() {
		return 0x2;
	}
	
}