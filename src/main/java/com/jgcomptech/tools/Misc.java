package com.jgcomptech.tools;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/** Contains methods to do misc tasks. */
public final class Misc {
    /**
     * Returns the conversion from bytes to the correct version (1024 bytes = 1 KB).
     * @param input Number to convert to a readable string
     * @return Specified number converted to a readable string
     */
    public static String ConvertBytes(final double input) {
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        final double factor = 1024d;
        double newNum = input;
        if(newNum >= factor) {
            newNum /= factor;
            if(newNum >= factor) {
                newNum /= factor;
                if(newNum >= factor) {
                    newNum /= factor;
                    if(newNum >= factor) {
                        newNum /= factor;
                        return df.format(newNum) + " TB";
                    }
                    return df.format(newNum) + " GB";
                }
                return df.format(newNum) + " MB";
            }
            return df.format(newNum) + " KB";
        }
        return df.format(newNum) + " Bytes";
    }

    /**
     * Contains methods to convert seconds into a readable format.
     * @since 1.3.0
     */
    public static final class SecondsConverter {
        /** The number of seconds in a minute. */
        public static final int Minute = 60;
        /** The number of seconds in a hour. */
        public static final int Hour = 3600;
        /** The number of seconds in a day. */
        public static final int Day = 86400;
        /** The number of seconds in a year. */
        public static final int Year = 31536000;
        /** One second less than the number of seconds in a decade. */
        public static final int SecondAwayFromADecade = 315359999;

        private static class TimeObj {
            private long years;
            private int days;
            private int hours;
            private int minutes;
            private int seconds;
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative
         */
        public static String toString(final long seconds) throws NegativeNumberException {
            return toString(seconds, false);
        }

        /**
         * Converts seconds into a readable format - years:days:hours:minutes:seconds.
         * @param seconds the amount of seconds to convert
         * @param allowNegative if true and the number is negative a negative sign will be returned with the string
         * @return a readable string
         * @throws NegativeNumberException if the specified number is negative and allowNegative is false
         */
        public static String toString(final long seconds, final boolean allowNegative) throws NegativeNumberException {
            if(seconds >= 0) return structureTime(seconds);
            if(allowNegative) return '-' + structureTime(Math.abs(seconds));
            else throw new NegativeNumberException("Seconds cannot be negative!");
        }

        private static String structureTime(final long seconds) {
            final TimeObj obj = new TimeObj();
            if(seconds >= 60) {
                obj.minutes = (int)(seconds / 60);
                obj.seconds = (int)(seconds % 60);

                if(obj.minutes >= 60) {
                    obj.hours = obj.minutes / 60;
                    obj.minutes %= 60;

                    if(obj.hours >= 24) {
                        obj.days = obj.hours / 24;
                        obj.hours %= 24;

                        if(obj.days >= 365) {
                            obj.years = obj.days / 365;
                            obj.days %= 365;
                        }
                    }
                }
            }
            else { obj.seconds = (int)seconds; }

            return obj.years + ":"
                    + obj.days + ':'
                    + String.format("%02d", obj.hours) + ':'
                    + String.format("%02d", obj.minutes) + ':'
                    + String.format("%02d", obj.seconds);
        }

        /** This exception is thrown if a negative number is supplied to the methods in this class. */
        public static class NegativeNumberException extends Exception {
            //Parameterless Constructor
            public NegativeNumberException() { }

            //Constructor that accepts a message
            public NegativeNumberException(final String message) { super(message); }
        }

        /** Prevents instantiation of this utility class. */
        private SecondsConverter() { }

        //10:364:23:59:59
        //59
        //3540
        //82800
        //31449600
        //315360000
        //346895999

        /*try {
            System.out.println(toString(-90, true));
            System.out.println(toString(1, true));
            System.out.println(toString(0));
            System.out.println(toString(Minute + Hour + Day + Year + 1));
            System.out.println(toString(SecondAwayFromADecade));
            System.out.println(toString(Integer.MAX_VALUE));
            System.out.println(toString(Long.MAX_VALUE));
        } catch(NegativeNumberException e) {
            e.printStackTrace();
        }*/
    }

    /** Prevents instantiation of this utility class. */
    private Misc() { }
}
