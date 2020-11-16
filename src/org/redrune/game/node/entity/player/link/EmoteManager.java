package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public final class EmoteManager {
	
	/**
	 * Sends the unlock configs
	 *
	 * @param player
	 * 		The player
	 */
	public static void sendUnlockConfigs(Player player) {
		player.getTransmitter().send(new ConfigPacketBuilder(465, 7).build(player)); // goblin quest emotes
		int value1 = 0;
		value1 += 1;
		value1 += 2;
		value1 += 4;
		value1 += 8;
		player.getTransmitter().send(new ConfigPacketBuilder(802, value1).build(player));
		// security emotes
		player.getTransmitter().send(new ConfigPacketBuilder(1085, 249852).build(player));
		int value2 = 0;
		value2 += 1;
		value2 += 2;
		value2 += 4;
		value2 += 8;
		value2 += 16;
		value2 += 32;
		value2 += 64;
		value2 += 128;
		value2 += 256;
		value2 += 512;
		value2 += 1024;
		value2 += 2048;
		value2 += 4096;
		value2 += 8192;
		value2 += 16384;
		value2 += 32768;
		player.getTransmitter().send(new ConfigPacketBuilder(313, value2).build(player));
	}
	
	/**
	 * Handles the emote
	 *
	 * @param player
	 * 		The player
	 * @param slotId
	 * 		The id of the emote
	 */
	public static void handleEmote(Player player, int slotId) {
		Emote emote = Emote.getEmoteBySlot(slotId);
		if (emote == null) {
			player.getTransmitter().sendMessage("That emote hasn't been added yet, please suggest it on forums.");
			return;
		}
		if (System.currentTimeMillis() < player.getUpdateMasks().getLastAnimationEndTime()) {
			player.getTransmitter().sendMessage("You are already doing an emote.");
			return;
		}
		emote.play(player);
	}
	
	public enum Emote {
		
		YES(855),
		NO(856),
		BOW(858),
		ANGRY(859),
		THINK(857),
		WAVE(863),
		SHRUG(2113),
		CHEER(862),
		BECKON(864),
		LAUGH(861),
		JUMP_FOR_JOY(2109),
		YAWN(2111),
		DANCE(866),
		JIG(2106),
		TWIRL(2107),
		HEADBANG(2108),
		CRY(860),
		KISS(1374, 1702, 17),
		PANIC(2105),
		RASPBERRY(2110),
		CLAP(865),
		SALUTE(2112),
		GOBLIN_BOW(0x84F),
		GOBLIN_SALUTE(0x850),
		GLASS_BOX(1131),
		CLIMB_ROPE(1130),
		LEAN(1129),
		GLASS_WALL(1128),
		IDEA(4275),
		STOMP(1745),
		FLAP(4280),
		SLAP_HEAD(4276),
		ZOMBIE_WALK(3544),
		ZOMBIE_DANCE(3543),
		ZOMBIE_HAND(7272, 1244),
		SCARED(2836),
		BUNNY_HOP(6111),
		SKILLCAPE {
			@Override
			public void play(Player player) {
				// TODO perform the rite ones for a cape
			}
		},
		SNOWMAN_DANCE(7531),
		AIR_GUITAR(2414, 1537),
		SAFETY_FIRST(8770, 1553),
		EXPLORE(9990, 1734),
		TRICK(10530, 1864),
		FREEZE(11044, 1973),
		GIVE_THANKS(-1, -1, 44) {
			@Override
			public void play(Player player) {
				// TODO: turkey animation
			}
		},
		AROUND_THE_WORLD(11542, 2037, 45),
		DRAMATIC_POINT(12658, 780),
		FAINT(14165),
		PUPPET_MASTER(14869, 2837),
		TASK_MASTER(15034, 2930),
		SEAL_OF_APPROVAL(-1, -1, 50) {
			@Override
			public void play(Player player) {
				// TODO: seal
			}
		},
		INVOKE_SPRING(15357, 1391, 63);
		
		/**
		 * The animation to use for this emote
		 */
		@Getter
		private final int animationId;
		
		/**
		 * The graphics to use for this emote
		 */
		@Getter
		private final int graphicsId;
		
		/**
		 * The slot id of the emote
		 */
		@Getter
		private final int slotId;
		
		Emote() {
			this.animationId = this.graphicsId = -1;
			this.slotId = ordinal();
		}
		
		Emote(int animationId) {
			this.animationId = animationId;
			this.graphicsId = -1;
			this.slotId = ordinal();
		}
		
		Emote(int animationId, int graphicsId) {
			this.animationId = animationId;
			this.graphicsId = graphicsId;
			this.slotId = ordinal();
		}
		
		Emote(int animationId, int graphicsId, int slotId) {
			this.animationId = animationId;
			this.graphicsId = graphicsId;
			this.slotId = slotId;
		}
		
		/**
		 * Gets an emote by the slot id
		 *
		 * @param slotId
		 * 		The slot id
		 */
		public static Emote getEmoteBySlot(int slotId) {
			for (Emote emote : Emote.values()) {
				if (emote.slotId == slotId) {
					return emote;
				}
			}
			return null;
		}
		
		/**
		 * Plays this emote
		 *
		 * @param player
		 * 		The player
		 */
		public void play(Player player) {
			if (getAnimationId() != -1) {
				player.sendAnimation(getAnimationId());
			}
			if (getGraphicsId() != -1) {
				player.sendGraphics(getGraphicsId());
			}
		}
		
	}
}
