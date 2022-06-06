package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.SpawnerUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class OptCommand extends CommandBase {

    @Override
    public String getName() {

        return "opt";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pouts opt <in|out> <module>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length < 3) {

            sender.sendMessage(FancyText.getFancyText("&e" + getUsage(sender)));
            return;

        }

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            String opt = args[1];
            String module = args[2];
            if (opt.equalsIgnoreCase("in")) {

                SpawnerUtils.remove(player, module);

            } else if (opt.equalsIgnoreCase("out")) {

                SpawnerUtils.add(player, module);

            }

        }

    }
}
