package kr.co.koreanmagic.commons;

import java.util.ArrayList;
import java.util.Date;

public class WorkList {
	
	private Date date; // 날짜
	private String customer; // 거래처
	private String item; // 품목
	private String type; // 재질
	private int count; // 건수
	private int amount; // 수량
	private String size; // 사이즈 90-50
	private String unit; // 단위 mm
	private int bleed; // 재단
	
	public WorkList() {
		
	}
	
	public WorkList(Date date, String customer, String item, String type, int count, int amount, String size, String unit, int bleed) {
		super();
		this.date = date;
		this.customer = customer;
		this.item = item;
		this.type = type;
		this.count = count;
		this.amount = amount;
		this.size = size;
		this.unit = unit;
		this.bleed = bleed;
	}
	
	public void set(ArrayList<?> list) {
		
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getBleed() {
		return bleed;
	}
	public void setBleed(int bleed) {
		this.bleed = bleed;
	}
	
	
	
	
	@Override
	public String toString() {
		String[] members = {
				getDate().toString(), // 날짜
				getCustomer(), // 거래처
				getItem(), // 품목
				getType(), // 재질
				String.valueOf(getCount()),// 건수
				String.valueOf(getAmount()), // 수량
				getSize(), // 사이즈
				String.valueOf(getBleed()) // 재단선
		};
		return String.format("%s_%s_%s(%s)_[%s-%s]_%s(%s)", members);
	}

}
