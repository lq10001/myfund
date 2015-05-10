package  com.ly.fund.service;

import com.ly.fund.vo.Member;
import org.nutz.dao.Condition;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.nutz.dao.Cnd;
import com.ly.comm.Page;

import java.util.List;


@IocBean(fields = { "dao" })
public class MemberService extends IdEntityService<Member> {

	public static String CACHE_NAME = "member";
    public static String CACHE_COUNT_KEY = "member_count";

    public List<Member> queryCache(Condition c,Page p)
    {
        List<Member> list_member = null;
        String cacheKey = "member_list_" + p.getPageCurrent();

        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(cacheKey) == null)
        {
            list_member = this.query(c, p);
            cache.put(new Element(cacheKey, list_member));
        }else{
            list_member = (List<Member>)cache.get(cacheKey).getObjectValue();
        }
        return list_member;
    }

    public int listCount(Condition c)
    {
        Long num = 0L;
        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(CACHE_COUNT_KEY) == null)
        {
            num = Long.valueOf(this.count(c));
            cache.put(new Element(CACHE_COUNT_KEY, num));
        }else{
            num = (Long)cache.get(CACHE_COUNT_KEY).getObjectValue();
        }
        return num.intValue();
    }



}


