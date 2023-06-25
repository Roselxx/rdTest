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
import java.io.PrintWriter;

@WebServlet( "/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收用户名和密码
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        //2调用mybatis完成查询
        //2.1获取SqlSessionFactory对象
//         String resource = "mybatis-config.xml";
//         InputStream inputStream = Resources.getResourceAsStream(resource);
//         SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSessionFactory sqlSessionFactory= SqlSessionFactoryUtils.getSqlSessionFactory();
         //2.2获取SqlSession
        SqlSession sqlSession=sqlSessionFactory.openSession();
        //2.3获取Mapper
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);

        //2.4调用方法
        User user=userMapper.select(username,password);
        //2.5释放资源
        sqlSession.close();

        //防止乱码
        response.setContentType("text/html;charset=utf-8");
        //获取字符流输出
        PrintWriter writer=response.getWriter();
        //3.判断user是否为null
        if(user!=null){
            writer.write("登入成功");
        }else{
            writer.write("登入失败");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}
