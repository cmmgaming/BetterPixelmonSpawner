package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class CheckCountCommand extends CommandBase {

    @Override
    public String getName() {

        return "checkcount";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("count");
        return a;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps checkcount <spawner>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.check")) {

                player.sendMessage(FancyText.getFancyText("&cYou don't have permission to use this command!"));
                return;

            }

            String checkArg = args[0];
            if (args.length < 2) {

                player.sendMessage(FancyText.getFancyText(getUsage(player)));
                return;

            }

            String spawnerArg = args[1];
            if (spawnerArg.equalsIgnoreCase("pokemon")) {

                int count = PokemonCounter.getCount(player.getUniqueID());
                player.sendMessage(FancyText.getFancyText("&eYou currently have " + count + " Pokemon spawned on you!"));

            }

        }

    }

}
