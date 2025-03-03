package com.jmp.userservice.unit.model;

import com.jmp.userservice.constant.UserStatus;
import com.jmp.userservice.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.sql.Timestamp;

class UserTest {
    @Test
    void onCreate_ShouldSetRegistrationDateAndStatus_WhenFieldsAreNull() throws Exception{
        User user = new User();
        user.setRegistrationDate(null);
        user.setStatus(null);

        Method method = User.class.getDeclaredMethod("onCreate");
        method.setAccessible(true);
        method.invoke(user);
        assertNotNull(user.getRegistrationDate(), "Registration date should not be null");
        assertEquals(UserStatus.ACTIVE, user.getStatus(), "Status should be set to ACTIVE");
    }

    @Test
    void onCreate_ShouldNotModifyFields_WhenFieldsAreAlreadySet() throws Exception{
        Timestamp existingTimestamp = new Timestamp(System.currentTimeMillis());
        UserStatus existingStatus = UserStatus.ACTIVE;
        User user = new User();
        user.setRegistrationDate(existingTimestamp);
        user.setStatus(existingStatus);
        Method method = User.class.getDeclaredMethod("onCreate");
        method.setAccessible(true);
        method.invoke(user);

        assertEquals(existingTimestamp, user.getRegistrationDate(), "Registration date should be set");
        assertEquals(existingStatus, user.getStatus(), "Status should be set to ACTIVE");
    }
}
