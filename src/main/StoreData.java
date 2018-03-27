package main;

/** To store all data for relevant unit attribute properties.
 *  7 different attributes, following given data files.
 */
public class StoreData {
	private String name;
	private String imgFile;
	private String type;
	private String monsterType;
	private int maxHP;
	private int damage;
	private int cooldown;
	
	/**
	 * Constructor for inputing
	 * @param name
	 * @param imgFile
	 * @param type
	 * @param monsterType
	 * @param maxHP
	 * @param damage
	 * @param cooldown
	 */
	public StoreData(String name, String imgFile, String type, String monsterType, int maxHP, int damage, int cooldown) {
		this.name = name;
		this.imgFile = imgFile;
		this.type = type;
		this.monsterType = monsterType;
		this.maxHP = maxHP;
		this.damage = damage;
		this.cooldown = cooldown;
	}
	
	/** Only getters are needed in this class.
	 */

	public String getName() {
		return name;
	}

	public String getImgFile() {
		return imgFile;
	}

	public String getType() {
		return type;
	}

	public String getMonsterType() {
		return monsterType;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getDamage() {
		return damage;
	}

	public int getCooldown() {
		return cooldown;
	}
	
	
}
