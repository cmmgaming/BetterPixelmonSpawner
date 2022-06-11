package com.lypaka.betterpixelmonspawner.Utils;

import net.minecraft.util.text.TextComponentString;

public class FancyText {

    public static String getFancyString (String value) {

        return value.replace("&", "\u00a7");

    }

    public static TextComponentString getFancyText (String value) {

        return new TextComponentString(getFancyString(value));

    }

}
