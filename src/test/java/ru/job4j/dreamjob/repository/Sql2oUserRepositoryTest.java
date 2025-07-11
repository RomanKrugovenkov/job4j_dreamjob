package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {
    private static Sql2o sql2o;
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearAllUsers() {
        sql2o.open().createQuery("DELETE FROM users").executeUpdate();
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User("test@mail.ru", "Roman", "123456"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword("test@mail.ru", "123456");
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveSameEmailTwice() {
        var user1 = sql2oUserRepository.save(new User("test@mail.ru", "Roman", "123456"));
        var user2 = sql2oUserRepository.save(new User("test@mail.ru", "Petr", "654321"));
        var query1 = sql2oUserRepository.findByEmailAndPassword("test@mail.ru", "123456");
        var query2 = sql2oUserRepository.findByEmailAndPassword("test@mail.ru", "654321");
        assertThat(user1).usingRecursiveComparison().isEqualTo(query1);
        assertThat(user2).isEqualTo(empty());
        assertThat(query2).isEqualTo(empty());
    }
}