package com.mycomp.test;

/*
 * 一对多关系的维护
 */

import com.mycomp.domain.Customer;
import com.mycomp.domain.Linkman;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class HibernateTest3 {

    @Test
    public void addTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // 创建对象
        Customer customer1 = new Customer();
        customer1.setCust_name("customer1");
        Customer customer2 = new Customer();
        customer2.setCust_name("customer2");

        Linkman linkman1 = new Linkman();
        linkman1.setLink_name("linkman1");
        Linkman linkman2 = new Linkman();
        linkman2.setLink_name("linkman2");
        Linkman linkman3 = new Linkman();
        linkman3.setLink_name("linkman3");

        // 配置关系 双向维护
        customer1.getLinkmen().add(linkman1);
        customer1.getLinkmen().add(linkman2);
        customer2.getLinkmen().add(linkman3);

        // 不设置以下这些, 就是单向维护
        // 一般在开发当中, 都使用双向维护
        linkman1.setCustomer(customer1);
        linkman2.setCustomer(customer1);
        linkman3.setCustomer(customer2);

        // 保存
        currentSession.save(customer1);
        currentSession.save(customer2);

        // 如果不保存以下这些, 会报异常
        // org.hibernate.TransientObjectException 瞬时对象异常
        // 意思是, 一个持久态的对象关联了一个瞬时态的对象
        // 所以, 关系的双方都需要保存
        // 或者, 可以配置支持级联保存或更新, 就不需要双方多保存了
        // 详见Customer.hbm.xml中的<set name="linkmen" cascade="save-update">
        // 级联的配置是单向的
        // 比如在customer的配置文件中配置了对linkman的级联保存, 这里就不需要保存linkman了
        // 但如果只保存linkman不保存customer, 依然会报错
        // 如果不想报错, 还得去linkman的配置文件中配置其对customer的级联保存
        // currentSession.save(linkman1);
        // currentSession.save(linkman2);
        // currentSession.save(linkman3);

        transaction.commit();
    }

    @Test
    public void getLinkmanTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        Linkman linkman = currentSession.get(Linkman.class, 24L);
        System.out.println(linkman.getCustomer());

        transaction.commit();
    }

    @Test
    public void getCustomerTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        Customer customer = currentSession.get(Customer.class, 20L);
        System.out.println(customer);

        transaction.commit();
    }

    @Test
    public void deleteTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // 默认删除
        // 它先去打断两条关联的记录之间的关系 (set link_cust_id = null)
        // 再去删除customer记录 (并没有级联删除)
        // Customer customer = currentSession.get(Customer.class, 20L);
        // currentSession.delete(customer);

        // 级联删除
        // 需要去配置
        // 详见Customer.hbm.xml中的<set name="linkmen" cascade="save-update, delete">
        Customer customer = currentSession.get(Customer.class, 1L);
        currentSession.delete(customer);

        transaction.commit();
    }

    @Test
    public void updateTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        Linkman linkman = currentSession.get(Linkman.class, 3L);
        Customer customer = currentSession.get(Customer.class, 2L);
        // 同add, 单向维护和双向维护都可以
        linkman.setCustomer(customer);
        customer.getLinkmen().add(linkman);
        // 但是双向维护会有性能问题 (会产生两条修改同一个地方的sql语句)

        // 所以, 在更新时, 在使用双向维护时, 我们要让其中一方放弃维护 (一般是让含有外键的那个类/表去维护)
        // 需要去配置, 在放弃维护的那个类的配置文件里
        // 参照customer.hbm.xml中的<set name="linkmen" cascade="save-update, delete" inverse="true">
        // 但是当配置了放弃维护时, 在更新关系时, 如果使用单向维护, 只能去让另一方去set
        // 例如, 在这个例子中, 如果customer设置了放弃维护, 在更新关系时, 如果使用单向维护, 只能linkman.setCustomer(customer)
        // 如果使用customer.getLinkmen().add(linkman), 数据库中的数据其实并没有更新(因为customer放弃了维护关系)

        // 总结一下就是, 在更新操作时, 我们一般使用双向维护, 并且让一对多关系中的"一"的那一方放弃维护外键

        transaction.commit();
    }

}
