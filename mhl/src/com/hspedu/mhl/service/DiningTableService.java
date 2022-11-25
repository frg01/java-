package com.hspedu.mhl.service;

import com.hspedu.mhl.dao.DiningTableDAO;
import com.hspedu.mhl.domain.DiningTable;

import java.util.List;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class DiningTableService {
    //先定义一个DiningTableDAO
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    public List<DiningTable> list(){
        return diningTableDAO.queryMulti("select id , state from diningTable",
                DiningTable.class);
    }
    //根据id查询对应的DiningTable对象
    //返回null， 表示对应id餐桌不存在
    public DiningTable getDiningTableId(int id){
        return diningTableDAO.querySingle("select * from diningTable where id = ?",DiningTable.class,id);
    }
    // 餐桌存在并且空闲，进行预定状态更新（还有人名，电话）
    public boolean orderDiningTable(int id ,String orderName,String orderTel){
        int update =
                diningTableDAO.update("update diningTable set state = '已预定',orderName = ?,orderTel = ? where id = ?",
                                        orderName, orderTel,id);
        return update > 0;
    }
    //需要提供一个更新 餐桌状态的方法
    public boolean updateDiningTable(int id,String state){
        int update =
                diningTableDAO.update("update diningTable set state = ? where id = ?", state, id);
        return update > 0;
    }
    //修改diningTable表,将指定餐桌设置为空闲状态
    public boolean updateDiningTableToFree(int id,String state){
        int update =
                diningTableDAO.update("update diningTable set state = ? ,orderName = ?,orderTel = ? where id = ?", state,"","", id);
        return update > 0;
    }
}
