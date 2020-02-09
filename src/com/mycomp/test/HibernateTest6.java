package com.mycomp.test;

/*
 * QBC
 */

import com.mycomp.domain.Customer;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.List;

public class HibernateTest6 {

    /*
     * QBC: 简单查询
     */
    @Test
    public void test1() {
        Session session = HibernateUtil.openSession();
        Criteria criteria = session.createCriteria(Customer.class);
        List<Customer> list = criteria.list();
        System.out.println(list);
    }

    /*
     * QBC: 排序查询
     */
    @Test
    public void test2() {
        Session session = HibernateUtil.openSession();
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.addOrder(Order.desc("cust_id"));
        List<Customer> list = criteria.list();
        System.out.println(list);
    }

    /*
     * QBC: 分页查询
     */
    @Test
    public void test3() {
        Session session = HibernateUtil.openSession();
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.setFirstResult(0);
        criteria.setMaxResults(2);
        List<Customer> list = criteria.list();
        System.out.println(list);
    }

    /*
     * QBC: 条件查询
     * =    eq
     * >    gt
     * >=   ge
     * <    lt
     * <=   le
     * <>   ne
     * like
     * in
     * and
     * or
     */
    @Test
    public void test4() {
        Session session = HibernateUtil.openSession();
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.add(Restrictions.eq("cust_source", "c1"));
        criteria.add(Restrictions.like("cust_name", "张%")); // 以"张"开头的
        List<Customer> list = criteria.list();
        System.out.println(list);
    }

    /*
     * QBC: 统计查询, 单个结果
     * setProjection(): 能够设置一些聚合函数
     */
    @Test
    public void test5() {
        Session session = HibernateUtil.openSession();
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.setProjection(Projections.rowCount());
        Object o = criteria.uniqueResult();
        System.out.println(o);
    }

    /*
     * QBC: 离线条件查询
     * 离线, 指的是脱离session添加条件
     * 可以在外部提前使用DetachedCriteria对象设置好条件, 最后再绑定到session中
     * 应用场景: 我们经常需要在三层架构的web层中设置好条件(例如搜索功能), 然后再传到Dao层去查询
     */
    @Test
    public void test6() {
        // 离线条件
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Customer.class);
        detachedCriteria.add(Restrictions.like("cust_name", "张%"));

        // 把条件绑定到session
        Session session = HibernateUtil.openSession();
        Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session);
        List<Customer> list = executableCriteria.list();
        System.out.println(list);
    }

}
