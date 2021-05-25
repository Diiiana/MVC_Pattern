package Model;

public class Person {
    protected String name;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    public Person(Person person) {
        this.name = person.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
