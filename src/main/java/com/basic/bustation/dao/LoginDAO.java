package com.basic.bustation.dao;

import com.basic.bustation.model.Admin;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("loginDAO")
public class LoginDAO extends BaseHibernateDAOImpl<Admin>{
    private static final Logger log = LoggerFactory
            .getLogger(LoginDAO.class);

    public Admin findByProperty(String propertyName1, Object value1,String propertyName2,Object value2) {
        log.debug("finding Admin instance with property: " + propertyName1
                + ", value: " + value1 + "," + propertyName2
                + ", value: " + value2);
        try {
            String queryString = "from Admin as model where model."
                    + propertyName1 + "= ? and model." + propertyName2 + "=?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value1);
            queryObject.setParameter(1,value2);
            return (Admin)queryObject.list().get(0);
        } catch (RuntimeException re) {
            log.error("find by property name and password failed", re);
            throw re;
        }
    }

    public long getCount(String name,String password) {
        // TODO 自动生成的方法存根
        return (long) getSession().createQuery("SELECT count(c) FROM Admin c WHERE c.name = ? AND c.password = ?")
                .setParameter(0,name).setParameter(1,password)
                .uniqueResult();
    }
}
