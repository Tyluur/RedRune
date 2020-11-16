package org.redrune.game.node.object;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ObjectDefinitionParser;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class GameObject extends Node {
	
	/**
	 * The id of the object
	 */
	@Getter
	private final int id;
	
	/**
	 * The type of object this is
	 */
	@Getter
	private final int type;
	
	/**
	 * The rotation of the object
	 */
	@Getter
	private final int rotation;
	
	/**
	 * The object's definition.
	 */
	@Setter
	private ObjectDefinition definitions;
	
	/**
	 * The type of object this is
	 */
	@Getter
	@Setter
	private ObjectType spawnType;
	
	public GameObject(GameObject other) {
		super(other.getLocation());
		this.id = other.id;
		this.type = other.type;
		this.rotation = other.rotation;
		this.definitions = other.definitions;
		this.spawnType = other.spawnType;
	}
	
	/**
	 * Constructs a regular object with a 0 rotation and type 10.
	 *
	 * @param id
	 * 		The id of the object
	 * @param location
	 * 		The location of the object
	 */
	public GameObject(int id, Location location) {
		this(id, 10, 0, location);
	}
	
	/**
	 * Constructs an object
	 *
	 * @param id
	 * 		The id of the object
	 * @param type
	 * 		The type of the object
	 * @param rotation
	 * 		The rotation of the object
	 * @param location
	 * 		The location of the object
	 */
	public GameObject(int id, int type, int rotation, Location location) {
		super(location);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public GameObject toGameObject() {
		return this;
	}
	
	@Override
	public int getSize() {
		return Math.max(getDefinitions().getSizeX(), getDefinitions().getSizeY());
	}
	
	/**
	 * Gets the definitions of the object
	 */
	public ObjectDefinition getDefinitions() {
		if (definitions == null) {
			setDefinitions(ObjectDefinitionParser.forId(id));
		}
		return definitions;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameObject)) {
			return false;
		}
		GameObject o = (GameObject) obj;
		return o.id == id && o.type == type && o.rotation == rotation && o.getLocation().equals(getLocation()) && o.getSpawnType() == spawnType;
	}
	
	@Override
	public String toString() {
		StringBuilder bldr = new StringBuilder();
		bldr.append("[id=").append(id).append(", ");
		if (getDefinitions().getName() != null && !getDefinitions().getName().equals("null")) {
			bldr.append("name=").append(getDefinitions().getName()).append(", ");
		}
		bldr.append("location=").append(getLocation().toString()).append(", ");
		bldr.append("type=").append(type).append(", ");
		//bldr.append("notClipped=").append(getDefinitions().isNotClipped()).append(",");
		//bldr.append("projectileClipped=").append(getDefinitions().isProjectileClipped()).append(", ");
		bldr.append("rotation=").append(rotation).append("]");
		return bldr.toString();
	}
	
	/**
	 * Checks if the object tiles used contains a specific tile
	 *
	 * @param tile
	 * 		The tile to check
	 */
	public boolean containsTile(Location tile) {
		for (Location tiles : tilesOccupied()) {
			if (tiles.equals(tile)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a list of the tiles the object occupies
	 */
	public Set<Location> tilesOccupied() {
		final int rotation = getRotation();
		final Set<Location> tileList = new HashSet<>();
		
		int sizeX;
		int sizeY;
		if (rotation != 1 && rotation != 3) {
			sizeX = getDefinitions().getSizeX();
			sizeY = getDefinitions().getSizeY();
		} else {
			sizeX = getDefinitions().getSizeY();
			sizeY = getDefinitions().getSizeX();
		}
		
		for (int i = 0; i < sizeX; i++) {
			tileList.add(new Location(getLocation().getX() + i, getLocation().getY(), getLocation().getPlane()));
		}
		for (int i = 0; i < sizeY; i++) {
			tileList.add(new Location(getLocation().getX(), getLocation().getY() + i, getLocation().getPlane()));
		}
		return tileList;
	}
	
	/**
	 * Gets the accurate size x of the object, taking the rotation into consideration.
	 */
	public int getAccurateSizeX() {
		int sizeX;
		if (rotation != 1 && rotation != 3) {
			sizeX = getDefinitions().getSizeX();
		} else {
			sizeX = getDefinitions().getSizeY();
		}
		return sizeX;
	}
	
	/**
	 * Gets the accurate size x of the object, taking the rotation into consideration
	 */
	public int getAccurateSizeY() {
		int sizeY;
		if (rotation != 1 && rotation != 3) {
			sizeY = getDefinitions().getSizeY();
		} else {
			sizeY = getDefinitions().getSizeX();
		}
		return sizeY;
	}
	
	/**
	 * The type of objects that objects can be
	 */
	public enum ObjectType {
		/**
		 * The object was generated from the cache
		 */
		CACHE,
		
		/**
		 * The object was generated from the server
		 */
		SERVER
	}
}