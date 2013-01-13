package com.csun.roulette;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RouletteTable extends View {
	private static final int TEXT_SIZE = 20;
	
	private Paint tablePaint;
	private Paint textPaint;
	private Paint betPaint;
	private Paint wheelPaint;
	private Paint borderPaint;
	private List<Chip> chips;
	private Rect rectangle;
	
	private static final int betW = 35;
	private static final int betH = 60;
	
	private final RouletteWheel wheel;
	
	public RouletteTable(Context context) {
		super(context);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	public RouletteTable(Context context, AttributeSet attrs) {
		super(context, attrs);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	public RouletteTable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setBackgroundDrawable(getResources().getDrawable(R.drawable.table));
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(TEXT_SIZE);
		
		tablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    tablePaint.setStyle(Paint.Style.FILL);
	    
	    betPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    betPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    betPaint.setColor(Color.BLACK);
	    
	    wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    wheelPaint.setStyle(Paint.Style.FILL);
	    wheelPaint.setColor(Color.YELLOW);
	    
	    borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    borderPaint.setStyle(Paint.Style.STROKE);
	    borderPaint.setColor(Color.WHITE);
	    
	    chips = new ArrayList<Chip>();
	    Chip.setBitmapResource(context);	
	    
	    rectangle = new Rect(0, 0, 0, 0);
	    
	} 
	
	private void drawTable(Canvas canvas) {
		int startX = 300;
		int startY = 20; 
		int counter = 1;
		for (int x = 1; x <= 12; ++x) {
			int tempX = startX;
			int tempY = startY;
			for (int y = 1; y <= 3; ++y) {
				rectangle.set(tempX, tempY, tempX + betW, tempY + betH);
				// move down
				tempY += betH;
				// draw it
				canvas.drawRect(rectangle, betPaint);
				canvas.drawText(Integer.toString(counter), tempX, tempY, textPaint);
				if (betPaint.getColor() == Color.BLACK) {
					betPaint.setColor(Color.RED);
				} else {
					betPaint.setColor(Color.BLACK);
				}
				counter++;
			}
			// move to the right
			startX += betW;
		}
		
		// draw 2 to 1
		textPaint.setColor(Color.BLACK);
		betPaint.setColor(Color.GREEN);
		int tempH = startY;
		for (int i = 0; i < 3; ++i) {
			rectangle.set(startX, tempH, startX + betW, tempH + betH);
			canvas.drawRect(rectangle, betPaint);
			canvas.drawRect(rectangle, borderPaint);
			canvas.drawText("2/1", startX, tempH + betH, textPaint);
			tempH += betH;
		}
		
		// draw 0 and 00
		startX = 300 - betW;
		tempH = startY;
		int newH = (betH * 3) /2;
		for (int i = 0; i < 2; ++i) {
			rectangle.set(startX, tempH, startX + betW, tempH + newH);
			canvas.drawRect(rectangle, betPaint);
			canvas.drawRect(rectangle, borderPaint);
			canvas.drawText("0", startX, tempH + betH, textPaint);
			tempH += newH;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		wheel.draw(canvas);
		for (Chip c : chips) {
			c.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		chips.add(new Chip(ChipType._1_DOLLAR, x, y));
		invalidate();
		return true;
	}
}
