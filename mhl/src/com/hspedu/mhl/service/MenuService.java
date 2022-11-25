package com.hspedu.mhl.service;

import com.hspedu.mhl.dao.MenuDAO;
import com.hspedu.mhl.domain.Menu;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 完成对menu表的各种操作(通过调用MenuDAO)
 */
public class MenuService {
    //定义一个MenuDAO
    private MenuDAO menuDAO = new MenuDAO();

    //返回所有餐品 ，给界面使用
    public List<Menu> list(){
        return menuDAO.queryMulti("select * from menu",Menu.class);
    }
    //根据menu的菜品id 返回Menu对象
    public Menu getMenuById(int id){
        return menuDAO.querySingle("select * from menu where id = ?",Menu.class,id);
    }
}
