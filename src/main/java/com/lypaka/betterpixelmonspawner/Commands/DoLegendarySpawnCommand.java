package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.API.HostileEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.LegendarySpawnEvent;
import com.lypaka.betterpixelmonspawner.Areas.Area;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryInfoGetters;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.lypaka.betterpixelmonspawner.Utils.PermissionHandler;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.LegendaryUtils;
import com.lypaka.lypakautils.FancyText;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.api.spawning.conditions.WorldTime;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DoLegendarySpawnCommand extends CommandBase {

    @Override
    public String getName() {

        return "dolegendaryspawn";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps dolegendaryspawn";

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

        LegendarySpawner.nextSpawnAttempt = LocalDateTime.now().plusSeconds(ConfigGetters.legendarySpawnInterval);
        List<EntityPlayerMP> onlinePlayers = new ArrayList<>();
        for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

            if (!ConfigGetters.legendaryOptOut.contains(entry.getValue().getUniqueID().toString())) {

                if (Area.getAreaFromLocation(entry.getValue()) != null) {

                    Area area = Area.getAreaFromLocation(entry.getValue());
                    List<String> entities = area.getEntities();
                    if (!entities.contains("legendaries")) {

                        onlinePlayers.add(entry.getValue());

                    }

                } else {

                    onlinePlayers.add(entry.getValue());

                }

            }

        }

        if (onlinePlayers.size() == 0) return;
        EntityPlayerMP player;
        if (onlinePlayers.size() == 1) {

            player = onlinePlayers.get(0);

        } else {

            player = onlinePlayers.get(BetterPixelmonSpawner.random.nextInt(onlinePlayers.size()));

        }
        String worldName = player.world.getWorldInfo().getWorldName();
        if (ConfigGetters.worldBlacklist.contains(worldName)) return;
        String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
        if (!BiomeList.biomesToPokemon.containsKey(biomeID)) return;

        List<LegendarySpawnInfo> possibleSpawns = new ArrayList<>();
        List<String> usedNames = new ArrayList<>();
        String location;
        if (player.isInWater()) {

            location = "water";

        } else if (player.onGround) {

            if (player.getPosition().getY() <= 63) {

                location = "underground";

            } else {

                location = "land";

            }

        }  else {

            location = "air";

        }
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

            if (!ConfigGetters.legendarySpawnFilterEnabled) {

                for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                    for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                        if (entry.getKey().equalsIgnoreCase(name)) {

                            List<LegendarySpawnInfo> infos = entry.getValue();
                            for (LegendarySpawnInfo info : infos) {

                                if (!usedNames.contains(info.getName())) {

                                    usedNames.add(info.getName());
                                    possibleSpawns.add(info);

                                }

                            }

                        }

                    }

                }

            } else {

                int ticks = (int) (player.world.getWorldTime() % 24000L);
                ArrayList<WorldTime> currentTimes = WorldTime.getCurrent(ticks);
                String weather;
                if (player.world.isRaining()) {

                    weather = "rain";

                } else if (player.world.isThundering()) {

                    weather = "storm";

                } else {

                    weather = "clear";

                }

                for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                    for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                        if (entry.getKey().equalsIgnoreCase(name)) {

                            List<LegendarySpawnInfo> infos = entry.getValue();
                            for (LegendarySpawnInfo info : infos) {

                                if (currentTimes.contains(WorldTime.valueOf(info.getTime().toUpperCase()))) {

                                    if (info.getWeather().equalsIgnoreCase(weather)) {

                                        if (info.getSpawnLocation().contains(location)) {

                                            if (!usedNames.contains(info.getName())) {

                                                if (RandomHelper.getRandomChance(info.getSpawnChance())) {

                                                    possibleSpawns.add(info);
                                                    usedNames.add(info.getName());

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

            if (possibleSpawns.size() == 0) {

                BetterPixelmonSpawner.logger.info("A legendary was not spawned!");
                return;

            }

            LegendarySpawnInfo selectedSpawn;
            if (possibleSpawns.size() == 1) {

                selectedSpawn = possibleSpawns.get(0);

            } else {

                selectedSpawn = possibleSpawns.get(BetterPixelmonSpawner.random.nextInt(possibleSpawns.size()));

            }

            String[] levelRange = selectedSpawn.getLevelRange().split("-");
            int max = Integer.parseInt(levelRange[0]);
            int min = Integer.parseInt(levelRange[1]);
            String pokemonName = selectedSpawn.getName();
            EntityPixelmon pokemon;
            pokemonName = pokemonName.replace(".conf", "");
            if (pokemonName.contains("-")) {

                if (pokemonName.equalsIgnoreCase("porygon-z")) {

                    pokemonName = "porygon-z";
                    pokemon = PokemonSpec.from(pokemonName).create(player.world);

                } else {

                    String[] split = pokemonName.split("-");
                    pokemonName = split[0];
                    String form = "";
                    for (int f = 1; f < split.length; f++) {

                        form = form + "-" + split[f];

                    }

                    pokemon = PokemonSpec.from(pokemonName).create(player.world);
                    int pokemonForm = FormIndexFromName.getFormNumberFromFormName(pokemonName, form);
                    pokemon.setForm(pokemonForm, true);

                }

            } else {

                pokemon = PokemonSpec.from(pokemonName).create(player.world);

            }
            int level = RandomHelper.getRandomNumberBetween(min, max);
            pokemon.getLvl().setLevel(level);
            pokemon.updateStats();

            if (selectedSpawn.getHeldItemID() != null) {

                pokemon.heldItem = new ItemStack(Item.getByNameOrId(selectedSpawn.getHeldItemID()));

            }

            if (selectedSpawn.getTexture() != null) {

                pokemon.setCustomSpecialTexture(selectedSpawn.getTexture());

            }

            int xzRadius = ConfigGetters.legendarySpawnLocationXZ;
            int yRadius = ConfigGetters.legendarySpawnLocationY;
            int spawnX;
            int spawnY;
            int spawnZ;

            if (RandomHelper.getRandomChance(50)) {

                spawnX = player.getPosition().getX() + xzRadius;

            } else {

                spawnX = player.getPosition().getX() - xzRadius;

            }
            if (RandomHelper.getRandomChance(50)) {

                spawnZ = player.getPosition().getZ() + xzRadius;

            } else {

                spawnZ = player.getPosition().getZ() - xzRadius;

            }

            BlockPos safeSpawn;
            if (location.equalsIgnoreCase("air")) {

                spawnY = player.getPosition().getY() + yRadius;
                safeSpawn = new BlockPos(spawnX, spawnY, spawnZ);

            } else {

                spawnY = player.getPosition().getY();
                BlockPos baseSpawn = new BlockPos(spawnX, spawnY, spawnZ);
                safeSpawn = new BlockPos(spawnX, player.world.getTopSolidOrLiquidBlock(baseSpawn).getY(), spawnZ);

            }
            pokemon.setLocationAndAngles(safeSpawn.getX() + BetterPixelmonSpawner.random.nextDouble(), safeSpawn.getY(), safeSpawn.getZ() + BetterPixelmonSpawner.random.nextDouble(),0, 0);
            boolean hostile = false;
            if (selectedSpawn.isHostile()) {

                hostile = true;
                HostileEvent hostileEvent = new HostileEvent(pokemon, player, selectedSpawn);
                MinecraftForge.EVENT_BUS.post(hostileEvent);
                if (hostileEvent.isCanceled()) {

                    hostile = false;

                }

            }
            pokemon.setHostile(hostile);

            LegendarySpawnEvent legendarySpawnEvent = new LegendarySpawnEvent(pokemon, player, selectedSpawn);
            MinecraftForge.EVENT_BUS.post(legendarySpawnEvent);
            if (!legendarySpawnEvent.isCanceled()) {

                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                    player.world.spawnEntity(legendarySpawnEvent.getPokemon());
                    LegendaryUtils.handleGlowing(legendarySpawnEvent.getPokemon());
                    LegendaryUtils.handleGracePeriod(legendarySpawnEvent.getPokemon(), legendarySpawnEvent.getPlayer());
                    LegendaryInfoGetters.setLegendaryName(legendarySpawnEvent.getPokemon().getPokemonName());
                    LegendaryInfoGetters.setPokemon(legendarySpawnEvent.getPokemon());
                    LegendaryInfoGetters.setSpawnedPlayer(player);
                    LegendaryInfoGetters.setSpawnPos(legendarySpawnEvent.getPokemon().getPosition());
                    LegendaryInfoGetters.setTime(LocalDateTime.now());
                    LegendaryListing.updateListingConfig(legendarySpawnEvent.getPokemon());
                    legendarySpawnEvent.getPokemon().addTag("SpawnedLegendary"); // used for the last legend list, so the event listeners know what is a BPS spawned legendary
                    if (!ConfigGetters.legendarySpawnMessage.equalsIgnoreCase("")) {

                        BetterPixelmonSpawner.server.getPlayerList().sendMessage(com.lypaka.betterpixelmonspawner.Utils.FancyText.getFancyText(ConfigGetters.legendarySpawnMessage
                                .replace("%biome%", LegendarySpawner.getPrettyBiomeName(biomeID))
                                .replace("%pokemon%", legendarySpawnEvent.getPokemon().getPokemonName())
                                .replace("%player%", legendarySpawnEvent.getPlayer().getName())
                        ));

                    }

                });

            }

        });

    }

}
