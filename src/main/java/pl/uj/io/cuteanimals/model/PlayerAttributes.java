package pl.uj.io.cuteanimals.model;

import pl.uj.io.cuteanimals.model.interfaces.ICharacter;
import pl.uj.io.cuteanimals.model.interfaces.RandomInteger;

/**
 * Provides methods to manage player's attributes.
 *
 * @version %I%
 * @since 0.2.0-SNAPSHOT
 */
public class PlayerAttributes extends NPCAttributes {
    private final ICharacter owner;
    private final RandomInteger random;
    private int experience;

    public PlayerAttributes(ICharacter owner) {
        super(30, 1, 1, 0, 100);
        this.owner = owner;
        this.experience = 0;
        random = new RandomIntegerImpl();
    }

    public PlayerAttributes(ICharacter owner, RandomInteger random) {
        super(30, 1, 1, 0, 100);
        this.owner = owner;
        this.experience = 0;
        this.random = random;
    }

    @Override
    public int getHealth() {
        return health
                + owner.getArmor()
                        .getItems()
                        .stream()
                        .mapToInt(i -> i.getAttributes().getHealth())
                        .sum();
    }

    @Override
    public int getAttack() {
        return attack
                + owner.getArmor()
                        .getItems()
                        .stream()
                        .mapToInt(i -> i.getAttributes().getAttack())
                        .sum();
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getDefence() {
        return defence
                + owner.getArmor()
                        .getItems()
                        .stream()
                        .mapToInt(i -> i.getAttributes().getDefence())
                        .sum();
    }

    public int getExperience() {
        return experience;
    }

    public int getRequiredExperience() {
        // TODO: some sane formula
        return super.getLevel() * 10;
    }

    public void addExperience(int experience) {
        var expBefore = this.experience;
        this.experience = (this.experience + experience) % getRequiredExperience();

        if (this.experience < expBefore) {
            addLevel(1);
        }
    }

    public int getMana() {
        return mana;
    }

    public void addMana(int mana) {
        this.mana += mana;
    }

    public int getDamage() {
        var attackBonus = (int) (getAttack() / 2.0);
        return getAttack() + (attackBonus > 0 ? random.nextInt(attackBonus) : 0);
    }

    @Override
    public String toString() {
        var exp = "Experience: " + getExperience() + " / " + getRequiredExperience() + "\n";
        var stats =
                "Health:     "
                        + getHealth()
                        + "\n"
                        + "Mana:       "
                        + getMana()
                        + "\n"
                        + "Level:      "
                        + getLevel()
                        + "\n"
                        + "Attack:     "
                        + getAttack()
                        + "\n"
                        + "Defence:    "
                        + getDefence();
        return exp + stats;
    }
}
