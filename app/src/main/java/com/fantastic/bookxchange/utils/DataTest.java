package com.fantastic.bookxchange.utils;

import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m3libea on 10/12/17.
 */

public class DataTest {



    public static List<User> fakeData(){
        User user = new User();
        User user1 = new User();
        User user2 = new User();

        user.setName("Rocio");
        user.setUsername("rommor");
        user.setLocation(new LatLng(37.7749,-122.419));

        user1.setName("Gretel");
        user1.setUsername("ghilbing");
        user1.setLocation(new LatLng(37.77841491,-122.4209547));

        user2.setName("Dharmesh");
        user2.setUsername("dgohil");
        user2.setLocation(new LatLng(37.77661714,-122.40840197));


        //Create books
        Book b1 = new Book();
        b1.setTitle("Harry Potter");
        b1.setAuthor("JKRowling");

        Book b2 = new Book();
        b2.setTitle("Harry Potter 2");
        b2.setAuthor("JKRowling");

        Book b = new Book();
        b.setTitle("Harry Potter 3");
        b.setAuthor("JKRowling");

        Book b4 = new Book();
        b4.setTitle("Pride and Prejuice");
        b4.setAuthor("Jane Austen");

        List<Book> l1 = new ArrayList<>();

        l1.add(b);
        l1.add(b1);

        List<Book> l2 = new ArrayList<>();

        l2.add(b2);
        l2.add(b4);

        //Add the books to users

        user.setBooks(l1);
        user1.setBooks(l2);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);

        return users;
    }

}
