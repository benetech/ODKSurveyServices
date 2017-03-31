package org.opendatakit.demoAndroidlibraryClasses.utilities;

public class UTMConverter {
    private static final double SM_A = 6378137.0D;
    private static final double SM_B = 6356752.314D;
    private static final double UTM_SCALE_FACTOR = 0.9996D;

    public UTMConverter() {
    }

    private static double degToRad(double deg) {
        return deg / 180.0D * 3.141592653589793D;
    }

    private static double radToDeg(double rad) {
        return rad / 3.141592653589793D * 180.0D;
    }

    private static double UTMCentralMeridian(int zone) {
        return degToRad(-183.0D + (double)zone * 6.0D);
    }

    private static double footpointLatitude(double y) {
        double n = 0.0016792204056685965D;
        double alpha_ = 6367444.657D * (1.0D + Math.pow(n, 2.0D) / 4.0D + Math.pow(n, 4.0D) / 64.0D);
        double y_ = y / alpha_;
        double beta_ = 3.0D * n / 2.0D + -27.0D * Math.pow(n, 3.0D) / 32.0D + 269.0D * Math.pow(n, 5.0D) / 512.0D;
        double gamma_ = 21.0D * Math.pow(n, 2.0D) / 16.0D + -55.0D * Math.pow(n, 4.0D) / 32.0D;
        double delta_ = 151.0D * Math.pow(n, 3.0D) / 96.0D + -417.0D * Math.pow(n, 5.0D) / 128.0D;
        double epsilon_ = 1097.0D * Math.pow(n, 4.0D) / 512.0D;
        double result = y_ + beta_ * Math.sin(2.0D * y_) + gamma_ * Math.sin(4.0D * y_) + delta_ * Math.sin(6.0D * y_) + epsilon_ * Math.sin(8.0D * y_);
        return result;
    }

    private static void mapXYToLatLon(double x, double y, double lambda0, double[] philambda) {
        double phif = footpointLatitude(y);
        double ep2 = (Math.pow(6378137.0D, 2.0D) - Math.pow(6356752.314D, 2.0D)) / Math.pow(6356752.314D, 2.0D);
        double cf = Math.cos(phif);
        double nuf2 = ep2 * Math.pow(cf, 2.0D);
        double Nf = Math.pow(6378137.0D, 2.0D) / (6356752.314D * Math.sqrt(1.0D + nuf2));
        double tf = Math.tan(phif);
        double tf2 = tf * tf;
        double tf4 = tf2 * tf2;
        double x1frac = 1.0D / (Nf * cf);
        double Nfpow = Nf * Nf;
        double x2frac = tf / (2.0D * Nfpow);
        Nfpow *= Nf;
        double x3frac = 1.0D / (6.0D * Nfpow * cf);
        Nfpow *= Nf;
        double x4frac = tf / (24.0D * Nfpow);
        Nfpow *= Nf;
        double x5frac = 1.0D / (120.0D * Nfpow * cf);
        Nfpow *= Nf;
        double x6frac = tf / (720.0D * Nfpow);
        Nfpow *= Nf;
        double x7frac = 1.0D / (5040.0D * Nfpow * cf);
        Nfpow *= Nf;
        double x8frac = tf / (40320.0D * Nfpow);
        double x2poly = -1.0D - nuf2;
        double x3poly = -1.0D - 2.0D * tf2 - nuf2;
        double x4poly = 5.0D + 3.0D * tf2 + 6.0D * nuf2 - 6.0D * tf2 * nuf2 - 3.0D * nuf2 * nuf2 - 9.0D * tf2 * nuf2 * nuf2;
        double x5poly = 5.0D + 28.0D * tf2 + 24.0D * tf4 + 6.0D * nuf2 + 8.0D * tf2 * nuf2;
        double x6poly = -61.0D - 90.0D * tf2 - 45.0D * tf4 - 107.0D * nuf2 + 162.0D * tf2 * nuf2;
        double x7poly = -61.0D - 662.0D * tf2 - 1320.0D * tf4 - 720.0D * tf4 * tf2;
        double x8poly = 1385.0D + 3633.0D * tf2 + 4095.0D * tf4 + 1575.0D * tf4 * tf2;
        philambda[0] = phif + x2frac * x2poly * x * x + x4frac * x4poly * Math.pow(x, 4.0D) + x6frac * x6poly * Math.pow(x, 6.0D) + x8frac * x8poly * Math.pow(x, 8.0D);
        philambda[1] = lambda0 + x1frac * x + x3frac * x3poly * Math.pow(x, 3.0D) + x5frac * x5poly * Math.pow(x, 5.0D) + x7frac * x7poly * Math.pow(x, 7.0D);
    }

    private static void UTMXYToLatLon(double x, double y, int zone, boolean southhemi, double[] latlon) {
        x -= 500000.0D;
        x /= 0.9996D;
        if(southhemi) {
            y -= 1.0E7D;
        }

        y /= 0.9996D;
        double cmeridian = UTMCentralMeridian(zone);
        mapXYToLatLon(x, y, cmeridian, latlon);
    }

    public static double[] parseUTM(double x, double y, int zone, boolean southhemi) {
        if(x >= 0.0D && y >= 0.0D && zone >= 1 && zone <= 60) {
            double[] latlon = new double[2];
            UTMXYToLatLon(x, y, zone, southhemi, latlon);
            latlon[0] = radToDeg(latlon[0]);
            latlon[1] = radToDeg(latlon[1]);
            return latlon[0] > -90.0D && latlon[0] < 90.0D && latlon[1] > -180.0D && latlon[1] < 180.0D?latlon:null;
        } else {
            return null;
        }
    }
}
