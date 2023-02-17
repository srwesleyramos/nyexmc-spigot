package tk.wesleyramos.mclib.formatter;

import java.text.DecimalFormat;

public class NumberFormatter {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");
    public static final String[] SUFFIXES = new String[]{
            "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG"
    };

    public static String format(double data) {
        if (data >= -999.99 && data <= 999.99) {
            return DECIMAL_FORMAT.format(data);
        }

        boolean negative = data < 0;
        double value = Math.abs(data);

        int zeros = (int) Math.log10(value);
        int div = zeros / 3;
        int arrayIndex = Math.min(div - 1, SUFFIXES.length - 1);

        return (negative ? "-" : "") + DECIMAL_FORMAT.format(value / Math.pow(1000.0, arrayIndex + 1.0)) + SUFFIXES[arrayIndex];
    }

    public static double parse(String data) {
        try {
            if (!data.endsWith("D")) {
                return Double.parseDouble(data.replace(".", "").replace(",", "."));
            }
        } catch (Exception ignored) {
        }

        for (int i = SUFFIXES.length - 1; i > -1; i--) {
            String suffix = SUFFIXES[i];

            if (!data.endsWith(suffix)) continue;

            double amount = Double.parseDouble(data.substring(0, data.length() - SUFFIXES[i].length())
                    .replace(".", "").replace(",", "."));

            return amount * Math.pow(10, i * 3 + 3);
        }

        return 0.0;
    }
}
