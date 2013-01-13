package com.csun.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csun.roulette.BetArea.BetType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RouletteTable extends View {
	/**
	 * Constants
	 */
	private static final int DISTANCE_FROM_FINGER = 100;
	private static final int TEXT_SIZE = 20;
	
	private static final int OFFSET_X = 40;
	private static final int OFFSET_Y = 60;
	
	private static final int BET_SQUARE_WIDTH = 40;
	private static final int BET_SQUARE_HEIGHT = 60;
	
	private static final int TABLE_ROWS = 3;
	private static final int TABLE_COLUMNS = 12;
	
	
	private static final int LOWER_BOUNDARY_X = OFFSET_X;
	private static final int UPPER_BOUNDARY_X = OFFSET_X + (BET_SQUARE_WIDTH * TABLE_COLUMNS);
	
	private static final int LOWER_BOUNDARY_Y = OFFSET_Y;
	private static final int UPPER_BOUNDARY_Y = OFFSET_Y + (BET_SQUARE_HEIGHT * TABLE_ROWS);
	
	private Paint tablePaint;
	private Paint textPaint;
	private Paint betPaint;

	
	private List<Chip> chips;
	
	private RouletteWheel wheel;
	
	private boolean inDrawArea;
	private Map<String, BetArea> betAreas;
	
	private Chip currentChip;
	private float currentFingerX;
	private float currentFingerY;
	
	
	public RouletteTable(Context context) {
		super(context);
		initialize(context);
	}
	
	public RouletteTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public RouletteTable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		initializeUi();
	    initializeCentersBetSquare();
	    
	    // others
	    wheel = new RouletteWheel(120, 120, 100);
	    
	    chips = new ArrayList<Chip>();
	    Chip.setBitmapResource(context);	
	    currentChip = new Chip(ChipType._25_DOLLARS, 0, 0);
	    inDrawArea = false;
	    currentFingerX = 0.0f;
	    currentFingerY = 0.0f;
	} 

	private void initializeUi() {
		setBackgroundDrawable(getResources().getDrawable(R.drawable.table));
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(TEXT_SIZE);
		
		tablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    tablePaint.setStyle(Paint.Style.FILL);
	    
	    betPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    betPaint.setStyle(Paint.Style.FILL);
	    betPaint.setColor(Color.BLACK);
	}
	
	private void initializeCentersBetSquare() {
		betAreas = new HashMap<String, BetArea>();
		int halfWidth = BET_SQUARE_WIDTH / 2;
		int halfHeight = BET_SQUARE_HEIGHT / 2;
		String key = "";
		boolean alternate = true;
		// centers
		for (int x = LOWER_BOUNDARY_X + halfWidth; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = LOWER_BOUNDARY_Y + halfHeight; y < UPPER_BOUNDARY_Y + halfHeight; y += BET_SQUARE_HEIGHT) {
				key = x + ":" + y;
				betAreas.put(key, new BetArea(x, y, BetArea.BetType.SINGLE_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
			}
		}
		
		// horizontal two
		for (int x = LOWER_BOUNDARY_X + BET_SQUARE_WIDTH; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = LOWER_BOUNDARY_Y + halfHeight; y < UPPER_BOUNDARY_Y; y += BET_SQUARE_HEIGHT) {
				key = x + ":" + y;
				betAreas.put(key, new BetArea(x, y, BetArea.BetType.TWO_NUMBER_HORIZONTAL, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
			}
		}
		
		// vertical two
		for (int x = LOWER_BOUNDARY_X + halfWidth; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = LOWER_BOUNDARY_Y + BET_SQUARE_HEIGHT; y < UPPER_BOUNDARY_Y; y += BET_SQUARE_HEIGHT) {
				key = x + ":" + y;
				betAreas.put(key, new BetArea(x, y, BetArea.BetType.TWO_NUMBER_VERTICAL, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
			}
		}
		
		// middle rows
		int y = LOWER_BOUNDARY_Y + (1 * BET_SQUARE_HEIGHT);
		for (int x = LOWER_BOUNDARY_X + BET_SQUARE_WIDTH; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			key = x + ":" + y;
			betAreas.put(key, new BetArea(x, y, BetArea.BetType.FOUR_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		}
		
		y = LOWER_BOUNDARY_Y + (2 * BET_SQUARE_HEIGHT);
		for (int x = LOWER_BOUNDARY_X + BET_SQUARE_WIDTH; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			key = x + ":" + y;
			betAreas.put(key, new BetArea(x, y, BetArea.BetType.FOUR_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		}
		
		// last row
		alternate = true;
		for (int x = LOWER_BOUNDARY_X + halfWidth; x <= (UPPER_BOUNDARY_X - halfWidth); x += halfWidth) {
			key = x + ":" + UPPER_BOUNDARY_Y;
			if (alternate) {
				betAreas.put(key, new BetArea(x, UPPER_BOUNDARY_Y, BetArea.BetType.THREE_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
				alternate = false;
			} else {
				betAreas.put(key, new BetArea(x, UPPER_BOUNDARY_Y, BetArea.BetType.SIX_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
				alternate = true;
			}
		}
	}
	
	private void drawTable(Canvas canvas) {
		betPaint.setColor(Color.RED);
		for (int x = LOWER_BOUNDARY_X; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = LOWER_BOUNDARY_Y; y < UPPER_BOUNDARY_Y; y += BET_SQUARE_HEIGHT) {
				canvas.drawRect(x, y, x + BET_SQUARE_WIDTH, y + BET_SQUARE_HEIGHT, betPaint);
				if (betPaint.getColor() == Color.RED) {
					betPaint.setColor(Color.BLACK);
				} else {
					betPaint.setColor(Color.RED);
				}
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		// wheel.draw(canvas);
		for (Chip c : chips) {
			c.draw(canvas);
		}
		currentChip.draw(canvas);
		if (inDrawArea) {
			drawBetRectangle(canvas, (int)currentFingerX, (int)currentFingerY);
		}
	}
	
	private void drawBetRectangle(Canvas canvas, int x, int y) {
		String key = new String(x + ":" + y);
		if (betAreas.containsKey(key)) {
			BetArea b = betAreas.get(key);
			b.draw(canvas);
		}
	}
	
	/**
	 * Estimate the x/y position of 
	 * finger position
	 * 
	 * @param offset
	 * 			width/height of betting rectangle
	 * 
	 * @return
	 * 		    the closest integer part of that coordinate
	 */
	private int estimateFingerPositionCoordinate(int coordinate, int offset) {
		if ((coordinate % offset) > offset/2) {
			return ((coordinate / offset) + 1) * offset;
		} else {
			return (coordinate / offset)  * offset;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		currentChip.setX(event.getX());
		currentChip.setY(event.getY() - DISTANCE_FROM_FINGER);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				onTouchDown();
				break;
				
			case MotionEvent.ACTION_UP:
				onTouchUp();
				break;
				
			case MotionEvent.ACTION_CANCEL:
				break;
				
			case MotionEvent.ACTION_MOVE:
				onTouchMove(currentChip);
				break;
				
			case MotionEvent.ACTION_OUTSIDE:
				break;
		}
		
		// force redraw 
		invalidate();
		return true;
	}
	
	public void addChip(Chip chip) {
		chips.add(chip);
	}
	
	private void onTouchMove(Chip c) {
		currentFingerX = estimateFingerPositionCoordinate((int) c.getX(), BET_SQUARE_WIDTH/2);
		currentFingerY = estimateFingerPositionCoordinate((int) c.getY(), BET_SQUARE_HEIGHT/2);
		inDrawArea = true;
		invalidate();
	}
	
	private void onTouchUp() {
		inDrawArea = false;
	}
	
	private void onTouchDown() {
		
	}
}
