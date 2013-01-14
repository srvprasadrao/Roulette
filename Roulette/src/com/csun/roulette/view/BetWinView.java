package com.csun.roulette.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class BetWinView extends View {
	private final int TEXT_SIZE = 14;
	private final int OFFSET = TEXT_SIZE;
	private final int WIDTH = 150;
	private final int HEIGHT = 60; 
	private Paint paint;
	
	public BetWinView(Context context) {
		super(context);
		initialize(context);
	}
	
	public BetWinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public BetWinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	void initialize(Context context) {
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		paint.setTextSize(TEXT_SIZE);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText("Bet", OFFSET, OFFSET, paint);
		canvas.drawText("Win", OFFSET, 2 * OFFSET + 5, paint);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4.0f);
		paint.setStrokeCap(Cap.ROUND);
		paint.setColor(Color.BLUE);
		canvas.drawRect(4, 4, WIDTH, HEIGHT, paint);
	}
	
}
