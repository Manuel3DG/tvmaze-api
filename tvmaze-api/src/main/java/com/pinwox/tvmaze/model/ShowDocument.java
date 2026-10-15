package com.pinwox.tvmaze.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shows")
public class ShowDocument {

    @Id
    private Long id;
    private Object data;

    public ShowDocument() {
    }

    public ShowDocument(Long id, Object data) {
        this.id = id;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
