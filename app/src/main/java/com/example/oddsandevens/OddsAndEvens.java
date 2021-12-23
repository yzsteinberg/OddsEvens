package com.example.oddsandevens;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Arrays;

public class OddsAndEvens {
    private int computerPick;
    private int mNumberOfRoundsPlayed = 0;
    private int[] mArrayPlayerWinCount = new int[2];

    public OddsAndEvens() {
        startNewRound();
    }

    public void startNewRound() {
        computerPick = (int) Math.ceil(Math.random() * 5);
    }

    public int getComputerPick() {
        return computerPick;
    }

    public int getNumberOfRoundsPlayed() {
        return mNumberOfRoundsPlayed;
    }

    public int[] getArrayPlayerWinCount() {
        return mArrayPlayerWinCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OddsAndEvens that = (OddsAndEvens) o;

        return computerPick == that.computerPick;
    }

    @Override
    public int hashCode() {
        return computerPick;
    }

    /**
     * toString method returns the object name and winning number
     *
     * @return Object name and winning number
     */
    @Override
    @NonNull
    public String toString() {
        return "OddsAndEvens{" +
                "the computer picked =" + computerPick +
                '}';
    }

    public int getNumberOfWinsForPlayer(int playerNumber) {
        return mArrayPlayerWinCount[playerNumber];
    }

    public String decideWinner(int total, boolean evenWins){
        String returnString;
        returnString =  evenWins?
                (total % 2 == 0
                        ? "Yes, the total was even!"
                        : "Sorry, the total was odd :(") :
                (total % 2 != 0
                        ? "Yes, the total was odd!"
                        : "sorry, the total was even :(");
        if (returnString.charAt(0) == 'Y'){
            mArrayPlayerWinCount[0]++;
        } else {
            mArrayPlayerWinCount[1]++;
        }
        return returnString;
    }

    public void updateRoundNumber(){
        mNumberOfRoundsPlayed++;
    }

    public String getJSONStringFromThis() {
        return new Gson().toJson(this);
    }

    public static OddsAndEvens getOddsAndEvensObjectFromJSONString(String jsonString) {
        return (OddsAndEvens) new Gson().fromJson(jsonString, OddsAndEvens.class);
    }

    public static String getJSONFromGame(OddsAndEvens obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static OddsAndEvens getGameFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, OddsAndEvens.class);
    }

    public String getJSONFromCurrentGame() {
        return getJSONFromGame(this);
    }

    public void resetStatistics() {
        mNumberOfRoundsPlayed = 0;
        Arrays.fill(mArrayPlayerWinCount, 0);
    }

}
