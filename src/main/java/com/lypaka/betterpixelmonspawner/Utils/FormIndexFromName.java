package com.lypaka.betterpixelmonspawner.Utils;

import com.pixelmongenerations.core.enums.forms.*;

public class FormIndexFromName {

    public static int getFormNumberFromFormName (String pokemon, String form) {

        int formNum = 0;
        if (form.equalsIgnoreCase("-alolan")) {

            formNum = 3;

        } else if (form.equalsIgnoreCase("-galarian")) {

            formNum = 10;

        } else if (form.equalsIgnoreCase("-hisuian")) {

            formNum = 11;

        } else {

            if (pokemon.equalsIgnoreCase("Aegislash")) {

                for (EnumAegislash f : EnumAegislash.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("alcremie")) {

                for (EnumAlcremie f : EnumAlcremie.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("arceus")) {

                for (EnumArceus f : EnumArceus.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("basculegion")) {

                for (EnumBasculegion f : EnumBasculegion.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("basculin")) {

                for (EnumBasculin f : EnumBasculin.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("burmy")) {

                for (EnumBurmy f : EnumBurmy.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("calyrex")) {

                for (EnumCalyrex f : EnumCalyrex.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("castform")) {

                for (EnumCastform f : EnumCastform.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("cherrim")) {

                for (EnumCherrim f : EnumCherrim.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("darmanitan")) {

                for (EnumDarmanitan f : EnumDarmanitan.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("deoxys")) {

                for (EnumDeoxys f : EnumDeoxys.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("dialga")) {

                for (EnumDialga f : EnumDialga.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("eiscue")) {

                for (EnumEiscue f : EnumEiscue.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("eternatus")) {

                for (EnumEternatus f : EnumEternatus.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("flabebe")) {

                for (EnumFlabebe f : EnumFlabebe.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("furfrou")) {

                for (EnumFurfrou f : EnumFurfrou.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("gastrodon")) {

                for (EnumGastrodon f : EnumGastrodon.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("genesect")) {

                for (EnumGenesect f : EnumGenesect.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("giratina")) {

                for (EnumGiratina f : EnumGiratina.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("greninja")) {

                for (EnumGreninja f : EnumGreninja.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("groudon")) {

                for (EnumGroudon f : EnumGroudon.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("hoopa")) {

                for (EnumHoopa f : EnumHoopa.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("indeedee")) {

                for (EnumIndeedee f : EnumIndeedee.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("keldeo")) {

                for (EnumKeldeo f : EnumKeldeo.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("kyogre")) {

                for (EnumKyogre f : EnumKyogre.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("kyurem")) {

                for (EnumKyurem f : EnumKyurem.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("landorus")) {

                for (EnumLandorus f : EnumLandorus.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("lycanroc")) {

                for (EnumLycanroc f : EnumLycanroc.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("mareep")) {

                for (EnumMareep f : EnumMareep.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("meloetta")) {

                for (EnumMeloetta f : EnumMeloetta.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("meowstic")) {

                for (EnumMeowstic f : EnumMeowstic.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("minior")) {

                for (EnumMinior f : EnumMinior.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("missingno")) {

                for (EnumMissingNo f : EnumMissingNo.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("morpeko")) {

                for (EnumMorpeko f : EnumMorpeko.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("necrozma")) {

                for (EnumNecrozma f : EnumNecrozma.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("oricorio")) {

                for (EnumOricorio f : EnumOricorio.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("pikachu")) {

                for (EnumPikachu f : EnumPikachu.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("polteageist")) {

                for (EnumPolteageist f : EnumPolteageist.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("rotom")) {

                for (EnumRotom f : EnumRotom.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("deerling") || pokemon.equalsIgnoreCase("sawsbuck")) {

                for (EnumSeason season : EnumSeason.values()) {

                    if (season.getFormSuffix().contains(form)) {

                        formNum = season.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("shaymin")) {

                for (EnumShaymin f : EnumShaymin.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("shellos")) {

                for (EnumShellos f : EnumShellos.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("silvally")) {

                for (EnumSilvally f : EnumSilvally.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("sinistea")) {

                for (EnumSinistea f : EnumSinistea.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("thundurus")) {

                for (EnumThundurus f : EnumThundurus.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("tornadus")) {

                for (EnumTornadus f : EnumTornadus.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("toxtricity")) {

                for (EnumToxtricity f : EnumToxtricity.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("unown")) {

                for (EnumUnown f : EnumUnown.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("urshifu")) {

                for (EnumUrshifu f : EnumUrshifu.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("vivillon")) {

                for (EnumVivillon f : EnumVivillon.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("wishiwashi")) {

                for (EnumWishiwashi f : EnumWishiwashi.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("wooloo")) {

                for (EnumWooloo f : EnumWooloo.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("wormadam")) {

                for (EnumWormadam f : EnumWormadam.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("xerneas")) {

                for (EnumXerneas f : EnumXerneas.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("zacian")) {

                for (EnumZacian f : EnumZacian.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("zamazenta")) {

                for (EnumZamazenta f : EnumZamazenta.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else if (pokemon.equalsIgnoreCase("zygarde")) {

                for (EnumZygarde f : EnumZygarde.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            } else {

                for (EnumForms f : EnumForms.values()) {

                    if (f.getFormSuffix().contains(form)) {

                        formNum = f.getForm();
                        break;

                    }

                }

            }

        }

        return formNum;

    }

}
