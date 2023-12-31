package red.oases.checkpoint.Commands.OpCommands;

import org.bukkit.command.CommandSender;
import red.oases.checkpoint.Commands.Command;
import red.oases.checkpoint.Utils.FileUtils;
import red.oases.checkpoint.Utils.LogUtils;
import red.oases.checkpoint.Utils.CommonUtils;

import java.util.HashSet;

public class CommandMove extends Command {
    public CommandMove(String[] args, CommandSender sender) {
        super(args, sender);
    }

    protected boolean execute() {
        if (args.length < 4) {
            LogUtils.send("参数不足：/cpt move <from-track> <to-track> <n1,n2,n3,...>", sender);
            return true;
        }

        var tracks = CommonUtils.getTrackNames();
        var fromTrack = args[1];
        var toTrack = args[2];
        var numbers = args[3].split(",");

        if (!tracks.contains(fromTrack)) {
            LogUtils.send("赛道不存在：" + fromTrack + "。", sender);
            return true;
        }

        if (tracks.contains(toTrack)) {
            LogUtils.send("赛道 " + toTrack + " 不为空。", sender);
            return true;
        }

        var targetNumbers = new HashSet<Integer>();

        for (var n : numbers) {
            n = n.trim();
            if (n.contains("-")) {
                var numberRange = n.split("-");

                if (numberRange.length != 2) {
                    LogUtils.send("数字范围不合法：" + n, sender);
                    return true;
                }

                var numberRangeStart = CommonUtils.mustPositive(numberRange[0]);
                var numberRangeEnd = CommonUtils.mustPositive(numberRange[1]);

                if (numberRangeStart == 0 || numberRangeEnd == 0) {
                    LogUtils.send("数字范围不合法：" + n, sender);
                    return true;
                }

                for (var nn = numberRangeStart; nn <= numberRangeEnd; nn++) {
                    targetNumbers.add(nn);
                }
            } else {
                var nn = CommonUtils.mustPositive(n);
                if (nn == 0) {
                    LogUtils.send("序号不合法：" + n, sender);
                    return true;
                }
                targetNumbers.add(nn);
            }
        }

        var section = FileUtils.tracks.getConfigurationSection("data");
        assert section != null;
        assert section.getConfigurationSection(fromTrack) != null;
        assert section.getConfigurationSection(toTrack) == null;
        var index = 0;

        for (var number : targetNumbers) {
            index++;
            var tg = section.getConfigurationSection(fromTrack + "." + number);
            section.set(toTrack + "." + index, tg);
            section.set(fromTrack + "." + number, null);
        }

        FileUtils.saveSelections();

        LogUtils.send("成功移动 " + index + " 个检查点。", sender);
        return true;
    }
}
