package model;

public class Weapon {
    private String weapon;
    private String weaponDescription;

    public Weapon(String weapon, String description) {
        this.weapon = weapon;
        this.weaponDescription = description;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getWeaponDescription() {
        return weaponDescription;
    }

    public void setWeaponDescription(String weaponDescription) {
        this.weaponDescription = weaponDescription;
    }

}
