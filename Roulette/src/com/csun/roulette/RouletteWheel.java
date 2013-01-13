package com.csun.roulette;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RouletteWheel implements Drawable {
	private final float originX;
	private final float originY;
	private final float radius;
	private Paint painter;
	
	public RouletteWheel(float originX, float originY, float radius) {
		this.originX = originX;
		this.originY = originY;
		this.radius = radius;
		painter = new Paint(Paint.ANTI_ALIAS_FLAG);
		painter.setStyle(Paint.Style.FILL);
		painter.setColor(Color.BLACK);
	}
	
	public void draw(Canvas canvas) {
		painter.setColor(Color.WHITE);
		canvas.drawCircle(originX, originY, radius, painter);
		painter.setColor(Color.BLACK);
		canvas.drawCircle(originX, originY, radius - 30, painter);
		
		// TODO Auto-generated method stub
		float angle = 0.0f;
		float x = 0.0f;
		float y = 0.0f;
		do {
			x = (float) (radius * Math.cos(angle * Math.PI / 180.0));
			y = (float) (radius * Math.sin(angle * Math.PI / 180.0));
			canvas.drawLine(originX, originY, originX + x, originY + y, painter);
			angle += 10.0f;
		} while (angle < 360.0f);
	}
}
