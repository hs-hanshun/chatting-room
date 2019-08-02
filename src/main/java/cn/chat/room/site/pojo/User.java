package cn.chat.room.site.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private Date created_date;
}