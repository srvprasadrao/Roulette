package com.csun.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csun.roulette.BetArea.BetType;
import com.csun.roulette.BetArea.NumberColor;

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
	private static final int TEXT_SIZE = 15;
	
	private static final int OFFSET_X = 60;
	private static final int OFFSET_Y = 80;
	
	private static final int BET_SQUARE_WIDTH = 60;
	private static final int BET_SQUARE_HEIGHT = 80;
	
	private static final int TABLE_ROWS = 3;
	private static final int TABLE_COLUMNS = 12;
	
	
	private static final int LOWER_BOUNDARY_X = OFFSET_X;
	private static final int UPPER_BOUNDARY_X = OFFSET_X + (BET_SQUARE_WIDTH * TABLE_COLUMNS);
	
	private static final int LOWER_BOUNDARY_Y = OFFSET_Y;
	private static final int UPPER_BOUNDARY_Y = OFFSET_Y + (BET_SQUARE_HEIGHT * TABLE_ROWS);
	
	private static final NumberColor[] BET_COLORS = {
		//    1					2                  3
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED,
		//    4					5                  6
		NumberColor.BLACK, NumberColor.RED,   NumberColor.BLACK,
		//    7					8                  9
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED,
		//    10				11                 12
		NumberColor.BLACK, NumberColor.BLACK, NumberColor.RED,
		//    13				14                 15
		NumberColor.BLACK, NumberColor.RED,   NumberColor.BLACK,
		//    16				17                 18
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED,
		//    19				20                 21 
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED,
		//    22				23                 24 
		NumberColor.BLACK, NumberColor.RED,   NumberColor.BLACK,
		//    25				26                 27 
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED,
		//    28				29                 30 
		NumberColor.BLACK, NumberColor.BLACK, NumberColor.RED,
		//    31				32                 33 
		NumberColor.BLACK, NumberColor.RED,   NumberColor.BLACK,
		//    34				35                 36 
		NumberColor.RED,   NumberColor.BLACK, NumberColor.RED
	};
	
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
	    BetArea.setLookupTable(betAreas);
	    
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
		int counter = 1;
		for (int x = LOWER_BOUNDARY_X + halfWidth; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = UPPER_BOUNDARY_Y - halfHeight; y > LOWER_BOUNDARY_Y; y -= BET_SQUARE_HEIGHT) {
				key = x + ":" + y;
				BetArea ba = new BetArea(x, y, BetArea.BetType.SINGLE_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT);
				ba.setNumber(Integer.toString(counter));
				ba.setNumberColor(BET_COLORS[counter - 1]);
				betAreas.put(key, ba);
				counter++;
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
		
		int x = UPPER_BOUNDARY_X + halfWidth;
		for (y = LOWER_BOUNDARY_Y + halfHeight; y < UPPER_BOUNDARY_Y; y += BET_SQUARE_HEIGHT) {
			key = x + ":" + y;
			betAreas.put(key, new BetArea(x, y, BetArea.BetType.TWO_TO_ONE, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		}
		
		y = UPPER_BOUNDARY_Y + halfHeight;
		for (x = LOWER_BOUNDARY_X + 2*BET_SQUARE_WIDTH; x < UPPER_BOUNDARY_X; x += 4*BET_SQUARE_WIDTH) {
			key = x + ":" + y;
			betAreas.put(key, new BetArea(x, y, BetArea.BetType.ONE_TO_TWELVE, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		}
		
		x = LOWER_BOUNDARY_X + BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.SMALL_18_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		
		x = UPPER_BOUNDARY_X - BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.LARGE_18_NUMBER, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		
		x = LOWER_BOUNDARY_X + 3*BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.EVEN, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		
		x = UPPER_BOUNDARY_X - 3*BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.ODD, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		
		x = LOWER_BOUNDARY_X + 5*BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.RED, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
				
		x = UPPER_BOUNDARY_X - 5*BET_SQUARE_WIDTH;
		y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT + halfHeight;
		key = x + ":" + y;
		betAreas.put(key, new BetArea(x, y, BetArea.BetType.BLACK, BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT));
		
		x = LOWER_BOUNDARY_X - 5*BET_SQUARE_WIDTH;
		
	}
	
	private void drawTable(Canvas canvas) {
		int halfWidth = BET_SQUARE_WIDTH/2;
		int halfHeight = BET_SQUARE_HEIGHT/2;
		int counter = 0;
		for (int x = LOWER_BOUNDARY_X + halfWidth; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = UPPER_BOUNDARY_Y - halfHeight; y > LOWER_BOUNDARY_Y; y -= BET_SQUARE_HEIGHT) {
				if (BET_COLORS[counter] == NumberColor.BLACK) {
					betPaint.setColor(Color.BLACK);
				} else if (BET_COLORS[counter] == NumberColor.RED) {
					betPaint.setColor(Color.RED);
				} else {
					betPaint.setColor(Color.GREEN);
				}
				canvas.drawRect(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight, betPaint);
				// draw border
				betPaint.setStyle(Style.STROKE);
				betPaint.setColor(Color.WHITE);
				canvas.drawRect(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight, betPaint);
				
				// draw tex
				betPaint.setStyle(Style.FILL);
				betPaint.setColor(Color.GREEN);
				canvas.drawText(Integer.toString(counter + 1), x, y, betPaint);
				counter++;
			}
		}
		
		// draw left panel
		betPaint.setStyle(Style.FILL);
		betPaint.setColor(Color.GREEN);
		canvas.drawRect(
				LOWER_BOUNDARY_X - BET_SQUARE_WIDTH, 
				LOWER_BOUNDARY_Y,
				LOWER_BOUNDARY_X,
				UPPER_BOUNDARY_Y, betPaint);
		
		betPaint.setStyle(Style.STROKE);
		betPaint.setColor(Color.WHITE);
		canvas.drawRect(
				LOWER_BOUNDARY_X - BET_SQUARE_WIDTH, 
				LOWER_BOUNDARY_Y,
				LOWER_BOUNDARY_X,
				UPPER_BOUNDARY_Y, betPaint);
		
		betPaint.setStyle(Style.FILL);
		betPaint.setColor(Color.BLACK);
		betPaint.setTextSize(TEXT_SIZE);
		canvas.drawText("0", 
			LOWER_BOUNDARY_X - BET_SQUARE_WIDTH + BET_SQUARE_WIDTH / 2, 
			LOWER_BOUNDARY_Y + 3 * BET_SQUARE_HEIGHT / 2,
			betPaint);
			
		
		// draw right panel
		for (int y = LOWER_BOUNDARY_Y; y < UPPER_BOUNDARY_Y; y += BET_SQUARE_HEIGHT) {
			betPaint.setStyle(Style.FILL);
			betPaint.setColor(Color.GREEN);
			canvas.drawRect(
				UPPER_BOUNDARY_X,
				y,
				UPPER_BOUNDARY_X + BET_SQUARE_WIDTH,
				y + BET_SQUARE_HEIGHT, betPaint);
			
			betPaint.setStyle(Style.STROKE);
			betPaint.setColor(Color.WHITE);
			canvas.drawRect(
				UPPER_BOUNDARY_X,
				y,
				UPPER_BOUNDARY_X + BET_SQUARE_WIDTH,
				y + BET_SQUARE_HEIGHT, betPaint);
			
			betPaint.setStyle(Style.FILL);
			betPaint.setColor(Color.BLACK);
			betPaint.setTextSize(TEXT_SIZE);
			canvas.drawText("2:1", UPPER_BOUNDARY_X + BET_SQUARE_WIDTH/3, y + BET_SQUARE_HEIGHT/2, betPaint);
		}
		
		// draw bottom panel
		String[] section = {"1st", "2nd", "3rd"};
		int i = 0;
		for (int x = LOWER_BOUNDARY_X; x < UPPER_BOUNDARY_X; x += 4*BET_SQUARE_WIDTH) {
			drawSpecialBetArea(canvas, x, UPPER_BOUNDARY_Y, 4*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, section[i] + " 12");
			i++;
		}
		
		int x = LOWER_BOUNDARY_X;
		int y = UPPER_BOUNDARY_Y + BET_SQUARE_HEIGHT;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "1-18");
		x += 2*BET_SQUARE_WIDTH;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "EVEN");
		x += 2*BET_SQUARE_WIDTH;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "RED");
		x += 2*BET_SQUARE_WIDTH;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "BLACK");
		x += 2*BET_SQUARE_WIDTH;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "ODD");
		x += 2*BET_SQUARE_WIDTH;
		drawSpecialBetArea(canvas, x, y, 2*BET_SQUARE_WIDTH, BET_SQUARE_HEIGHT, betPaint, "19-36");
	}
	
	private void drawSpecialBetArea(Canvas canvas, int x, int y, int width, int height, Paint paint, String text) {
		paint.setStyle(Style.FILL);
		paint.setColor(Color.GREEN);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.WHITE);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTextSize(TEXT_SIZE);
		canvas.drawText(text, x + width/3, y + height/2, paint);
	}
	
	public void drawNumbers(Canvas canvas) {
		canvas.save();
		betPaint.setColor(Color.GREEN);
		betPaint.setTextSize(TEXT_SIZE);
		int halfWidth = BET_SQUARE_WIDTH / 2;
		int halfHeight = BET_SQUARE_HEIGHT / 2;
		int counter = 1;
		for (int x = LOWER_BOUNDARY_X; x < UPPER_BOUNDARY_X; x += BET_SQUARE_WIDTH) {
			for (int y = UPPER_BOUNDARY_Y; y > LOWER_BOUNDARY_Y; y -= BET_SQUARE_HEIGHT) {
				canvas.drawText(Integer.toString(counter), x + halfWidth , y - halfHeight, betPaint);
				counter++;
			}
		}
		canvas.restore();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		// drawNumbers(canvas);
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
