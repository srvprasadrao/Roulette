package com.csun.roulette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Chip implements Drawable {
	private final ChipType chipType;
	private float x;
	private float y;
	private static final Paint painter;
	private Bitmap bitmap;
	
	private static Bitmap _1_BM;
	private static Bitmap _5_BM;
	private static Bitmap _25_BM;
	private static Bitmap _100_BM;

	static {
		painter = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public static void setBitmapResource(Context context) {
		_1_BM = BitmapFactory.decodeResource(context.getResources(), R.drawable.casino_chip_1_tiny);
		_5_BM = BitmapFactory.decodeResource(context.getResources(), R.drawable.casino_chip_5_tiny);
		_25_BM = BitmapFactory.decodeResource(context.getResources(), R.drawable.casino_chip_25_tiny);
		_100_BM = BitmapFactory.decodeResource(context.getResources(), R.drawable.casino_chip_100_tiny);
	}

	public Chip(ChipType chipType, float x, float y) {
		this.chipType = chipType;
		this.x = x;
		this.y = y;
		switch (chipType) {
			case _1_DOLLAR:
				bitmap = _1_BM;
				break;
				
			case _5_DOLLARS:
				bitmap = _5_BM;
				break;
				
			case _25_DOLLARS:
				bitmap = _25_BM;
				break;
				
			case _100_DOLLARS:
				bitmap = _100_BM;
				break;
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y, painter);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public ChipType getChipType() {
		return chipType;
	}

}
