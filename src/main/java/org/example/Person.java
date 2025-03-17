package org.example;

import java.util.Objects;

public class Person {
    protected String surname;
    protected String name;
    protected String role;
    protected int age;
    protected String email;

    public Person(String surname, String name, String role) {
        this.surname = surname;
        this.name = name;
        this.role = role;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email == null || email.isEmpty() ? "null" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "surname=" + surname +
                ", name=" + name +
                ", role=" + role +
                ", age=" + age +
                ", email=" + getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(surname, person.surname) &&
                Objects.equals(name, person.name) &&
                Objects.equals(role, person.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, role);
    }
}
