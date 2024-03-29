package com.track.smarterping;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

import android.location.Location;

/**
 * Protocol formatting
 */
public class Protocol {

    /**
     * Format device id message
     */
    public static String createLoginMessage(String id) {
        StringBuilder s = new StringBuilder("$PGID,");
        Formatter f = new Formatter(s, Locale.ENGLISH);

        s.append(id);

        byte checksum = 0;
        for (byte b : s.substring(1).getBytes()) {
            checksum ^= b;
        }
        f.format("*%02x\r\n", (int) checksum);
        f.close();

        return s.toString();
    }

    /**
     * Format location message
     */
    public static String createLocationMessage(boolean extended, Location l, double battery) {
        StringBuilder s = new StringBuilder(extended ? "$TRCCR," : "$GPRMC,");
        Formatter f = new Formatter(s, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
        calendar.setTimeInMillis(l.getTime());

        if (extended) {

            f.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS.%1$tL,A,", calendar);

            f.format("%.6f,%.6f,", l.getLatitude(), l.getLongitude());
            f.format("%.2f,%.2f,", l.getSpeed() * 1.943844, l.getBearing());
            f.format("%.2f,", l.getAltitude());
            f.format("%.0f,", battery);

        } else {

            f.format("%1$tH%1$tM%1$tS.%1$tL,A,", calendar);

            double lat = l.getLatitude();
            double lon = l.getLongitude();
            f.format("%02d%07.4f,%c,", (int) Math.abs(lat), Math.abs(lat) % 1 * 60, lat < 0 ? 'S' : 'N');
            f.format("%03d%07.4f,%c,", (int) Math.abs(lon), Math.abs(lon) % 1 * 60, lon < 0 ? 'W' : 'E');

            double speed = l.getSpeed() * 1.943844; // speed in knots
            f.format("%.2f,%.2f,", speed, l.getBearing());
            f.format("%1$td%1$tm%1$ty,,", calendar);

        }

        byte checksum = 0;
        for (byte b : s.substring(1).getBytes()) {
            checksum ^= b;
        }
        f.format("*%02x\r\n", (int) checksum);
        f.close();

        return s.toString();
    }
}

