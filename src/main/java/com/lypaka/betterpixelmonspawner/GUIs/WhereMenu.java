package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.item.ItemPixelmonSprite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WhereMenu {

    private final EntityPlayerMP player;
    private String pokemonName;

    public WhereMenu (EntityPlayerMP player, String pokemonName) {

        this.player = player;
        this.pokemonName = pokemonName;

    }

    public void open() {

        EntityPixelmon pokemon;
        this.pokemonName = this.pokemonName.replace(".conf", "");
        if (this.pokemonName.contains("-")) {

            if (this.pokemonName.equalsIgnoreCase("ho-oh")) {

                this.pokemonName = "Ho-Oh";
                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);

            } else if (pokemonName.equalsIgnoreCase("Hakamo-o")) {

                this.pokemonName = "Hakamo-o";
                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);

            } else if (pokemonName.equalsIgnoreCase("Jangmo-o")) {

                this.pokemonName = "Jangmo-o";
                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);

            } else if (pokemonName.equalsIgnoreCase("Kommo-o")) {

                this.pokemonName = "Kommo-o";
                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);

            } else if (pokemonName.equalsIgnoreCase("Porygon-Z")) {

                this.pokemonName = "Porygon-Z";
                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);

            } else {

                String[] split = this.pokemonName.split("-");
                this.pokemonName = split[0];
                String form = "";
                for (int f = 1; f < split.length; f++) {

                    form = form + "-" + split[f];

                }

                pokemon = PokemonSpec.from(this.pokemonName).create(this.player.world);
                int pokemonForm = FormIndexFromName.getFormNumberFromFormName(this.pokemonName, form);
                pokemon.setForm(pokemonForm, true);

            }

        } else {

            pokemon = PokemonSpec.from(this.pokemonName).create(player.world);

        }
        ChestTemplate template = ChestTemplate.builder(3).build();
        GooeyPage page = GooeyPage.builder()
                .title(FancyText.getFancyString("&eBiomes..."))
                .template(template)
                .build();

        for (int i = 0; i < 27; i++) {

            page.getTemplate().getSlot(i).setButton(getGlass());

        }


        List<String> biomes = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : BiomeList.biomesToPokemon.entrySet()) {

            String biome = entry.getKey();
            for (String names : entry.getValue()) {

                if (names.contains(this.pokemonName)) {

                    if (!biomes.contains(biome)) {

                        biomes.add(biome);

                    }

                }

            }

        }

        NBTTagList lore = new NBTTagList();
        for (String s : biomes) {

            lore.appendTag(new NBTTagString(FancyText.getFancyString("&e" + s)));

        }

        ItemStack sprite = ItemPixelmonSprite.getPhoto(pokemon);
        sprite.setStackDisplayName(FancyText.getFancyString("&a" + pokemon.getPokemonName()));
        sprite.getOrCreateSubCompound("display").setTag("Lore", lore);
        page.getTemplate().getSlot(13).setButton(GooeyButton.builder().display(sprite).build());

        UIManager.openUIForcefully(this.player, page);

    }

    private Button getGlass() {

        ItemStack glass = new ItemStack(Item.getByNameOrId("minecraft:stained_glass_pane"));
        glass.setItemDamage(14);
        glass.setStackDisplayName(FancyText.getFancyString(""));
        return GooeyButton.builder().display(glass).build();

    }

}
