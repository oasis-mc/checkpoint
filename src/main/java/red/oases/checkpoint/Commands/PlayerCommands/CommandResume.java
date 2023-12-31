package red.oases.checkpoint.Commands.PlayerCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.oases.checkpoint.Commands.Command;
import red.oases.checkpoint.Extra.Annotations.DisableConsole;
import red.oases.checkpoint.Extra.Annotations.PermissionLevel;
import red.oases.checkpoint.Objects.Config;
import red.oases.checkpoint.Objects.Logic;
import red.oases.checkpoint.Utils.LogUtils;
@DisableConsole
@PermissionLevel(0)
public class CommandResume extends Command {
    public CommandResume(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {

        if (!Config.getAllowResume()) {
            LogUtils.send("此指令已被禁用。", sender);
            return true;
        }

        Logic.handleAutoResume((Player) sender, true);

        return true;
    }
}
