package org.redrune.game.content.event.impl;

import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.context.ObjectEventContext;
import org.redrune.game.content.skills.woodcutting.TreeDefinitions;
import org.redrune.game.content.skills.woodcutting.WoodcuttingAction;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class ObjectEvent extends Event<ObjectEventContext> {
	
	@Override
	public void run(Player player, ObjectEventContext context) {
		final GameObject object = context.getObject();
		ObjectDefinition objectDef = object.getDefinitions();
		int id = object.getId();
		String name = objectDef.getName().toLowerCase();
		
		player.turnToObject(object);
		
		if (ModuleRepository.handle(player, object, context.getOption())) {
			return;
		}
		if (context.getOption() == InteractionOption.FIRST_OPTION && DialogueRepository.handleObject(player, object)) {
			return;
		}
		if (context.getOption().equals(InteractionOption.FIRST_OPTION)) {
			if (id == 61190 || id == 61191 || id == 61192 || id == 61193) {
				if (objectDef.hasOption(0, "Chop down")) {
					player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.NORMAL));
					return;
				}
			}
			switch (name) {
				case "tree":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.NORMAL));
						return;
					}
					break;
				case "evergreen":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.EVERGREEN));
						return;
					}
					break;
				case "dead tree":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.DEAD));
						return;
					}
					break;
				case "oak":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.OAK));
						return;
					}
					break;
				case "willow":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.WILLOW));
						return;
					}
					break;
				case "mahogany":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.MAHOGANY));
						return;
					}
					break;
				case "teak":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.TEAK));
						return;
					}
					break;
				case "maple tree":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.MAPLE));
						return;
					}
					break;
				case "ivy":
					if (objectDef.hasOption(0, "Chop")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.IVY));
						return;
					}
					break;
				case "yew":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.YEW));
						return;
					}
					break;
				case "magic tree":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.MAGIC));
						return;
					}
					break;
				case "cursed magic tree":
					if (objectDef.hasOption(0, "Chop down")) {
						player.getManager().getActions().startAction(new WoodcuttingAction(object, TreeDefinitions.CURSED_MAGIC));
						return;
					}
					break;
			}
		} else if (context.getOption().equals(InteractionOption.ITEM_ON_OBJECT)) {
			System.out.println(object);
			System.out.println(context.getItem());
		}
		player.getTransmitter().sendMessage("Nothing interesting happens.");
	}
	
	@Override
	public boolean canStart(Player player, ObjectEventContext context) {
		return !player.getManager().getLocks().isLocked(LockType.OBJECT_INTERACTION);
	}
}
