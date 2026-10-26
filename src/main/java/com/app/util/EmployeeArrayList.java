package com.app.util;

import java.util.ArrayList;
import java.util.Scanner;

class Employee
{
	int id;
	int salary;
	String name;
	String dept;
   public void register()
   {
	   Scanner sc=new Scanner(System.in);
	   System.out.println("Enter id,salary,name and dept");
	   id=sc.nextInt();
	   salary=sc.nextInt();
	   name=sc.next();
	   dept=sc.next();
   }
   public void display()
   { 
	  System.out.println("id is:"+id);
	  System.out.println("salary is:"+salary);
	  System.out.println("name is:"+name);
	  System.out.println("dept is:"+dept);
   }
}
public class EmployeeArrayList 
{
	public static void main (String args[])
	{
		ArrayList<Employee> em=new ArrayList<Employee>();
		Scanner sc=new Scanner(System.in);
		System.out.println("enter how many employee");
	    int n=sc.nextInt();
	    int i;
	    for(i=0;i<n;i++)
	    {
	    	Employee emp = new Employee();
	    	emp.register();
	    	em.add(emp);
	    	}
	    em.remove(em.size()-1);
	    
	    for(i=0;i<em.size();i++)
	    {
	    	System.out.println("Employee details");
	        Employee emp=em.get(i);
	    	emp.display();
	    }
	    
	 }

}
