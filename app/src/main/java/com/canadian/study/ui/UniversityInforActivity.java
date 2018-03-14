package com.canadian.study.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.canadian.study.R;

public class UniversityInforActivity extends AppCompatActivity {

    private int[] bottomSelectBackgrounds = {R.drawable.left_blue_conver_rect, R.drawable.blue_rect, R.drawable.blue_rect};
    private  Button[] buttons;
    private int selectedType = 0;
    private Fragment zoneFragment;
    private Fragment cityTypeFragment;
    private Fragment cateproyTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_infor);

        findViewById(R.id.backImage).setOnClickListener(view -> {
            onBackPressed();
        });

        findViewById(R.id.searchImage).setOnClickListener(view -> {
            startActivity(new Intent(this, SearchUniversityActivity.class));
        });

        findViewById(R.id.frameLayout).setVisibility(View.GONE);

        buttons = new Button[]{(Button) findViewById(R.id.zoneTypeButton)
                , (Button) findViewById(R.id.cittTypeButton), (Button) findViewById(R.id.univerityTypeButton)};

        buttons[0].setOnClickListener(view -> {
            clickBottomButtons(0);
            getSupportFragmentManager().beginTransaction().show(zoneFragment)
                    .hide(cateproyTypeFragment)
                    .hide(cityTypeFragment).commit();
        });

        buttons[1].setOnClickListener(view -> {
            clickBottomButtons(1);
            getSupportFragmentManager().beginTransaction().show(cityTypeFragment)
                    .hide(cateproyTypeFragment)
                    .hide(zoneFragment).commit();
        });

        buttons[2].setOnClickListener(view -> {
            clickBottomButtons(2);
            getSupportFragmentManager().beginTransaction().show(cateproyTypeFragment)
                    .hide(cityTypeFragment)
                    .hide(zoneFragment).commit();
        });

        zoneFragment = Fragment.instantiate(this, SimpleImageFragment.class.getName());
        cityTypeFragment = Fragment.instantiate(this, CityTypeFragment.class.getName());
        cateproyTypeFragment = Fragment.instantiate(this, SimpleImageFragment.class.getName());
        getSupportFragmentManager().beginTransaction().add(R.id.contentFrame, zoneFragment)
                .add(R.id.contentFrame, cityTypeFragment)
                .add(R.id.contentFrame, cateproyTypeFragment)
                .hide(cityTypeFragment)
                .hide(cateproyTypeFragment)
                .commit();

    }

    private void clickBottomButtons(int index){
        if (selectedType != index){
            selectedType = index;
            for (int i = 0; i < buttons.length; i++) {
                if (i == index) {
                    buttons[i].setBackgroundResource(bottomSelectBackgrounds[i]);
                    buttons[i].setTextColor(Color.WHITE);
                }else {
                    buttons[i].setBackgroundResource(R.drawable.transparent_rect);
                    buttons[i].setTextColor(Color.BLACK);
                }
            }
        }

    }
}
