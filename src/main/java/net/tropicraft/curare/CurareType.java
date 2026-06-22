package net.tropicraft.curare;

import net.minecraft.potion.Potion;

public class CurareType {

    public static final CurareType[] curareTypeList;
    public static final CurareType paralysis;
    public static final CurareType poison;
    public static final CurareType moveSlowdown;
    public static final CurareType harm;
    public static final CurareType confusion;
    public static final CurareType hunger;
    public static final CurareType weakness;
    private static final String[] names;
    public int curareId;
    public Potion potion;

    public CurareType(final int id, final Potion potion) {
        this.curareId = id;
        this.potion = potion;
        CurareType.curareTypeList[id] = this;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public static CurareType getCurareFromDamage(final int damage) {
        return CurareType.curareTypeList[damage];
    }

    @Override
    public String toString() {
        return CurareType.names[this.curareId];
    }

    static {
        curareTypeList = new CurareType[128];
        paralysis = new CurareType(0, Potion.blindness);
        poison = new CurareType(1, Potion.poison);
        moveSlowdown = new CurareType(2, Potion.moveSlowdown);
        harm = new CurareType(3, Potion.harm);
        confusion = new CurareType(4, Potion.confusion);
        hunger = new CurareType(5, Potion.hunger);
        weakness = new CurareType(6, Potion.weakness);
        names = new String[] { "Paralysis", "Poison", "Slowdown", "Harm", "Confusion", "Hunger", "Weakness" };
    }
}
