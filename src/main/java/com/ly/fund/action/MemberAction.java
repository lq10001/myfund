package com.ly.fund.action;

import com.ly.comm.Bjui;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;


import com.ly.fund.vo.Member;
import com.ly.fund.service.MemberService;


@IocBean
@At("/member")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class MemberAction {

	private static final Log log = Logs.getLog(MemberAction.class);
	
	@Inject
	private MemberService memberService;

    @At("/")
    @Ok("beetl:/WEB-INF/fund/member_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Member member,
                      HttpServletRequest request){

        Cnd c = new ParseObj(member).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(memberService.listCount(c));
            request.setAttribute("list_obj", memberService.queryCache(c,p));
        }else{
            p.setRecordCount(memberService.count(c));
            request.setAttribute("list_obj", memberService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("member", member);
    }

    @At
    @Ok("beetl:/WEB-INF/fund/member.html")
    public void edit(@Param("action")int action,
                     @Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("member", null);
        }else{

            Member member = memberService.fetch(id);

            request.setAttribute("member", member);
        }
        request.setAttribute("action", action);
    }

    @At
    @Ok("json")
    public Map<String,String> save(@Param("action")int action,
                                @Param("..")Member member){
        Object rtnObject;
        if (member.getId() == null || member.getId() == 0) {
            rtnObject = memberService.dao().insert(member);
        }else{
            if (action == 3) {
                member.setId(null);
                rtnObject = memberService.dao().insert(member);
            }else{
                rtnObject = memberService.dao().updateIgnoreNull(member);
            }
        }
        CacheManager.getInstance().getCache(MemberService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_member", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  memberService.delete(id);
        CacheManager.getInstance().getCache(MemberService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_member",false);
    }

}
