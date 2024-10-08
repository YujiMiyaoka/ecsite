package jp.co.internous.ecsite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.internous.ecsite.model.dto.HistoryDto;
// 作成した TblPurchaseMapperに、商品の購入情報をデータベースにインサートするメソッドの作成
@Mapper
public interface TblPurchaseMapper {
	
	List<HistoryDto>findHistory(int userId);// 購入履歴を検索する為のメソッド（findHistory）

	int insert(int userId, int goodsId, String goodsName, int itemCount, int total);
}
