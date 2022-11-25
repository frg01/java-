package com.hspedu.mhl.view;

import com.alibaba.druid.support.json.JSONUtils;
import com.hspedu.mhl.domain.*;
import com.hspedu.mhl.service.BillService;
import com.hspedu.mhl.service.DiningTableService;
import com.hspedu.mhl.service.EmployeeService;
import com.hspedu.mhl.service.MenuService;
import com.hspedu.mhl.utils.Utility;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.List;

/**
 * @author: guorui fu
 * @versiion: 1.0
 */
public class MHLView {
    //控制是否退出菜单
    private boolean loop = true;
    private String key = "";//接收用户输入
    //定义EmployeeService 属性
    private EmployeeService employeeService = new EmployeeService();
    //定义DiningTableService 属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义Billservice属性
    private BillService billService = new BillService();

    public static void main(String[] args) {
        MHLView mhlView = new MHLView();
        mhlView.menu();
    }

    //登录用户管理
    public void manageEmoloyee(){
        //显示员工表
        List<Employee> employees = employeeService.allEmployee();
        for (Employee employee : employees){
            System.out.println(employee);
        }
        //从选项输入如何操作员工员工表
        System.out.println("=========员工表操作选项==========");
        System.out.println("\t\t 1 删除一个员工");
        System.out.println("\t\t 2 修改某员工密码");
        System.out.println("\t\t 3 增 加 员 工");
        System.out.println("请输入选择（-1为退出）：");
        String key = Utility.readString(1);
        if (key.equals("-1")){
            return;
        }
        switch(key){
            case "1":
                System.out.println("请输入要删除的员工employeeId");
                String employeeId = Utility.readString(32);
                if (!employeeService.deleteEmployee(employeeId)){
                    System.out.println("抱歉，删除失败");
                    return;
                }
                System.out.println("删除成功");
                break;
            case "2":
                System.out.println("请输入要修改的员工employeeId");
                String employeeId2 = Utility.readString(32);
                System.out.println("请输入新密码：");
                String newPwd = Utility.readString(50);
                if(!employeeService.updatePwd(newPwd,employeeId2)){
                    System.out.println("抱歉，修改失败");
                    return;
                }
                System.out.println("修改成功");
                break;
            case "3":
                System.out.println("请输入新增的员工employeeId");
                String employeeId3 = Utility.readString(32);
                System.out.println("请输入登录密码：");
                String pwd = Utility.readString(50);
                System.out.println("请输入员工姓名：");
                String name = Utility.readString(50);
                System.out.println("请输入员工职务：");
                String job = Utility.readString(50);
                if (!employeeService.addEmployee(employeeId3,pwd,name,job)){
                    System.out.println("员工添失败");
                    return;
                }
                System.out.println("添加成功");
                break;
        }
    }

    //完成结账
    public void payBill(){
        System.out.println("=========结账服务==========");
        System.out.println("请选择要结账的餐桌编号(-1退出): ");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1){
            System.out.println("=========取消结账==========");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable
                = diningTableService.getDiningTableId(diningTableId);
        if (diningTable == null){
            System.out.println("=========结账餐桌不存在==========");
            return;
        }
        //这个餐桌是否有需要结账的菜单
        if (!billService.hasPayBillByDiningTableId(diningTableId)){
            System.out.println("=========无菜单需要结账==========");
            return;
        }
        System.out.println("结账方式（现金/支付宝/微信）(回车退出): ");
        String payMode = Utility.readString(20, "");//说明如果是回车，就是返回""
        if ("".equals(payMode)){
            System.out.println("=========取消结账==========");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key == 'Y'){
            if (billService.payBill(diningTableId, payMode)){
                System.out.println("=========成功结账==========");
            }else{
                System.out.println("=========结账失败==========");
            }
        }else{
            System.out.println("=========取消结账==========");
        }
    }
    //显示账单信息
//    public void listBill(){
//        List<Bill> allBills = billService.list();
//        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
//        for (Bill bill:allBills){
//            System.out.println(bill);
//        }
//        System.out.println("=========显示完毕==========");
//    }
    //显示账单信息2
    public void listBill2(){
        List<MultiTableBean> allBills = billService.list();
        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜名");
        for (MultiTableBean bill:allBills){
            System.out.println(bill);
        }
        System.out.println("=========显示完毕==========");
    }
    //完成点餐
    public void orderMenu(){
        System.out.println("=============点餐============");
        System.out.println("请输入点餐的桌号（-1退出）");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId == -1){
            return;
        }
        System.out.println("请输入点餐的菜品号（-1退出）");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1){
            return;
        }
        System.out.println("请输入点餐的菜品数量（-1退出）");
        int orderNums = Utility.readInt();
        if (orderNums == -1){
            return;
        }

        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableId(orderDiningTableId);
        if (diningTable == null ){
            System.out.println("=============餐桌号不存在============");
            return;
        }
        //验证菜品编号
        Menu menu = menuService.getMenuById(orderMenuId);
        if (menu == null){
            System.out.println("=============菜品号不存在============");
        }

        //点餐
        if (billService.orderMenu(orderMenuId, orderNums, orderDiningTableId)){
            System.out.println("============点餐成功=============");
        }else{
            System.out.println("============点餐失败=============");
        }
    }

    //显示所有菜品
    public void listMenu(){
        System.out.println("\n=============菜单如下============");
        System.out.println("\n菜品编号\t\t菜名\t\t\t类别\t\t价格");
        List<Menu> list = menuService.list();
        for (Menu menu : list){
            System.out.println(menu);
        }
        System.out.println("=============显示完毕============");
    }
    //完成订座
    public void orderDiningTable(){
        System.out.println("=============预定餐桌============");
        System.out.println("请选择要预定的餐桌编号(-1退出)：");
        int orderId = Utility.readInt();
        if (orderId == -1){
            System.out.println("============取消预定餐桌===========");
            return;
        }
        //该方法得到 Y/N
        char key = Utility.readConfirmSelection();
        if (key == 'Y'){

            //根据orderId 返回 对应DiningTable 对象 如果为null,说明对象不存在
            DiningTable diningTable = diningTableService.getDiningTableId(orderId);
            if (diningTable == null ){
                System.out.println("============预定餐桌不存在===========");
                return;
            }
            //判断餐桌的状态
            if (!("空".equals(diningTable.getState()))){//当前餐桌不是空
                System.out.println("=======餐桌已被预定或就餐中，请重新预定======");
                return;
            }
            //这时说明可以真的预定，更新餐桌状态
            System.out.println("请输入预定人姓名：");
            String orderName = Utility.readString(20);
            System.out.println("请输入预定人电话：");
            String orderTel = Utility.readString(50);
            if(diningTableService.orderDiningTable(orderId, orderName, orderTel)){
                System.out.println("=========== 预定成功 ===========");
            }else{
                System.out.println("=========== 预定失败 ===========");
            }

        }else{
            System.out.println("============取消预定餐桌===========");
        }
    }

    //显示所有餐桌状态
    public void listDiningTable(){
        System.out.println("\n餐桌编号\t\t餐桌状态");
        List<DiningTable> list = diningTableService.list();
        for (DiningTable diningTable : list){
            System.out.println(diningTable);
        }
        System.out.println("=============显示完毕============");
    }

    //显示主菜单
    public void menu(){
        while(loop){
            System.out.println("===============满汉楼==============");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.println("请输入您的选择: ");
            key = Utility.readString(1);
            switch(key){
                case "1":
                    System.out.println("请输入员工号：");
                    String empIid = Utility.readString(50);
                    System.out.println("请输入密  码：");
                    String pwd = Utility.readString(50);
                    //到数据库取判断
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empIid, pwd);
                    if (employee != null){
                        System.out.println("===============登陆成功[" + empIid + "]==============");
                        //显示二级菜单,是循环操作
                        while(loop) {
                            System.out.println("\n==============满汉楼二级菜单============");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预 定 餐 桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点 餐 服 务");
                            System.out.println("\t\t 5 查 看 账 单");
                            System.out.println("\t\t 6 结      账");
                            System.out.println("\t\t 7 管理员工信息");
                            System.out.println("\t\t 8 员工详细信息");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.println("请输入选择（数字）");
                            key = Utility.readString(1);
                            switch(key){
                                case "1":
                                    listDiningTable();//显示餐桌状态
                                    break;
                                case "2":
                                    orderDiningTable();//预定餐桌
                                    break;
                                case "3":
                                    listMenu();//显示菜品单
                                    break;
                                case "4":
                                    orderMenu();
                                    break;
                                case "5":
                                    listBill2();
                                    break;
                                case "6":
                                    payBill();
                                    break;
                                case "7":
                                    manageEmoloyee();
                                    break;
                                case "8":
                                    System.out.println("员工信息");
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                            }
                        }
                        }else {
                        System.out.println("==============登录失败===============");
                    }
                    break;

                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("你输入有误，请重新输入！");
            }
        }
        System.out.println("退出成功");
    }
}
