package com.example.todo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.example.todo.model.Todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
  "spring.datasource.url=jdbc:h2:mem:h2db;DB_CLOSE_ON_EXIT=TRUE" })
// @SpringBootTest(classes = TodoApplication.class)
public class TodoRepositoryTest {

  @Autowired
  TodoRepository todoRepository;
  
  @Test
  public void 全件取得の確認() {

    List<Todo> todoList = todoRepository.findAll();
    assertThat(todoList.size() == 2);
  }

  @Test
  public void 単体取得の確認() {

    int checkId = 1;
    String checkContent = "hello";
    Boolean checkDone = false;

    Todo todo = todoRepository.selectOne(checkId);
    // IDが一致している
    assertThat(todo.getId() == checkId);
    // contentの内容が一致している
    assertThat(todo.getContent().equals(checkContent));
    // doneが一致している
    assertThat(todo.getDone() == checkDone);
  }
  
  @Test
  public void データ追加の確認() {
    
    int beforeCount = 2;
    int afterCount = 3;
    int checkId = 3;
    String checkContent = "!!";
    Boolean checkDone = false;
    Date now = new Date(System.currentTimeMillis());

    // 現状確認
    List<Todo> beforeTodoList = todoRepository.findAll();
    assertThat(beforeTodoList.size() == beforeCount);

    // 追加確認
    Todo todo = new Todo(checkId, checkContent, checkDone, now, now);
    Boolean result = todoRepository.add(todo);
    assertThat(result == true);
    
    List<Todo> afterTodoList = todoRepository.findAll();
    // 1レコード増えて全部で3レコードになっている
    assertThat(afterTodoList.size() == afterCount);

    Todo nowTodo = afterTodoList.get(afterCount-1);

    // contentの内容が一致している
    assertThat(nowTodo.getContent().equals(checkContent));
    // doneが一致している
    assertThat(nowTodo.getDone().equals(checkDone));
    // 作成日が一致している
    assertThat(nowTodo.getCreatedAt().equals(now));
    // 更新日が一致している
    assertThat(nowTodo.getUpdatedAt().equals(now));
  }

  @Test
  public void データ更新の確認() {
    
    int checkId = 2;
    String content = "!!";
    Date now = new Date(System.currentTimeMillis());
    
    Todo todo = todoRepository.selectOne(checkId);
    todo.setContent(content);
    todo.setUpdatedAt(now);
    
    Boolean result = todoRepository.update(todo);
    assertThat(result == true);

    Todo newTodo = todoRepository.selectOne(checkId);
    // contentの内容が更新されている
    assertThat(newTodo.getContent().equals(content));
    // 更新日が更新されている
    assertThat(newTodo.getUpdatedAt().equals(now));
    // doneは更新されていない
    assertThat(newTodo.getDone() == todo.getDone());
    // 作成日は更新されていない
    assertThat(newTodo.getCreatedAt().equals(todo.getCreatedAt()));
  }

  @Test
  public void データ削除の確認() {
    
    int beforeCount = 3;
    int afterCount = 2;

    List<Todo> todoList = todoRepository.findAll();
    assertThat(todoList.size() == 3);

    Boolean result = todoRepository.delete(beforeCount);
    assertThat(result == true);

    todoList = todoRepository.findAll();
    assertThat(todoList.size() == afterCount);
  }

  @Test
  public void MAXIDの確認() {

    int maxId = 2;

    int max = todoRepository.getMaxcount();
    assertThat(max == maxId);
  }
}
