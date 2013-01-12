package com.csun.roulette;

public enum ChipType {
	_1_DOLLAR,
	_5_DOLLARS,
	_25_DOLLARS,
	_100_DOLLARS;
	
	ChipType() {
	}
	
	public double pay(int howMany, double rate) {
		switch (this) {
			case _1_DOLLAR:
				return howMany * 1.0 * rate;
				
			case _5_DOLLARS:
				return howMany * 5.0 * rate;
				
			case _25_DOLLARS:
				return howMany * 25.0 * rate;
				
			case _100_DOLLARS:
				return howMany * 100 * rate;
	
			default: 
				throw new AssertionError("Unknown chip's type" + this);
		}
	}
}
