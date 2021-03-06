/*
 * Copyright (C) 2014 Pablo Campillo-Sanchez <pabcampi@ucm.es>
 *
 * This software has been developed as part of the 
 * SociAAL project directed by Jorge J. Gomez Sanz
 * (http://grasia.fdi.ucm.es/sociaal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package phat.world;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Calendar of the simulation.
 * 
 * @author pablo
 */
public class PHATCalendar implements Serializable{            
    Calendar calendar;
    
    public static void main(String [] args) {
        System.out.println("init...");
        PHATCalendar c = new PHATCalendar(2013, 1, 1, 0, 0, 0);
        System.out.println("Calendar = "+c.toString());
        System.out.println("Other!");
    }
    java.util.Date d=new java.util.Date();
    public PHATCalendar() {
    	
        calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
//        calendar.set(Calendar.YEAR, d.getYear());
//        calendar.set(Calendar.MONTH, d.getMonth()-1);
//        calendar.set(Calendar.DAY_OF_MONTH, d.getDay()-1);
//        calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
//        calendar.set(Calendar.MINUTE, d.getMinutes());
//        calendar.set(Calendar.SECOND, d.getSeconds());
    }
    
    public PHATCalendar(PHATCalendar copy) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(copy.getTimeInMillis());
    }
    
    @Override
    public Object clone() {
    	return new PHATCalendar(getYear(), getMonth(), getDayOfMonth(), getHourOfDay(), getMinute(), getSecond());
    }
    
    /**
     * 
     * @param year
     * @param month 1-12
     * @param dayOfMonth 1-31
     * @param hour 0-23
     * @param min 0-59
     * @param sec 0-59
     */
    public PHATCalendar(int year, int month, int dayOfMonth, int hourOfDay, int min, int sec) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
    }
    
    /**
     * Updates the calendar.
     * @param millis 
     */
    public void setTimeInMillis(long millis) {
        calendar.setTimeInMillis(millis);
    }
    
    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss E, d MMM YYYY");        
        return sdf.format(calendar.getTime());
    }
    
    public long spentTimeTo(PHATCalendar ref) {
    	long millis = ref.getTimeInMillis() - getTimeInMillis();
    	return millis/1000;
    }
    
    public boolean pastTime(int hours, int minutes, int seconds, int marginInSecs) {
    	int secondsOfDay = getHourOfDay()*3600+getMinute()*60+getSecond();
    	int refSec = hours*3600+minutes*60+seconds;
        return secondsOfDay > refSec && ((secondsOfDay - refSec) < marginInSecs);
    }
    
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
    
    public void setYear(int year) {
        calendar.set(Calendar.YEAR, year);
    }
    
    public int getMonth() {
        return calendar.get(Calendar.MONTH)+1;
    }
    
    public void setMonth(int month) {
        calendar.set(Calendar.MONTH, month-1);
    }
    
    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public void setDayOfMonth(int dayOfMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
    
    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);        
    }
    
    public int getHourOfDay() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    public void setHourOfDay(int hourOfDay) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    }
    
    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }
    
    public void setMinute(int min) {
        calendar.set(Calendar.MINUTE, min);
    }
    
    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }
    
    public void setSecs(int sec) {
        calendar.set(Calendar.SECOND, sec);
    }
    
    public int getMillisecond() {
        return calendar.get(Calendar.MILLISECOND);
    }
}
