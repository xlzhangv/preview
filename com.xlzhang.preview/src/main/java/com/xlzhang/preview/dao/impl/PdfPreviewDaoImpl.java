/**
 * Date:2015年5月14日 下午4:49:45
 * Copyright (c) 2015, www.wisdombud.com All Rights Reserved.
 */

package com.xlzhang.preview.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xlzhang.preview.dao.PdfPreviewDao;
import com.xlzhang.preview.pojo.PdfPreview;

import freemarker.template.utility.StringUtil;

/**
 * 功能: TODO.<br/>
 * date: 2015年7月31日 下午2:32:21 <br/>
 *
 * @author lilei@wisdombud.com
 * @version
 * @since JDK 1.7
 */
@Repository
public class PdfPreviewDaoImpl implements PdfPreviewDao {

    private SessionFactory sessionFactory;
    
    
    @Override
    public List<PdfPreview> findByBaseId(String baseId) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<>();
        params.put("baseId", baseId);
        String hql = "From PdfPreview where baseId=:baseId order by sortFlag";
        Query query = this.getCurrentSession().createQuery(hql);
        fillParams(query, params);
        return query.list();
    }
    
    /**
     * 填充参数. <br/>
     * 
     * @author ghlin
     * @param query query
     * @param params 条件
     */
    private void fillParams(final Query query, final Map<String, ?> params) {
        if (null == query || null == params) {
            return;
        }
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (null == entry.getValue() || StringUtils.isEmpty(entry.getValue().toString().trim())) {
                continue;
            }
            if (entry.getValue() instanceof Collection<?>) {
                query.setParameterList(entry.getKey(), (Collection<?>) entry.getValue());
            } else if (entry.getValue() instanceof Object[]) {
                query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }
    
    @Override
    public void save(PdfPreview pojo){
        
        this.getCurrentSession().save(pojo);
    }
    
    /**
     * 功能: 获取session.<br/>
     * date: 2016年8月9日 上午11:20:07 <br/>
     *
     * @author xlzhang@wisdombud.com
     * @return
     */
    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
