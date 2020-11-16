package org.redrune.game.world.punishment;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public final class Punishment {
	
	/**
	 * The name of the player carried out the punishment
	 */
	@Getter
	private final String punisher;
	
	/**
	 * The name of the player who was punished
	 */
	@Getter
	private final String punished;
	
	/**
	 * The type of punishment this was
	 */
	@Getter
	private final PunishmentType type;
	
	/**
	 * The time the punishment will be over at
	 */
	@Getter
	private final long time;
	
	@Override
	public String toString() {
		return "Punishment{" + "punisher='" + punisher + '\'' + ", punished='" + punished + '\'' + ", type=" + type + ", time=" + time + '}';
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Punishment)) {
			return false;
		}
		Punishment p = (Punishment) obj;
		return Objects.equals(punished, p.punished) && Objects.equals(punisher, p.punisher) && Objects.equals(type, p.type);
	}
	
	public Punishment(String punisher, String punished, PunishmentType type, long time) {
		this.punisher = punisher;
		this.punished = punished;
		this.type = type;
		this.time = time;
	}
}
