package edu.shanda.persistence;

import edu.shanda.entity.User;
import edu.shanda.test.util.Database;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

class UserTest {
    /**
     * The Dao.
     */
    GenericDao dao;
    /**
     * The Database utility.
     */
    Database database;
    /**
     * The Users.
     */
    List<User> users;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        dao = new GenericDao(User.class);
        database = new Database();
        database.runSQL("cleandb.sql");
        database.runSQL("createTestData.sql");
        users = dao.getAll();
    }

    /**
     * Test get all users.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllUsers() throws Exception {
        assertNotNull(users);
        assertTrue(users.size() > 0);
        assertFalse(users.get(0).getUsername().equals(""));
    }

    /**
     * Test update user.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateUser() throws Exception {
        User user = users.get(0);
        int id = user.getUserId();
        String updateValue = LocalDate.now().toString();
        String emailBeforeUpdate = user.getEmail();
        // it would be a good idea to test each value like this

        user.setEmail(user.getEmail() + updateValue);

        dao.saveOrUpdate(user);

        User updatedUser = (User) dao.getById(id);

        assertEquals(user, updatedUser);

    }

    /**
     * Test delete user.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeleteUser() throws Exception {
        int sizeBeforeDelete = users.size();
        User userToDelete = users.get(0);
        int id = userToDelete.getUserId();
        dao.delete(userToDelete);
        int sizeAfterDelete = dao.getAll().size();

        User deletedUser = (User) dao.getById(id);

        assertEquals(sizeBeforeDelete - 1, sizeAfterDelete);
        assertNull(deletedUser);
    }


    /**
     * Test add user.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAddUser() throws Exception {

        int insertedUserId = 0;

        User user = new User();
        user.setUsername("Unit");
        user.setEmail("UserDaoTesterB@gmail.com");

        insertedUserId = dao.insert(user);
        User retrievedUser = (User) dao.getById(insertedUserId);

        assertTrue(insertedUserId > 0);
        assertEquals(user, retrievedUser);
    }

    /**
     * Test get all users with last name exact.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllUsersWithLastNameExact() throws Exception {
        users = dao.findByPropertyEqual("lastName", "Test1");
        assertTrue(users.size() > 0);
        assertTrue(users.get(0).getUsername().equals("Unit1"));
    }
}