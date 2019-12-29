package xyz.peypey.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.peypey.community.mapper.QuestionMapper;
import xyz.peypey.community.mapper.UserMapper;
import xyz.peypey.community.model.Question;
import xyz.peypey.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

      @GetMapping("/publish")
    public String publish(){
          return "publish";
      }

      @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            HttpServletRequest request,
            Model model){
          model.addAttribute("title",title);
          model.addAttribute("description",description);
          model.addAttribute("tag",tag);//保存填写的问题
          if (title==null||title==""){//判空
              model.addAttribute("error", "标题不能为空");
              return "publish";
          }
          if (description==null||description==""){
              model.addAttribute("error","内容不能为空");
              return "publish";
          }
          if(tag==null||tag==""){
              model.addAttribute("error", "标签不能为空");
              return "publish";
          }
          User user=null;
          Cookie[] cookies = request.getCookies();//读取cookie 判断是否有用户登录信息
          if(cookies!=null) {
              for (Cookie cookie : cookies) {
                  if ("token".equals(cookie.getName())) {
                      String token = cookie.getValue();
                      user = userMapper.findByToken(token);
                      if (user != null) {
                          request.getSession().setAttribute("user", user);//写入session
                      }
                      break;
                  }
              }
          }
          if (user==null){
              model.addAttribute("error","用户未登录");
              return "publish";
          }
          //存问题
          Question question=new Question();
          question.setTitle(title);
          question.setDescription(description);
          question.setTag(tag);
          question.setCreator(user.getId());
          question.setGmtCreate(System.currentTimeMillis());
          question.setGmtModified(question.getGmtCreate());
          questionMapper.create(question);
          return "redirect:/";
      }
}
