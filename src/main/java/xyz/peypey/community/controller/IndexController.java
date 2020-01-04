package xyz.peypey.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.peypey.community.dto.PaginationDTO;
import xyz.peypey.community.dto.QuestionDTO;
import xyz.peypey.community.mapper.QuestionMapper;
import xyz.peypey.community.mapper.UserMapper;
import xyz.peypey.community.model.Question;
import xyz.peypey.community.model.User;
import xyz.peypey.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;

      @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "2") Integer size
                            ){
          Cookie[] cookies = request.getCookies();//读取cookie 判断是否有用户登录信息
          if(cookies!=null&&cookies.length!=0){
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
          PaginationDTO list = questionService.list(page,size);
          model.addAttribute("pagination",list);
          return "index";
      }
}
