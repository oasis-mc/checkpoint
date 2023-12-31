package red.oases.checkpoint.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CommonUtils {
    public static Set<String> getCampaignNames() {
        FileUtils.reload();
        var section = FileUtils.campaigns;

        if (section == null) {
            return new HashSet<>();
        } else {
            return section.getKeys(false);
        }
    }

    public static Set<String> getTrackNames() {
        FileUtils.reload();
        var section = FileUtils.tracks.getConfigurationSection("data");

        if (section == null) {
            return new HashSet<>();
        } else {
            return section.getKeys(false);
        }
    }


    /**
     * 转换对应字符串为非负整数。如果转换失败，返回 0。
     *
     * @param target 待转换的字符串
     * @return 成功为对应整数，不成功为 0
     */
    public static int mustPositive(String target) {
        int result;
        try {
            result = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            return 0;
        }
        return result;
    }

    public static String formatTimestamp(long epoch) {
        return formatDate(new Date(epoch));
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 将长整型 ms 转换为两位小数 s
     * 例如 1200 返回值为 1.20
     *
     * @param ms 长整型 单位 ms
     * @return 两位小数字符串
     */
    public static String millisecondsToSeconds(long ms) {
        return String.format("%.2f", (double) ms / 1000);
    }

    /**
     * 将长整型 ms 转换为人类可读字符串
     * 例如 120010（120.01 秒）返回值为 2 分 0.01 秒
     *
     * @param ms 长整型 单位 ms
     * @return 人类可读
     */
    public static String millisecondsToReadable(long ms) {
        var target = Double.parseDouble(millisecondsToSeconds(ms)) / 60;
        var minutes = BigDecimal.valueOf(target).setScale(0, RoundingMode.DOWN).intValue();
        var seconds = String.format("%.2f", (target - (double) minutes) * 60);
        if (minutes > 0) {
            return String.format("%s 分 %s 秒", minutes, seconds);
        } else {
            return String.format("%s 秒", seconds);
        }
    }
}
