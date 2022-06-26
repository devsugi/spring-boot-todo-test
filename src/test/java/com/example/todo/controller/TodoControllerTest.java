package com.example.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.result.XpathResultMatchers;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.fail;
// import static org.mockito.Answers.valueOf;
// import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
// import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

// import java.sql.Date;
import java.util.List;



@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
  "spring.datasource.url=jdbc:h2:mem:h2db;DB_CLOSE_ON_EXIT=TRUE" })
public class TodoControllerTest {
  
  @Autowired
  MockMvc mockMvc;

  @Autowired
  TodoRepository todoRepository;

  @Test
  public void todo一覧の確認() throws Exception {

    mockMvc.perform(get("/todo"))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(view().name("todo"))
    .andExpect(model().hasNoErrors())

    // ヘッダー行の確認
    .andExpect(xpath("//table/thead/tr/th[1]").string("ID"))
    .andExpect(xpath("//table/thead/tr/th[2]").string("項目"))
    .andExpect(xpath("//table/thead/tr/th[3]").string("完了"))
    .andExpect(xpath("//table/thead/tr/th[4]").string("作成日"))
    .andExpect(xpath("//table/thead/tr/th[5]").string("更新日"))
    // 1行目のcontentはhello
    .andExpect(xpath("//table/tbody/tr[1]/td[2]").string("hello"))
    // 2行目のcontentはworld
    // .andExpect(xpath("//table/tbody/tr[2]/td[2]").string("world"))
    ;
  }

  @Test
  public void 編集画面の確認() throws Exception {

    mockMvc.perform(get("/todo/edit/1"))
    // .andDo(print())
    .andExpect(status().isOk())
    .andExpect(view().name("edit"))
    .andExpect(model().hasNoErrors())

    .andExpect(xpath("//form/div[1]/lavel").string("ID"))
    // contentはhello
    .andExpect(xpath("//input[@name='content']/@value").string("hello"))
    // done(チェックボックス)はfalse
    .andExpect(xpath("//input[@name='done']").booleanValue(false))
    ;
  }

  @Test
  public void 新規登録画面の確認() throws Exception {
    
    mockMvc.perform(get("/todo/add"))
    .andExpect(status().isOk())
    .andExpect(view().name("add"))
    .andExpect(model().hasNoErrors())
    // IDは空
    .andExpect(xpath("//input[@name='id']/@value").string(""))
    // contentは空
    .andExpect(xpath("//input[@name='content']/@value").string(""))
    // done(チェックボックス)はfalse
    .andExpect(xpath("//input[@name='done']").booleanValue(false))
    // 作成日は空
    .andExpect(xpath("//input[@name='createdAt']/@value").string(""))
    // 更新日は空
    .andExpect(xpath("//input[@name='updatedAt']/@value").string(""))
    ;
  }

  @Test
  public void 新規登録の確認() throws Exception {

    mockMvc.perform(
      post("/todo/add")
      .param("ID", "")
      .param("content", "newcontent")
      .param("done", "false")
    )
    
    .andExpect(status().isOk())
    .andExpect(forwardedUrl("/todo/edit/3"))
    .andExpect(model().hasNoErrors())
    .andDo(print())
    ;
  }

  @Test
  public void 削除の確認() throws Exception {

    String deleteId = "2";
    List<Todo> todoList = todoRepository.findAll();
    int beforeDataSize = todoList.size();

    mockMvc.perform(post("/todo/delete/{id}", deleteId))
      .andExpect(status().isOk())
      .andExpect(forwardedUrl("/todo"))
      .andExpect(model().hasNoErrors())
    ;

    todoList = todoRepository.findAll();
    assertTrue(todoList.size() == (beforeDataSize-1));
  }
}
