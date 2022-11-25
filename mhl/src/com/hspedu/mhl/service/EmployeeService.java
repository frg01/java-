package com.hspedu.mhl.service;

import com.hspedu.mhl.dao.EmployeeDAO;
import com.hspedu.mhl.domain.Employee;

import java.util.List;

/**
 * @author: guorui fu
 * @versiion: 1.0
 *该类完成对employee  表的各种操作（通过调用EmployeeDAO对象完成）
 */
public class EmployeeService {

    //定义一个EmployeeDAO属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    //方法，根据empId he pwd 返回一个Employee对象
    //富国重新为空，就返回Null
    public Employee getEmployeeByIdAndPwd(String empId, String pwd){
       return employeeDAO.querySingle("select * from employee where empId = ? and pwd = md5(?)",
                Employee.class, empId, pwd);

    }
    //返回employee表
    public List<Employee> allEmployee(){
        return employeeDAO.queryMulti("select * from employee",Employee.class);
    }

    //删除员工
    public boolean deleteEmployee(String employeeId){
        int update = employeeDAO.update("delete from employee where empId = ?", employeeId);
        return update > 0;
    }

    //修改员工密码
    public boolean updatePwd(String newPwd,String employeeId2){
        int update = employeeDAO.update("update employee set pwd = md5(?) where empId = ?", newPwd,employeeId2);
        return update > 0;
    }

    //增加员工
    public boolean addEmployee(String employeeId3,String pwd,String name,String job){
        int update = employeeDAO.update("insert into employee values(null,?,md5(?),?,?)", employeeId3, pwd, name, job);
        return update > 0;
    }
}
