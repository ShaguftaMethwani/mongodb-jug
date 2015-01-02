/*
 * Copyright (c) 2003-2015 Tugdual Grall
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

package org.mongodb.demos.tailable;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.UUID;


public class RealTimeInjector {

    static DB db = null;
    static DBCollection coll = null;

    static {
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug");
            coll = db.getCollection("messages");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        int counter = 0;
        while (counter <= 20) {
            Thread.sleep(2500);
            DBObject person = BasicDBObjectBuilder.start()
                    .add("status", "ok")
                    .add("value", UUID.randomUUID().toString())
                      .add("counter", counter++).get();
            coll.insert(person);
            System.out.println("Doc inserted "+ counter);
        }
    }
}
