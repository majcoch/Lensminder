package com.appnuggets.lensminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Solution;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Database testing ground
        try {
            Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse("06.01.2021");
            Date expirationDate = new SimpleDateFormat("dd.MM.yyyy").parse("06.04.2021");

            AppDatabase db = AppDatabase.getInstance(this);

            List<Solution> allSolutions = db.solutionDao().getAll();
            Solution solutionsInUse = db.solutionDao().getInUse();
            List<Solution> pastSolutions = db.solutionDao().getAllNotInUse();

            Solution solution = new Solution("solution", true, expirationDate,
                    startDate, 96L);

            /* Insert one in use */
            db.solutionDao().insert(solution);
            /* Create some history */
            solution.inUse = false;
            db.solutionDao().insert(solution);
            db.solutionDao().insert(solution);
            db.solutionDao().insert(solution);

            allSolutions = db.solutionDao().getAll();
            solutionsInUse = db.solutionDao().getInUse();
            pastSolutions = db.solutionDao().getAllNotInUse();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Dashboard:
                                return true;
                            case R.id.Lenses:
                                return true;
                            case R.id.Solution:
                                startActivity(new Intent(getApplicationContext(), SolutionActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.Drops:
                                startActivity(new Intent(getApplicationContext(), DropsActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.Settings:
                                return true;
                        }
                        return false;
                    }
                });

        MaterialCardView lensesCardView = findViewById(R.id.lensesCard);
        lensesCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lenses selected!");
            }
        });

        MaterialCardView containerCardView = findViewById(R.id.containerCard);
        containerCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Container selected!");
            }
        });

        MaterialCardView dropsCardView = findViewById(R.id.dropsCard);
        dropsCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Drops selected!");
                startActivity(new Intent(getApplicationContext(), DropsActivity.class));
                overridePendingTransition(0,0);
            }
        });

        MaterialCardView solutionCardView = findViewById(R.id.solutionCard);
        solutionCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Solution selected!");
                startActivity(new Intent(getApplicationContext(), SolutionActivity.class));
                overridePendingTransition(0,0);
            }
        });
    }

/*    private void updateLensesCard(Lenses lenses) {
        CircularProgressBar lensesProgressBar = findViewById(R.id.lensesProgressBar);
        TextView lensesDaysCount = findViewById(R.id.lensesDaysCount);
        TextView lensesExpDate = findViewById(R.id.lensesExpDateTextView);
        TextView lensesDaysUsed = findViewById(R.id.lensesDaysUsedTextView);

        lensesProgressBar.setProgressMax(lenses.getDuration());
        lensesExpDate.setText(lenses.getExpirationDateString());
        lensesDaysUsed.setText(Long.toString(lenses.getCurrentUsage()));
        lensesProgressBar.setProgressWithAnimation(lenses.getDuration() -  lenses.getUsageLeft(), (long) 1000); // =1s
        if( 0 >= lenses.getUsageLeft() ) {
            lensesDaysCount.setText(Long.toString(lenses.getUsageLeft()));
            lensesProgressBar.setProgressBarColor(Color.RED);
            lensesDaysCount.setTextColor(Color.RED);
        }
        else {
            lensesDaysCount.setText(Long.toString(lenses.getUsageLeft()));
            lensesDaysCount.setTextSize(40);
        }
    }

    private void updateContainerCard(Container container) {
        CircularProgressBar containerProgressBar = findViewById(R.id.containerProgressBar);
        TextView containerDaysCount = findViewById(R.id.containerDaysCount);
        TextView containerExpDate = findViewById(R.id.containerExpDateTextView);
        TextView containerDaysUsed = findViewById(R.id.containerDaysUsedTextView);

        containerProgressBar.setProgressMax(container.getDuration());
        containerExpDate.setText(container.getExpirationDateString());
        containerDaysUsed.setText(Long.toString(container.getCurrentUsage()));
        containerProgressBar.setProgressWithAnimation(container.getDuration() -  container.getUsageLeft(), (long) 1000); // =1s
        if( 0 >= container.getUsageLeft() ) {
            containerDaysCount.setText(Long.toString(container.getUsageLeft()));
            containerProgressBar.setProgressBarColor(Color.RED);
            containerDaysCount.setTextColor(Color.RED);
        }
        else {
            containerDaysCount.setText(Long.toString(container.getUsageLeft()));
            containerDaysCount.setTextSize(40);
        }
    }

    private void updateDropsCard(Drops drops) {
        CircularProgressBar dropsProgressBar = findViewById(R.id.dropsProgressBar);
        TextView dropsDaysCount = findViewById(R.id.dropsDaysCount);
        TextView dropsExpDate = findViewById(R.id.dropsExpDateTextView);
        TextView dropsDaysUsed = findViewById(R.id.dropsDaysUsedTextView);

        dropsProgressBar.setProgressMax(drops.getDuration());
        dropsExpDate.setText(drops.getExpirationDateString());
        dropsDaysUsed.setText(Long.toString(drops.getCurrentUsage()));
        dropsProgressBar.setProgressWithAnimation(drops.getDuration() -  drops.getUsageLeft(), (long) 1000); // =1s
        if( 0 >= drops.getUsageLeft() ) {
            dropsDaysCount.setText(Long.toString(drops.getUsageLeft()));
            dropsProgressBar.setProgressBarColor(Color.RED);
            dropsDaysCount.setTextColor(Color.RED);
        }
        else {
            dropsDaysCount.setText(Long.toString(drops.getUsageLeft()));
            dropsDaysCount.setTextSize(40);
        }
    }

    private void updateSolutionCard (Solution solution) {
        CircularProgressBar solutionProgressBar = findViewById(R.id.solutionProgressBar);
        TextView solutionDaysCount = findViewById(R.id.solutionDaysCount);
        TextView solutionExpDate = findViewById(R.id.solutionExpDateTextView);
        TextView solutionDaysUsed = findViewById(R.id.solutionDaysUsedTextView);

        solutionProgressBar.setProgressMax(solution.getDuration());
        solutionExpDate.setText(solution.getExpirationDateString());
        solutionDaysUsed.setText(Long.toString(solution.getCurrentUsage()));
        solutionProgressBar.setProgressWithAnimation(solution.getDuration() -  solution.getUsageLeft(), (long) 1000); // =1s
        if( 0 >= solution.getUsageLeft() ) {
            solutionDaysCount.setText(Long.toString(solution.getUsageLeft()));
            solutionProgressBar.setProgressBarColor(Color.RED);
            solutionDaysCount.setTextColor(Color.RED);
        }
        else {
            solutionDaysCount.setText(Long.toString(solution.getUsageLeft()));
            solutionDaysCount.setTextSize(40);
        }
    }*/
}