package com.example.oddsandevens;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.oddsandevens.OddsAndEvens.getGameFromJSON;
import static com.example.oddsandevens.OddsAndEvens.getJSONFromGame;
import static com.example.oddsandevens.lib.Utils.showInfoDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private OddsAndEvens mOAEGame;
    private boolean playerIsEven, mUseAutoSave;
    private final String mKEY_GAME = "GAME";
    private String mKEY_AUTO_SAVE, mKey_Is_Even;
    private int[] playerFingerPicArray, compPlayerPicArray;
    private ImageView playerFingerPic, compFingerPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFields();
        setUpPicArrays();
        setUpViews();
        mOAEGame = savedInstanceState != null
                ? OddsAndEvens.getOddsAndEvensObjectFromJSONString(
                savedInstanceState.getString("GAME"))
                : new OddsAndEvens();
    }

    private void setUpPicArrays() {
        setupPlayerFingerPicArray();
        setupCompFingerPicArray();
    }

    private void setUpViews() {
        playerFingerPic = findViewById(R.id.player_finger_pic);
        compFingerPic = findViewById(R.id.comp_finger_pic);
    }

    private void setupFields() {
        mKEY_AUTO_SAVE = getString(R.string.auto_save_key);
        mKey_Is_Even = getString(R.string.win_on_even_key);
    }

    private void setupCompFingerPicArray() {
        compPlayerPicArray = new int[]{R.drawable.comp_0, R.drawable.comp_1, R.drawable.comp_2,
                R.drawable.comp_3, R.drawable.comp_4};
    }

    private void setupPlayerFingerPicArray() {
        playerFingerPicArray = new int[]{R.drawable.player_0, R.drawable.player_1, R.drawable.player_2,
                R.drawable.player_3, R.drawable.player_4};
    }



    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_statistics: {
                showStatistics();
                return true;
            }
            case R.id.action_reset_stats: {
                mOAEGame.resetStatistics();
                return true;
            }
            case R.id.action_settings: {
                showSettings();
                return true;
            }
            case R.id.action_about: {
                showAbout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() {
        showInfoDialog(MainActivity.this, "About Odds and Evens",
                "A quick one person game; have fun!\n" +
                        "pick a number of fingers to put out\n" +
                        "and if the total that you and the computer\n" +
                        "put out equals even or odds (choose in the " +
                        "settings) you win!\n\n" +
                        "by chezky");
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        intent.putExtra("GAME", mOAEGame.getJSONFromCurrentGame());
        startActivity(intent);
    }

    private void showSettings() {
//        dismissSnackBarIfShown();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mUseAutoSave = sp.getBoolean(mKEY_AUTO_SAVE, true);
        playerIsEven = sp.getBoolean(mKey_Is_Even, true);
    }

    public void pickNumFingers(View view) {
        Button currentButton = (Button) view;
        int playerPick = Integer.parseInt(currentButton.getText().toString());
        int totalNum = playerPick + mOAEGame.getComputerPick();
        updatePics(playerPick);
        showResultSnackbar(totalNum, playerIsEven, view);
        mOAEGame.updateRoundNumber();
        mOAEGame.startNewRound();
    }

    private void showResultSnackbar(int totalNum, boolean playerIsEven, View view) {
        String resultsText = mOAEGame.decideWinner(totalNum, playerIsEven);
        Snackbar.make(view, resultsText, Snackbar.LENGTH_SHORT).show();
    }

    private void updatePics(int playerPick) {
        compFingerPic.setImageDrawable(
                ContextCompat.getDrawable(this, compPlayerPicArray[mOAEGame.getComputerPick() - 1]));
        try {
            playerFingerPic.setImageDrawable(
                    ContextCompat.getDrawable(this, playerFingerPicArray[playerPick - 1]));
        } catch (Exception e) {
            System.out.println("why is this not working?");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOrDeleteGameInSharedPrefs();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreFromPreferences_SavedGameIfAutoSaveWasSetOn();
        restoreOrSetFromPreferences_AllAppAndGameSettings();
    }

    private void
    restoreFromPreferences_SavedGameIfAutoSaveWasSetOn() {
        SharedPreferences defaultSharedPreferences =
                getDefaultSharedPreferences(this);
        if (defaultSharedPreferences.getBoolean(mKEY_AUTO_SAVE, true)) {
            String gameString =
                    defaultSharedPreferences.getString(mKEY_GAME, null);
            if (gameString != null) {
                mOAEGame = getGameFromJSON(gameString);
            }
        }
    }

    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences =
                getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
// Save current game or remove any prior game to/from default shared preferences
        if (mUseAutoSave)
            editor.putString(mKEY_GAME, mOAEGame.getJSONFromCurrentGame());
        else
            editor.remove(mKEY_GAME);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(mKEY_GAME, getJSONFromGame(mOAEGame));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOAEGame = getGameFromJSON(savedInstanceState.getString(mKEY_GAME));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            restoreOrSetFromPreferences_AllAppAndGameSettings();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}