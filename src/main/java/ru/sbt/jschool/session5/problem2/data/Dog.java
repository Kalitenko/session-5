package ru.sbt.jschool.session5.problem2.data;

/**
 * @author NIzhikov
 */
public class Dog extends Animal {
    public String bread;

    public Dog(String name) {
        super(name);
    }

    public String getBread() {
        return bread;
    }

    public void setBread(String bread) {
        this.bread = bread;
    }
}
