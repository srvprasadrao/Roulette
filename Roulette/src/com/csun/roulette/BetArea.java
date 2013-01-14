package com.csun.roulette;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BetArea implements Drawable {
	private static final Paint paint;
	private static Map<String, BetArea> betAreaHashMap;

	private interface BettingFunctor {
		public boolean isIt(BetArea betArea);
	}
	
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
		TWO_TO_ONE,
		ONE_TO_TWELVE,
		LARGE_18_NUMBER,
		SMALL_18_NUMBER,
		ODD,
		EVEN,
		RED,
		BLACK
	}
	
	public enum NumberColor {
		RED,
		BLACK,
		SPECIAL,
	}

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final BetType betType;
	private String number;
	private NumberColor color;
	
	public static void setLookupTable(Map<String, BetArea> map) {
		betAreaHashMap = map;
	}

	public BetArea(int x, int y, BetType betType, int width, int height) {
		this.x = x;
		this.y = y;
		this.betType = betType;
		this.width = width;
		this.height = height;
		this.number = "99";
		this.color = NumberColor.SPECIAL;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setNumberColor(NumberColor color) {
		this.color = color;
	}
	
	public String getNumber() {
		return number;
	}
	
	public NumberColor getNumberColor() {
		return color;
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
				
			case TWO_TO_ONE:
				canvas.drawRect(x - (12*width + width/2), y - height/2, x - width/2, y + height/2, paint);
				break;
				
			case ONE_TO_TWELVE:
				canvas.drawRect(x - 2*width, y - 3*height - height/2, x + 2*width, y - height/2, paint);
				break;
				
			case SMALL_18_NUMBER:
				canvas.drawRect(x - width, y - 4*height - height/2, x + 5*width, y - height/2 - height, paint);
				break;
				
			case LARGE_18_NUMBER:
				canvas.drawRect(x - 5*width, y - 4*height - height/2, x + width, y - height/2 - height, paint);
				break;
				
			case ODD:
				drawIf(canvas, new BettingFunctor() {
					public boolean isIt(BetArea betArea) {
						int n = Integer.parseInt(betArea.getNumber());
						return (n != 99 && n % 2 == 1);
					}
				});
				break;
				
			case EVEN:
				drawIf(canvas, new BettingFunctor() {
					public boolean isIt(BetArea betArea) {
						int n = Integer.parseInt(betArea.getNumber());
						return (n != 99 && n % 2 == 0);
					}
				});
				break;
				
			case RED:
				drawIf(canvas, new BettingFunctor() {
					public boolean isIt(BetArea betArea) {
						return betArea.getNumberColor() == NumberColor.RED;
					}
				});
				break;
				
			case BLACK:
				drawIf(canvas, new BettingFunctor() {
					public boolean isIt(BetArea betArea) {
						return betArea.getNumberColor() == NumberColor.BLACK;
					}
				});
				break;
		}
	}
	
	private void drawIf(Canvas canvas, BettingFunctor predicate) {
		int x = 0;
		int y = 0;
		for (Entry<String, BetArea> entry : betAreaHashMap.entrySet()) {
	        if (predicate.isIt(entry.getValue())) {
		        String position = entry.getKey();
		        x = Integer.parseInt(position.substring(0, position.indexOf(":")));
		        y = Integer.parseInt(position.substring(position.indexOf(":") + 1));
		        canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, paint);
	        }
		}
	}
}
