package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.time.Duration;
import java.time.LocalDateTime;

public class WhenCommand extends CommandBase {

    @Override
    public String getName() {

        return "when";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps when";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.when")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

        }

        if (LegendarySpawner.nextSpawnAttempt != null) {

            LocalDateTime now = LocalDateTime.now();
            sender.sendMessage(FancyText.getFormattedText("&eThe next legendary spawn attempt will be in: " + makeTimeReadable(LegendarySpawner.nextSpawnAttempt, now)));

        } else {

            sender.sendMessage(FancyText.getFormattedText("&cThe legendary spawner has not yet run to determine when the next spawn attempt will be! Check again later!"));

        }

    }

    private static String makeTimeReadable (LocalDateTime node, LocalDateTime now) {

        Duration duration = Duration.between(now, node);
        return printSeconds(duration.getSeconds());

    }

    private static String printSeconds (long seconds) {

        StringBuilder timeString = new StringBuilder();
        if (timeString.length() != 0 || seconds >= 86400) timeString.append(seconds / 86400).append(" days, ");
        if (timeString.length() != 0 || seconds >= 3600) timeString.append(seconds % 86400 / 3600).append(" hours, ");
        if (timeString.length() != 0 || seconds >= 60) timeString.append(seconds % 3600 / 60).append(" minutes, ");
        timeString.append(seconds % 60).append(" seconds");
        return timeString.toString();

    }

}
