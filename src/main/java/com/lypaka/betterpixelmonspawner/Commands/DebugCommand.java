package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.DebugSystem.PlayerDebug;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.UUID;

public class DebugCommand extends CommandBase {

    @Override
    public String getName() {

        return "debug";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps debug <player> <spawner>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                player.sendMessage(FancyText.getFancyText("&cYou don't have permission to use this command!"));
                return;

            }

        }

        if (args.length < 3) {

            sender.sendMessage(FancyText.getFancyText(getUsage(sender)));

        } else {

            String debugArg = args[0];
            String playerArg = args[1];
            String moduleArg = args[2];

            EntityPlayerMP target = null;
            for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

                if (entry.getValue().getName().equalsIgnoreCase(playerArg)) {

                    target = entry.getValue();
                    break;

                }

            }

            if (target == null) {

                sender.sendMessage(FancyText.getFancyText("&cInvalid player name!"));
                return;

            }

            if (moduleArg.equalsIgnoreCase("pokemon")) {

                if (!PlayerDebug.trackedPlayers.contains(target.getUniqueID())) {

                    PlayerDebug.trackedPlayers.add(target.getUniqueID());
                    sender.sendMessage(FancyText.getFancyText("&aSuccessfully added " + target.getName() + " to the Pokemon debug list!"));
                    sender.sendMessage(FancyText.getFancyText("&eRun \"/bps debug " + target.getName() + " Pokemon\" again to remove this player from the debug list!"));

                } else {

                    EntityPlayerMP finalTarget = target;
                    PlayerDebug.trackedPlayers.removeIf(entry -> {

                        if (entry.toString().equalsIgnoreCase(finalTarget.getUniqueID().toString())) {

                            sender.sendMessage(FancyText.getFancyText("&aSuccessfully removed " + finalTarget.getName() + " from the Pokemon debug list!"));
                            return true;

                        }

                        return false;

                    });

                }

            }

        }

    }

}
