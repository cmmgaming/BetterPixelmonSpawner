package com.lypaka.betterpixelmonspawner.DeadZones;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.lypakautils.WorldDimGetter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.List;

public class DeadZone {

    private final List<String> entities;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final World world;

    public DeadZone (List<String> entities, int maxX, int maxY, int maxZ, int minX, int minY, int minZ, World world) {

        this.entities = entities;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.world = world;

    }

    public void create() {

        BetterPixelmonSpawner.deadZones.add(this);

    }

    public List<String> getEntities() {

        return this.entities;

    }

    public int getMaxX() {

        return this.maxX;

    }

    public int getMaxY() {

        return this.maxY;

    }

    public int getMaxZ() {

        return this.maxZ;

    }

    public int getMinX() {

        return this.minX;

    }

    public int getMinY() {

        return this.minY;

    }

    public int getMinZ() {

        return this.minZ;

    }

    public World getWorld() {

        return this.world;

    }

    public static boolean isInArea (EntityPlayerMP player) {

        if (BetterPixelmonSpawner.deadZones.size() == 0) return false;
        try {

            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            int playerDim = WorldDimGetter.getDimID(player.world, player);

            if (playerDim == -99999) return false;

            for (DeadZone deadZone : BetterPixelmonSpawner.deadZones) {

                if (x >= deadZone.getMinX() && x <= deadZone.getMaxX() &&
                        y >= deadZone.getMinY() && y <= deadZone.getMaxY() &&
                        z >= deadZone.getMinZ() && z <= deadZone.getMaxZ() &&
                        WorldDimGetter.getDimID(deadZone.getWorld(), player) == playerDim) {

                    return true;

                }

            }

        } catch (IndexOutOfBoundsException er) {

            if (!player.world.getMinecraftServer().isDedicatedServer()) {

                return false;

            } else {

                BetterPixelmonSpawner.logger.error("Couldn't get dimension ID!");
                return true;

            }

        }


        return false;

    }

    public static DeadZone getAreaFromLocation (EntityPlayerMP player) {

        if (BetterPixelmonSpawner.deadZones.size() == 0) return null;
        DeadZone deadZone = null;
        try {

            int x = player.getPosition().getX();
            int y = player.getPosition().getY();
            int z = player.getPosition().getZ();
            int playerDim = WorldDimGetter.getDimID(player.world, player);

            if (playerDim == -99999) return null;

            for (DeadZone a : BetterPixelmonSpawner.deadZones) {

                if (x >= a.getMinX() && x <= a.getMaxX() &&
                        y >= a.getMinY() && y <= a.getMaxY() &&
                        z >= a.getMinZ() && z <= a.getMaxZ() &&
                        WorldDimGetter.getDimID(a.getWorld(), player) == playerDim) {

                    deadZone = a;
                    break;

                }

            }

        } catch (IndexOutOfBoundsException er) {

            if (player.world.getMinecraftServer().isDedicatedServer()) {

                BetterPixelmonSpawner.logger.error("Detected an error getting an Area from a location!");

            }

        }

        return deadZone;

    }

}
