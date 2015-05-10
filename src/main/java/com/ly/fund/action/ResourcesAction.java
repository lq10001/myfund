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


import com.ly.fund.vo.Resources;
import com.ly.fund.service.ResourcesService;


@IocBean
@At("/resources")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class ResourcesAction {

	private static final Log log = Logs.getLog(ResourcesAction.class);
	
	@Inject
	private ResourcesService resourcesService;

    @At("/")
    @Ok("beetl:/WEB-INF/fund/resources_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Resources resources,
                      HttpServletRequest request){

        Cnd c = new ParseObj(resources).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(resourcesService.listCount(c));
            request.setAttribute("list_obj", resourcesService.queryCache(c,p));
        }else{
            p.setRecordCount(resourcesService.count(c));
            request.setAttribute("list_obj", resourcesService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("resources", resources);
    }

    @At
    @Ok("beetl:/WEB-INF/fund/resources.html")
    public void edit(@Param("action")int action,
                     @Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("resources", null);
        }else{

            Resources resources = resourcesService.fetch(id);
            if (action == 3)
            {
                resources.setName(null);
            }
            request.setAttribute("resources", resources);
        }
        request.setAttribute("action", action);
    }

    @At
    @Ok("json")
    public Map<String,String> save(@Param("action")int action,
                                @Param("..")Resources resources){
        Object rtnObject;
        if (resources.getId() == null || resources.getId() == 0) {
            rtnObject = resourcesService.dao().insert(resources);
        }else{
            if (action == 3) {
                resources.setId(null);
                rtnObject = resourcesService.dao().insert(resources);
            }else{
                rtnObject = resourcesService.dao().updateIgnoreNull(resources);
            }
        }
        CacheManager.getInstance().getCache(ResourcesService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_resources", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  resourcesService.delete(id);
        CacheManager.getInstance().getCache(ResourcesService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_resources",false);
    }

}
