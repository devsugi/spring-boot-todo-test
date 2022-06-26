package com.example.todo.repository;

import java.util.List;

import com.example.todo.model.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepository {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Todo> findAll() {

    return jdbcTemplate.query(
        "SELECT * FROM todo ORDER BY id",
        new BeanPropertyRowMapper<Todo>(Todo.class));
  }

  public Todo selectOne(int id) {
  
    return jdbcTemplate.queryForObject(
            "SELECT * FROM todo WHERE id = ?",
            new BeanPropertyRowMapper<Todo>(Todo.class),
            id);
  }

  public Integer getMaxcount() {
    return jdbcTemplate.queryForObject(
          "SELECT max(id) FROM todo",  Integer.class);
  }

  public Boolean add(Todo todo) {
    
    int count = jdbcTemplate.update(
                      "INSERT INTO todo"
                      + "(id, content, done, created_at, updated_at) "
                      + "Values(?, ?, ?, ?, ?)",
                      todo.getId(),
                      todo.getContent(),
                      todo.getDone(),
                      todo.getCreatedAt().toString(),
                      todo.getUpdatedAt().toString());

    return (count == 1)? true: false;
  }

  public Boolean update(Todo todo) {
    
    int count = jdbcTemplate.update(
                        "UPDATE todo SET "
                        + "content=?, done=?, updated_at=? "
                        + "where id=?",
                        todo.getContent(),
                        todo.getDone(),
                        todo.getUpdatedAt().toString(),
                        todo.getId());

      return (count == 1)? true: false;
  }

  public Boolean delete(int id) {

    int count = jdbcTemplate.update("DELETE FROM todo WHERE id = ?", id);
    return (count == 1)? true: false;
  }
}
