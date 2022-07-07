package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.SpawnList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            SpawnList list = new SpawnList(player);
            list.build();
            list.open(1);

        }

    }

}
