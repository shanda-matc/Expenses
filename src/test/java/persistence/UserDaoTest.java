
package persistence;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type User dao test.
 */
class UserDaoTest {

    /**
     * The Dao.
     */
    UserDao dao;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        test.util.Database database = test.util.Database.getInstance();
        database.runSQL("cleandb.sql");

        dao = new UserDao();
    }

    /**
     * Gets all users success.
     */
    @Test
    void getAllUsersSuccess() {
        int expectedSize = 3;
        assertEquals(expectedSize, dao.getAllUsers().size());
    }

    /**
     * Gets by user name.
     */
    @Test
    void getByUserNameSuccess() {
        List<User> users = dao.getByUserName("userName");
        assertEquals(3, users.size());
    }

    /**
     * Insert success.
     */
    @Test
    void insertSuccess() {
        User newUser = new User("shanda@gmail.com","Secret123", "Shilpa", "shanda.jpg", "Shanda");
        int id = dao.insert(newUser);
        assertNotEquals(0,id);
        User retrievedUser= dao.getById(newUser.getId());
        assertNotNull(retrievedUser);
    }

    /**
     * Save or update success.
     */
    @Test
    void saveOrUpdateSuccess() {
        /*String newLastName = "Davis";
        User userToUpdate = dao.getById(3);
        userToUpdate.setLastName(newLastName);
        dao.saveOrUpdate(userToUpdate);
        User retrievedUser = dao.getById(3);
        assertEquals(newLastName, retrievedUser.getLastName());*/
    }

    /**
     * Delete success.
     */
    @Test
    void deleteSuccess() {
        dao.delete(dao.getById(3));
        assertNull(dao.getById(3));
    }
}