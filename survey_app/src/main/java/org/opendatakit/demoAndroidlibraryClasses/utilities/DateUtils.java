package org.opendatakit.demoAndroidlibraryClasses.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.utilities.UTMConverter;

public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();
    private static final String[] USER_FULL_DATETIME_PATTERNS = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSSZ", "M/d/yy h:mm:ssa", "M/d/yy HH:mm:ss", "M/d/yyyy h:mm:ssa", "M/d/yyyy HH:mm:ss", "M/d h:mm:ssa", "M/d HH:mm:ss", "d h:mm:ssa", "d HH:mm:ss", "E h:mm:ssa", "E HH:mm:ss", "HH:mm:ss.SSSZ"};
    private static final String[][] USER_PARTIAL_DATETIME_PATTERNS = new String[][]{{"M/d/yy h:mma", "M/d/yy HH:mm", "M/d/yyyy h:mma", "M/d/yyyy HH:mm", "M/d h:mma", "M/d HH:mm", "d h:mma", "d HH:mm", "E h:mma", "E HH:mm"}, {"M/d/yy ha", "M/d/yy HH", "M/d/yyyy ha", "M/d/yyyy HH", "M/d ha", "M/d HH", "d ha", "d HH", "E ha", "E HH"}, {"yyyy-MM-dd", "M/d/yy", "M/d/yyyy", "M/d", "d", "E"}};
    private static final int[] USER_INTERVAL_DURATIONS = new int[]{60, 3600, 86400};
    private static final Pattern USER_DURATION_FORMAT = Pattern.compile("(\\d+)(s|m|h|d)");
    private static final Pattern USER_NOW_RELATIVE_FORMAT = Pattern.compile("^now\\s*(-|\\+)\\s*(\\d+(s|m|h|d))$");
    private static final Pattern USER_LOCATION_LAT_LON_FORMAT = Pattern.compile("^\\s*\\(?(-?(\\d+|\\.\\d+|\\d+\\.\\d+)),\\s*(-?(\\d+|\\.\\d+|\\d+\\.\\d+))\\)?\\s*$");
    private static final Pattern USER_LOCATION_LAT_LON_ALT_ACC_FORMAT = Pattern.compile("^\\s*\\(?(-?(\\d+|\\.\\d+|\\d+\\.\\d+))\\s+(-?(\\d+|\\.\\d+|\\d+\\.\\d+))(\\s+(-?(\\d+|\\.\\d+|\\d+\\.\\d+))\\s+(-?(\\d+|\\.\\d+|\\d+\\.\\d+)))?\\)?\\s*$");
    private static final Pattern USER_LOCATION_UTM_COMMA_FORMAT = Pattern.compile("(-?(\\d+|\\.\\d+|\\d+\\.\\d+)),\\s*(-?(\\d+|\\.\\d+|\\d+\\.\\d+)),\\s*(\\d+),\\s*(n|N|s|S)");
    private static final Pattern USER_LOCATION_UTM_SPACE_FORMAT = Pattern.compile("(\\d+)(n|N|s|S)\\s+(-?(\\d+|\\.\\d+|\\d+\\.\\d+))\\s+(-?(\\d+|\\.\\d+|\\d+\\.\\d+))");
    private static final String USER_SHORT_FORMAT = "M/d h:mma";
    private static final String USER_LONG_FORMAT = "M/d/yyyy h:mm:ssa";
    private final Locale locale;
    private final DateTimeFormatter userFullParser;
    private final DateTimeFormatter[] userPartialParsers;
    private final DateTimeFormatter userShortFormatter;
    private final DateTimeFormatter userLongFormatter;

    public DateUtils(Locale locale, TimeZone tz) {
        this.locale = locale;
        DateTimeZone timeZone = DateTimeZone.forTimeZone(tz);
        DateTimeFormatterBuilder fpBuilder = new DateTimeFormatterBuilder();
        String[] i = USER_FULL_DATETIME_PATTERNS;
        int dtfb = i.length;

        for(int arr$ = 0; arr$ < dtfb; ++arr$) {
            String len$ = i[arr$];
            DateTimeFormatter i$ = DateTimeFormat.forPattern(len$);
            fpBuilder.appendOptional(i$.getParser());
        }

        this.userFullParser = fpBuilder.toFormatter().withLocale(locale).withZone(timeZone);
        this.userPartialParsers = new DateTimeFormatter[USER_PARTIAL_DATETIME_PATTERNS.length];

        for(int var12 = 0; var12 < USER_PARTIAL_DATETIME_PATTERNS.length; ++var12) {
            DateTimeFormatterBuilder var13 = new DateTimeFormatterBuilder();
            String[] var14 = USER_PARTIAL_DATETIME_PATTERNS[var12];
            int var15 = var14.length;

            for(int var16 = 0; var16 < var15; ++var16) {
                String pattern = var14[var16];
                DateTimeFormatter f = DateTimeFormat.forPattern(pattern);
                var13.appendOptional(f.getParser());
            }

            this.userPartialParsers[var12] = var13.toFormatter().withLocale(locale).withZone(timeZone);
        }

        this.userShortFormatter = DateTimeFormat.forPattern("M/d h:mma");
        this.userLongFormatter = DateTimeFormat.forPattern("M/d/yyyy h:mm:ssa");
    }

    public String validifyDateValue(String input) {
        DateTime instant = this.tryParseInstant(input);
        if(instant != null) {
            return this.formatDateTimeForDb(instant);
        } else {
            Interval interval = this.tryParseInterval(input);
            return interval != null?this.formatDateTimeForDb(interval.getStart()):null;
        }
    }

    public String validifyDateTimeValue(String input) {
        DateTime instant = this.tryParseInstant(input);
        if(instant != null) {
            return this.formatDateTimeForDb(instant);
        } else {
            Interval interval = this.tryParseInterval(input);
            return interval != null?this.formatDateTimeForDb(interval.getStart()):null;
        }
    }

    public String validifyTimeValue(String input) {
        DateTime instant = this.tryParseInstant(input);
        if(instant != null) {
            return this.formatDateTimeForDb(instant);
        } else {
            Interval interval = this.tryParseInterval(input);
            return interval != null?this.formatDateTimeForDb(interval.getStart()):null;
        }
    }

    public String validifyDateRangeValue(String input) {
        Interval interval = this.tryParseInterval(input);
        return interval != null?this.formatIntervalForDb(interval):null;
    }

    public String validifyNumberValue(String input) {
        if(input != null && input.length() != 0) {
            try {
                Double.parseDouble(input);
                return input;
            } catch (NumberFormatException var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    public String validifyIntegerValue(String input) {
        if(input != null && input.length() != 0) {
            try {
                Integer.parseInt(input);
                return input;
            } catch (NumberFormatException var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    private String validifyMultipleChoiceValue(ArrayList<String> choices, String input) {
        Iterator i$ = choices.iterator();

        String opt;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            opt = (String)i$.next();
        } while(!opt.equalsIgnoreCase(input));

        return opt;
    }

    private String validifyLocationValue(String input) {
        if(input == null) {
            return null;
        } else {
            Matcher matcher = USER_LOCATION_LAT_LON_FORMAT.matcher(input);
            if(matcher.matches()) {
                return matcher.group(1) + "," + matcher.group(3);
            } else {
                matcher = USER_LOCATION_LAT_LON_ALT_ACC_FORMAT.matcher(input);
                if(matcher.matches()) {
                    return matcher.group(1) + "," + matcher.group(3);
                } else {
                    matcher = USER_LOCATION_UTM_COMMA_FORMAT.matcher(input);
                    double x;
                    double y;
                    int zone;
                    String hemi;
                    boolean isSouthHemi;
                    double[] latLon;
                    String latStr;
                    String lonStr;
                    if(matcher.matches()) {
                        x = Double.parseDouble(matcher.group(1));
                        y = Double.parseDouble(matcher.group(3));
                        zone = Integer.parseInt(matcher.group(5));
                        hemi = matcher.group(6);
                        isSouthHemi = hemi.equalsIgnoreCase("s");
                        latLon = UTMConverter.parseUTM(x, y, zone, isSouthHemi);
                        if(latLon == null) {
                            return null;
                        } else {
                            latStr = String.format(Locale.US, "%.5g", new Object[]{Double.valueOf(latLon[0])});
                            lonStr = String.format(Locale.US, "%.5g", new Object[]{Double.valueOf(latLon[1])});
                            return latStr + "," + lonStr;
                        }
                    } else {
                        matcher = USER_LOCATION_UTM_SPACE_FORMAT.matcher(input);
                        if(matcher.matches()) {
                            x = Double.parseDouble(matcher.group(3));
                            y = Double.parseDouble(matcher.group(5));
                            zone = Integer.parseInt(matcher.group(1));
                            hemi = matcher.group(2);
                            isSouthHemi = hemi.equalsIgnoreCase("s");
                            latLon = UTMConverter.parseUTM(x, y, zone, isSouthHemi);
                            if(latLon == null) {
                                return null;
                            } else {
                                latStr = String.format(Locale.US, "%.5g", new Object[]{Double.valueOf(latLon[0])});
                                lonStr = String.format(Locale.US, "%.5g", new Object[]{Double.valueOf(latLon[1])});
                                return latStr + "," + lonStr;
                            }
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    public DateTime tryParseInstant(String input) {
        input = input.trim();
        if(input.equalsIgnoreCase("now")) {
            return new DateTime();
        } else {
            Matcher matcher = USER_NOW_RELATIVE_FORMAT.matcher(input);
            if(matcher.matches()) {
                int e = this.tryParseDuration(matcher.group(2));
                return e < 0?null:(matcher.group(1).equals("-")?(new DateTime()).minusSeconds(e):(new DateTime()).plusSeconds(e));
            } else {
                try {
                    return this.userFullParser.parseDateTime(input);
                } catch (IllegalArgumentException var4) {
                    return null;
                }
            }
        }
    }

    public Interval tryParseInterval(String input) {
        int start = 0;

        DateTime end;
        while(start < this.userPartialParsers.length) {
            try {
                DateTime match = this.userPartialParsers[start].parseDateTime(input);
                end = match.plusSeconds(USER_INTERVAL_DURATIONS[start]);
                return new Interval(match, end);
            } catch (IllegalArgumentException var5) {
                ++start;
            }
        }

        if(!this.locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return null;
        } else {
            DateTime var6 = (new DateTime()).withTimeAtStartOfDay();
            boolean var7 = false;
            if(input.equalsIgnoreCase("today")) {
                var7 = true;
            } else if(input.equalsIgnoreCase("yesterday")) {
                var6 = var6.minusDays(1);
                var7 = true;
            } else if(input.equalsIgnoreCase("tomorrow") || input.equalsIgnoreCase("tmw")) {
                var6 = var6.plusDays(1);
                var7 = true;
            }

            if(var7) {
                end = var6.plusDays(1);
                return new Interval(var6, end);
            } else {
                return null;
            }
        }
    }

    public int tryParseDuration(String input) {
        Matcher matcher = USER_DURATION_FORMAT.matcher(input);
        if(!matcher.matches()) {
            return -1;
        } else {
            int quant = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            switch(unit) {
                case 'd':
                    return quant * 86400;
                case 'h':
                    return quant * 3600;
                case 'm':
                    return quant * 60;
                case 's':
                    return quant;
                default:
                    return -1;
            }
        }
    }

    public String formatDateTimeForDb(DateTime dt) {
        return TableConstants.nanoSecondsFromMillis(Long.valueOf(dt.getMillis()));
    }

    public String formatIntervalForDb(Interval interval) {
        return this.formatDateTimeForDb(interval.getStart()) + "/" + this.formatDateTimeForDb(interval.getEnd());
    }

    public String formatNowForDb() {
        return this.formatDateTimeForDb(new DateTime());
    }

    public DateTime parseDateTimeFromDb(String dbString) {
        DateTime t = new DateTime(TableConstants.milliSecondsFromNanos(dbString), DateTimeZone.UTC);
        return t;
    }

    public Interval parseIntervalFromDb(String dbString) {
        String[] split = dbString.split("/");
        return new Interval(this.parseDateTimeFromDb(split[0]), this.parseDateTimeFromDb(split[1]));
    }

    public String formatShortDateTimeForUser(DateTime dt) {
        return this.userShortFormatter.print(dt);
    }

    public String formatLongDateTimeForUser(DateTime dt) {
        return this.userLongFormatter.print(dt);
    }

    public String formatShortIntervalForUser(Interval interval) {
        return this.formatShortDateTimeForUser(interval.getStart()) + "-" + this.formatShortDateTimeForUser(interval.getEnd());
    }

    public String formatLongIntervalForUser(Interval interval) {
        return this.formatLongDateTimeForUser(interval.getStart()) + " - " + this.formatLongDateTimeForUser(interval.getEnd());
    }

    public double[] parseLocationFromDb(String dbString) {
        String[] split = dbString.split(",");
        return new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
    }
}
