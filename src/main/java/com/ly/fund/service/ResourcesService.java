package com.ly.fund.service;

import com.ly.fund.vo.Resources;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;

import java.util.List;


@IocBean(fields = { "dao" })
public class ResourcesService extends IdEntityService<Resources> {
}


