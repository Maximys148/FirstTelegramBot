package org.example.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.glassfish.grizzly.http.util.TimeStamp;

import java.util.Date;

@Entity
@Table(name = "botClient")
public class User {
    @Id
    private String chatId;
    private String firstName;
    private String lastName;
    private String userName;
    @Column(name = "registered_at_time")
    private Date registeredAtTime;

    private Integer countMassage;

    public User() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRegisteredAtTime() {
        return registeredAtTime;
    }

    public void setRegisteredAtTime(Date registeredAtTime) {
        this.registeredAtTime = registeredAtTime;
    }

    public Integer getCountMassage() {
        return countMassage;
    }

    public void setCountMassage(Integer countMassage) {
        this.countMassage = countMassage;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId='" + chatId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAtTime=" + registeredAtTime +
                ", countMassage=" + countMassage +
                '}';
    }
}
