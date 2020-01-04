package xyz.peypey.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.peypey.community.dto.PaginationDTO;
import xyz.peypey.community.dto.QuestionDTO;
import xyz.peypey.community.mapper.QuestionMapper;
import xyz.peypey.community.mapper.UserMapper;
import xyz.peypey.community.model.Question;
import xyz.peypey.community.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pey
 * @date 2019/12/30
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);
        if (page<1){
            page=1;
        }

        if (page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }


        Integer offset=size*(page-1);

        List<QuestionDTO> list=new ArrayList<>();
        List<Question> questions = questionMapper.list(offset,size);

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            list.add(questionDTO);
        }
        paginationDTO.setQuestions(list);






        return paginationDTO;
    }

}
