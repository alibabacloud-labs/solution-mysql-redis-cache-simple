package demo;

// Java MySQL Connector
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
// JSON support for Java
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
// Jedis is one of the popular Java Redis Client
import redis.clients.jedis.Jedis;

/**
 * The RedisCache program implements an application to demonstrate the cache(often
 * called REmote cache) capability with MySQL as RDS service and Redis as Cache
 * layer
 *
 * Architecture
 * 	- Application : this java program on a Linux Server
 * 	- Redis       : verson 5.0
 * 	- MySQL       : version 5.7
 *
 * Test Envoriment:
 * 	- Alibaba Cloud ECS, Redis and RDS(MySQL) on Singapore Region within the same VPC.
 *
 * Program Environment:
 * 	- Complile: v1.8
 * 	- Jar : jedis-3.2.0.jar, json-20190722.jar, mysql-connector-java.jar
 *
 */

public class RedisCache {

  /**
   * Simple method to fetch MySQL query result
   * @param con mysql connection
   * @param query string of query text
   * @return JSONArray all rows of the result with the total row count at the end
   */
  public static JSONArray getResultsFromMySQL(Connection con, String query)
    throws SQLException, JSONException {
    JSONArray jsonA = new JSONArray();
    Statement stmt = con.createStatement();
    try {
      ResultSet rs = stmt.executeQuery(query);
      ResultSetMetaData rsmd = rs.getMetaData();
      int numColumns = rsmd.getColumnCount();
      JSONObject obj;
      while (rs.next()) {
        obj = new JSONObject();
        for (int i = 1; i <= numColumns; i++) {
          String column_name = rsmd.getColumnName(i);
          obj.put(column_name, rs.getObject(column_name));
        }
        jsonA.put(obj);
      }
      obj = new JSONObject();
      obj.put("total row count", jsonA.length());
      jsonA.put(obj);
    } finally {
      stmt.close();
    }
    return jsonA;
  }

  /**
   * Time to Live value for Redis Key
   * Unit: second
   */
  public static final int TTL = 5;

  /**
   * Scanner input the following environment parameters for Redis and MySQL.
   * SG_REDISURL - Redis endpoint,  which allows "without password access" in
   * 		    a VPC-connected network
   * SG_DBHOST   - MySQL end point, and SG_DBUSER, SG_DBUSERPW, SG_DBNAME
   */
  public static void main(String[] args) {
    System.out.println("Please input environment parameters: ");

    // Get Redis connection
    System.out.print("Redis URL: ");
    Scanner scanner = new Scanner(System.in);
    String SG_REDISURL = scanner.nextLine();

    System.out.print("Redis password: ");
    String SG_REDIS_PASSWORD = scanner.nextLine();

    // Connecting to Redis server
    Jedis jedis = new Jedis(SG_REDISURL);

    // Instance password
    String authString = jedis.auth(SG_REDIS_PASSWORD);
    if (!authString.equals("OK")) {
      System.err.println("Redis AUTH Failed: " + authString);
      return;
    }

    System.out.println("Connecting to Redis server successfully!");

    // Check whether server is running or not
    System.out.println("Redis Server is running: " + jedis.ping());

    // Set up MySQL connection
    System.out.print("RDS MySQL URL: ");
    String SG_DBHOST = scanner.nextLine();
    System.out.print("RDS database name: ");
    String SG_DBNAME = scanner.nextLine();
    System.out.print("RDS database username: ");
    String SG_DBUSER = scanner.nextLine();
    System.out.print("RDS database password: ");
    String SG_DBUSERPW = scanner.nextLine();

    String myUrl = "jdbc:mysql://" + SG_DBHOST + "/" + SG_DBNAME;

    // SQL Query, and ResulT
    String query, result;

    // Get the first query
    System.out.print("Enter a SQL Query (Input 'EXIT' or 'QUIT' to exit) : ");
    query = scanner.nextLine();

    try {
      // get MySQL connection
      Connection con = DriverManager.getConnection(
        myUrl,
        SG_DBUSER,
        SG_DBUSERPW
      );

      // Loop to receive Query, exit if the query string is 'exit' or 'quit'
      while (
        !(query.equalsIgnoreCase("exit") || query.equalsIgnoreCase("quit"))
      ) {
        result = jedis.get(query); // first try Redis cache
        if (result != null) { // cache hit
          System.out.println("=== Result from Redis Cache ===");
          System.out.println(result);
        } else { // get result from MySQL
          result = getResultsFromMySQL(con, query).toString();
          System.out.println("=== Result directly from MySQL === ");
          System.out.println(result);
          jedis.set(query, result); // put result into Redis Cache
        }
        jedis.expire(query, TTL); // set TTL = 5 seconds of this query in Redis Cache
        System.out.print("Enter a SQL Query : "); // get the next query
        query = scanner.nextLine();
      }
    } catch (SQLException ex) { // illegal SQL Query will cause the program exit
      Logger lgr = Logger.getLogger("JdbcMySQLVersion.class.getName()");
      lgr.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }
}
