package com.mycomp.test;

import com.mycomp.domain.Customer;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/*
 * Hibernate常用接口, 以及持久化
 */

public class HibernateTest {

    @Test
    public void addOneCustomerTest() {
        // 通过SessionFactory获取到Session对象(封装进了HibernateUtil工具类中), 类似于JDBC中的connection
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 编写代码
        Customer customer = new Customer();
        customer.setCust_name("zzq");
        customer.setCust_level("2");

        // 保存到数据库中
        session.save(customer);

        transaction.commit();
        // 释放资源
        // session.close(); // 使用getCurrentSession()获得的session可以不去close(), Hibernate内部帮你去关闭
        // 如果整个应用程序关闭了或不需要用sessionFactory了, 才关闭它
        // HibernateUtil.sessionFactory.close();
    }

    @Test
    public void getOneCustomerTest() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, 1L); // 通过Hibernate获取的对象, 会自动进入持久态
        System.out.println(customer);
        transaction.commit();
        // session.close();
    }

    @Test
    public void updateOneCustomerTestTypeOne() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();

        // 开启事务
        Transaction transaction = session.beginTransaction();

        // 更新, 方式一:
        // 直接创建一个新的customer, 设置其值, 然后update
        Customer customer = new Customer(); // 瞬时态对象, 如果后面没有Hibernate的相关操作, 它之后会被垃圾回收
        // 必须要设置id
        customer.setCust_id(1L);
        // 如果没有指定其他字段, 其他字段会设置为null
        customer.setCust_name("zzq_update");
        session.update(customer); // 持久态对象
        // 持久态对象能够自动更新数据库
        customer.setCust_name("zzq_持久态更新");

        // 如果使用c3p0, 必须手动提交事务(transaction)
        // 提交事务
        transaction.commit();
        // session.close();
        // session关闭之后, customer就不是持久态了, 现在是游离态
        // 处于游离态的对象, 之后也会被垃圾回收
    }

    @Test
    public void updateOneCustomerTestTypeTwo() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 更新, 方式二:
        // 先从数据库中查出要更新的数据, 再更新
        // 以这种方式update, 其余未设置的字段就不会更新为null
        Customer customer = session.get(Customer.class, 6L);
        customer.setCust_name("zzq233");
        session.update(customer);

        transaction.commit();
        // session.close();
    }

    @Test
    public void deleteOneCustomerTestTypeOne() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 删除, 方式一:
        // 直接创建新的customer对象, 然后删除
        // 此方式不支持级联删除
        Customer customer = new Customer();
        customer.setCust_id(13L);
        session.delete(customer);

        transaction.commit();
        // session.close();
    }

    @Test
    public void deleteOneCustomerTestTypeTwo() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 删除, 方式二:
        // 先查询, 再删除
        // 此方式支持级联删除
        Customer customer = session.get(Customer.class, 12L);
        session.delete(customer);

        transaction.commit();
        // session.close();
    }

    @Test
    public void saveOrUpdateTest() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // saveOrUpdate()方法
        // 如果没有设置id, 是添加操作
        // 如果设置了id, 是更新操作. 但如果设置的id在数据库中没有, 会报错
        Customer customer = new Customer();
        // customer.setCust_id(9L);
        customer.setCust_name("hydraGo233");
        customer.setCust_level("1001");
        session.saveOrUpdate(customer);

        transaction.commit();
        // session.close();
    }

    @Test
    public void getAllCustomerTestTypeOne() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 查询所有, 方式一:
        // 使用HQL
        Query query = session.createQuery("from Customer");
        List<Customer> list = query.list();
        for (Customer c : list) {
            System.out.println(c);
        }

        transaction.commit();
        // session.close();
    }

    @Test
    public void getAllCustomerTestTypeTwo() {
        // Session session = HibernateUtil.openSession();
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 查询所有, 方式二:
        // 使用原生SQL(过时了)
        NativeQuery sqlQuery = session.createSQLQuery("select * from customer");
        // 它给的是数组, 需要自己封装成对象
        List<Object[]> list = sqlQuery.list();
        for (Object[] c : list) {
            System.out.println(Arrays.toString(c));
        }

        transaction.commit();
        // session.close();
    }
}
