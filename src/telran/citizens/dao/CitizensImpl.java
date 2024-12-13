package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CitizensImpl implements Citizens {
    private static Comparator<Person> lastNameComparator;
    private static Comparator<Person> ageComparator;
    private NavigableSet<Person> idSet;
    private NavigableSet<Person> lastNameSet;
    private NavigableSet<Person> ageSet;

    static {
        lastNameComparator = (p1, p2) -> {
            int res = p1.getLastName().compareTo(p2.getLastName());
            return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
        };
        ageComparator = (p1, p2) -> {
            int res = Integer.compare(p1.getAge(), p2.getAge());
            return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
        };
    }

    public CitizensImpl() {
        idSet = new TreeSet<>();
        lastNameSet = new TreeSet<>(lastNameComparator);
        ageSet = new TreeSet<>(ageComparator);
    }

    public CitizensImpl(Iterable<Person> citizens) {
        this();
        citizens.forEach(p -> add(p));
    }

    // O(log(n)
    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }
        if (!idSet.add(person)) {
            return false;
        }
        lastNameSet.add(person);
        ageSet.add(person);
        return true;
    }

    // O(log(n)
    @Override
    public boolean remove(int id) {
        Person victim = find(id);
        if (victim == null) {
            return false;
        }
        idSet.remove(victim);
        lastNameSet.remove(victim);
        ageSet.remove(victim);
        return true;
    }

    // O(log(n)
    @Override
    public Person find(int id) {
        Person person = new Person(id, null, null, null);
        if (idSet.contains(person)) {
            return idSet.ceiling(person);
        }
        return null;
    }

    // O(log(n)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        Person from = new Person(Integer.MIN_VALUE, null, null, now.minusYears(minAge));
        Person to = new Person(Integer.MAX_VALUE, null, null, now.minusYears(maxAge));
        return ageSet.subSet(from, true, to, true);
    }

    // O(log(n)
    @Override
    public Iterable<Person> find(String lastName) {
        Person from = new Person(Integer.MIN_VALUE, null, lastName, null);
        Person to = new Person(Integer.MAX_VALUE, null, lastName, null);
        return lastNameSet.subSet(from, true, to, true);
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedById() {
        return idSet;
    }
    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByLastName() {
        return lastNameSet;
    }
    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByAge() {
        return ageSet;
    }
    // O(1)
    @Override
    public int size() {
        return idSet.size();
    }
}
