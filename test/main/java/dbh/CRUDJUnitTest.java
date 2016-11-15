/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbh;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author PM, PJ, SK
 */
public class CRUDJUnitTest {
    
    private static SessionFactory factory;
    
    public CRUDJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            fail("Failed to create sessionFactory object. Aborting test.");
	}
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Session session = factory.openSession();
        Transaction tx = null;
         try{
            tx = session.beginTransaction();
            List all = session.createQuery("FROM Employee").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            all = session.createQuery("FROM Certificate").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        } finally {
            session.close();
        }
    }
    
    @After
    public void tearDown() {
        Session session = factory.openSession();
        Transaction tx = null;
         try{
            tx = session.beginTransaction();
            List all = session.createQuery("FROM Employee").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            all = session.createQuery("FROM Certificate").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        }finally{
            session.close();
        }
    }

    @Test
    public void saveTest() {
        System.out.println("CRUD Create test: session.save()");
        
        String[] fname = {"Paweł", "Maciej"};
        String[] lname = {"Jaruga", "Stepnowski"};
        int[] salary = {20000, 1850};
        
        Set c1 = new HashSet();
        c1.add(new Certificate("AXA"));
        c1.add(new Certificate("XAXA"));
        Set c2 = new HashSet();
        c2.add(new Certificate("FGT"));
        Set[] cert = {c1, c2};
        Employee[] employee = new Employee[fname.length];
        int id[] = new int[2];
        
        for(int i = 0 ; i < fname.length ; i++)
        {
            employee[i] = new Employee(fname[i], lname[i], salary[i]);
            employee[i].setCertificates(cert[i]);
        }
        
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            int i = 0;
            for(Employee em : employee) {
                id[i] = (int) session.save(em);
                i++;
            }
            Employee e;
            try {
                for (i = 0; i<2 ;i++){
                    e =(Employee) session.createQuery("FROM Employee WHERE id="
                            + id[i] + "").uniqueResult();
                }
            } catch (HibernateException ex){
                fail("Failed to read saved objects. Aborting test.");
            } catch (ClassCastException ex) {
                fail("Returned object is not an Employee. Aborting test.");
            }
            
            List result = session.createQuery("FROM Employee").list();
            if (result.size() != 2) {
                fail("Wrong number of Employees saved.");
            }
            else
            {
                Iterator it = result.iterator();
                i = 0;
                while(it.hasNext())
                {
                        e = (Employee) it.next();
                        assertEquals(e.getFirstName(), fname[i]);
                        assertEquals(e.getLastName(), lname[i]);
                        assertEquals(e.getSalary(), salary[i]);
                        assertEquals(e.getCertificates(), cert[i]);
                        i++;
                }
            }
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
    }
    
    @Test
    public void getTest() {
        System.out.println("CRUD Read test: session.get()");
        
        String[] fname = {"Paweł", "Maciej"};
        String[] lname = {"Jaruga", "Stepnowski"};
        int[] salary = {20000, 1850};
        int id[] = new int[2];
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
            for (int i = 0; i < fname.length; i++)
                id[i] = (int) session.save(new Employee(fname[i], lname[i], salary[i]));
            
            Employee result = (Employee) session.get(Employee.class, id[1]);
            
            List t = session.createQuery("FROM Employee").list();
            if (t.size() != 2) {
                fail("Number of Employees modified.");
            }
            
            assertEquals(result.getFirstName(), fname[1]);
            assertEquals(result.getLastName(), lname[1]);
            assertEquals(result.getSalary(), salary[1]);
            
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
    }
    
    @Test
    public void updateTest() {
        System.out.println("CRUD Update test: session.update()");
        String[] fname = {"Andrzej", "Adam"};
        String[] lname = {"Kowalski", "Majewski"};
        int[] salary = {5000, 1000};
        
        Set c1 = new HashSet();
        c1.add(new Certificate("WPD"));
        c1.add(new Certificate("RN"));
        Set c2 = new HashSet();
        c2.add(new Certificate("FGS"));
        Set[] cert = {c1, c2};
        Employee[] employee = new Employee[fname.length];
        int id[] = new int[2];
        
        for(int i = 0 ; i < fname.length ; i++)
        {
            employee[i] = new Employee(fname[i], lname[i], salary[i]);
            employee[i].setCertificates(cert[i]);
        }
        
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            int i = 0;
            for(Employee em : employee) {
                id[i] = (int) session.save(em);
                i++;
            }
            employee[1].setSalary(2000);
            employee[1].setLastName("Majewiak");
            session.update(employee[1]);
            
            Employee e = (Employee) session.get(Employee.class, id[1]);
            
            List t = session.createQuery("FROM Employee").list();
            if (t.size() != 2) {
                fail("Number of Employees modified.");
            }
            
            assertEquals(e.getFirstName(), "Adam");
            assertEquals(e.getLastName(), "Majewiak");
            assertEquals(e.getSalary(), 2000);
            assertEquals(e.getCertificates(), cert[1]);
            i++;
                
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
    }
    
    @Test
    public void deleteTest() {
        System.out.println("CRUD Delete test: session.delete()");
        String[] fname = {"Andrzej", "Adam"};
        String[] lname = {"Kowalski", "Majewski"};
        int[] salary = {7000, 15000};
        int id[] = new int[2];
        
        Set c1 = new HashSet();
        c1.add(new Certificate("WPD"));
        c1.add(new Certificate("RN"));
        Set c2 = new HashSet();
        c2.add(new Certificate("FGS"));
        Set[] cert = {c1, c2};
        Employee[] employee = new Employee[fname.length];
        
        for(int i = 0 ; i < fname.length ; i++)
        {
            employee[i] = new Employee(fname[i], lname[i], salary[i]);
            employee[i].setCertificates(cert[i]);
        }
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            int i = 0;
            
            for(Employee em : employee) {
                id[i] = (int) session.save(em);
                i++;
            }
            
            session.delete(employee[0]);
            
            List t = session.createQuery("FROM Employee").list();
            if (t.size() != 1) {
                fail("Expected number of Employees was: [1], but actual was: [" + t.size() + "].");
            }
            
            Employee result = (Employee) session.get(Employee.class, id[1]);
            
            assertEquals(result.getFirstName(), fname[1]);
            assertEquals(result.getLastName(), lname[1]);
            assertEquals(result.getSalary(), salary[1]);
            
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
    }
}