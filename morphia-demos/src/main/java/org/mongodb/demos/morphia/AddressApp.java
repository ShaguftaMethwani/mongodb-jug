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

package org.mongodb.demos.morphia;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.mongodb.demos.morphia.model.Address;
import org.mongodb.demos.morphia.model.Car;
import org.mongodb.demos.morphia.model.Person;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.UnknownHostException;

public class AddressApp {

    private MongoClient mongoClient;
    private DB db;
    private Morphia morphia;
    private Datastore datastore;


    // static block for init
    {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug_morphia");
            morphia = new Morphia();
            datastore = morphia.createDatastore(mongoClient,"jug_morphia");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){

        AddressApp app = new AddressApp();

        app.insertPersons();

        app.queryPersons();

    }

    private void queryPersons() {
        // find employee by age
        for (Person p : datastore.find(Person.class, "age", 35)) {
            System.out.println(p);
            System.out.println("\t"+ datastore.getKey(p) );
            System.out.println("-- -- -- --");
        }
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

        Car car = new Car("Toyota", "Prius" , 2013);
        datastore.save(car);
        person.addCar(car);

        datastore.save(person);


        person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Owdy");
        person.setAge(30);
        datastore.save(person);
        datastore.save(person);


    }


}
