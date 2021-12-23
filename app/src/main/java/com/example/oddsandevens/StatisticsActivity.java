package com.example.oddsandevens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StatisticsActivity extends AppCompatActivity {
    private TextView tvDataGamesPlayed, tvDataPlayer1Wins,
            tvDataPlayer2Wins;

    private OddsAndEvens mCurrentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupToolbar();
        setupFAB();
        setupViews();
        getIncomingData();
        processAndOutputIncomingData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupViews() {
        tvDataGamesPlayed = findViewById(R.id.tv_data_games_played);
        tvDataPlayer1Wins = findViewById(R.id.tv_data_player1_wins);
        tvDataPlayer2Wins = findViewById(R.id.tv_data_comp_wins);
    }

    private void getIncomingData() {
        Intent intent = getIntent();
        String gameJSON = intent.getStringExtra("GAME");
        mCurrentGame = OddsAndEvens.getGameFromJSON(gameJSON);
    }

    private void processAndOutputIncomingData() {
        int numberOfRoundsPlayed = mCurrentGame.getNumberOfRoundsPlayed();
//        if (numberOfRoundsPlayed > 0)
//            numberOfRoundsPlayed--;
        int p1Wins = mCurrentGame.getNumberOfWinsForPlayer(0);
        int p2Wins = mCurrentGame.getNumberOfWinsForPlayer(1);
        tvDataGamesPlayed.setText(String.valueOf(numberOfRoundsPlayed));     // don't forget String.valueOf()
        tvDataPlayer1Wins.setText(String.valueOf(p1Wins));
        tvDataPlayer2Wins.setText(String.valueOf(p2Wins));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
