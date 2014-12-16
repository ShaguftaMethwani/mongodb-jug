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

package org.mongodb.demos.replication;

import com.mongodb.*;

import java.util.Arrays;

/**
 * Created by tgrall on 12/16/14.
 */
public class RetryDemo {


    public static void main(String args[]) throws Exception {

        //TODO : See https://gist.github.com/tgrall/954aa021ba420639d614

        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018)
        ));


        DB db = client.getDB("jug");
        DBCollection coll = db.getCollection("bar");

        System.out.println("BEFORE");

        boolean loop = true;

        while(loop) {

            int backoff = 0, counter = 0;

            DBObject obj = null;

            do {
                try {
                    obj = BasicDBObjectBuilder.start().add("name", "mydoc").add("counter", counter++).get();
                    //System.out.print("\t inserting...");
                    coll.insert(obj, new WriteConcern(2, 3000, true, false));
                    backoff = 0;
                    // System.out.println(" OK : Document inserted...");
                } catch (Exception e) {
                    System.out.println(e.toString());
                    if (backoff == 3) {
                        throw new Exception("Tried 3 times... still failed");
                    }

                    backoff++;
                    System.out.println("Waiting for " + backoff + "s");
                    Thread.sleep(1500 * backoff);
                }

            } while (backoff != 0);

        }

        System.out.println("AFTER");

    }
}
