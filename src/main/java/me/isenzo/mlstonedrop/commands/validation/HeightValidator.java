package me.isenzo.mlstonedrop.commands.validation;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.exceptions.message.ExceptionMessage;
import me.isenzo.mlstonedrop.commands.validation.regexpattern.RegexPattern;
import me.isenzo.mlstonedrop.commands.validation.support.ValidationSupport;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class HeightValidator {
    private final Main plugin;

    public boolean validateHeightRange(CommandSender sender, String materialName, String minimalHeightStr, String maximalHeightStr) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §7Nie znaleziono materiału: §c" + materialName);
            return false;
        }

        // Walidacja minimalHeight i maximalHeight z użyciem wyrażenia regularnego
        if (!validateHeightValue(sender, minimalHeightStr) || !validateHeightValue(sender, maximalHeightStr)) {
            return false;
        }

        int minimalHeight = Integer.parseInt(minimalHeightStr);
        int maximalHeight = Integer.parseInt(maximalHeightStr);

        // Sprawdzenie, czy minimalna wysokość jest mniejsza lub równa maksymalnej
        if (minimalHeight > maximalHeight) {
            sender.sendMessage("§8[§6MLDrop§8] §cMinimalna wysokość nie może być większa od maksymalnej.");
            return false;
        }

        // Tutaj możesz dodać logikę aktualizacji konfiguracji, jeśli wszystkie walidacje przebiegły pomyślnie
        updateHeightInConfig(materialName, minimalHeight, maximalHeight);

        return true;
    }

    private static boolean validateHeightValue(CommandSender sender, String heightValue) {
        Pattern pattern = Pattern.compile(RegexPattern.HEIGHT_PATTERN);
        if (!pattern.matcher(heightValue).matches()) {
            sender.sendMessage(ValidationSupport.validateStringWithRegex(ExceptionMessage.INCORRECT_STRING_HEIGHT, heightValue));
            return false;
        }
        return true;
    }

    private void updateHeightInConfig(String materialName, int minimalHeight, int maximalHeight) {
        String configPathMinY = "drops." + materialName + ".min-y";
        String configPathMaxY = "drops." + materialName + ".max-y";
        plugin.getConfig().set(configPathMinY, minimalHeight);
        plugin.getConfig().set(configPathMaxY, maximalHeight);
        plugin.saveConfig();
        // Możesz tutaj dodać logikę wysyłania wiadomości do nadawcy o powodzeniu operacji
    }
}

//    private static ConfigManager configManager;
//
//    private static boolean validateMaximalHeightValue(CommandSender sender, String maximalHeight) {
//        return ValidationSupport.validateStringWithRegex(sender, maximalHeight, RegexValidation.HEIGHT_PATTERN, ExceptionMessage.INCORRECT_STRING_HEIGHT, maximalHeight);
//    }
//
//    private static boolean validateMinimalHeightValue(CommandSender sender, String minimalHeight) {
//        return ValidationSupport.validateStringWithRegex(sender, minimalHeight, RegexValidation.HEIGHT_PATTERN, ExceptionMessage.INCORRECT_STRING_HEIGHT, minimalHeight);
//    }
//
//    private static boolean minimalValueChanged(Material material, String newValue) {
//        int defaultMinY = configManager.getMinY(material);
//        String oldValue = defaultMinY;
//        return !Objects.equals(oldValue, newValue);
//    }
//
//    private static boolean maximalValueChanged(Material material, String newValue) {
//        String oldValue = String.valueOf(configManager.getMaxY(material));
//        return !Objects.equals(oldValue, newValue);
//    }
//
//    private static boolean heightValueChanged(CommandSender sender, Material material, String maximalHeight, String minimalHeight) {
//        if (!maximalValueChanged(material, maximalHeight)) {
//            sender.sendMessage("[MLDrop] wprowadzona maksymalna wysokość " + maximalHeight + "jest taka sama jak obecna");
//            return false;
//        }
//        if (!minimalValueChanged(material, minimalHeight)) {
//            sender.sendMessage("[MLDrop] wprowadzona minmalna wysokość " + minimalHeight + "jest taka sama jak obecna");
//            return false;
//        }
//
//        return true;
//    }
//
//    private static boolean minGreaterThanMax(CommandSender sender, Integer minimalHeight, Integer maximalHeight) {
//        if (minimalHeight > maximalHeight) {
//            sender.sendMessage("[MLDrop] minimalna wysokość (" + minimalHeight + "), nie może być większa od maksymalnej wysokości " + maximalHeight + ")");
//            return false;
//        }
//
//        return true;
//    }
//
//    public static boolean stringValid(Material material, CommandSender sender, String maximalHeight, String minimalHeight) {
//        if (heightValueChanged(sender, material, maximalHeight, minimalHeight)) {
//            return validateMaximalHeightValue(sender, maximalHeight) && validateMinimalHeightValue(sender, minimalHeight);
//        }
//
//        return false;
//    }
//
//    public static boolean validateHeightRange(Material material, CommandSender sender, int minHeight, int maxHeight, String maximalHeight, String minimalHeight) {
//        if (stringValid(material, sender, maximalHeight, minimalHeight)) {
//            return minGreaterThanMax(sender, minHeight, maxHeight);
//        }
//
//        return false;
//    }

//}
