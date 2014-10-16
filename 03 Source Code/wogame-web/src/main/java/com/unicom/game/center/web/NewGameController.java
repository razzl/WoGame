package com.unicom.game.center.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unicom.game.center.service.GameService;
import com.unicom.game.center.service.StatisticsLogger;
import com.unicom.game.center.util.Constants;
import com.unicom.game.center.vo.NewListVo;
import com.unicom.game.center.vo.NewVo;

/**
 * 管理员管理用户的Controller.
 *
 * @author calvin
 */
@Controller
@RequestMapping(value = "/newGame")
public class NewGameController {

    @Autowired
    private StatisticsLogger statisticsLogger;

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpSession session) {

        return "newGame";
    }

    @RequestMapping(value = "/ajaxList", method = RequestMethod.GET)
    @ResponseBody
    public NewVo ajaxList(@RequestParam("pageNum") int pageNum, @RequestParam(value="pageSize", required=false) Integer pageSize, HttpSession session) {
        int size = Constants.PAGE_SIZE_DEFAULT;

        if(null != pageSize && pageSize > 0){
            size = pageSize;
        }

        NewListVo newListVo = gameService.readNewList(pageNum, size);

        return newListVo.getDataList();
    }

}
