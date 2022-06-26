package com.example.todo.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;

@Controller
public class TodoController {
  
  @Autowired
  TodoRepository todoRepository;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
      // bind empty strings as null
      binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @RequestMapping({"/todo", "/"})
  public String todo(Model model) {
    
    List<Todo> todoList = todoRepository.findAll();
    model.addAttribute("todoList", todoList);
    
    return "todo";
  }

  @GetMapping("/todo/add")
  public String add() {
    return "add";
  }

  @PostMapping("/todo/add")
  public String add(@ModelAttribute Todo todoForm, Model model) {

    int max = todoRepository.getMaxcount() + 1;
    Date now = new Date(System.currentTimeMillis());

    todoForm.setId(max);
    todoForm.setCreatedAt(now);
    todoForm.setUpdatedAt(now);
    todoRepository.add(todoForm);
    model.addAttribute("resultMessage", "更新しました。");
    
    return "forward:/todo/edit/" + todoForm.getId();
  }

  @RequestMapping("/todo/edit/{id}")
  public String edit(@PathVariable("id") int id, Model model) {
    
    Todo todo = todoRepository.selectOne(id);
    model.addAttribute("todo", todo);

    return "edit";
  }

  @PostMapping("/todo/update")
  public String update(@ModelAttribute Todo todoForm, Model model) {
        
    Date now = new Date(System.currentTimeMillis());

    todoForm.setUpdatedAt(now);
    todoRepository.update(todoForm);
    model.addAttribute("resultMessage", "更新しました。");

    return "forward:/todo/edit/" + todoForm.getId();
  }

  @PostMapping("/todo/delete/{id}")
  public String delete(@PathVariable("id") int id, Model model) {

    todoRepository.delete(id);
    model.addAttribute("resultMessage", "削除しました。");

    return "forward:/todo";
  }
}
