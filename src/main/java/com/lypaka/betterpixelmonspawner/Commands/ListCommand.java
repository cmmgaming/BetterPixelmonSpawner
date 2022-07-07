package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.MainSpawnMenu;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Loader;

public class ListCommand extends CommandBase {

    @Override
    public String getName() {

        return "list";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps list";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (!Loader.isModLoaded("gooeylibs2")) {

            sender.sendMessage(FancyText.getFancyText("&cMissing GooeyLibs2! Its needed to open this menu!"));
            sender.sendMessage(FancyText.getFancyText("&eIf on single player, message me!"));
            return;

        }
        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            MainSpawnMenu.open(player);

        }

    }

}
