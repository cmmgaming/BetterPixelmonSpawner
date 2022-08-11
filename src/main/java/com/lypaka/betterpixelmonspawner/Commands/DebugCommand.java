package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.DebugSystem.NPCDebug;
import com.lypaka.betterpixelmonspawner.DebugSystem.PokemonDebug;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Spawners.NPCSpawner;
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

        return "/bps debug <spawner> [<player>]";

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

        if (args.length < 2) {

            sender.sendMessage(FancyText.getFancyText(getUsage(sender)));

        } else {

            String debugArg = args[0];
            String moduleArg = args[1];
            if (args.length == 3) {

                String playerArg = args[2];
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

                    if (!PokemonDebug.trackedPlayers.contains(target.getUniqueID())) {

                        PokemonDebug.trackedPlayers.add(target.getUniqueID());
                        sender.sendMessage(FancyText.getFancyText("&aSuccessfully added " + target.getName() + " to the Pokemon debug list!"));
                        sender.sendMessage(FancyText.getFancyText("&eRun \"/bps debug Pokemon " + target.getName() + "\" again to remove this player from the debug list!"));

                    } else {

                        EntityPlayerMP finalTarget = target;
                        PokemonDebug.trackedPlayers.removeIf(entry -> {

                            if (entry.toString().equalsIgnoreCase(finalTarget.getUniqueID().toString())) {

                                sender.sendMessage(FancyText.getFancyText("&aSuccessfully removed " + finalTarget.getName() + " from the Pokemon debug list!"));
                                return true;

                            }

                            return false;

                        });

                    }

                } else if (moduleArg.equalsIgnoreCase("npc")) {

                    if (!NPCDebug.trackedPlayers.contains(target.getUniqueID())) {

                        NPCDebug.trackedPlayers.add(target.getUniqueID());
                        sender.sendMessage(FancyText.getFancyText("&aSuccessfully added " + target.getName() + " to the NPC debug list!"));
                        sender.sendMessage(FancyText.getFancyText("&eRun \"/bps debug npc " + target.getName() + "\" again to remove this player from the debug list!"));

                    } else {

                        EntityPlayerMP finalTarget = target;
                        NPCDebug.trackedPlayers.removeIf(entry -> {

                            if (entry.toString().equalsIgnoreCase(finalTarget.getUniqueID().toString())) {

                                sender.sendMessage(FancyText.getFancyText("&aSuccessfully removed " + finalTarget.getName() + " from the NPC debug list!"));
                                return true;

                            }

                            return false;

                        });

                    }

                }

            } else {

                if (moduleArg.equalsIgnoreCase("npc")) {

                    String mode;
                    String opposite;
                    if (NPCSpawner.debugEnabled) {

                        NPCSpawner.debugEnabled = false;
                        mode = "&cDisabled";
                        opposite = "enable";

                    } else {

                        NPCSpawner.debugEnabled = true;
                        mode = "&aEnabled";
                        opposite = "disable";

                    }

                    sender.sendMessage(FancyText.getFancyText("&eNPC Spawner debug mode: " + mode));
                    sender.sendMessage(FancyText.getFancyText("&eRun this command again to " + opposite + " NPC debug mode."));

                } else if (moduleArg.equalsIgnoreCase("pokemon")) {

                    sender.sendMessage(FancyText.getFancyText("&eYou must specify a player name when wanting to debug the Pokemon spawner!"));

                }

            }

        }

    }

}
