package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.domain.MstGoods;
import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.mapper.MstGoodsMapper;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;
import jp.co.internous.ecsite.model.mapper.TblPurchaseMapper;
// お客様（ユーザー）がサイトにアクセスすると、トップページに商品一覧が初期表示される
@Controller
@RequestMapping("/ecsite")// localhost:8080/ecsite/ のURLでアクセスできるよう設定
public class IndexController {

	@Autowired
	private MstGoodsMapper goodsMapper;// MstGoodsを介してmst_goodsテーブルにアクセスするためのmapper(DAO)
	//                                    @Autowiredにより自動的にインスタンス化
	@Autowired
	private MstUserMapper userMapper;// MstUserを介してmst_userテーブルにアクセスするためのmapper(DAO)
	//                                  @Autowiredにより自動的にインスタンス化
	@Autowired
	private TblPurchaseMapper purchaseMapper;
	
	private Gson gson=new Gson();// webサービスAPIとして作成する為JSON形式を扱えるようGsonをインスタンス化しておく
	
	@GetMapping("/")//                                 トップページ(index.html)に移るメソッド
	public String index(Model model) {//               ・goodsテーブルから取得した商品エンティティの一覧を
		List<MstGoods>goods = goodsMapper.findAll();// 　フロントに渡すModelに追加する
		model.addAttribute("goods", goods);//           ・return "index";により、index.htmlに移る
		
		return "index";
	}
	
	@ResponseBody// 文字列そのものが返却される @ResponseBody
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm f) {
		MstUser user = userMapper.findByUserNameAndPassword(f);// mst_userテーブルからユーザー名とパスワードによって
		//                                                        探索し、結果を取得
		if(user == null) {
			user = new MstUser();
			user.setFullName("ゲスト");
		}
		
		return gson.toJson(user);// MstUser型のuserをJSON形式の文字列に変換し、画面側に返却
	}
	
	@ResponseBody
	@PostMapping("/api/purchase")
	public int purchaseApi(@RequestBody CartForm f) {
		
		f.getCartList().forEach((c) -> {
			int total = c.getPrice() * c.getCount();
			purchaseMapper.insert(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});
		
		return f.getCartList().size();
	}
	// 購入履歴の表示を担うメソッド(historyApi)
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm f) {
		int userId = f.getUserId();
		List<HistoryDto>history = purchaseMapper.findHistory(userId);
		
		return gson.toJson(history);
	}
}
