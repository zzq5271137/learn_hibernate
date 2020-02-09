package com.mycomp.test;

import com.mycomp.domain.Customer;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/*
 * OID, HQL
 */

public class HibernateTest5 {

    /*
     * OID查询:
     * 1. get(): 直接发送sql语句, 返回实际对象, 包含所有你查询的字段, 如果找不到会返回null
     * 2. load(): 不发送sql语句, 返回代理对象, 只包含id, 当你需要用其他字段时再去查询, 如果找不到会报错
     */
    @Test
    public void testGetAndLoad() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // Customer customer = currentSession.get(Customer.class, 5L);
        Customer customer = currentSession.load(Customer.class, 5L);

        transaction.commit();
    }

    /*
     * HQL: 简单查询
     */
    @Test
    public void test2() {
        Session session = HibernateUtil.openSession();
        // from后面跟上的是类名, 可以写全路径也可以不写
        Query query = session.createQuery("from com.mycomp.domain.Customer");
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 别名查询
     */
    @Test
    public void test3() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select c from Customer c");
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 排序查询
     */
    @Test
    public void test4() {
        Session session = HibernateUtil.openSession();
        // desc: 降序
        // asc: 升序
        Query query = session.createQuery("select c from Customer c order by cust_id desc");
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 条件查询
     */
    @Test
    public void test5() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("from Customer where cust_name=:param1 and cust_source=:param2");
        query.setParameter("param1", "zzq");
        query.setParameter("param2", "zzq1");
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 投影查询, 查询单个属性
     */
    @Test
    public void test6() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select c.cust_name from Customer c");
        List<Object> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 投影查询, 查询多个属性
     */
    @Test
    public void test7() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select c.cust_name, c.cust_source from Customer c");
        List<Object[]> res = query.list();
        for (Object[] item : res) {
            System.out.println(Arrays.toString(item));
        }
    }

    /*
     * HQL: 投影查询, 查询多个属性, 并把查询的值封装成对象(需要提供构造器)
     */
    @Test
    public void test8() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select new Customer(cust_name, cust_source) from Customer c");
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 分页查询
     */
    @Test
    public void test9() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("from Customer");
        query.setFirstResult(0); // 查询的开始位置
        query.setMaxResults(2); // 查询几条
        List<Customer> res = query.list();
        System.out.println(res);
    }

    /*
     * HQL: 统计查询 (注意, 不是调用list()来得到结果, 而是调用uniqueResult())
     */
    @Test
    public void test10() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select count(*) from Customer");
        Object res = query.uniqueResult();
        System.out.println(res);
    }

    /*
     * HQL: 分组查询
     */
    @Test
    public void test11() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("select cust_name, count(*) from Customer group by cust_name");
        List<Object[]> res = query.list();
        for (Object[] item : res) {
            System.out.println(Arrays.toString(item));
        }
    }

    /*
     * HQL: 多表查询(连接查询)
     * 分为内连接, 左连接和右连接, 此处以内连接举例
     * 每种连接查询分为普通连接和迫切连接.
     * 这里是普通内连接
     */
    @Test
    public void test12() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("from Customer c inner join c.linkmen");
        List<Object[]> res = query.list();
        for (Object[] item : res) {
            System.out.println(Arrays.toString(item));
        }
    }

    /*
     * HQL: 多表查询(连接查询)
     * 迫切内连接: 会自动的封装成对象
     */
    @Test
    public void test13() {
        Session session = HibernateUtil.openSession();
        Query query = session.createQuery("from Customer c inner join fetch c.linkmen");
        List<Customer> res = query.list();
        for (Customer c : res) {
            System.out.println(c);
        }
    }
}
