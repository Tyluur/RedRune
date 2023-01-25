package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class CS2ScriptBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The script id.
	 */
	private int scriptId;
	
	/**
	 * The types.
	 */
	private String types;
	
	/**
	 * The parameters.
	 */
	private Object[] parameters;
	
	public CS2ScriptBuilder(int scriptId, Object... parameters) {
		StringBuilder bldr = new StringBuilder();
		if (parameters != null) {
			for (int count = parameters.length - 1; count >= 0; count--) {
				if (parameters[count] instanceof String) {
					bldr.append("s"); // string
				} else {
					bldr.append("i"); // integer
				}
			}
		}
		this.scriptId = scriptId;
		this.types = bldr.toString();
		this.parameters = parameters;
	}
	
	public CS2ScriptBuilder(int scriptId, String types, Object... parameters) {
		if (parameters.length != types.length()) {
			throw new IllegalArgumentException("Parameters size should be the same size as types!");
		}
		this.scriptId = scriptId;
		this.types = types;
		this.parameters = parameters;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(38, PacketType.VAR_SHORT);
		bldr.writeRS2String(types);
		int index = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's') {
				bldr.writeRS2String((String) parameters[index++]);
			} else {
				bldr.writeInt((Integer) parameters[index++]);
			}
		}
		bldr.writeInt(scriptId);
		return bldr.toPacket();
	}
	
	public static CS2ScriptBuilder getInterfaceUnlockScript(int interfaceId, int componentId, int key, int width, int height, String... options) {
		return new CS2ScriptBuilder(150, getParameters(interfaceId, componentId, key, width, height, options));
	}
	
	private static Object[] getParameters(int interfaceId, int componentId, int key, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--) {
			parameters[index++] = options[count];
		}
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		return parameters;
	}
}
