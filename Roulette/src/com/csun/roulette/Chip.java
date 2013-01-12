package com.csun.roulette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Chip implements Drawable {
	private final ChipType chipType;
	private final float x;
	private final float y;
	private static final Paint painter;
	private static Bitmap bitmap;
	
	static {
		painter = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public static void setBitmapResource(Context context) {
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chip);
	}
	
	public Chip(ChipType chipType, float x, float y) {
		this.chipType = chipType;
		this.x = x;
		this.y = y;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y, painter);
	}
}
