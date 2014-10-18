package com.unicom.game.center.web;

import com.unicom.game.center.service.GameService;
import com.unicom.game.center.vo.GameDetailListVo;
import com.unicom.game.center.vo.GameDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/gamedetail")
public class GameDetailController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/detaillist", method = RequestMethod.GET)

    public String gameDetailList(@RequestParam("product_id") String productId, Model model) {
        GameDetailListVo gameDetailListVo = gameService.readGameDetailList(productId);
        GameDetailVo v = gameDetailListVo.getGameDetailVo();
        model.addAttribute("v", v);
        return "gameDetail";
    }
}
