package com.mycomp.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory sessionFactory;

    static {
        // 1. 加载Hibernate核心配置文件
        Configuration configuration = new Configuration().configure();
        // 2. 创建一个SessionFactory对象, 类似于JDBC中的连接池
        sessionFactory = configuration.buildSessionFactory(); // 这个东西一个工程只需要一个就行了
    }

    /*
     * 从ThreadLocal中获取session.
     * 需要在核心配置文件中开启才可以使用(默认是关闭的):
     * <property name="current_session_context_class">thread</property>
     * 使用此方法获得的session可以不去close(), Hibernate会自动帮你关闭.
     */
    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /*
     * 在开发当中处理事务时, service层中的方法以及Dao层中的多个处理(一个事务可能包含多个处理)需要使用同一个session.
     * 所以就不能使用以下的方式获取session了.
     * 需要使用getCurrentSession()的方式从ThreadLocal中获取线程.
     */
    public static Session openSession() {
        return sessionFactory.openSession();
    }
}
