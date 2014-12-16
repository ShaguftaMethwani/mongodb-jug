
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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;


/**
 * This class shows how to use MongoDB GridFS to store large files
 */
public class GridFSDemo {

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

        String fileToLoad = "/Users/tgrall/Downloads/proud_to_be_polyglot_devfest_2014.pdf";
        String fileToSave = "/Users/tgrall/Downloads/tmp/0001-DEMO.pdf";

        GridFSDemo app = new GridFSDemo();

        // using GridFS
        Object id = app.saveLargeFile(fileToLoad);
        app.readLargeFile(id, fileToSave);

    }

    /**
     * Save the file into MongoDB using GridFS
     * @param fileName
     * @return
     * @throws IOException
     */
    public Object saveLargeFile( String fileName ) throws IOException {
        System.out.println("\n== ==  WRITE IN GRIFS / MONGODB  == == == ");
        GridFS fs = new GridFS(db);
        // large file
        byte[] fileAsBytes = extractBytes(fileName);
        GridFSInputFile in = fs.createFile( fileAsBytes );
        in.save();
        System.out.println(  "File ID : "+ in.getId()   );
        System.out.println("\n== ==  == == == ");
        return in.getId();
    }

    /**
     * Read the file stored in GridFS and  save it into the fileToSave location
     * @param id
     * @param fileToSave
     * @throws Exception
     */
    public void readLargeFile(Object id, String fileToSave) throws Exception {
        GridFS fs = new GridFS(db);
        GridFSDBFile out = fs.findOne( new BasicDBObject( "_id" , id ) );
        out.writeTo(fileToSave);

        System.out.println("File saved into "+ fileToSave);
    }


    /**
     * Get the bytes of the file passes as parameter
     * @param fileName
     * @return
     * @throws IOException
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


}
