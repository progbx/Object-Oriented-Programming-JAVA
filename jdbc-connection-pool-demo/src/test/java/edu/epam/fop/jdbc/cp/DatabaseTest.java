package edu.epam.fop.jdbc.cp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceCamelCase.class)
class DatabaseTest {

	private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
//	private static final String JDBC_URL = "jdbc:derby:memory:test;create=true";

	private static final int GROUP_NUM = 3;
	private static final int STUD_NUM = 10;

	private static String[] groupNames = new String[GROUP_NUM];
	private static String[] firstNames = new String[STUD_NUM];
	private static String[] lastNames = new String[STUD_NUM];
	private static int[] groupIds = new int[STUD_NUM];

	private static Connection connection;

	@BeforeAll
	static void init() throws SQLException {
		connection = DriverManager.getConnection(JDBC_URL);
		Arrays.setAll(groupNames, s -> UUID.randomUUID().toString());
		Arrays.setAll(firstNames, s -> UUID.randomUUID().toString());
		Arrays.setAll(lastNames, i -> UUID.randomUUID().toString());
		Random rnd = new Random();
		Arrays.setAll(groupIds, s -> rnd.nextInt(groupNames.length) + 1);
		try (Statement statement = connection.createStatement()) {
			statement.execute("""
					CREATE TABLE groups (
					  id INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
					  group_name VARCHAR(255) not null
					)
					""");
			statement.execute("""
					CREATE TABLE students (
					  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
					  first_name VARCHAR(255) not null,
					  last_name VARCHAR(255) not null,
					  group_id INTEGER,
					  FOREIGN KEY (group_id) REFERENCES groups (id)
					)
					""");
			for (String group : groupNames) {
				statement.execute("INSERT INTO groups (group_name) VALUES ('" + group + "')");
			}
			for (int i = 0; i < STUD_NUM; ++i) {
				statement.execute("INSERT INTO students (first_name, last_name, group_id) VALUES ('" + firstNames[i]
						+ "', '" + lastNames[i] + "', " + groupIds[i] + ")");
			}
		}
	}

	@AfterAll
	static void shutdown() throws SQLException {
		connection.close();
	}

	@Test
	void testFetchStudents() throws Exception {
		String expected = IntStream.range(0, firstNames.length)
				.mapToObj(i -> firstNames[i] + " " + lastNames[i] + " " + groupNames[groupIds[i] - 1]).sorted()
				.collect(Collectors.joining(", "));

		final String fetchStudentsQuery = """
				SELECT students.*, groups.group_name FROM students JOIN groups ON students.group_id = groups.id
				""";

		String dbUrl = JDBC_URL;
		String dbUser = "";
		String dbPassword = "";
		int poolSize = 5;

		try (ConnectionPool connectionPool = ConnectionPool.create(dbUrl, dbUser, dbPassword, poolSize)) {
			try (Connection connection = connectionPool.takeConnection()) {
				List<Student> students = new ArrayList<>();
				try (Statement statement = connection.createStatement();
						ResultSet resultSet = statement.executeQuery(fetchStudentsQuery)) {
					while (resultSet.next()) {
						int id = resultSet.getInt("id");
						String firstName = resultSet.getString("first_name");
						String lastName = resultSet.getString("last_name");
						String groupName = resultSet.getString("group_name");
						int groupId = resultSet.getInt("group_id");
						Group group = new Group(groupId, groupName);
						Student student = new Student(id, firstName, lastName, group);
						students.add(student);
					}
				}
				assertNotNull(students, "Student list should not be null");

				String actual = students.stream().map(Object::toString).sorted().collect(Collectors.joining(", "));
				assertEquals(expected, actual, "Student list doesn't match");
			}
		}
	}
}