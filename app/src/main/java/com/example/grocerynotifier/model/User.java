package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.grocerynotifier.converters.DateFormatter;
import com.example.grocerynotifier.converters.JsonConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "users")
@TypeConverters({JsonConverter.class, DateFormatter.class})
public class User {

   @PrimaryKey
   @NotNull
   private String email;
   @NotNull
   private String name;
   @NotNull
   private String password;
   private String resetPasswordToken;
   @NotNull
   private String phone;
   @NotNull
   private Date dateOfBirth;
   @NotNull
   private Account account;

   public User(String email, Account account, Date dateOfBirth, String phone, String resetPasswordToken, String password, String name) {
      this.email = email;
      this.account = account;
      this.dateOfBirth = dateOfBirth;
      this.phone = phone;
      this.resetPasswordToken = resetPasswordToken;
      this.password = password;
      this.name = name;
   }

   public User() {
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   @NotNull
   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   @NotNull
   public Date getDateOfBirth() {
      return dateOfBirth;
   }

   public void setDateOfBirth(Date dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   @NotNull
   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   @NotNull
   public String getResetPasswordToken() {
      return resetPasswordToken;
   }

   public void setResetPasswordToken(String resetPasswordToken) {
      this.resetPasswordToken = resetPasswordToken;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
