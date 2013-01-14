package com.csun.roulette.controller;

import com.csun.roulette.Chip;
import com.csun.roulette.ChipType;
import com.csun.roulette.R;
import com.csun.roulette.R.id;
import com.csun.roulette.R.layout;
import com.csun.roulette.R.menu;
import com.csun.roulette.view.RouletteTableView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
	private RouletteTableView rouletteTable;
	private RadioGroup chipButtonGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rouletteTable = (RouletteTableView) findViewById(R.id.activity_main_xml_roulette_table);
		rouletteTable.setCurrentChip(new Chip(ChipType._1_DOLLAR, 0.0f, 0.0f));
		registerCurrentChipSelection();
	}

	private void registerCurrentChipSelection() {
		chipButtonGroup = (RadioGroup) findViewById(R.id.activity_main_xml_radiogroup_chips);
		chipButtonGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int selectedId = chipButtonGroup.getCheckedRadioButtonId();
				switch (selectedId) {
				case R.id.activity_main_xml_radio_button_chip_1 :
					rouletteTable.setCurrentChip(new Chip(ChipType._1_DOLLAR, 0.0f, 0.0f));
					break;

				case R.id.activity_main_xml_radio_button_chip_5 :
					rouletteTable.setCurrentChip(new Chip(ChipType._5_DOLLARS, 0.0f, 0.0f));
					break;

				case R.id.activity_main_xml_radio_button_chip_25 :
					rouletteTable.setCurrentChip(new Chip(ChipType._25_DOLLARS, 0.0f, 0.0f));
					break;

				case R.id.activity_main_xml_radio_button_chip_100 :
					rouletteTable.setCurrentChip(new Chip(ChipType._100_DOLLARS, 0.0f, 0.0f));
					break;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
