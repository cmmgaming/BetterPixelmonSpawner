package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.FormIndexFromName;
import com.lypaka.lypakautils.FancyText;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.item.ItemCustomIcon;
import com.pixelmongenerations.common.item.ItemPixelmonSprite;
import com.pixelmongenerations.core.enums.items.EnumCustomIcon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.text.DecimalFormat;
import java.util.*;

public class SpawnList {

    private final EntityPlayerMP player;
    private final Map<Integer, List<PokemonSpawnInfo>> spawns;
    private int min;
    private int max;
    private final List<Integer> pages;

    public SpawnList (EntityPlayerMP player) {

        this.player = player;
        this.spawns = new HashMap<>();
        this.min = 0;
        this.max = 53;
        this.pages = new ArrayList<>();

    }

    public void build() {

        String biome = this.player.world.getBiome(this.player.getPosition()).getRegistryName().toString();
        if (BiomeList.biomePokemonMap.containsKey(biome)) {

            List<PokemonSpawnInfo> pokemonThatSpawn = BiomeList.biomePokemonMap.get(biome);
            List<String> pokemonNames = new ArrayList<>();
            for (PokemonSpawnInfo psi : pokemonThatSpawn) {

                if (!pokemonNames.contains(psi.getName())) {

                    pokemonNames.add(psi.getName());

                }

            }

            List<PokemonSpawnInfo> base = new ArrayList<>(pokemonNames.size());
            List<String> usedNames = new ArrayList<>();
            for (PokemonSpawnInfo pokemonSpawnInfo : pokemonThatSpawn) {

                if (!usedNames.contains(pokemonSpawnInfo.getName())) {

                    usedNames.add(pokemonSpawnInfo.getName());
                    base.add(pokemonSpawnInfo);

                }

            }

            List<PokemonSpawnInfo> pokemonToDisplay = arrangePokemon(base);
            int spawnAmount = pokemonNames.size(); // we use this list because of the different PokemonSpawnInfo objects for each Pokemon
            int pages = 1;
            if (spawnAmount > 54) {

                int dividedInt = spawnAmount / 54;
                double dividedDouble = (double) spawnAmount / 54;
                double dummyDouble = dividedInt + 0.0;
                if (dividedDouble > dummyDouble) {

                    pages = dividedInt + 1;

                }

            }

            for (int i = 1; i <= pages; i++) {

                this.pages.add(i);

            }

            for (int i = 1; i <= pages; i++) {

                setInts(i);
                List<PokemonSpawnInfo> pokemonToPutInMap = new ArrayList<>(pokemonToDisplay.size());
                for (int j = this.min; j < this.max; j++) {

                    try {

                        PokemonSpawnInfo spawnInfo = pokemonToDisplay.get(j);
                        pokemonToPutInMap.add(spawnInfo);

                    } catch (IndexOutOfBoundsException er) {

                        break;

                    }

                }

                List<PokemonSpawnInfo> arrangedList = arrangePokemon(pokemonToPutInMap);
                this.spawns.put(i, arrangedList);

            }

        }

    }

    public void open (int pageNum) {

        ChestTemplate template = ChestTemplate.builder(6).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&dSpawns: P" + pageNum))
                .build();

        List<PokemonSpawnInfo> pokemon = this.spawns.get(pageNum);
        int startingIndex = 0;
        if (pageNum > 1) {

            startingIndex = 1;
            int pageToGoTo = pageNum - 1;
            page.getTemplate().getSlot(0).setButton(getPrevButton(pageToGoTo));

        }
        for (PokemonSpawnInfo psi : pokemon) {

            page.getTemplate().getSlot(startingIndex).setButton(getPokemonSprite(psi.getName(), psi));
            startingIndex++;

        }
        int nextPage = pageNum + 1;
        if (this.spawns.containsKey(nextPage)) {

            page.getTemplate().getSlot(53).setButton(getNextButton(nextPage));

        }

        UIManager.openUIForcefully(this.player, page);

    }

    private List<PokemonSpawnInfo> arrangePokemon (List<PokemonSpawnInfo> pokemonList) {

        List<PokemonSpawnInfo> listToReturn = new ArrayList<>(pokemonList.size());
        List<Integer> pokedexNumbers = new ArrayList<>(pokemonList.size());
        Map<PokemonSpawnInfo, Integer> map = new HashMap<>();
        for (int i = 0; i < pokemonList.size(); i++) {

            PokemonSpawnInfo info = pokemonList.get(i);
            String name = info.getName().replace(".conf", "");
            EntityPixelmon pokemon;
            if (name.contains("-")) {

                if (name.equalsIgnoreCase("porygon-z")) {

                    name = "porygon-z";
                    pokemon = PokemonSpec.from(name).create(player.world);

                } else {

                    String[] split = name.split("-");
                    name = split[0];
                    String form = "";
                    for (int f = 1; f < split.length; f++) {

                        form = form + "-" + split[f];

                    }

                    pokemon = PokemonSpec.from(name).create(player.world);
                    int pokemonForm = FormIndexFromName.getFormNumberFromFormName(name, form);
                    pokemon.setForm(pokemonForm, true);

                }

            } else {

                pokemon = PokemonSpec.from(name).create(player.world);

            }

            int dex = pokemon.baseStats.nationalPokedexNumber;
            pokedexNumbers.add(i, dex);
            map.put(info, dex);

        }

        Collections.sort(pokedexNumbers);
        for (int i = 0; i < pokemonList.size(); i++) {

            int dex = pokedexNumbers.get(i);
            int finalI = i;
            map.entrySet().removeIf(entry -> {

                if (entry.getValue() == dex) {

                    listToReturn.add(finalI, entry.getKey());
                    return true;

                }

                return false;

            });

        }

        return listToReturn;

    }

    private void setInts (int page) {

        if (page > 1) {

            this.min = this.max + 1;
            if (this.pages.contains(page + 1)) {

                this.max = this.min + 52;

            } else {

                this.max = this.min + 53;

            }

        }

    }

    private Button getNextButton (int pageToGoTo) {

        ItemStack icon = ItemCustomIcon.getIcon(EnumCustomIcon.Button_Right);
        icon.setStackDisplayName(FancyText.getFormattedString("&eNext Page"));
        return GooeyButton.builder()
                .display(icon)
                .onClick(() -> open(pageToGoTo))
                .build();

    }

    private Button getPrevButton (int pageToGoTo) {

        ItemStack icon = ItemCustomIcon.getIcon(EnumCustomIcon.Button_Left);
        icon.setStackDisplayName(FancyText.getFormattedString("&ePrevious Page"));
        return GooeyButton.builder()
                .display(icon)
                .onClick(() -> open(pageToGoTo))
                .build();

    }

    private Button getPokemonSprite (String name, PokemonSpawnInfo info) {

        name = name.replace(".conf", "");
        EntityPixelmon pokemon;
        if (name.contains("-")) {

            if (name.equalsIgnoreCase("porygon-z")) {

                name = "porygon-z";
                pokemon = PokemonSpec.from(name).create(player.world);

            } else {

                String[] split = name.split("-");
                name = split[0];
                String form = "";
                for (int f = 1; f < split.length; f++) {

                    form = form + "-" + split[f];

                }

                pokemon = PokemonSpec.from(name).create(player.world);
                int pokemonForm = FormIndexFromName.getFormNumberFromFormName(name, form);
                pokemon.setForm(pokemonForm, true);

            }

        } else {

            pokemon = PokemonSpec.from(name).create(player.world);

        }

        ItemStack sprite = ItemPixelmonSprite.getPhoto(pokemon);
        double spawnChance = info.getSpawnChance();
        String percent;
        if (spawnChance >= 1.0) {

            percent = "100%";

        } else {

            DecimalFormat df = new DecimalFormat("#.##");
            percent = df.format(spawnChance * 100) + "%";

        }
        String spawnLocation = info.getSpawnLocation();
        sprite.setStackDisplayName(FancyText.getFormattedString("&e" + pokemon.getPokemonName()));
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString(FancyText.getFormattedString("&eSpawn Chance:&a " + percent)));
        lore.appendTag(new NBTTagString(FancyText.getFormattedString("&eSpawn Location:&a " + spawnLocation)));
        sprite.getOrCreateSubCompound("display").setTag("Lore", lore);
        return GooeyButton.builder().display(sprite).build();

    }

    private Button getPokemonSprite (String name, LegendarySpawnInfo info) {

        name = name.replace(".conf", "");
        EntityPixelmon pokemon;
        if (name.contains("-")) {

            if (name.equalsIgnoreCase("porygon-z")) {

                name = "porygon-z";
                pokemon = PokemonSpec.from(name).create(player.world);

            } else {

                String[] split = name.split("-");
                name = split[0];
                String form = "";
                for (int f = 1; f < split.length; f++) {

                    form = form + "-" + split[f];

                }

                pokemon = PokemonSpec.from(name).create(player.world);
                int pokemonForm = FormIndexFromName.getFormNumberFromFormName(name, form);
                pokemon.setForm(pokemonForm, true);

            }

        } else {

            pokemon = PokemonSpec.from(name).create(player.world);

        }

        ItemStack sprite = ItemPixelmonSprite.getPhoto(pokemon);
        double spawnChance = info.getSpawnChance();
        String spawnLocation = info.getSpawnLocation();
        sprite.setStackDisplayName(FancyText.getFormattedString("&e" + pokemon.getPokemonName()));
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString(FancyText.getFormattedString("&eSpawn Chance:&a " + spawnChance)));
        lore.appendTag(new NBTTagString(FancyText.getFormattedString("&eSpawn Location:&a " + spawnLocation)));
        sprite.getOrCreateSubCompound("display").setTag("Lore", lore);
        return GooeyButton.builder().display(sprite).build();

    }

}
