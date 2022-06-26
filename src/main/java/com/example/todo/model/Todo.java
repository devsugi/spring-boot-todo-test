package com.example.todo.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
  private Integer id;
  private String content;
  private Boolean done;
  private Date createdAt;
  private Date updatedAt;
}
