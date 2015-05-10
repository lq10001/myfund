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


import com.ly.fund.vo.Datum;
import com.ly.fund.service.DatumService;


@IocBean
@At("/datum")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class DatumAction {

	private static final Log log = Logs.getLog(DatumAction.class);
	
	@Inject
	private DatumService datumService;

    @At("/")
    @Ok("beetl:/WEB-INF/fund/datum_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Datum datum,
                      HttpServletRequest request){

        Cnd c = new ParseObj(datum).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(datumService.listCount(c));
            request.setAttribute("list_obj", datumService.queryCache(c,p));
        }else{
            p.setRecordCount(datumService.count(c));
            request.setAttribute("list_obj", datumService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("datum", datum);
    }

    @At
    @Ok("beetl:/WEB-INF/fund/datum.html")
    public void edit(@Param("action")int action,
                     @Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("datum", null);
        }else{

            Datum datum = datumService.fetch(id);
            if (action == 3)
            {
                datum.setName(null);
            }
            request.setAttribute("datum", datum);
        }
        request.setAttribute("action", action);
    }

    @At
    @Ok("json")
    public Map<String,String> save(@Param("action")int action,
                                @Param("..")Datum datum){
        Object rtnObject;
        if (datum.getId() == null || datum.getId() == 0) {
            rtnObject = datumService.dao().insert(datum);
        }else{
            if (action == 3) {
                datum.setId(null);
                rtnObject = datumService.dao().insert(datum);
            }else{
                rtnObject = datumService.dao().updateIgnoreNull(datum);
            }
        }
        CacheManager.getInstance().getCache(DatumService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_datum", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  datumService.delete(id);
        CacheManager.getInstance().getCache(DatumService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_datum",false);
    }

}
