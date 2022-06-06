package com.lypaka.betterpixelmonspawner.Utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.PermissionAPI;

public class PermissionHandler {

    public static boolean hasPermission (EntityPlayerMP player, String permission) {

        return player.canUseCommand(0, permission) || PermissionAPI.hasPermission(player, permission);

    }

}
