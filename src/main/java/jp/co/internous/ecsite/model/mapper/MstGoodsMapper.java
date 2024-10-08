package jp.co.internous.ecsite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.ecsite.model.domain.MstGoods;

@Mapper
public interface MstGoodsMapper {
	
	@Select(value="SELECT * FROM mst_goods")
	List<MstGoods> findAll();
	
	@Insert("INSERT INTO mst_goods (goods_name, price) VALUES (#{goodsName}, #{price})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	int insert(MstGoods goods);
	
	@Update("DELETE FROM mst_goods WHERE id = #{ id }")
	int deleteById(int id);

}
// 全商品をmst_goodsテーブルから探索する為のDAO（MstGoodsMapper）の作成。
//
// カッコ内に記述するSQLの種類により、アノテーションが変わります。
// SELECT文であれば、「@Select」えお、INSERT文であれば「@Insert」を、
// UPDATE文やDELETE文であれば、「@Update」アノテーションを付与します。
// mst_goodsテーブルはidカラムを持ちますが、INSERT情報として列挙されていません。
// idがauto_incrementで作成されている場合、「@Options」アノテーションにより、
// データベース側で自動裁判されるよう設定します。