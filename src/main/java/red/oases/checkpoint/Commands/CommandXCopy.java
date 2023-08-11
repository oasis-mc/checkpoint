package red.oases.checkpoint.Commands;

import org.bukkit.command.CommandSender;
import red.oases.checkpoint.Files;
import red.oases.checkpoint.LogUtil;

import java.util.Date;

public class CommandXCopy extends Command {

    public CommandXCopy(String[] args, CommandSender sender) {
        super(args, sender);
    }

    public boolean collect() {
        if (args.length < 3) {
            LogUtil.send("参数不足：/cpt xcopy <T1.N1,T2.N2,...> <t1.n1,t2.n2,...> [force?]", sender);
            return true;
        }

        var from = args[1].split(",");
        var to = args[2].split(",");
        var force = false;

        if (from.length != to.length) {
            LogUtil.send("复制来源和去向数量不匹配。", sender);
            return true;
        }

        if (args.length >= 4) {
            if (args[3].equals("true")) {
                force = true;
            }

            if (!args[3].equals("true") && !args[3].equals("false")) {
                LogUtil.send("参数 force? 只能为 true 或者 false。", sender);
                return true;
            }
        }

        var section = Files.selections.getConfigurationSection("data");

        if (section == null) {
            LogUtil.send("目前还没有任何检查点。", sender);
            return true;
        }

        for (var i = 0; i < from.length; i++) {
            var f = from[i].trim();
            var t = to[i].trim();
            var fromSection = section.getConfigurationSection(f);
            if (fromSection == null) {
                LogUtil.send("已跳过复制 " + f + " 到 " + t + "。原因：" + f + " 对应数据不存在。", sender);
                continue;
            }
            var toSection = section.getConfigurationSection(t);
            if (toSection != null && !force) {
                LogUtil.send("已跳过复制 " + f + " 到 " + t + "。原因：没有指定覆盖参数为 true。", sender);
                continue;
            }

            if (toSection == null) LogUtil.send("成功复制 " + f + " 到 " + t + "。", sender);
            else LogUtil.send("成功将 " + f + " 覆盖到 " + t, sender);
            section.set(t, fromSection);
        }

        Files.saveSelections();
        return true;
    }
}