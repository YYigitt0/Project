// Main.java — Students version
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
                              "July","August","September","October","November","December"};
    
    static int[][][] profits = new int[MONTHS][DAYS][COMMS];

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        for (int m=0; m<MONTHS; m++){
            Scanner reader = null;
            try {
                String fileName = "Data_Files/" + months[m] + ".txt";
                 reader = new Scanner(Paths.get(fileName));

                while (reader.hasNextLine()){
                    String line = reader.nextLine();
                    String[] parts = line.split(",");

                    if (parts.length != 3) continue;

                    int day = Integer.parseInt(parts[0]);
                    String commodity = parts[1];
                    int profit = Integer.parseInt(parts[2]);

                    if (day < 1 || day > DAYS) continue;

                    int commIndex = -1;
                    for (int c=0; c<COMMS; c++){
                        if (commodities[c].equals(commodity)){
                            commIndex = c;
                            break;
                        }
                    }

                    if (commIndex == -1) continue;

                    profits[m][day - 1][commIndex] = profit;
                }

            } catch (Exception e){
                return;
            } finally {
                if (reader != null){
                    reader.close();
                }
            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month > 11){
            return "INVALID_MONTH";
        }

        int maxProfit = -100000000;
        int bestCommNum = -1;

        for (int c=0; c<COMMS; c++){
            int total = 0;

            for (int d=0; d<DAYS; d++){
                total += profits[month][d][c];
            }

            if (total > maxProfit){
                maxProfit = total;
                bestCommNum = c;
            }
        }
        return commodities[bestCommNum] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month > 11 || day < 1 || day > 28 ){
            return -99999;
        }

        int total = 0;

        for (int c=0; c<COMMS; c++){
            total += profits[month][day - 1][c];
        }

        return total;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        if (from < 1 || to > DAYS || from > to) {
            return -99999;
        }

        int commNum = -1;
        for (int c=0; c<COMMS; c++){
            if (commodities[c].equals(commodity)){
                commNum = c;
                break;
            }
        }
        if (commNum == -1){
            return -99999;
        }

        int total = 0;

        for (int m=0; m<MONTHS; m++){
            for (int d=from - 1; d<= to - 1; d++){
                total += profits[m][d][commNum];
            }
        }
        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month > 11){
            return -1;
        }

        int maxProfit = -100000000;
        int bestDay = 1;

        for (int d=0; d<DAYS; d++){
            int dayProfit = 0;

            for (int c=0; c<COMMS; c++){
                dayProfit += profits[month][d][c];
            }

            if (dayProfit > maxProfit){
                maxProfit = dayProfit;
                bestDay = d + 1;
            }
        }

        return bestDay;
    }

    public static String bestMonthForCommodity(String comm) { 
        int commNum = -1;
        for (int c=0; c<COMMS; c++){
            if (commodities[c].equals(comm)){
                commNum = c;
                break;
            }
        }
        if (commNum == -1){
            return "INVALID_COMMODITY";
        }

        int maxProfit = -100000000;
        int bestMonthNum = 0;

        for (int m=0; m<MONTHS; m++){
            int monthlyTotal = 0;

            for (int d=0; d<DAYS; d++){
                monthlyTotal += profits[m][d][commNum];
            }

            if (monthlyTotal > maxProfit){
                maxProfit = monthlyTotal;
                bestMonthNum = m;
            }
        }
        return months[bestMonthNum];
    }

    public static int consecutiveLossDays(String comm) { 
        int commNum = -1;
        for (int c=0; c<COMMS; c++){
            if (commodities[c].equals(comm)){
                commNum = c;
                break;
            }
        }
        if (commNum == -1){
            return -1;
        }
        int currentStreak = 0;
        int maxStreak = 0;
        for (int m=0; m<MONTHS; m++){
            for (int d=0; d<DAYS; d++){
                if (profits[m][d][commNum] < 0){
                    currentStreak++;
                    if (currentStreak > maxStreak){
                        maxStreak = currentStreak;
                    }
                }else {
                    currentStreak = 0;
                }
            }

        }
        return maxStreak;
    }
    
    public static int daysAboveThreshold(String comm, int threshold) { 
        int commNum = -1;
        for (int c=0; c<COMMS; c++){
            if (commodities[c].equals(comm)){
                commNum = c;
                break;
            }
        }
        if (commNum == -1){
            return -1;
        }

        int dayCount = 0;
        for (int m=0; m<MONTHS; m++){
            for (int d=0; d<DAYS; d++){
                if (profits[m][d][commNum] > threshold){
                    dayCount++;
                }
            }
        }
        return dayCount;
    }

    public static int biggestDailySwing(int month) { 
        if (month < 0 || month >= MONTHS){
            return -99999;
        }

        int maxDifference = 0;

        for (int d=0; d<DAYS - 1; d++){
            int firstDayProfit = 0;
            int secondDayProfit = 0;

            for (int c=0; c<COMMS; c++){
                firstDayProfit += profits[month][d][c];
                secondDayProfit += profits[month][d + 1][c];
            }

            int difference = secondDayProfit - firstDayProfit;
            if (difference < 0 ){
                difference *= -1;
            }

            if (difference > maxDifference){
                maxDifference = difference;
            }
        }
        return maxDifference;
    }
    
    public static String compareTwoCommodities(String c1, String c2) { 
        int commNum1 = -1;
        int commNum2 = -1;

        for (int c=0; c<COMMS; c++){
            if (commodities[c].equals(c1)){
                commNum1 = c;
            }
            if (commodities[c].equals(c2)){
                commNum2 = c;
            }
        }
        if (commNum1 == -1 || commNum2 == -1){
            return "INVALID_COMMODITY";
        }

        int total1 = 0;
        int total2 = 0;
        for (int m=0; m<MONTHS; m++){
            for (int d=0; d<DAYS; d++){
                total1 += profits[m][d][commNum1];
                total2 += profits[m][d][commNum2];
            }
        }
        if (total1 > total2){
            return c1 + " is better by " + (total1 - total2);
        } else if (total2 > total1){
            return c2 + " is better by " + (total2 - total1);
        } else {
            return "Equal";
        }
    }
    
    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestWeek = 1;
        int maxProfit = -100000000;

        for (int w=0; w<4; w++) {
            int weekProfit = 0;
            int startDay = w * 7;
            int endDay = startDay + 7;

            for (int d=startDay; d<endDay; d++) {
                for (int c=0; c<COMMS; c++) {
                    weekProfit += profits[month][d][c];
                }
            }

            if (weekProfit > maxProfit) {
                maxProfit = weekProfit;
                bestWeek = w + 1;
            }
        }
        return "Week " + bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}