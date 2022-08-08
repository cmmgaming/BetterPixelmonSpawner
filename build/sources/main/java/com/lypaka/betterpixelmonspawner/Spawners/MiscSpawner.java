package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.Spawning.MiscSpawnEvent;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.EntityUtils;
import com.lypaka.betterpixelmonspawner.Utils.Counters.MiscCounter;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class MiscSpawner {

    private static Timer timer;
    public static boolean debugEnabled = false;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.miscSpawnerEnabled) return;

        long interval = ConfigGetters.miscEntityInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                    for (Map.Entry<UUID, EntityPlayerMP> playerEntry : JoinListener.playerMap.entrySet()) {

                        EntityPlayerMP player = playerEntry.getValue();
                        if (ConfigGetters.miscOptOut.contains(player.getUniqueID().toString())) continue;
                        if (MiscCounter.getCount(player.getUniqueID()) > ConfigGetters.maxMiscEntities) {

                            if (ConfigGetters.maxMiscEntities > 0) {

                                continue;

                            }

                        }
                        if (player.isCreative() && ConfigGetters.ignoreCreativeMisc) continue;
                        if (player.isSpectator() && ConfigGetters.ignoreSpectatorMisc) continue;
                        String worldName = player.world.getWorldInfo().getWorldName();
                        if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                        if (ConfigGetters.unsafeMiscSpawnLocations) {

                            if (player.isInWater()) {

                                continue;

                            } else if (player.onGround) {

                                if (player.getPosition().getY() <= 63) {

                                    continue;

                                }

                            }  else {

                                continue;

                            }

                        }
                        int xzRadius = ConfigGetters.miscRadiusXZ;
                        int x;
                        int z;
                        int playerX = player.getPosition().getX();
                        int playerY = player.getPosition().getY();
                        int playerZ = player.getPosition().getZ();
                        if (RandomHelper.getRandomChance(50)) {

                            x = playerX + RandomHelper.getRandomNumberBetween(1, xzRadius);

                        } else {

                            x = playerX - RandomHelper.getRandomNumberBetween(1, xzRadius);

                        }
                        if (RandomHelper.getRandomChance(50)) {

                            z = playerZ + RandomHelper.getRandomNumberBetween(1, xzRadius);

                        } else {

                            z = playerZ - RandomHelper.getRandomNumberBetween(1, xzRadius);

                        }
                        BlockPos pos = new BlockPos(x, playerY, z);
                        int y = player.world.getTopSolidOrLiquidBlock(pos).getY();
                        BlockPos spawn = new BlockPos(x, y, z);
                        double sum = ConfigGetters.miscEntitySpawnMap.values().stream().mapToDouble(c -> c).sum();
                        double rng = BetterPixelmonSpawner.random.nextDouble() * sum;
                        String selectedID = null;
                        for (Map.Entry<String, Double> entry : ConfigGetters.miscEntitySpawnMap.entrySet()) {

                            if (Double.compare(entry.getValue(), rng) <= 0) {

                                selectedID = entry.getKey();
                                break;

                            } else {

                                rng -= entry.getValue();

                            }

                        }
                        if (selectedID == null) continue;
                        if (DeadZone.getAreaFromLocation(player) != null) {

                            DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                            List<String> entities = deadZone.getEntities();
                            if (entities.contains(selectedID)) {

                                continue;

                            }

                        }
                        EntityLivingBase entity = EntityUtils.getEntityFromID(null, selectedID, player.world);
                        if (entity == null) {

                            BetterPixelmonSpawner.logger.error("Couldn't get an entity from ID: " + selectedID);
                            continue;

                        }
                        entity.setLocationAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
                        entity.setEntityBoundingBox(new AxisAlignedBB(entity.getPosition()));
                        MiscSpawnEvent miscSpawnEvent = new MiscSpawnEvent(entity, player, selectedID);
                        MinecraftForge.EVENT_BUS.post(miscSpawnEvent);
                        if (!miscSpawnEvent.isCanceled()) {

                            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                if (ConfigGetters.miscBroadcastMap.containsKey(miscSpawnEvent.getSelectedEntityID())) {

                                    String message = ConfigGetters.miscBroadcastMap.get(miscSpawnEvent.getSelectedEntityID());
                                    if (!message.equalsIgnoreCase("")) {

                                        String biome = miscSpawnEvent.getEntity().world.getBiome(miscSpawnEvent.getEntity().getPosition()).getRegistryName().toString();
                                        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(FancyText.getFancyText(message
                                                .replace("%biome%", biome)
                                                .replace("%player%", miscSpawnEvent.getPlayer().getName())
                                        ));

                                    }

                                }
                                player.world.spawnEntity(miscSpawnEvent.getEntity());
                                MiscCounter.increment(miscSpawnEvent.getEntity(), miscSpawnEvent.getPlayer().getUniqueID());
                                EntityUtils.handleDespawning(miscSpawnEvent.getEntity());

                            });

                        }

                    }

                });

            }

        }, 0, interval);

    }

}
