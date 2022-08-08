package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.Utils.FancyText;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PixelmonSpawnerCommand extends CommandTreeBase {

    public PixelmonSpawnerCommand() {

        addSubcommand(new OptCommand());
        addSubcommand(new ReloadCommand());
        addSubcommand(new ListCommand());
        addSubcommand(new WhenCommand());
        addSubcommand(new ForceClearCommand());
        addSubcommand(new DoLegendarySpawnCommand());
        addSubcommand(new DebugCommand());
        addSubcommand(new CheckCountCommand());
        addSubcommand(new WhereCommand());
        addSubcommand(new LocationCommand());

    }

    @Override
    public String getName() {

        return "betterpixelmonspawner";

    }

    @Override
    public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {

        List<String> cmds = new ArrayList<>();
        if (args.length <= 1) {

            cmds.add("reload");
            cmds.add("opt");
            cmds.add("when");
            cmds.add("list");
            cmds.add("ll");
            cmds.add("clear");
            cmds.add("dolegendaryspawn");
            cmds.add("debug");
            cmds.add("count");
            cmds.add("where");
            cmds.add("loc");

        } else {

            String arg = args[0];
            if (arg.equalsIgnoreCase("opt")) {

                if (args.length <= 2) {

                    List<String> argList = new ArrayList<>();
                    argList.add("in");
                    argList.add("out");
                    return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, argList);

                } else {

                    List<String> modules = new ArrayList<>();
                    modules.add("pokemon");
                    modules.add("npc");
                    modules.add("legendary");
                    modules.add("misc");
                    return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, modules);

                }

            } else if (arg.equalsIgnoreCase("debug")) {

                if (args.length <= 2) {

                    return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());

                } else {

                    List<String> modules = new ArrayList<>();
                    modules.add("pokemon");
                    return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, modules);

                }

            } else if (arg.equalsIgnoreCase("checkcount") || arg.equalsIgnoreCase("count")) {

                List<String> modules = new ArrayList<>();
                modules.add("pokemon");
                return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, modules);

            } else if (arg.equalsIgnoreCase("reload")) {

                List<String> modules = new ArrayList<>();
                modules.add("all");
                modules.add("tasks");
                modules.add("deadzones");
                modules.add("holidays");
                modules.add("pokemon");
                modules.add("settings");
                return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, modules);

            } else if (arg.equalsIgnoreCase("where")) {

                return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, PokemonConfig.fileNames);

            } else if (arg.equalsIgnoreCase("loc") || arg.equalsIgnoreCase("location")) {

                List<String> modules = new ArrayList<>();
                modules.add("air");
                modules.add("water");
                modules.add("land");
                modules.add("underground");
                modules.add("clear");
                return PixelmonSpawnerCommand.getListOfStringsMatchingLastWord(args, modules);

            }

        }

        return cmds;

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("bpspawner");
        a.add("bps");
        return a;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bpspawner <args>";

    }

    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {

        return true;

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length < 1) {

            sender.sendMessage(FancyText.getFancyText(getUsage(sender)));
            return;

        }

        String arg = args[0];
        switch (arg.toLowerCase()) {

            case "opt":
                OptCommand optCommand = new OptCommand();
                optCommand.execute(server, sender, args);
                break;

            case "reload":
                ReloadCommand reloadCommand = new ReloadCommand();
                reloadCommand.execute(server, sender, args);
                break;

            case "list":
            case "check":
                ListCommand listCommand = new ListCommand();
                listCommand.execute(server, sender, args);
                break;

            case "when":
                WhenCommand whenCommand = new WhenCommand();
                whenCommand.execute(server, sender, args);
                break;

            case "lastlegend":
            case "ll":
                LastLegendCommand llc = new LastLegendCommand();
                llc.execute(server, sender, args);
                break;

            case "clear":
                ForceClearCommand fcc = new ForceClearCommand();
                fcc.execute(server, sender, args);
                break;

            case "dolegendaryspawn":
                DoLegendarySpawnCommand dlspc = new DoLegendarySpawnCommand();
                dlspc.execute(server, sender, args);
                break;

            case "debug":
                DebugCommand debugCommand = new DebugCommand();
                debugCommand.execute(server, sender, args);
                break;

            case "checkcount":
            case "count":
                CheckCountCommand ccc = new CheckCountCommand();
                ccc.execute(server, sender, args);
                break;

            case "where":
                WhereCommand whereCommand = new WhereCommand();
                whereCommand.execute(server, sender, args);
                break;

            case "loc":
            case "location":
                LocationCommand locationCommand = new LocationCommand();
                locationCommand.execute(server, sender, args);
                break;

        }

    }

}
