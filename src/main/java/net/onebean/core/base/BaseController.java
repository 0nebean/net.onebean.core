package net.onebean.core.base;


import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class BaseController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Timestamp.class,
                new CustomDateEditor(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                        true    //true:允许输入空值，false:不能为空值
                )
        );
    }
}