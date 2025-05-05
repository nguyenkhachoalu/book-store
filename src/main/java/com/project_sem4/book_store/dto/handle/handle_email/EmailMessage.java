package com.project_sem4.book_store.dto.handle.handle_email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private List<String> to;
    private String subject;
    private String content;
}