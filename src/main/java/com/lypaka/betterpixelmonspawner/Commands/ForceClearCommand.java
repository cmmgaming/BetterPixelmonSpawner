package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.WorldMap;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ForceClearCommand extends CommandBase {

    @Override
    public String getName() {

        return "clear";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps clear";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

        }

        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

            for (Map.Entry<String, World> entry : WorldMap.worldMap.entrySet()) {

                World world = entry.getValue();
                List<Entity> entityList;
                if (!world.getMinecraftServer().isDedicatedServer()) {

                    EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUsername(world.getMinecraftServer().getPlayerList().getOnlinePlayerNames()[0]);
                    entityList = player.world.loadedEntityList;

                } else {

                    entityList = world.loadedEntityList;

                }
                for (Entity ent : entityList) {

                    if (ent instanceof EntityPixelmon) {

                        EntityPixelmon pokemon = (EntityPixelmon) ent;
                        if (!ClearTask.isBlacklisted(pokemon)) {

                            // decrements a player's Pokemon counter
                            for (String tag : pokemon.getTags()) {

                                if (tag.contains("SpawnedPlayerUUID:")) {

                                    String[] split = tag.split(":");
                                    UUID uuid = UUID.fromString(split[1]);
                                    PokemonCounter.decrement(uuid);
                                    break;

                                }

                            }
                            ClearTask.count++;
                            pokemon.setDead();

                        }

                    }

                }

            }

            String msg = "&e" + ClearTask.count + " Pokemon have been forcefully cleared.";
            if (ClearTask.count == 1 && msg.contains("have")) {

                msg = msg.replace("have", "has");

            }

            BetterPixelmonSpawner.server.getPlayerList().sendMessage(com.lypaka.betterpixelmonspawner.Utils.FancyText.getFancyText(msg));
            ClearTask.count = 0;

        });

    }

}
