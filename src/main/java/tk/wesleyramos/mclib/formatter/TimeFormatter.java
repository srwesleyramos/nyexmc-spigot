package tk.wesleyramos.mclib.formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public static String format(long milliseconds) {
        StringBuilder builder = new StringBuilder();
        List<String> strings = new ArrayList<>();

        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);

        if (days > 0) {
            milliseconds -= TimeUnit.DAYS.toMillis(days);

            strings.add(days + " dia" + (days != 1 ? "s" : ""));
        }

        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);

        if (hours > 0) {
            milliseconds -= TimeUnit.HOURS.toMillis(hours);

            strings.add(hours + " hora" + (hours != 1 ? "s" : ""));
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        if (minutes > 0) {
            milliseconds -= TimeUnit.MINUTES.toMillis(minutes);

            strings.add(minutes + " minuto" + (minutes != 1 ? "s" : ""));
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

        if (seconds > 0) {
            strings.add(seconds + " segundo" + (seconds != 1 ? "s" : ""));
        }

        for (int i = 0; i < strings.size(); i++) {
            builder.append(i != 0 ? (i + 1 == strings.size() ? " e " : ", ") : "").append(strings.get(i));
        }

        return builder.toString();
    }

    public static long parse(String data) {
        long value = 0L, result = 0L;

        for (String s : data.split(" ")) {
            try {
                value = Long.parseLong(s);
            } catch (NumberFormatException ignored) {
                switch (s) {
                    case "dia,":
                    case "dia":
                    case "dias":
                    case "dias,":
                        result += TimeUnit.DAYS.toMillis(value);
                        break;
                    case "hora":
                    case "hora,":
                    case "horas":
                    case "horas,":
                        result += TimeUnit.HOURS.toMillis(value);
                        break;
                    case "minuto":
                    case "minuto,":
                    case "minutos":
                    case "minutos,":
                        result += TimeUnit.MINUTES.toMillis(value);
                        break;
                    case "segundo":
                    case "segundo,":
                    case "segundos":
                    case "segundos,":
                        result += TimeUnit.SECONDS.toMillis(value);
                        break;
                }
            }
        }

        return result;
    }
}
