package shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * @author FaceFeel
 * @Created 2018-01-26 13:11
 */

public class ShiroFilter extends AuthorizingRealm {

    /**
     * 为当前登录的Subject授予角色和权限
     * 经测试:本例中该方法的调用时机为需授权资源被访问时
     * 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
     * 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
     * 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取当前登录的用户名
        String userName = (String) super.getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();

        if (null != userName && "zhangsna".equals(userName)) {
            //添加一个角色，不是配置意义上的添加，而是证明该用户具有Admin角色
            authenticationInfo.addRole("admin");
            //添加权限
            authenticationInfo.addStringPermission("admin:manage");
            System.out.println("已为用户[papio]赋予了[admin]角色和[admin:manage]权限");
            return authenticationInfo;
        } else if (null != userName && "big".equals(userName)) {

            System.out.println("当前用户[" + userName + "]无授权");
            return authenticationInfo;
        }
        //若该方法什么都不做直接返回null的话,就会导致任何用户访问/admin/listUser.jsp时都会自动跳转到unauthorizedUrl指定的地址
        //详见applicationContext.xml中的<bean id="shiroFilter">的配置
        return null;
    }

    /**
     * 验证当前登录的Subject
     * 经测试:本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //获取基于用户名和密码的令牌
//        实际上，这个authenticationToken是由登陆类里面的currentUser.login（token）传过来的，两个token的引用是一样的
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        System.out.println("验证当前Subject时获取到token为" + token);

        //此处无需比对,比对的逻辑Shiro会做,我们只需返回一个和令牌相关的正确的验证信息
        //说白了就是第一个参数填登录用户名,第二个参数填合法的登录密码(可以是从数据库中取到的,本例中为了演示就硬编码了)
        //这样一来,在随后的登录页面上就只有这里指定的用户和密码才能通过验证

        if (null != token && "zhangsan".equals(token.getUsername())) {
            SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo("zhangsan", "zhangsan", this.getName());
            this.setSession("currentUser", "zhangsan");
            return authorizationInfo;
        } else if ("big".equals(token.getUsername())) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo("big", "big", this.getName());
            this.setSession("currentUser", "big");
            return authcInfo;
        }
        //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
        return null;
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    private void setSession(Object key, Object value) {

        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            Session session = subject.getSession();
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            session.setAttribute(key, value);
        }
    }
}
