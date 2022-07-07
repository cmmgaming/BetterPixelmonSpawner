package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.lypakautils.FancyText;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.item.ItemPixelmonSprite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MainSpawnMenu {

    public static void open (EntityPlayerMP player) {

        ChestTemplate template = ChestTemplate.builder(3).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&dMain Menu"))
                .build();

        // 11 pokemon
        // 13 fish
        // 15 legendary
        for (int i = 0; i < 27; i++) {

            if (i == 11) {

                page.getTemplate().getSlot(i).setButton(getPokemonButton(player));

            } else if (i == 13) {

                page.getTemplate().getSlot(i).setButton(getFishButton(player));

            } else if (i == 15) {

                page.getTemplate().getSlot(i).setButton(getLegendaryButton(player));

            } else {

                page.getTemplate().getSlot(i).setButton(getGlass());

            }

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getGlass() {

        ItemStack glass = new ItemStack(Item.getByNameOrId("minecraft:stained_glass_pane"));
        glass.setItemDamage(5);
        glass.setStackDisplayName(FancyText.getFormattedString(""));
        return GooeyButton.builder().display(glass).build();

    }

    private static Button getPokemonButton (EntityPlayerMP player) {

        EntityPixelmon pixelmon = PokemonSpec.from("Bulbasaur").create(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        ItemStack bulbasaur = ItemPixelmonSprite.getPhoto(pixelmon);
        bulbasaur.setStackDisplayName(FancyText.getFormattedString("&eClick me to view spawns for normal Pokemon!"));
        return GooeyButton.builder()
                .display(bulbasaur)
                .onClick(() -> {

                    PokemonSpawnList list = new PokemonSpawnList(player);
                    list.build();
                    list.open(1);

                })
                .build();

    }

    private static Button getFishButton (EntityPlayerMP player) {

        EntityPixelmon pixelmon = PokemonSpec.from("Magikarp").create(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        ItemStack magikarp = ItemPixelmonSprite.getPhoto(pixelmon);
        magikarp.setStackDisplayName(FancyText.getFormattedString("&eClick me to view spawns for fishing!"));
        return GooeyButton.builder()
                .display(magikarp)
                .onClick(() -> {

                    FishSpawnList list = new FishSpawnList(player);
                    list.build();
                    list.open(1);

                })
                .build();

    }

    private static Button getLegendaryButton (EntityPlayerMP player) {

        EntityPixelmon pixelmon = PokemonSpec.from("Mew").create(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        ItemStack mew = ItemPixelmonSprite.getPhoto(pixelmon);
        mew.setStackDisplayName(FancyText.getFormattedString("&eClick me to view spawns for legendary Pokemon!"));
        return GooeyButton.builder()
                .display(mew)
                .onClick(() -> {

                    LegendarySpawnList list = new LegendarySpawnList(player);
                    list.build();
                    list.open(1);

                })
                .build();

    }

}
