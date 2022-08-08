package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.Spawning.NPCSpawnEvent;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DebugSystem.NPCDebug;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.EntityUtils;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import com.pixelmongenerations.common.entity.npcs.EntityNPC;
import com.pixelmongenerations.common.entity.npcs.NPCTrainer;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class NPCSpawner {

    private static Timer timer;
    public static boolean debugEnabled = false;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.npcSpawnerEnabled) return;

        long interval = ConfigGetters.npcSpawnInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                    for (Map.Entry<UUID, EntityPlayerMP> playerEntry : JoinListener.playerMap.entrySet()) {

                        EntityPlayerMP player = playerEntry.getValue();
                        NPCDebug.printPlayerDebugInformation(player);
                        if (ConfigGetters.npcOptOut.contains(player.getUniqueID().toString())) continue;
                        if (NPCCounter.getCount(player.getUniqueID()) > ConfigGetters.maxNPCs) {

                            if (ConfigGetters.maxNPCs > 0) {

                                continue;

                            }

                        }
                        if (player.isCreative() && ConfigGetters.ignoreCreativeNPC) continue;
                        if (player.isSpectator() && ConfigGetters.ignoreSpectatorNPC) continue;
                        String worldName = player.world.getWorldInfo().getWorldName();
                        if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                        if (ConfigGetters.unsafeSpawnLocations) {

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

                        int xzRadius = ConfigGetters.npcSpawnLocationXZ;
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

                        int y = -1;
                        try {

                            y = player.world.getTopSolidOrLiquidBlock(pos).getY();

                        } catch (Exception er) {

                            // empty catch block

                        }
                        if (y == -1) continue;
                        BlockPos spawn = new BlockPos(x, y, z);
                        double sum = ConfigGetters.npcSpawnMap.values().stream().mapToDouble(c -> c).sum();
                        double rng = sum * BetterPixelmonSpawner.random.nextDouble();
                        String selectedID = null;
                        for (Map.Entry<String, Double> entry : ConfigGetters.npcSpawnMap.entrySet()) {

                            if (debugEnabled) {

                                BetterPixelmonSpawner.logger.info("NPC DEBUG: Pinging " + entry.getKey() + " / " + entry.getValue());

                            }
                            if (Double.compare(entry.getValue(), rng) <= 0) {

                                selectedID = entry.getKey();
                                if (debugEnabled) {

                                    BetterPixelmonSpawner.logger.info("NPC DEBUG: " + selectedID + " was chosen for spawn");

                                }
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
                        String biomeID = player.world.getBiome(spawn).getRegistryName().toString();
                        EntityLivingBase entity = EntityUtils.getEntityFromID(biomeID, selectedID, player.world);
                        if (entity == null) {

                            BetterPixelmonSpawner.logger.error("Couldn't get an entity from ID: " + selectedID);
                            continue;

                        }
                        entity.setLocationAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
                        NPCSpawnEvent npcSpawnEvent = new NPCSpawnEvent((EntityNPC) entity, player);
                        MinecraftForge.EVENT_BUS.post(npcSpawnEvent);
                        if (!npcSpawnEvent.isCanceled()) {

                            int finalY = y;
                            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                player.world.spawnEntity(npcSpawnEvent.getNPC());
                                NPCCounter.increment(npcSpawnEvent.getNPC(), npcSpawnEvent.getPlayer().getUniqueID());
                                if (npcSpawnEvent.getNPC() instanceof NPCTrainer) {

                                    if (ConfigGetters.scaleNPCLevelsByDistance) {

                                        int level = 3;
                                        if ((int)((double)level + Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(x, finalY, z)) / (double)ConfigGetters.blocksBeforeNPCIncrease + Math.random() * 3.0)) > ConfigGetters.maxNPCScaleLevel) {

                                            level = ConfigGetters.maxNPCScaleLevel;

                                        } else {

                                            int distance = (int) Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(x, finalY, z)));
                                            if (distance > ConfigGetters.blocksBeforeNPCIncrease) {

                                                int mod = (distance / ConfigGetters.blocksBeforeNPCIncrease) * ConfigGetters.npcLevelModifier;
                                                level = mod + level;

                                            }

                                        }

                                        NPCTrainer trainer = (NPCTrainer) npcSpawnEvent.getNPC();
                                        trainer.setLevel(level);

                                    }

                                }
                                EntityUtils.handleDespawning(npcSpawnEvent.getNPC());

                            });

                        }

                    }


                });

            }

        }, interval, interval);

    }

}
