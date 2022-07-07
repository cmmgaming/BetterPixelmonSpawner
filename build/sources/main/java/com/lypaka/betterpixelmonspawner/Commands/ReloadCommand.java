package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Areas.AreaRegistry;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigManager;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Spawners.MiscSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.NPCSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.PokemonSpawner;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Loader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ReloadCommand extends CommandBase {

    @Override
    public String getName() {

        return "reload";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pspawner reload";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                player.sendMessage(FancyText.getFancyText("&cYou do not have permission to use this command!"));
                return;

            }

        }

        try {

            sender.sendMessage(FancyText.getFancyText("&eStarting reloading of BPS spawner, please wait..."));
            ConfigManager.load();
            PokemonConfig.load();
            ConfigGetters.load();
            BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
            InfoRegistry.loadPokemonSpawnData();
            BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
            if (Loader.isModLoaded("betterbosses")) {

                if (!com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                    BossPokemonUtils.loadBossList();

                }

            } else {

                BossPokemonUtils.loadBossList();

            }

            // Loads holidays from config, not worth having config getters for it since it only loads on startup unless reload command is ran
            HolidayHandler.loadHolidays();

            // Loads the areas
            AreaRegistry.loadAreas();

            BetterPixelmonSpawner.logger.info("Starting spawners...");
            PokemonSpawner.startTimer();
            LegendarySpawner.startTimer();
            NPCSpawner.startTimer();
            MiscSpawner.startTimer();

            ClearTask.startClearTask();

        } catch (ObjectMappingException e) {

            e.printStackTrace();

        }

        sender.sendMessage(FancyText.getFancyText("&aSuccessfully reloaded BetterPixelmonSpawner."));

    }

}
