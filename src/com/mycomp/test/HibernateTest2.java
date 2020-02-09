package com.mycomp.test;

/*
 * Hibernate一级缓存
 */

import com.mycomp.domain.Customer;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.io.Serializable;

public class HibernateTest2 {

    @Test
    public void cacheTest1() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 此时会自动的把这个数据存到一级缓存中
        Customer customer1 = session.get(Customer.class, 9L);

        // 从缓存中取(从缓存中取得值,不会生成sql语句)
        Customer customer2 = session.get(Customer.class, 9L);

        System.out.println(customer1 == customer2);

        transaction.commit();
        // session.close();
    }

    @Test
    public void cacheTest2() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("zzqCache");
        // 此时, 会把对象先存到一级缓存里边
        Serializable id = session.save(customer);

        // 从缓存中取
        Customer customer1 = session.get(Customer.class, id);

        System.out.println(customer == customer1);

        transaction.commit();
        // session.close();
    }

    /*
     * 持久态对象的自动更新:
     * 当使用Id进行数据库查询时, 会把查询到的数据放到一级缓存中, 同时, 也会放到快照区中.
     * 当你对获取的对象进行修改时, 你修改的是缓存区中的对象.
     * 当时用transaction.commit()时, 同时会清理一级缓存.
     * 当清理一级缓存时, 会对比快照区和一级缓存中的对象(使用OID定位)
     * 如果两个对象中的属性发生变化, 会执行update语句, 此时更新数据库, 同时也会更新快照区的数据使其与缓冲区同步.
     */
    @Test
    public void autoUpdateTest() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, 9L);
        customer.setCust_name("zzqAuto");
        transaction.commit();
        // session.close();
    }

}
