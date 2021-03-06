package com.unicom.game.center.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.unicom.game.center.business.AccountBusiness;
import com.unicom.game.center.business.BannerBusiness;
import com.unicom.game.center.business.ChannelInfoBusiness;
import com.unicom.game.center.model.AccountInfo;
import com.unicom.game.center.model.BannerInfo;
import com.unicom.game.center.model.ChannelInfo;
import com.unicom.game.center.utils.Constant;

/**
 * @author Alex Yin
 * 
 * @Date Jun 11, 2014
 */

@Controller
public class HomeController
{
    @Autowired
    private AccountBusiness accountService;
    
	@Autowired
	private ChannelInfoBusiness channelService;
	
    @Autowired
    private BannerBusiness bannerBusiness;	

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public String index() {
		return "index";
	}

    @RequestMapping(value = "/checkNamePwd", method = {RequestMethod.POST})
    public @ResponseBody Integer checkNamePwd(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password){    	
    	AccountInfo account = accountService.login(username, password);
        return account.getLoginStatus();
    }
    
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ModelAndView login(
    		@RequestParam(value = "username", required = true) String username,
    		@RequestParam(value = "password", required = true) String password,
    		@RequestParam(value = "remember", required = false) String remember,
    		HttpServletRequest request, HttpServletResponse response,HttpSession session){
    	ModelMap model = new ModelMap();
    	
		if(null == session){
			session = request.getSession(true);
		}

		AccountInfo account = accountService.login(username, password);
    	if(account.getLoginStatus() == 0){
    		String viewName = "/site";
    		List<ChannelInfo> channelInfos = channelService.fetchActiveChannelInfos();
			model.put("channelInfos", channelInfos);
			if(Constant.BACKEND_ADMIN_ROLE == account.getRole()){
				session.setAttribute("admin", true);	
				session.setAttribute("showBanner", true);
			}else if(Constant.BACKEND_BANNER_ROLE == account.getRole()){
				session.setAttribute("showBanner", true);
	            List<BannerInfo> floatWindowInfos = bannerBusiness.fetchBannerInfos(Constant.HOMEPAGE_TOP_AD);
	            if(null != floatWindowInfos){
	                model.put("floatWindowInfos",floatWindowInfos);
	            }				
				viewName = "/floatWindow";
			}

            if("1".equals(remember)){
                addCookie(response,"login_code",username,30);
                addCookie(response,"pwd",password,30);
            }else {
                delCookie("login_code",response,request);
                delCookie("pwd",response,request);
            }
			return new ModelAndView(viewName, model);
    	}
        return new ModelAndView("index", model);

    }
    
	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session,HttpServletResponse response,HttpServletRequest request)
	{
		ModelMap model = new ModelMap();

		if(null != session)
		{
			session.invalidate();
		}
		delCookie("login_code",response,request);
        delCookie("pwd",response,request);
		return new ModelAndView("/index", model);
	}


    public void addCookie(HttpServletResponse response,String name,String value,int maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        if(maxAge>0){
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public void delCookie(String name,HttpServletResponse response,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(null != cookies){
            for(int i=0;i<cookies.length;i++){
                Cookie cookie = new Cookie(name,null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }        	
        }
    }
}
