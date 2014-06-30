package com.unicom.game.center.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.unicom.game.center.business.GameTrafficBusiness;
import com.unicom.game.center.business.LoginInfoBusiness;
import com.unicom.game.center.business.PageTrafficBusiness;
import com.unicom.game.center.model.GameDisplayModel;
import com.unicom.game.center.model.GameInfo;
import com.unicom.game.center.model.JsonParent;
import com.unicom.game.center.utils.AESEncryptionHelper;
import com.unicom.game.center.utils.Utility;

/**
 * Created with IntelliJ IDEA.
 * User: lenovo
 * Date: 14-6-26
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LogController {

    @Autowired
    private LoginInfoBusiness logInfoService;

    @Autowired
    private PageTrafficBusiness pageTrafficService;

    @Autowired
    private GameTrafficBusiness gameTrafficService;
    
	@Value("#{properties['backend.secret.key']}")
	private String backendKey;    
    
    @RequestMapping(value = "/log", method = {RequestMethod.GET})
    public ModelAndView ShowLogInfo(@RequestParam(value="token",required = false) String token,
    		@RequestParam(value="type",required=false) String type,
    		HttpSession session){
    	ModelMap model = new ModelMap();
    	String channelId = null;
    	
    	if(!Utility.isEmpty(token)){
    		try {
				channelId = AESEncryptionHelper.decrypt(token, backendKey);
				session.setAttribute("developer_channel", channelId);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	channelId = (String)session.getAttribute("developer_channel");
    	
    	if(null == channelId){
    		Boolean isAdmin = (Boolean)session.getAttribute("admin");    		
    		if(null == isAdmin || (null != isAdmin && !isAdmin.booleanValue())){
    			return new ModelAndView("/index", model); 
    		}
    	}

        model.put("channelId",(null != channelId) ? Integer.parseInt(channelId) : 0);
        model.put("type",(null != type) ? type : "1");
        return new ModelAndView("/log", model); 
    }    

    @RequestMapping(value = "/getlog", method = {RequestMethod.POST})
    public ModelAndView getLog(@RequestParam(value="type",required=false) String type,
    		@RequestParam(value="channelId",required = true) Integer channelID){
    	ModelMap model = new ModelMap();    	
        model.put("channelId",channelID);
        model.put("type",(null != type) ? type : "1");
        return new ModelAndView("/log", model); 
    }

    @RequestMapping(value = "/userLoginLog", method = {RequestMethod.POST})
    public @ResponseBody JsonParent userLoginCount(@RequestParam(value="type",required=false) String type,
    		@RequestParam(value="channelId",required = true) Integer channelID){
    	
    	if(Utility.isEmpty(type)){
    		type = "1";
    	}
    	
        int dateType = Integer.parseInt(type);
        JsonParent displayModel = null;
        if(dateType == 1){
            displayModel = logInfoService.fetchLoginInfoByDate(channelID);
        }else if(dateType == 2){
            displayModel = logInfoService.fetchLoginInfoByMonth(channelID);
        }
        return displayModel;
    }

    @RequestMapping(value = "/pageTrafficLog", method = {RequestMethod.POST})
    public @ResponseBody JsonParent pageTrafficCount(@RequestParam(value="type",required=false) String type,
    		@RequestParam(value="channelId",required = true) Integer channelID){
    	if(Utility.isEmpty(type)){
    		type = "1";
    	}    	
    	
        int dateType = Integer.parseInt(type);
        JsonParent displayModel = null;
        if(dateType == 1){
            displayModel = pageTrafficService.fetchTrafficInfoByDate(channelID);
        }else if(dateType == 2){
            displayModel = pageTrafficService.fetchTrafficInfoByMonth(channelID);
        }
        return displayModel;
    }

    @RequestMapping(value = "/firstPageBannerLog", method = {RequestMethod.POST})
    public @ResponseBody List<List<GameInfo>>  firstPageBannerLog(@RequestParam(value="type",required=false) String type,
    		@RequestParam(value="channelId",required = true) Integer channelID){
    	if(Utility.isEmpty(type)){
    		type = "1";
    	}
    	
        int dateType = Integer.parseInt(type);
        List<List<GameInfo>>  gameInfos = null;
        if(dateType == 1){
            gameInfos = gameTrafficService.getBannerDateModel(channelID);
        }else if(dateType == 2){
            gameInfos = gameTrafficService.getBannerMothModel(channelID);
        }
        return gameInfos;
    }

    @RequestMapping(value = "/topGameLog", method = {RequestMethod.POST})
    public @ResponseBody List<GameDisplayModel>  topGameLog(@RequestParam(value="type",required=false) String type,
    		@RequestParam(value="channelId",required = true) Integer channelID){
    	if(Utility.isEmpty(type)){
    		type = "1";
    	}
    	
        int dateType = Integer.parseInt(type);
        List<GameDisplayModel>  gameInfos = null;
        if(dateType == 1){
            gameInfos = gameTrafficService.getGameDayModel(channelID);
        }else if(dateType == 2){
            gameInfos = gameTrafficService.getGameMonthModel(channelID);
        }
        return gameInfos;
    }
}
