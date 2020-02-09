package com.mycomp.test;

/*
 * 多对多关系的维护
 */

import com.mycomp.domain.Role;
import com.mycomp.domain.User;
import com.mycomp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Set;

public class HibernateTest4 {

    @Test
    public void addTest() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // 创建user
        User user1 = new User();
        user1.setUser_name("user1");
        User user2 = new User();
        user2.setUser_name("user2");

        // 创建role
        Role role1 = new Role();
        role1.setRole_name("role1");
        Role role2 = new Role();
        role2.setRole_name("role2");
        Role role3 = new Role();
        role3.setRole_name("role3");

        // 配置关系
        // 单向维护 (也可以双向维护)
        user1.getRoles().add(role1);
        user1.getRoles().add(role2);
        user2.getRoles().add(role2);
        user2.getRoles().add(role3);

        // 如果要使用双向维护, 必须得要有一方放弃外键维护权
        // 因为如果双方都去维护, 那在双方都保存的时候, 会往中间表插入两条一模一样的数据, 而这是不允许的
        // 到底是谁放弃呢?
        // 一般是被动的一方放弃
        // 在这个例子中, role放弃维护权 (inverse="true")
        role1.getUsers().add(user1);
        role2.getUsers().add(user1);
        role2.getUsers().add(user2);
        role3.getUsers().add(user2);

        // 保存
        currentSession.save(user1);
        currentSession.save(user2);
        // 没有配置级联保存或更新, 双方都必须保存
        // 要保存谁, 就到谁中去做级联
        // currentSession.save(role1);
        // currentSession.save(role2);
        // currentSession.save(role3);

        transaction.commit();
    }

    @Test
    public void getUser() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        User user = currentSession.get(User.class, 13L);
        Set<Role> roles = user.getRoles();
        System.out.println(roles);

        transaction.commit();
    }

    /*
     * 在多对多中, 关系的操作其实就是操作对象中的集合
     */
    @Test
    public void relationshipTest1() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // 给用户添加一个新角色(该角色已在数据库中)
        User user = currentSession.get(User.class, 13L);
        Role role = currentSession.get(Role.class, 14L);
        // 可以单向维护或双向维护, 但是使用双向维护的前提是有一方已经放弃双向维护(参考addTest())
        user.getRoles().add(role);
        role.getUsers().add(user);

        transaction.commit();
    }

    @Test
    public void relationshipTest2() {
        Session currentSession = HibernateUtil.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        // 更改用户一个已有的权限
        User user = currentSession.get(User.class, 14L);
        Role role_new = currentSession.get(Role.class, 13L);
        Role role_old = currentSession.get(Role.class, 12L);

        user.getRoles().remove(role_old);
        user.getRoles().add(role_new);

        transaction.commit();
    }

}
