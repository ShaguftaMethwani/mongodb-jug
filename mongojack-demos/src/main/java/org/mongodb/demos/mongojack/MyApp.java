/*
 * Copyright (c) 2003-2014 Tugdual Grall
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.mongodb.demos.mongojack;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongodb.demos.mongojack.model.Address;
import org.mongodb.demos.mongojack.model.Car;
import org.mongodb.demos.mongojack.model.Person;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.net.UnknownHostException;

/**
 * Sample application for MongoJack
 */
public class MyApp {

    private MongoClient mongoClient;
    private DB db;
    DBCollection personCollectionDriver;
    DBCollection carCollectionDriver;
    JacksonDBCollection<Person, String> personCollJack;
    JacksonDBCollection<Car, String> carCollJack;

    // static block for init
    {
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug_mongojack");
            personCollectionDriver = db.getCollection("persons");
            carCollectionDriver = db.getCollection("cars");
            personCollJack = JacksonDBCollection.wrap(personCollectionDriver, Person.class, String.class);
            carCollJack = JacksonDBCollection.wrap(carCollectionDriver, Car.class, String.class);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){

        MyApp app = new MyApp();

        app.insertPersons();

        app.queryPersons();


    }


    private void insertPersons() {


        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setNickName("JoeD");
        person.setAge(35);

        // new address
        Address address = new Address();
        address.setStreet("21 Jump Street");
        address.setCity("Los Angeles");

        person.addAddress(address);


        // add new car
        Car car = new Car("Toyota", "Prius" , 2013);
        WriteResult<Car, String> resultCarInsert = carCollJack.save(car);
        car.setId( resultCarInsert.getSavedId() );
        person.addCar(car);

        WriteResult<Person, String> result = personCollJack.save(person);

        // search by ID
        Person pSearch = personCollJack.findOneById( result.getSavedId() );
        System.out.println("-- -- -- -- --");
        System.out.println("\tFind By ID");
        System.out.println( pSearch );
        System.out.println("-- -- -- -- --");

        person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Owdy");
        person.setAge(30);
        personCollJack.save(person);

    }

    private void queryPersons() {

        System.out.println("-- -- -- -- --");
        System.out.println("\tQuery Data");
        DBCursor<Person> cursor = personCollJack.find().is("age", 35);
        while (cursor.hasNext()) {
            Person person = cursor.next();
            System.out.println( person );

            if (person.getCars().size() !=0) {
                System.out.println("\t CAR-0 : " +
                                person.getCars().get(0).fetch()
                );
            }

        }
        System.out.println("-- -- -- -- --");

    }



}
