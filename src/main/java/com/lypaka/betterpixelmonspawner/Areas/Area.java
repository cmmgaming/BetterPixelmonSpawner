package com.lypaka.betterpixelmonspawner.Areas;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.lypakautils.WorldDimGetter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class Area {

    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final World world;

    public Area (int maxX, int maxY, int maxZ, int minX, int minY, int minZ, World world) {

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.world = world;

    }

    public void create() {

        BetterPixelmonSpawner.areas.add(this);

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

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        int playerDim = WorldDimGetter.getDimID(player.world);

        for (Area area : BetterPixelmonSpawner.areas) {

            if (x >= area.getMinX() && x <= area.getMaxX() &&
                y >= area.getMinY() && y <= area.getMaxY() &&
                z >= area.getMinZ() && z <= area.getMaxZ() &&
                WorldDimGetter.getDimID(area.getWorld()) == playerDim) {

                return true;

            }

        }

        return false;

    }

}
