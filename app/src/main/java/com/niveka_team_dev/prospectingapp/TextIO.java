package com.niveka_team_dev.prospectingapp;

import com.niveka_team_dev.prospectingapp.utilities.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class TextIO {
    public static void main(String []args){
        final DateTime today = new DateTime().toDateTime();
        System.out.println(today.toString());
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssSZZ")));
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")));
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ")));
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")));
        System.out.println(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")));
        System.out.println("èèèèèèèèèèèè");
        final DateTime tod = new DateTime().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
        System.out.println(Utils.convertJodaTimeToReadable2(today.toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))));


    }
}
