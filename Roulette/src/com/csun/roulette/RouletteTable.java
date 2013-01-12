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
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RouletteTable extends View {
	private Paint tablePaint;
	private Paint textPaint;
	private Paint betPaint;
	private List<Chip> chips;
	private Rect rectangle;
	
	private static final int betW = 60;
	private static final int betH = 90;
	
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
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(35);
		
		tablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    tablePaint.setStyle(Paint.Style.FILL);
	    
	    betPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    betPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    betPaint.setColor(Color.BLACK);
	    
	    chips = new ArrayList<Chip>();
	    Chip.setBitmapResource(context);	
	    
	    rectangle = new Rect(0, 0, 0, 0);
	} 
	
	private void drawTable(Canvas canvas) {
		int startX = 50;
		int startY = 50; 
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
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
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