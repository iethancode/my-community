package xyz.peypey.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.peypey.community.mapper.UserMapper;
import xyz.peypey.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
      @GetMapping("/")
    public String index(HttpServletRequest request){
          Cookie[] cookies = request.getCookies();//读取cookie 判断是否有用户登录信息
          if(cookies!=null){
              for (Cookie cookie : cookies) {
                  if ("token".equals(cookie.getName())){
                      String token = cookie.getValue();
                      User user = userMapper.findByToken(token);
                      if (user!=null){
                          request.getSession().setAttribute("user",user);//写入session
                      }
                      break;
                  }
              }

          }
          return "index";
      }
}
