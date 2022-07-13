package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.LastLegendList;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class LastLegendCommand extends CommandBase {

    @Override
    public String getName() {

        return "lastlegend";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps lastlegend";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("ll");
        return a;

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.lastlegend")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

            LastLegendList.openList(player);

        }

    }

}
