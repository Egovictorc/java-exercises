package org.tekhub.reactiveprogramming;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Exercise1 {
    public static void main(String[] args) {

        //print all numbers in the intNumbersStream stream
        //TODO: write code here
        StreamSources.integerNumbersStream().forEach(System.out::println);

        //print numbers from intNumbersStream stream that are less than 5
        //TODO: write code here
        Predicate<Integer> lessThanFive = i -> i < 5;
        StreamSources.integerNumbersStream().filter(lessThanFive).forEach(System.out::println);

        //print the second and third numbers from intNumbersStream stream that are greather than 5
        //TODO: write code here
        Predicate<Integer> aboveFive = i -> i > 5;
        StreamSources.integerNumbersStream().filter(aboveFive).skip(1).limit(2).forEach(System.out::println);

        //print the first number in intNumbersStream stream that is greather than 5 and if nothing is found then print -1
        //TODO: write code here
        Optional<Integer> firstNumber = StreamSources.integerNumbersStream().filter(aboveFive).findFirst();
        if (firstNumber.isPresent()) {
            System.out.println(firstNumber.get());
        } else {
            System.out.println(-1);
        }
        //print the first names of all users in usersStream stream
        //TODO: write code here
        StreamSources.usersStream().forEach(user -> System.out.println(user.getFirstName()));

        // print the first names in usersStream stream for users that have IDs from number 3
        //TODO: write code here
            StreamSources.usersStream().filter( user -> user.getId() > 3).forEach(user -> System.out.println(user.getFirstName() ));
    }

}
