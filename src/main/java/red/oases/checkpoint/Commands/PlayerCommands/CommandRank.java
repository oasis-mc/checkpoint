package red.oases.checkpoint.Commands.PlayerCommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import red.oases.checkpoint.Commands.Command;
import red.oases.checkpoint.Extra.Annotations.PermissionLevel;
import red.oases.checkpoint.Objects.Analytics;
import red.oases.checkpoint.Objects.Campaign;
import red.oases.checkpoint.Objects.DisplayList;
import red.oases.checkpoint.Utils.CommonUtils;
import red.oases.checkpoint.Utils.FileUtils;
import red.oases.checkpoint.Utils.LogUtils;

import java.util.Comparator;

@PermissionLevel(0)
public class CommandRank extends Command {
    public CommandRank(String[] args, CommandSender sender) {
        super(args, sender);
    }

    protected boolean execute() {
        if (args.length < 2) {
            LogUtils.send("参数不足: /cpt rank <campaign> [page]", sender);
            return true;
        }

        var campaign = args[1];
        var page = 1;
        var section = FileUtils.campaigns.getConfigurationSection(campaign);

        if (args.length == 3) {
            page = CommonUtils.mustPositive(args[2]);
            if (page == 0) {
                LogUtils.send("页码无效。", sender);
                return true;
            }
        }

        if (section == null) {
            LogUtils.send(String.format("找不到竞赛 %s", campaign), sender);
            return true;
        }

        var targetCampaign = new Campaign(campaign);

        var analytics = targetCampaign.getAnalytics()
                .stream()
                .sorted(Comparator.comparing(Analytics::getTimeTotal))
                .toList();
        var list = new DisplayList(
                10,
                analytics.size(),
                sender,
                DisplayList.getTitle(targetCampaign.getName(), "的排名信息")
        );

        list.sendPage(page, i -> {
            var targetAnalytics = analytics.get(i);
            return LogUtils.getListItemColored(i + 1,
                    Component.text(targetAnalytics.getPlayerName()).color(NamedTextColor.WHITE)
                            .appendSpace().append(Component.text("@").color(NamedTextColor.LIGHT_PURPLE))
                            .appendSpace().append(Component.text(CommonUtils.millisecondsToReadable(targetAnalytics.getTimeTotal())).color(NamedTextColor.YELLOW))
                            .appendSpace().append(Component.text("完成于 " + CommonUtils.formatDate(targetAnalytics.getFinishedAt())).decorate(TextDecoration.ITALIC).color(NamedTextColor.GRAY))
                            .appendNewline()
            );
        });

        return true;
    }
}
