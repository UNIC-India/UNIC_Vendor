package com.unic.unic_vendor_final_1.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlusCodeConverter {

    public class Location {
        double latitude;
        double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public static String convertToPlusCodes(double latitude,double longitude){

        StringBuilder str = new StringBuilder("");
        latitude+=90;
        longitude+=180;

        List<Character> codes = new ArrayList<>();

        str.append(convertToLocationCode((int)Math.floor(latitude/20)));
        str.append(convertToLocationCode((int)Math.floor(longitude/20)));
        latitude -= Math.floor(latitude/20)*20;
        longitude -= Math.floor(longitude/20)*20;

        str.append(convertToLocationCode((int)Math.floor(latitude)));
        str.append(convertToLocationCode((int)Math.floor(longitude)));
        latitude -= Math.floor(latitude);
        longitude -= Math.floor(longitude);

        str.append(convertToLocationCode((int)Math.floor(latitude*20)));
        str.append(convertToLocationCode((int)Math.floor(longitude*20)));
        latitude -= Math.floor(latitude*20)/20;
        longitude -= Math.floor(longitude*20)/20;

        str.append(convertToLocationCode((int)Math.floor(latitude*400)));
        str.append(convertToLocationCode((int)Math.floor(longitude*400)));
        latitude -= Math.floor(latitude*400)/400;
        longitude -= Math.floor((longitude*400))/400;

        str.append('+');

        str.append(convertToLocationCode((int)Math.floor(latitude*8000)));
        str.append(convertToLocationCode((int)Math.floor(longitude*8000)));
        latitude -= Math.floor(latitude*8000)/8000;
        longitude = Math.floor(longitude*8000)/8000;

        str.append(getLastCode((int)latitude*8000,(int)longitude*8000));
        return str.toString();

    }

    private static char convertToLocationCode(int code){

        char[] codes = {'2','3','4','5','6','7','8','9','C','F','G','H','J','M','P','Q','R','V','W','X'};
        return codes[code];
    }

    private static char getLastCode(int latitude, int longitude){

        char[] codes = {'2','3','4','5','6','7','8','9','C','F','G','H','J','M','P','Q','R','V','W','X'};
        return codes[(latitude/2000)*4+longitude/1600];
    }

    public static boolean isPlusCodeInRange(String initialPlusCode,String finalPlusCode , int distance){
        Map<String,Double> initialLocation = getCoordinatesFromPlusCode(initialPlusCode);
        Map<String,Double> finalLocation = getCoordinatesFromPlusCode(finalPlusCode);

        double d = Math.acos(Math.sin(initialLocation.get("latitude"))*Math.sin(finalLocation.get("latitude")) + Math.cos(initialLocation.get("latitude"))*Math.cos(finalLocation.get("latitude"))*(finalLocation.get("longitude")-initialLocation.get("longitude")))*6371;
        return d <= distance;
    }

    public static Map<String,Double> getCoordinatesFromPlusCode(String plusCode){
        List<Character> codes = new ArrayList<>(Arrays.asList('2','3','4','5','6','7','8','9','C','F','G','H','J','M','P','Q','R','V','W','X'));

        char[] code = plusCode.toCharArray();

        double latitude = codes.indexOf(code[0])*20 + codes.indexOf(code[2]) + codes.indexOf(code[4])*0.05 + codes.indexOf(code[6])*0.0025 + codes.indexOf(code[9])*0.000125;
        double longitude = codes.indexOf(code[1])*20 + codes.indexOf(code[3]) + codes.indexOf(code[5])*0.05 + codes.indexOf(code[7])*0.0025 + codes.indexOf(code[10])*0.000125;

        Map<String,Double> location = new HashMap<>();
        location.put("latitude",latitude);
        location.put("longitude",longitude);
        return location;
    }

}
