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

package org.mongodb.demos.binary;


import com.mongodb.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

public class JsonDemo {

    static DB db = null;
    static DBCollection fooCollection = null;

    // static block for init
    {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("jug");
            fooCollection = db.getCollection("foo");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        // TODO: set the file you want to load and where to you want
        //       to save it back (not using parameters to run it easily from IDE

        String employeePic = "/Users/tgrall/Downloads/tgrall.jpeg";
        String imageRestoreTo = "/Users/tgrall/Downloads/tmp/";

        JsonDemo app = new JsonDemo();
        app.insertEmployee("jdoe", "John", "Doe", employeePic);
        app.readEmployee("jdoe", imageRestoreTo );

    }


    /**
     * Create a new employee and set the picture inside the Document
     *   - data are store as bytes
     * @param firstName
     * @param lastName
     * @param picture
     * @throws Exception
     */
    public void insertEmployee(String id, String firstName, String lastName, String picture) throws Exception {
        System.out.println("\n== ==  WRITE TO MONGODB  == == == ");

        // Pure DBObject approach
        DBObject emp = BasicDBObjectBuilder.start()
                .add("_id",id)
                .add("first_name",firstName)
                .add("last_name", lastName)
                .get();
        System.out.println(emp);

        // import an image
        byte[] imageAsBytes = extractBytes(picture);
        emp.put("image",imageAsBytes );

        // save data in the collection
        fooCollection.save( emp  );
        System.out.println("\n== == == == == ");
    }

    /**
     * Read employee and save the file into the location
     * @param id
     * @param location
     * @throws Exception
     */
    public void readEmployee(String id, String location) throws Exception{
        DBObject emp = fooCollection.findOne( QueryBuilder.start("_id").is(id).get() );

        System.out.println("\n== ==  READ FROM MONGODB  == == == ");
        System.out.println(emp);

        byte[] imageAsBytes = (byte[])emp.get("image");
        String fileCreated = saveImageToFile(imageAsBytes, location);

        System.out.println("\n\t File saved at : "+ location + fileCreated);
        System.out.println("\n== == == == == ");

    }


    /**
     * Get the bytes of the file passes as parameter
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public byte[] extractBytes (String fileName) throws IOException {
        FileInputStream fileInputStream=null;
        File file = new File(fileName);
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bFile;
    }

    /**
     * Save bytes into a file
     * @param imagesAsBytes
     * @param location
     * @return
     * @throws Exception
     */
    public String saveImageToFile( byte[] imagesAsBytes , String location  ) throws Exception {
        String fileName = location+ "mongodemo-"+    UUID.randomUUID().toString() +".jpeg";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(imagesAsBytes);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return fileName;

    }

}
