package com.ly.fund.action;

import com.ly.comm.Dwz;
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
        List<Member> list_m = memberService.query(c, p);
        p.setRecordCount(memberService.count(c));

        request.setAttribute("list_obj", list_m);
        request.setAttribute("page", p);
        request.setAttribute("member", member);
    }

    @At
    @Ok("beetl:/WEB-INF/fund/member.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("member", null);
        }else{
            request.setAttribute("member", memberService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Member member){
        Object rtnObject;
        if (member.getId() == null || member.getId() == 0) {
            rtnObject = memberService.dao().insert(member);
        }else{
            rtnObject = memberService.dao().updateIgnoreNull(member);
        }
        return Dwz.rtnMap((rtnObject == null) ? false : true, "member", "closeCurrent");
    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  memberService.delete(id);
        return Dwz.rtnMap((num > 0) ? true : false , "member", "");
    }

}
