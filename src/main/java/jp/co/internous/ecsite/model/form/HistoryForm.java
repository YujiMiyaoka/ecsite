package jp.co.internous.ecsite.model.form;

import java.io.Serializable;
// 購入履歴をデータベースに問い合わせる為に必要な情報は user_id のみです。
public class HistoryForm implements Serializable{
	
	private int userId;
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
