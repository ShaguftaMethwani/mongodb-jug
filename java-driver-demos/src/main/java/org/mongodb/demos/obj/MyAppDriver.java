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

package org.mongodb.demos.obj;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample application with basic Java MongoDB Driver code
 *
 */
public class MyAppDriver {


    static DB db = null;
    static DBCollection fooCollection = null;

    static {
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug");
            fooCollection = db.getCollection("persons");

            System.out.println(fooCollection);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws UnknownHostException {

        MyAppDriver app = new MyAppDriver();

        app.insertPersons();

        app.findPersons();


        System.out.println("=== === end == === ");




    }


    private void insertPersons() {
        List<DBObject> addresses = new ArrayList<>();

        DBObject address = BasicDBObjectBuilder.start()
                .add("city", "Paris")
                .add("street", "Rue de la Paix")
                .add("zip_code", "75015")
                .get();
        addresses.add(address);


        DBObject person = BasicDBObjectBuilder.start()
                .add("first_name", "John")
                .add("last_name", "Doe")
                .add("addresses", addresses)
                .add("age", 35).get();

        fooCollection.insert(person);



        person = BasicDBObjectBuilder.start()
                .add("first_name", "Jane")
                .add("last_name", "Owy")
                .add("age", 32).get();

        fooCollection.insert(person);


    }


    private void findPersons() {

        System.out.println(" == == == Query == == == ");
        DBObject query = QueryBuilder.start("age").is(35).get();
        System.out.println("\tfilter : "+ query);

        for (DBObject dbo : fooCollection.find(query )) {
            System.out.println( dbo  );
        }

        System.out.println(" == == == ===  == == == ");

    }



}
