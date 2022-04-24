package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;

public class Utils {

    public static boolean isNameValid(String name) {
        int nameLength = name.length();
        if(ZGangs.getGangManager().nameExistsInDatabase(name)) return false;
        if(nameLength < Config.minNameLength) return false;
        if(nameLength > Config.maxNameLength) return false;
        if(Config.bannedNames.contains(name.toLowerCase())) return false;

        return name.matches(Config.nameRegex);
    }

}
