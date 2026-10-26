package com.app.util;

import java.util.*;
import java.lang.*;
import java.io.*;

class Student{
    
    int sid;
    int age;
    String sname,course;
    public void register(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the student sid,name,age,course");
        
        sid=sc.nextInt();
        sname=sc.next();
        age=sc.nextInt();
        course=sc.next();
        
    }
    public void display(){
        System.out.println("Student id:"+sid);
        System.out.println("Student name:"+sname);
        System.out.println("Student age:"+age);
        System.out.println("Student course:"+course);
    }
}
 class StudentArrayList{
    public static void main(String[] args){
        ArrayList<Student> st=new ArrayList<Student>();
        System.out.println("Enter the no.of students to be inserted:");
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++){
            Student s=new Student();
            s.register();
            st.add(s);
            System.out.println("student "+i+"added successfully");
        }
        st.remove(0);
        for(int i=0;i<st.size();i++){
        	System.out.println("Student "+i+"details");
            st.get(i).display();
        }
        
        
    }
}