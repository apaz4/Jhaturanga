package jhaturanga.test.model.user.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jhaturanga.model.user.datastorage.UsersDataStorageJsonStrategy;
import jhaturanga.model.user.datastorage.UsersDataStorageStrategy;
import jhaturanga.commons.DirectoryConfigurations;
import jhaturanga.model.user.UserImpl;

class UserStorageTest {

    private static final String U1 = "user1";
    private static final String U2 = "user2";
    private static final String U3 = "user3";
    private UsersDataStorageStrategy users;

    @BeforeAll
    static void setUp() throws IOException  {
        if (Files.exists(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH))) {
            Files.copy(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH), 
                    Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH + ".tmp"));
        }
    }

    @BeforeEach
    void init() throws IOException {
        Files.deleteIfExists(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH));
        this.users = new UsersDataStorageJsonStrategy();
        this.users.put(new UserImpl(U1, "password1", 0, 0, 0));
        this.users.put(new UserImpl(U2, "password2", 0, 0, 0));
        this.users.put(new UserImpl(U3, "password3", 0, 0, 0));
    }

    @Test
    void reading() throws IOException {
        assertEquals(Optional.of(new UserImpl(U1, "password1", 0, 0, 0)), this.users.getUserByUsername(U1));
        assertEquals(Optional.of(new UserImpl(U2, "password2", 0, 0, 0)), this.users.getUserByUsername(U2));
        assertEquals(Optional.of(new UserImpl(U3, "password3", 0, 0, 0)), this.users.getUserByUsername(U3));
        assertEquals(Optional.empty(), this.users.getUserByUsername("user4"));
    }

    @Test
    void isPresent() throws IOException {
        assertTrue(this.users.isPresent(U1));
        assertFalse(this.users.isPresent("user4"));
    }

    @Test
    void size() throws IOException {
        assertEquals(3, this.users.getAllUsers().size());
    }

    @Test
    void remove() throws IOException {
        this.users.remove(U1);
        assertEquals(Optional.empty(), this.users.getUserByUsername(U1));
    }

    @Test
    void save() throws IOException {
        this.users = new UsersDataStorageJsonStrategy();
        reading();
    }

    @AfterEach
    void end() throws IOException {
        Files.deleteIfExists(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH));
    }

    @AfterAll
    static void restore() throws IOException {
        if (Files.exists(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH + ".tmp"))) {
            Files.move(Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH + ".tmp"),
                    Path.of(DirectoryConfigurations.USERS_DATA_FILE_PATH));
        }
    }
}
