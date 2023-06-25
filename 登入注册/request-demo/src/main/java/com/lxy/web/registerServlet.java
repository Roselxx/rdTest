package com.lxy.web;

import com.lxy.mapper.UserMapper;
import com.lxy.pojo.User;
import com.lxy.util.SqlSessionFactoryUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收用户数据
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        //封装用户对象
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);

        //2.调用mapper
        //2.1获取SqlsessionFactory
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSessionFactory sqlSessionFactory= SqlSessionFactoryUtils.getSqlSessionFactory();
        //2.2获取SqlSession对象
        SqlSession sqlSession=sqlSessionFactory.openSession();

        //2.3获取Mapper
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);

        //2.4调用方法
        User u=userMapper.selectByUsername(username);

        if(u==null){
            //用户名不存在添加用户
            userMapper.add(user);

            //提交事务
            sqlSession.commit();

            //释放资源
            sqlSession.close();
        }else{
            //存在
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("用户名已存在");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}
