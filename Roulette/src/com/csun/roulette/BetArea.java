package com.csun.roulette;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BetArea implements Drawable {
	private static final Paint paint;
	static {
		 paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 paint.setStyle(Paint.Style.FILL);
		 paint.setARGB(128, 255, 255, 255);
	}
	
	public enum BetType {
		SINGLE_NUMBER,
		TWO_NUMBER_HORIZONTAL,
		TWO_NUMBER_VERTICAL,
		THREE_NUMBER,
		FOUR_NUMBER,
		SIX_NUMBER,
	}

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final BetType betType;


	public BetArea(int x, int y, BetType betType, int width, int height) {
		this.x = x;
		this.y = y;
		this.betType = betType;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return x;
	}
	
	public BetType getBetType() {
		return betType;
	}
	
	public void draw(Canvas canvas) {
		switch (betType) {
			case SINGLE_NUMBER:
				canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, paint);
				break;
				
			case TWO_NUMBER_HORIZONTAL:
				canvas.drawRect(x - width, y - height/2, x + width, y + height/2, paint);
				break;
				
			case TWO_NUMBER_VERTICAL:
				canvas.drawRect(x - width/2, y - height, x + width/2, y + height, paint);
				break;
	
				
			case THREE_NUMBER:
				canvas.drawRect(x - width/2, y - 3*height, x + width/2, y, paint);
				break;
			
			case FOUR_NUMBER:
				canvas.drawRect(x - width, y - height, x + width, y + height, paint);
				break;
				
			case SIX_NUMBER:
				canvas.drawRect(x - width, y - 3*height, x + width, y, paint);
				break;
		}
	}
}
