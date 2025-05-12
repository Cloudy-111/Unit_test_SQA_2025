package com.example.gradutionthsis;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.gradutionthsis.dto.NotificationTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotificationTaskTest {

    @Mock
    private SQLiteDatabase mockDatabase;

    private DBHelper dbHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dbHelper = spy(new DBHelper(null)); // context null vì không cần thực sự chạy app
        doReturn(mockDatabase).when(dbHelper).getWritableDatabase();
    }

    @Test
    public void testInsertNotifyTask_success() {
        // Tạo task giả
        NotificationTask fakeTask = new NotificationTask();
        fakeTask.setStatus(1); // 1 = ON
        fakeTask.setDay(1);    // giả định Monday = 1
        fakeTask.setHour(10);
        fakeTask.setMinute(30);

        // Giả lập kết quả trả về từ SQLite
        when(mockDatabase.insert(eq("NOTIFICATIONTASK"), isNull(), any(ContentValues.class))).thenReturn(1L);

        // Gọi hàm cần test
        int result = dbHelper.insertNotifyTask(fakeTask);

        // Kiểm tra kết quả trả về
        assert(result == 1);

        // Kiểm tra insert được gọi đúng
        verify(mockDatabase, times(1)).insert(eq("NOTIFICATIONTASK"), isNull(), any(ContentValues.class));
        verify(mockDatabase, times(1)).close();
    }
}