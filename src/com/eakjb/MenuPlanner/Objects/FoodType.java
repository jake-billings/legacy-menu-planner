package com.eakjb.MenuPlanner.Objects;

public enum FoodType {
	MEAT(false, "Meat"),
	VEGI(true, "Vegitable"),
	FRUIT(true, "Fruit"),
	BREAD(true, "Bread"),
	SOUP(true, "Soup"),
	DRINK(false, "Drink"),
	OTHER(true, "Other");
	private final boolean isSafeWithAll;
	private final String title;
	FoodType(boolean isSafeWithAll, String title) {
		this.isSafeWithAll = isSafeWithAll;
		this.title = title;
	}
	public boolean isSafeWithAll() {
		return isSafeWithAll;
	}
	public String toString() {
		return title;
	}
	public String getTitle() {
		return title;
	}
}
