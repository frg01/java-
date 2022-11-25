package com.hspedu.mhl.service;

import com.hspedu.mhl.dao.BillDAO;
import com.hspedu.mhl.dao.DiningTableDAO;
import com.hspedu.mhl.dao.MultiTableBeanDAO;
import com.hspedu.mhl.domain.Bill;
import com.hspedu.mhl.domain.Menu;
import com.hspedu.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 处理和长的那相关的业务逻辑
 */
public class BillService {
    //定义BillDAO属性
    private BillDAO billDAO = new BillDAO();
    //定义MultiTableBeanDAO属性
    private MultiTableBeanDAO multiTableBeanDAO = new MultiTableBeanDAO();
    //定义MenuService 属性
    private MenuService menuService = new MenuService();
    //定义DiningTableService
    private DiningTableService diningTableService = new DiningTableService();

    //编写点餐方法
    //生成账单，更新对应餐桌的状态,成功返回ture ,否则false
    public boolean orderMenu(int menuId,int nums,int diningTableId){
        //生成一个账单号 ，UUID
        String billId = UUID.randomUUID().toString();

        //将账单生成到bill表   要求直接计算出长的那的金额
        int update = billDAO.update("insert into bill values(null,?,?,?,?,?,now(),'未结账')",
                billId, menuId, nums, menuService.getMenuById(menuId).getPrice(), diningTableId);
        if (update <= 0){
            return false;
        }
        //更新对应餐桌的状态
        return diningTableService.updateDiningTable(diningTableId, "就餐中");
    }

    //返回账单
    public List<MultiTableBean> list(){
        return multiTableBeanDAO.queryMulti("SELECT bill.* ,NAME " +
                "FROM bill,menu " +
                "WHERE bill.menuId = menu.id;" ,MultiTableBean.class);
    }

    //查看某个餐桌是否有未结账的账单
    public boolean hasPayBillByDiningTableId(int diningTableId){
        Bill bill = billDAO.querySingle("select * from bill where diningTableId = ? and state = '未结账'", Bill.class, diningTableId);
        return bill != null;
    }

    //完成结账，餐桌存在且有未结账的情况
    public boolean payBill(int diningTableId,String payMode){
        //如果这里使用事务的话，需要用ThreadLocal来解决 ，框架中比如mybatis 提供了事务
        //1.修改bill表
        int update = billDAO.update("update bill set state = ? where diningTableId = ? and state = '未结账'",payMode,diningTableId);
        if (update <= 0){//没有更新成功，则表示失败
            return false;
        }

        //调用DiningTableService的方法 修改diningTable表
        if (!diningTableService.updateDiningTableToFree(diningTableId,"空")){
            return false;
        }
        return true;
    }



}
