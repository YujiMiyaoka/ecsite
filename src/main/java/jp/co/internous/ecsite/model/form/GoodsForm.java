package jp.co.internous.ecsite.model.form;

import java.io.Serializable;

public class GoodsForm implements Serializable{
	// ユーザーがブラウザで入力した新規商品情報を受け取る為のフィールドを作成
	private int id;
	private String goodsName;
	private int price;
	// 新商品情報それぞれのsetter/getterを作成
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
}
