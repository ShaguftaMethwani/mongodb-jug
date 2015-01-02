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

package org.mongodb.demos.tailable;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by tgrall on 1/1/15.
 */
public class RealTimeAppServer {


    static DB db = null;

    static {
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {

        RealTimeAppServer realTimeAppServer = new RealTimeAppServer();
        DBCollection coll = realTimeAppServer.createAndGetCappedCollection("messages");

        DBCursor cur = coll.find().sort(BasicDBObjectBuilder.start( "$natural", 1 ).get()).addOption(Bytes.QUERYOPTION_TAILABLE).addOption(Bytes.QUERYOPTION_AWAITDATA);

        System.out.println("== open cursor ==");

        Runnable task = () -> {
            System.out.println("\tWaiting for events");
            while (cur.hasNext()) {
                DBObject obj = cur.next();
                System.out.println( obj );

            }
        };
        new Thread(task).start();

    }

    private DBCollection createAndGetCappedCollection(String name) throws InterruptedException {
        DBCollection coll = db.getCollection(name);
        coll.drop();
        DBObject options = new BasicDBObject("capped", true);
        options.put("size", 1000);
        coll = db.createCollection(name, options);
        coll.insert( BasicDBObjectBuilder.start(    "status", "initialized").get() );
        System.out.println("== capped collection created ===");
        return coll;
    }

}
