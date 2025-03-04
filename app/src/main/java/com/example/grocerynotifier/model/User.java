package com.example.grocerynotifier.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.grocerynotifier.utils.DateFormatter;
import com.example.grocerynotifier.utils.JsonConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "users", foreignKeys = @ForeignKey(entity = Account.class, parentColumns = "id", childColumns = "accountId", onDelete = ForeignKey.CASCADE))
@TypeConverters({JsonConverter.class, DateFormatter.class})
public class User {

   @PrimaryKey
   @NotNull
   private String email;
   @NotNull
   private String name;
   @NotNull
   private String password;
   @NotNull
   private String gender;
   private String resetPasswordToken;
   @NotNull
   private String phone;
   @NotNull
   private Date dateOfBirth;
   @NotNull
   private String accountId;
   @NotNull
   boolean isActive;

   public User(String email, String accountId, Date dateOfBirth, String phone,String gender, String resetPasswordToken, String password, String name, boolean isActive) {
      this.email = email;
      this.accountId = accountId;
      this.dateOfBirth = dateOfBirth;
      this.gender = gender;
      this.phone = phone;
      this.resetPasswordToken = resetPasswordToken;
      this.password = password;
      this.name = name;
      this.isActive = isActive;
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

   @NotNull
   public String getGender() {
      return gender;
   }

   public void setGender(@NotNull String gender) {
      this.gender = gender;
   }

   @NotNull
   public String getAccountId() {
      return accountId;
   }

   public void setAccountId(@NotNull String accountId) {
      this.accountId = accountId;
   }
}
