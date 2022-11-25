package com.hspedu.mhl.domain;

/**
 * @author: guorui fu
 * @versiion: 1.0
 * 这是一个javabean 和 employee 对应
 */
public class Employee {

    private Integer id;
    private String empid;
    private String pwd;
    private String name;
    private String job;

    public Employee() {//无参构造器，底层apache-dbutils反射需要
    }

    public Employee(Integer id, String empid, String pwd, String name, String job) {
        this.id = id;
        this.empid = empid;
        this.pwd = pwd;
        this.name = name;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return   id +
                "\t\t" + empid +
                "\t\t" + pwd +
                "\t\t" + name +
                "\t\t" + job ;
    }
}
