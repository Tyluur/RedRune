package org.redrune.utility.rs.constant;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.node.item.Item;
import org.redrune.cache.parse.definition.ItemDefinition;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public interface ItemConstants {
	
	/**
	 * The id of blood money
	 */
	int BLOOD_MONEY = 995;
	
	/**
	 * The id of the gold ticket
	 */
	int GOLD_TICKET = 13663;
	
	/**
	 * This method checks for untradeables that should drop on death.
	 *
	 * @param item
	 * 		The item
	 */
	static boolean untradeableDropsOnDeath(Item item) {
		switch (item.getId()) {
			case 20137:
			case 20141:
			case 20145:
			case 20149:
			case 20153:
			case 20157:
			case 20165:
			case 20169:
			case 18363:
			case 13860:
			case 18335:
			case 13863:
			case 13866:
			case 13869:
			case 13872:
			case 13875:
			case 13878:
			case 13886:
			case 13889:
			case 13892:
			case 13895:
			case 13898:
			case 13901:
			case 13904:
			case 13907:
			case 13910:
			case 13913:
			case 13916:
			case 13919:
			case 13922:
			case 13925:
			case 13928:
			case 13931:
			case 13934:
			case 13937:
			case 13940:
			case 13943:
			case 13946:
			case 13949:
			case 13952:
			case 4856:
			case 4857:
			case 4858:
			case 4859:
			case 4862:
			case 4863:
			case 4864:
			case 4865:
			case 4868:
			case 4869:
			case 4870:
			case 4871:
			case 4874:
			case 4875:
			case 4876:
			case 4877:
			case 4880:
			case 4881:
			case 4882:
			case 4883:
			case 4886:
			case 4887:
			case 4888:
			case 4889:
			case 4892:
			case 4893:
			case 4894:
			case 4895:
			case 4898:
			case 4899:
			case 4900:
			case 4901:
			case 4904:
			case 4905:
			case 4906:
			case 4907:
			case 4910:
			case 4911:
			case 4912:
			case 4913:
			case 4916:
			case 4917:
			case 4918:
			case 4919:
			case 4922:
			case 4923:
			case 4924:
			case 4925:
			case 4928:
			case 4929:
			case 4930:
			case 4931:
			case 4934:
			case 4935:
			case 4936:
			case 4937:
			case 4940:
			case 4941:
			case 4942:
			case 4943:
			case 4946:
			case 4947:
			case 4948:
			case 4949:
			case 4952:
			case 4953:
			case 4954:
			case 4955:
			case 4958:
			case 4959:
			case 4960:
			case 4961:
			case 4964:
			case 4965:
			case 4966:
			case 4967:
			case 4970:
			case 4971:
			case 4972:
			case 4973:
			case 4976:
			case 4977:
			case 4978:
			case 4979:
			case 4982:
			case 4983:
			case 4984:
			case 4985:
			case 4988:
			case 4989:
			case 4990:
			case 4991:
			case 4994:
			case 4995:
			case 4996:
			case 4997:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Gets the amount of special energy required by a weapon
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 */
	static int getSpecialEnergy(int weaponId) {
		if (weaponId == -1) {
			return 0;
		}
		ItemDefinition definition = ItemDefinitionParser.forId(weaponId);
		if (definition.isLended()) {
			weaponId = definition.getLendId();
		}
		switch (weaponId) {
			case 4587: // dragon sci
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 19149:// zamorak bow
			case 19151:
			case 19143:// saradomin bow
			case 19145:
			case 19146:
			case 19148:// guthix bow
				return 55;
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				return 65;
			case 13899: // vls
			case 13901:
			case 1305: // dragon long
			case 1215: // dragon dagger
			case 5698: // dds
			case 1434: // dragon mace
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
			case 11716:
				return 25;
			case 15442:// whip start
			case 15443:
			case 15444:
			case 15441:
			case 4151:
			case 23691:
			case 11698: // sgs
			case 23681:
			case 11694: // ags
			case 23679:
			case 13904:
			case 13905: // vesta spear
			case 13907:
			case 14484: // d claws
			case 23695:
			case 10887: // anchor
			case 3204: // d hally
			case 4153: // granite maul
			case 15241: // hand cannon
			case 13908:
			case 13954:// morrigan javelin
			case 13955:
			case 13956:
			case 13879:
			case 13880:
			case 13881:
			case 13882:
			case 13883:// morigan thrown axe
			case 13957:
				return 50;
			case 11730: // ss
			case 23690:
			case 11696: // bgs
			case 23680:
			case 11700: // zgs
			case 23682:
			case 35:// Excalibur
			case 8280:
			case 14632:
			case 1377:// dragon battle axe
			case 13472:
			case 15486:// staff of lights
			case 22207:
			case 22209:
			case 22211:
			case 22213:
				return 100;
			case 19784: // korasi sword
			case 7158: // d2h
			case 21371: // vine whip
				return 60;
			case 14684: // zanik cbow
				return 50;
			case 13902: // statius hammer
				return 35;
			default:
				return 0;
		}
	}
}
