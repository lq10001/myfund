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

import com.ly.fund.vo.Product;
import com.ly.fund.service.ProductService;


@IocBean
@At("/product")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class ProductAction {

	private static final Log log = Logs.getLog(ProductAction.class);
	
	@Inject
	private ProductService productService;

    @At("/")
    @Ok("beetl:/WEB-INF/fund/product_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Product product,
                      HttpServletRequest request){
        Cnd c = new ParseObj(product).getCnd();
        List<Product> list_m = productService.query(c, p);
        p.setRecordCount(productService.count(c));

        request.setAttribute("list_obj", list_m);
        request.setAttribute("page", p);
        request.setAttribute("product", product);
    }

    @At
    @Ok("beetl:/WEB-INF/fund/product.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("product", null);
        }else{
            request.setAttribute("product", productService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Product product){
        Object rtnObject;
        if (product.getId() == null || product.getId() == 0) {
            rtnObject = productService.dao().insert(product);
        }else{
            rtnObject = productService.dao().updateIgnoreNull(product);
        }
        return Dwz.rtnMap((rtnObject == null) ? false : true, "product", "closeCurrent");
    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  productService.delete(id);
        return Dwz.rtnMap((num > 0) ? true : false , "product", "");
    }

}
