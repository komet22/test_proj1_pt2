package main.java.dbh;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author https://www.tutorialspoint.com/hibernate/hibernate_list_mapping.htm
 */

public class Certificate {
   private int id;
   private String name;

   public Certificate() {}
   public Certificate(String name) {
      this.name = name;
   }
   public int getId() {
      return id;
   }
   public void setId( int id ) {
      this.id = id;
   }
   public String getName() {
      return name;
   }
   public void setName( String name ) {
      this.name = name;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj != null && obj instanceof Certificate) {
        Certificate obj2 = (Certificate)obj;
        if((id == obj2.getId()) && (name.equals(obj2.getName())))
        {
            return true;
        }
      }
      return false;
   }
   
   @Override
   public int hashCode() {
      return name.hashCode();
   }
}