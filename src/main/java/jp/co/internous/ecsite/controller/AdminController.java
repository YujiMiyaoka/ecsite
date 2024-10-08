package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.ecsite.model.domain.MstGoods;
import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.mapper.MstGoodsMapper;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;

@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {
	
	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private MstGoodsMapper goodsMapper;
	
	@RequestMapping("/")
	public String index() {
		return "admintop";
	}

	@PostMapping("/welcome")
	public String welcome(LoginForm form,Model model) {
		
		MstUser user = userMapper.findByUserNameAndPassword(form);// ⓵MstUserMapperの13行目に定義されているメソッドを呼び出す
		// ⓶MstUserMapperの13行目SQLの実行結果が返却され「MstUser型」の「変数user」に代入される。
		if(user == null) {
			model.addAttribute("errMessage", "ユーザー名またはパスワードが違います。");
			return "forward:/ecsite/admin/";
			}// if文によって、ユーザー探索の結果を判定。null（ヒットしなかった）場合、
			//  ブロック内の処理が実行される。※forwardによりトップページに移る
			if(user.getIsAdmin() == 0) {
				model.addAttribute("errMessage", "管理者ではありません。");
				return "forward:/ecsite/admin/";
			}// if文によって、ログインしたユーザーが管理者かどうか（isAdmin）を判定。
			//  管理者ではなかった場合、ブロック内の処理が実行される。※forwardによりトップページに移る。
			
			// if文による2度の判定で、「ログイン失敗」と「管理者以外のログイン」の際はトップページに移るため、
			// 以降の処理は行われない。
			
			// ここに到達した時点で、「ログイン成功」かつ「管理者でのログイン」の条件が保証されている。
			List<MstGoods> goods = goodsMapper.findAll();// 　　  MstGoodsMapperのfindAllメソッドによって、
			model.addAttribute("userName", user.getUserName());// 商品情報をすべて探索し、HTMLに渡す情報を
			model.addAttribute("password", user.getPassword());// modelに登録している。
			model.addAttribute("goods", goods);
		
			return "welcome";
	}
	
	@PostMapping("/goodsMst")
	public String goodsMst(LoginForm f, Model m) {
		m.addAttribute("userName", f.getUserName());
		m.addAttribute("password", f.getPassword());
		
		return "goodsmst";
	}
	
	@PostMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm, LoginForm loginForm, Model m) {
		m.addAttribute("userName", loginForm.getUserName());
		m.addAttribute("password", loginForm.getPassword());
		
		MstGoods goods = new MstGoods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		
		goodsMapper.insert(goods);
		
		return "forward:/ecsite/admin/welcome";
	}
	@ResponseBody
	@PostMapping("/api/deleteGoods")
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsMapper.deleteById(f.getId());
		}catch(IllegalArgumentException e){
			return "-1";// ←例外がキャッチされた場合は、「処理に失敗した」印として"-1"を返却している
		}
		
		return "1";// ←例外が起きず、ここまで到達出来れば「処理が成功した」印として"1"を返却している
	}
}
