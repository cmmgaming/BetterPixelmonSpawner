package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Utils.FancyText;
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

    }

    @Override
    public String getName() {

        return "betterpixelmonspawner";

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {

        List<String> cmds = new ArrayList<>();
        if (args.length <= 1) {

            cmds.add("reload");
            cmds.add("opt");

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
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

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

        }

    }

}
