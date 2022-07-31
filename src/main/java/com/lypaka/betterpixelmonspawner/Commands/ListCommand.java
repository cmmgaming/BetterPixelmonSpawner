package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.MainSpawnMenu;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
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

        return "/bps list [biome]";

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
            String biome = player.world.getBiome(player.getPosition()).getRegistryName().toString();
            if (args.length == 2) {

                biome = args[1];

            }
            if (BiomeList.biomesToPokemon.containsKey(biome)) {

                MainSpawnMenu.open(player, biome);

            } else {

                player.sendMessage(FancyText.getFancyText("&eEither invalid biome ID (like \"minecraft:ocean\" for example) or no Pokemon spawn in that biome!"));

            }

        }

    }

}
