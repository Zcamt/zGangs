package me.Zcamt.zgangs.utils;

import me.Zcamt.zgangs.ZGangs;
import me.Zcamt.zgangs.config.Config;
import org.bukkit.ChatColor;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isNameValid(String name) {
        int nameLength = name.length();
        if(ZGangs.getGangManager().nameExistsInDatabase(name)) return false;
        if(nameLength < Config.minNameLength) return false;
        if(nameLength > Config.maxNameLength) return false;
        if(Config.bannedWords.contains(name.toLowerCase())) return false;

        return name.matches(Config.nameRegex);
    }

    public static String formatDateFromEpochMilli(long epochMilli) {
        Date date = Date.from(Instant.ofEpochMilli(epochMilli));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return dateFormat.format(date);
    }

    public static String formattedCurrency(int amount) {
        /*NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("da-DK"));
        format.setMaximumFractionDigits(0);*/
        return formattedNumber(amount) + ChatColor.GOLD + "⛃";
    }

    public static String formattedNumber(int number) {
        NumberFormat format = NumberFormat.getIntegerInstance(Locale.forLanguageTag("da-DK"));
        format.setMaximumFractionDigits(0);
        return format.format(number);
    }

}
