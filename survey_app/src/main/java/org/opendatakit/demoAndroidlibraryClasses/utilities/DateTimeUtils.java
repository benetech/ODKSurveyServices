package org.opendatakit.demoAndroidlibraryClasses.utilities;

import android.annotation.SuppressLint;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTimeUtils {
    private static final String t = "DateTimeUtils";
    private static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
    private static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    private static final String PATTERN_DATE_TOSTRING = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final String PATTERN_ISO8601_JAVAROSA = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
    private static final String PATTERN_DATE_ONLY_JAVAROSA = "yyyy-MM-dd";
    private static final String PATTERN_TIME_ONLY_JAVAROSA = "HH:mm:ss.SSS\'Z\'";
    private static final String PATTERN_ISO8601 = "yyyy-MM-dd\'T\'HH:mm:ss.SSSZ";
    private static final String PATTERN_ISO8601_WITHOUT_ZONE = "yyyy-MM-dd\'T\'HH:mm:ss.SSS";
    private static final String PATTERN_ISO8601_DATE = "yyyy-MM-ddZ";
    private static final String PATTERN_ISO8601_TIME = "HH:mm:ss.SSSZ";
    private static final String PATTERN_YYYY_MM_DD_DATE_ONLY_NO_TIME_DASH = "yyyy-MM-dd";
    private static final String PATTERN_NO_DATE_TIME_ONLY = "HH:mm:ss.SSS";
    private static final String PATTERN_GOOGLE_DOCS = "MM/dd/yyyy HH:mm:ss.SSS";
    private static final String PATTERN_GOOGLE_DOCS_DATE_ONLY = "MM/dd/yyyy";
    private static final GregorianCalendar g = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    private static DateTimeUtils dateTimeUtils = new DateTimeUtils();

    public static DateTimeUtils get() {
        return dateTimeUtils;
    }

    public static void set(DateTimeUtils utils) {
        dateTimeUtils = utils;
    }

    protected DateTimeUtils() {
    }

    @SuppressLint({"SimpleDateFormat"})
    private Date parseDateSubset(String value, String[] parsePatterns, Locale l, TimeZone tz) {
        Date d = null;
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);

        for(int i = 0; i < parsePatterns.length; ++i) {
            if(i == 0) {
                if(l == null) {
                    parser = new SimpleDateFormat(parsePatterns[0]);
                } else {
                    parser = new SimpleDateFormat(parsePatterns[0], l);
                }
            } else {
                parser.applyPattern(parsePatterns[i]);
            }

            parser.setTimeZone(tz);
            pos.setIndex(0);
            d = parser.parse(value, pos);
            if(d != null && pos.getIndex() == value.length()) {
                return d;
            }
        }

        return d;
    }

    public Date parseDate(String value) {
        if(value != null && value.length() != 0) {
            String[] javaRosaPattern = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'", "yyyy-MM-dd", "HH:mm:ss.SSS\'Z\'"};
            String[] iso8601Pattern = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSSZ"};
            String[] localizedParsePatterns = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss zzz yyyy"};
            String[] localizedNoTzParsePatterns = new String[]{"EEE MMM d HH:mm:ss yyyy"};
            String[] tzParsePatterns = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSSZ", "yyyy-MM-ddZ", "HH:mm:ss.SSSZ"};
            String[] noTzParsePatterns = new String[]{"yyyy-MM-dd\'T\'HH:mm:ss.SSS", "HH:mm:ss.SSS", "yyyy-MM-dd", "MM/dd/yyyy HH:mm:ss.SSS"};
            Date d = null;
            d = this.parseDateSubset(value, iso8601Pattern, (Locale)null, TimeZone.getTimeZone("GMT"));
            if(d != null) {
                return d;
            } else {
                d = this.parseDateSubset(value, javaRosaPattern, (Locale)null, TimeZone.getTimeZone("GMT"));
                if(d != null) {
                    return d;
                } else {
                    d = this.parseDateSubset(value, localizedParsePatterns, Locale.ENGLISH, TimeZone.getTimeZone("GMT"));
                    if(d != null) {
                        return d;
                    } else {
                        d = this.parseDateSubset(value, localizedParsePatterns, (Locale)null, TimeZone.getTimeZone("GMT"));
                        if(d != null) {
                            return d;
                        } else {
                            d = this.parseDateSubset(value, localizedNoTzParsePatterns, Locale.ENGLISH, TimeZone.getTimeZone("GMT"));
                            if(d != null) {
                                return d;
                            } else {
                                d = this.parseDateSubset(value, localizedNoTzParsePatterns, (Locale)null, TimeZone.getTimeZone("GMT"));
                                if(d != null) {
                                    return d;
                                } else {
                                    d = this.parseDateSubset(value, tzParsePatterns, (Locale)null, TimeZone.getTimeZone("GMT"));
                                    if(d != null) {
                                        return d;
                                    } else {
                                        d = this.parseDateSubset(value, noTzParsePatterns, (Locale)null, TimeZone.getTimeZone("GMT"));
                                        if(d != null) {
                                            return d;
                                        } else {
                                            throw new IllegalArgumentException("Unable to parse the date: " + value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return null;
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String asSubmissionDateTimeString(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asJavarosaDateTime = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");
            asJavarosaDateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asJavarosaDateTime.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String asSubmissionDateOnlyString(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asJavarosaDate = new SimpleDateFormat("yyyy-MM-dd");
            asJavarosaDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asJavarosaDate.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String asSubmissionTimeOnlyString(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asJavarosaTime = new SimpleDateFormat("HH:mm:ss.SSS\'Z\'");
            asJavarosaTime.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asJavarosaTime.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String googleDocsDateTime(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asGoogleDoc = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
            asGoogleDoc.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asGoogleDoc.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String googleDocsDateOnly(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asGoogleDocDateOnly = new SimpleDateFormat("MM/dd/yyyy");
            asGoogleDocDateOnly.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asGoogleDocDateOnly.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String iso8601Date(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asGMTiso8601 = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSSZ");
            asGMTiso8601.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asGMTiso8601.format(d);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public String rfc1123Date(Date d) {
        if(d == null) {
            return null;
        } else {
            SimpleDateFormat asGMTrfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
            asGMTrfc1123.setTimeZone(TimeZone.getTimeZone("GMT"));
            return asGMTrfc1123.format(d);
        }
    }

    static {
        StaticStateManipulator.get().register(50, new StaticStateManipulator.IStaticFieldManipulator() {
            public void reset() {
                DateTimeUtils.dateTimeUtils = new DateTimeUtils();
            }
        });
    }
}
