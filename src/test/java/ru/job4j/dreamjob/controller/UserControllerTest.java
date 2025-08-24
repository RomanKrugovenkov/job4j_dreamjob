package ru.job4j.dreamjob.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);

    }

    @Test
    public void whenRequestRegisterListPageThenGetPageWithRegister() {
        var view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostUserThenRedirectToVacanciesPage() throws Exception {
        var user = new User("test@mail.com", "name1", "12345");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).isEqualTo(user);
    }

    /*@Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage2() {
        var expectedException = new RuntimeException("User with this email does not exist");
        when(userService.save(any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(model, new User());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }*/

    @Test
    public void whenRequest1VacancyPageThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("User with this email does not exist");
        when(userService.save(any())).thenThrow(expectedException);
        var model = new ConcurrentModel();

        assertThatThrownBy(() -> userController.register(model, new User()))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("User with this email does not exist");
    }

    @Test
    public void whenRequestLoginPageThenGetPageWithLogin() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenPostLoginUserThenRedirectToVacanciesPage() throws Exception {
        var user = new User("test@mail.com", "name1", "12345");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);
        doNothing().when(session).setAttribute(any(), userArgumentCaptor.capture());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRequestLogoutPageThenGetPageWithLogin() {
        HttpSession session = mock(HttpSession.class);
        var view = userController.logout(session);

        verify(session).invalidate();
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}