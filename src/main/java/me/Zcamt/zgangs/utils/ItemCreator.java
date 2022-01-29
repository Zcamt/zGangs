package me.Zcamt.zgangs.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class ItemCreator {

    /**
     * Vars
     */
    private Inventory inventory;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private SkullMeta skullMeta;
    private PotionMeta potionMeta;

    private double position = -1;

    /**
     * init ItemBuilder without argument
     */
    public ItemCreator(){
        this(Material.AIR);
    }

    /**
     * init ItemBuilder
     * @param material
     */
    public ItemCreator(Material material){
        this(material, 1);
    }

    /**
     * init ItemBuilder
     * @param material
     * @param amount
     */
    public ItemCreator(Material material, int amount) {
        this(material, amount, 0);
    }

    /**
     * init ItemBuilder
     * @param material
     * @param amount
     * @param data
     */
    public ItemCreator(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (byte)data);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * init ItemBuilder
     * @param itemStack
     */
    public ItemCreator(ItemStack itemStack){
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Set item
     * @param material
     * @return
     */
    public ItemCreator setItem(Material material){
        this.itemStack.setType(material);
        return this;
    }

    /**
     * Set amount
     * @param amount
     * @return
     */
    public ItemCreator setAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set data
     * @param data
     * @return
     */
    public ItemCreator setData(int data){
        this.itemStack = new ItemStack(itemStack.getType(), itemStack.getAmount(), (byte)data);
        return this;
    }

    /**
     * Set ItemStack
     * @param itemStack
     * @return
     */
    public ItemCreator setItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    /**
     * set this.inventory value
     * @param inventory
     * @return
     */
    public ItemCreator inventory(Inventory inventory){
        this.inventory = inventory;
        return this;
    }

    /**
     * @param unbreakable
     * Set item in unbreakable/breakable
     * @return
     */
    public ItemCreator setUnbreakable(boolean unbreakable){
        if (this.itemMeta == null) {
            if (this.itemStack == null)
                return null;
            this.itemMeta = this.itemStack.getItemMeta();
        }
        this.itemMeta.setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    /**
     * set the display name of the item
     * @param name
     * @return
     */
    public ItemCreator setName(String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * Add lore from String list
     * @param lores
     * @return
     */
    public ItemCreator addLore(List<String> lores){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * Add lore from String...
     * @param lores
     * @return
     */
    public ItemCreator addLore(String... lores){
        addLore(Arrays.asList(lores));
        return this;
    }

    /**
     * add enchant to the item
     * @param enchantment
     * @return
     */
    public ItemCreator addEnchantment(Enchantment enchantment) {
        addEnchantment(enchantment, 1);
        return this;
    }

    public ItemCreator addEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemCreator addEnchantments(HashMap<Enchantment, Integer> enchantments) {
        for(Enchantment enchantment : enchantments.keySet()) {
            itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        }
        return this;
    }

    public ItemCreator removeEnchantment(Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    /**
     * remove all enchant on item from a list
     * @param enchantments
     * @return
     */
    public ItemCreator removeEnchants(List<Enchantment> enchantments){
        for (Enchantment enchantment : enchantments) {
            if (!this.getEnchantments().containsKey(enchantment))
                continue;
            this.removeEnchantment(enchantment);
        }
        return this;
    }

    /**
     * remove all enchantment on the item
     * @return
     */
    public ItemCreator clearEnchants() {
        if (this.getEnchantments() == null)
            return this;
        for (Enchantment enchantment : this.getEnchantments().keySet())
            this.removeEnchantment(enchantment);
        return this;
    }

    public ItemCreator setEnchants(Map<Enchantment, Integer> enchantment){
        for (Map.Entry<Enchantment, Integer> entry : enchantment.entrySet()) {
            Enchantment enchant = entry.getKey();
            addEnchantment(enchant, entry.getValue());
        }
        return this;
    }

    /**
     * add ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public ItemCreator addItemFlag(ItemFlag itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * add ItemFlags on your item
     * @param itemFlag
     * @return
     */
    public ItemCreator addItemFlag(ItemFlag... itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * add ItemFlags on your item from ItemFlag list
     * @param itemFlag
     * @return
     */
    public ItemCreator addItemFlag(List<ItemFlag> itemFlag){
        itemFlag.forEach(this::addItemFlag);
        return this;
    }

    /**
     * remove ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public ItemCreator removeItemFlag(ItemFlag itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * hide enchant
     * @return
     */
    public ItemCreator hideEnchant(){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * show enchant
     * @return
     */
    public ItemCreator showEnchant(){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * Set durrability of item
     * /!\ 100 >= percent >= 0
     * @param percent
     * @return
     */
    public ItemCreator setDurability(float percent){
        if (percent > 100.0){
            return this;
        }else if (percent < 0.0){
            return this;
        }
        itemStack.setDurability((short) (itemStack.getDurability() * (percent / 100)));
        return this;
    }

    /**
     * Set durrability of item
     * @param durability
     * @return
     */
    public ItemCreator setNewDurability(int durability){
        itemStack.setDurability((short)durability);
        return this;
    }

    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param playerName
     * @return
     */
    public ItemCreator setSkullTextureFromePlayerName(String playerName){
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
        this.skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);
        return this;
    }

    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param player
     * @return
     */
    public ItemCreator setSkullTexture(Player player){
        setSkullTextureFromePlayerName(player.getName());
        return this;
    }

    /**
     * apply potion effect type on the potion bottle
     * @param potionEffectType
     * @return
     */
    public ItemCreator addPotionEffect(PotionEffectType potionEffectType) {
        addPotionEffect(potionEffectType, 10);
        return this;
    }

    /**
     * apply potion effect type with duration on the potion bottle
     * @param potionEffectType
     * @param duration
     * @return
     */
    public ItemCreator addPotionEffect(PotionEffectType potionEffectType, int duration) {
        addPotionEffect(potionEffectType, duration, 1);
        return this;
    }

    /**
     * apply potion effect type with duration and level on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @return
     */
    public ItemCreator addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        addPotionEffect(potionEffectType, duration, amplifier, true);
        return this;
    }

    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @return
     */
    public ItemCreator addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient) {
        addPotionEffect(potionEffectType, duration, amplifier, ambient, false);
        return this;
    }

    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle, can make this effect to overwrite
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @param overwrite
     * @return
     */
    public ItemCreator addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient, boolean overwrite) {
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        this.potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient), overwrite);
        this.itemStack.setItemMeta(this.potionMeta);
        return this;
    }

    /**
     * remove specific potion effect type
     * @param potionEffectType
     * @return
     */
    public ItemCreator removePotionEffect(PotionEffectType potionEffectType) {
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
            return this;
        this.potionMeta.removeCustomEffect(potionEffectType);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    /**
     * remove all potion effect from a list
     * @param potionEffectTypes
     * @return
     */
    public ItemCreator removePotionEffect(List<PotionEffectType> potionEffectTypes) {
        for (PotionEffectType potionEffectType : potionEffectTypes) {
            if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
                continue;
            removePotionEffect(potionEffectType);
        }
        return this;
    }

    /**
     * clear potion effect on item
     * @return
     */
    public ItemCreator clearPotionEffect() {
        if (this.getPotionEffects() == null)
            return this;
        for (PotionEffect potionEffect : this.getPotionEffects()) {
            removePotionEffect(potionEffect.getType());
        }
        return this;
    }

    /**
     * set potion type on the potion
     * @param potionType
     * @return
     */
    public ItemCreator setPotion(PotionType potionType) {
        setPotion(potionType, true);
        return this;
    }

    /**
     * set potion type on the item with splash option
     * @param potionType
     * @param splash
     * @return
     */
    public ItemCreator setPotion(PotionType potionType, boolean splash) {
        Potion potion = new Potion(PotionType.SPEED);
        potion.setSplash(splash);
        potion.setType(potionType);
        potion.apply(this.itemStack);
        return this;
    }

    /**
     * Inject item in inventory
     * @param inventory
     * @param position
     * @return
     */
    public ItemCreator inject(Inventory inventory, int position){
        inventory.setItem(position, make());
        return this;
    }

    /**
     * Inject item in inventory
     * @param inventory
     * @return
     */
    public ItemCreator inject(Inventory inventory){
        inventory.addItem(make());
        return this;
    }

    /**
     * Inject item in inventory
     * @param position
     * @return
     */
    public ItemCreator inject(int position){
        inventory.setItem(position, make());
        return this;
    }

    /**
     * Inject item in inventory
     * @return
     */
    public ItemCreator inject(){
        this.inventory.addItem(make());
        return this;
    }

    /**
     * Open inventory to the player
     * @param player
     */
    public void open(Player player){
        player.openInventory(inventory);
    }

    /**
     * Set the position of the item
     * @param position
     * @return
     */
    public ItemCreator setPosition(int position) {
        this.position = position;
        return this;
    }

    /**
     * get position
     * @return
     */
    public long getPosition(){
        return (long) this.position;
    }

    /**
     * build item
     * @return
     */
    public ItemStack make(){
        return itemStack;
    }

    /**
     * @param itemCreator
     * returns if two item builder are similar
     * This method compare type, data, and display name of items
     * @return
     */
    public boolean isSimilar(ItemCreator itemCreator){
        return hasSameMaterial(itemCreator) && hasSameData(itemCreator) && hasSameDisplayName(itemCreator);
    }

    /**
     * @param itemCreator
     * returns if two item builder are exactly same
     * This method compare all parameter of items
     * @return
     */
    public boolean isExactlySame(ItemCreator itemCreator){
        return hasSameMaterial(itemCreator) && hasSameData(itemCreator) && hasSameDisplayName(itemCreator)
                && hasSameAmount(itemCreator) && hasSameDurability(itemCreator) && hasSameEnchantment(itemCreator)
                && hasSameItemFlag(itemCreator) && hasSameLore(itemCreator) && hasSameBreakableStat(itemCreator);
    }

    /**
     * @param itemCreator
     * returns if two item builder has same type
     * @return
     */
    public boolean hasSameMaterial(ItemCreator itemCreator){
        return getMaterial() == itemCreator.getMaterial();
    }

    /**
     * @param itemCreator
     * returns if two item builder has same display name
     * @return
     */
    public boolean hasSameDisplayName(ItemCreator itemCreator){
        return getDisplayName().equalsIgnoreCase(itemCreator.getDisplayName());
    }

    /**
     * @param itemCreator
     * returns if two item builder has same enchantments
     * @return
     */
    public boolean hasSameEnchantment(ItemCreator itemCreator){
        return getEnchantments().equals(itemCreator.getEnchantments());
    }

    /**
     * @param itemCreator
     * returns if two item builder has same item flags
     * @return
     */
    public boolean hasSameItemFlag(ItemCreator itemCreator){
        return getItemFlag().equals(itemCreator.getItemFlag());
    }

    /**
     * @param itemCreator
     * returns if two item builder has same lore
     * @return
     */
    public boolean hasSameLore(ItemCreator itemCreator){
        if (getLore() == null)
            return false;
        return getLore().equals(itemCreator.getLore());
    }

    /**
     * @param itemCreator
     * returns if two item builder has same data
     * @return
     */
    public boolean hasSameData(ItemCreator itemCreator){
        return getData() == itemCreator.getData();
    }

    /**
     * @param itemCreator
     * returns if two item builder has same amount
     * @return
     */
    public boolean hasSameAmount(ItemCreator itemCreator){
        return getAmount() == itemCreator.getAmount();
    }

    /**
     * @param itemCreator
     * returns if two item builder has same durability
     * @return
     */
    public boolean hasSameDurability(ItemCreator itemCreator){
        return getDurability() == itemCreator.getDurability();
    }

    /**
     * @param itemCreator
     * returns if two item builder has same breakable stat
     * @return
     */
    public boolean hasSameBreakableStat(ItemCreator itemCreator){
        return isUnbreakable() == itemCreator.isUnbreakable();
    }

    /**
     * get type
     * @return
     */
    public Material getMaterial(){
        return itemStack.getType();
    }

    /**
     * get amount
     * @return
     */
    public int getAmount(){
        return itemStack.getAmount();
    }

    /**
     * get data
     * @return
     */
    public int getData(){
        return itemStack.getData().getData();
    }

    /**
     * get durability
     * @return
     */
    public int getDurability(){
        return itemStack.getDurability();
    }

    /**
     * get item meta
     * @return
     */
    public ItemMeta getItemMeta(){
        return itemMeta;
    }

    /**
     * get display name
     * @return
     */
    public String getDisplayName(){
        return itemStack.hasItemMeta() && itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "";
    }

    /**
     * get enchant
     * @return
     */
    public Map<Enchantment, Integer> getEnchantments(){
        return this.itemStack.hasItemMeta() && this.itemMeta.hasEnchants() ? this.itemMeta.getEnchants() : null;
    }

    /**
     * get lore
     * @return
     */
    public List<String> getLore(){
        return itemStack.hasItemMeta() && itemMeta.hasLore() ? itemMeta.getLore() : null;
    }

    /**
     * get item flag
     * @return
     */
    public Set<ItemFlag> getItemFlag(){
        return itemStack.hasItemMeta() && itemMeta.getItemFlags().size() > 0 ? itemMeta.getItemFlags() : null;
    }

    /**
     * get potion effects
     * @return
     */
    public List<PotionEffect> getPotionEffects() {
        return this.potionMeta != null && this.potionMeta.getCustomEffects().size() > 0 ? this.potionMeta.getCustomEffects() : null;
    }

    /**
     * get if item is or isn't unbreakable
     * @return
     */
    public boolean isUnbreakable(){
        return itemStack.hasItemMeta() && itemMeta.isUnbreakable();
    }

    /**
     * Convert ItemBuilder variable into a String
     * @return
     */
    public String toString() {
        String[] splitValues = new String[] {"{^}", "[^]", "`SECTION_TYPE`", "|", ","};
        StringBuilder itemBuilderString = new StringBuilder();
        itemBuilderString.append("item: ").append(splitValues[2]).append(splitValues[1])
                .append("type: ").append(getMaterial()).append(splitValues[1])
                .append("data: ").append(getData()).append(splitValues[1])
                .append("amount: ").append(getAmount()).append(splitValues[1])
                .append("durability: ").append(getDurability()).append(splitValues[1])
                .append("unbreakable: ").append(isUnbreakable()).append(splitValues[1])
                .append(splitValues[0]);
        itemBuilderString.append("other: ").append(splitValues[2]).append(splitValues[1]);
        itemBuilderString.append("position: ").append(getPosition()).append(splitValues[1]);
        itemBuilderString.append(splitValues[0]);
        if (this.itemStack.hasItemMeta()) {
            itemBuilderString.append("meta: ").append(splitValues[2]).append(splitValues[1]);
            if (getDisplayName() != null)
                itemBuilderString.append("name: ").append(getDisplayName()).append(splitValues[1]);
            if (getEnchantments() != null) {
                itemBuilderString.append("enchants: ");
                getEnchantments().forEach((enchantment, integer) -> itemBuilderString.append("enchantType: ")
                        .append(enchantment.getName()).append(splitValues[4])
                        .append("enchantLevel: ").append(integer)
                        .append(splitValues[4]).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            if (getLore() != null) {
                itemBuilderString.append("lores: ");
                getLore().forEach(s -> itemBuilderString.append("lore: ").append(uncoloredString(s)).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            if (getItemFlag() != null) {
                itemBuilderString.append("itemFlags: ");
                getItemFlag().forEach(s -> itemBuilderString.append("itemflag: ").append(s).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            itemBuilderString.append(splitValues[0]);
        }

        return itemBuilderString.toString();
    }

    /**
     * Convert string variable into an ItemBuilder
     * @param string
     * @return
     */
    public ItemCreator fromString(String string){
        String[] splitValues = new String[] {"\\{\\^}", "\\[\\^]", "`SECTION_TYPE`", "\\|", ","};
        ItemCreator itemCreator = new ItemCreator();
        String[] sections = string.split(splitValues[0]);
        if (sections.length == 0 || Arrays.stream(sections).filter(s -> s.split(splitValues[2])[0]
                .equalsIgnoreCase("item: ")).count() != 1)
            return itemCreator;
        String[] sectionType = sections[0].split(splitValues[2]);
        String[] object = sections[0].split(splitValues[1]);
        if (object.length < 6)
            return itemCreator;
        itemSection(itemCreator, sectionType, object);
        if (sections.length == 1 || !sections[1].startsWith("other: "))
            return itemCreator;
        sectionType = sections[1].split(splitValues[2]);
        object = sections[1].split(splitValues[1]);
        otherPropertySection(itemCreator, sectionType, object);
        if (sections.length == 2)
            return itemCreator;
        sectionType = sections[2].split(splitValues[2]);
        object = sections[2].split(splitValues[1]);
        if (sectionType[0].equalsIgnoreCase("meta: "))
            metaSection(itemCreator, sectionType, object);
        return itemCreator;
    }

    /**
     * This method returns the item
     * @param itemCreator
     * @param sectionType
     * @param object
     */
    private void itemSection(ItemCreator itemCreator, String[] sectionType, String[] object) {
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])){
                if (s.startsWith("type: "))
                    itemCreator.setItem(Material.valueOf(s.replace("type: ", "")));
                if (s.startsWith("data: "))
                    itemCreator.setData(Integer.parseInt(s.replace("data: ", "")));
                if (s.startsWith("amount: "))
                    itemCreator.setAmount(Integer.parseInt(s.replace("amount: ", "")));
                if (s.startsWith("durability: "))
                    itemCreator.setNewDurability(Integer.parseInt(s.replace("durability: ", "")));
                if (s.startsWith("unbreakable: "))
                    itemCreator.setUnbreakable(Boolean.parseBoolean(s.replace("unbreakable: ", "")));
            }
        });
    }

    /**
     * This method returns specific properties of item from of ItemBuilder
     * @param itemCreator
     * @param sectionType
     * @param object
     */
    private void otherPropertySection(ItemCreator itemCreator, String[] sectionType, String[] object) {
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])){
                if (s.startsWith("position: ")){
                    itemCreator.setPosition(Integer.parseInt(s.replace("position: ", "")));
                }
            }
        });
    }

    /**
     * This method returns the meta of the item
     * @param itemCreator
     * @param sectionType
     * @param object
     */
    private void metaSection(ItemCreator itemCreator, String[] sectionType, String[] object) {
        String[] splitValues = new String[] {"\\{\\^}", "\\[\\^]", "`SECTION_TYPE`", "\\|", ","};
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])){
                if (s.startsWith("name: "))
                    itemCreator.setName(coloredString(s.replace("name: ", "")));
                if (s.startsWith("enchants: ")){
                    Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
                    String[] enchant = s.split(splitValues[3]);
                    Arrays.asList(enchant).forEach(s1 -> {
                        String[] enchantObject = s1.split(splitValues[4]);
                        enchantmentMap.put(Enchantment.getByName(enchantObject[0].replace("enchants: ", "")
                                        .replace("enchantType: ", "")),
                                Integer.valueOf(enchantObject[1].replace("enchantLevel: ", "")));
                    });
                    itemCreator.setEnchants(enchantmentMap);
                }
                if (s.startsWith("lores: ")) {
                    String[] lores = s.split(splitValues[3]);
                    List<String> loreList = new ArrayList<>();
                    Arrays.asList(lores).forEach(s1 -> loreList.add(coloredString(s1)
                            .replace("lores: ", "").replace("lore: ", "")));
                    itemCreator.addLore(loreList);
                }
                if (s.startsWith("itemFlags: ")) {
                    String[] itemFlags = s.split(splitValues[3]);
                    List<ItemFlag> itemFlagList = new ArrayList<>();
                    Arrays.asList(itemFlags).forEach(s1 -> itemFlagList.add(ItemFlag.valueOf(s1.replace("itemFlags: ", "")
                            .replace("itemflag: ", ""))));
                    itemCreator.addItemFlag(itemFlagList);
                }
            }
        });
    }

    /**
     * @param string
     * @return
     */
    private String uncoloredString(String string){
        return string.replace("§", "&");
    }

    /**
     * @param string
     * @return
     */
    private String coloredString(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
