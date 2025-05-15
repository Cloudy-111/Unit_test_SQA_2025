package com.example.gradutionthsis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmReceiverTest {
    private AlarmReceiver alarmReceiver;

    @Before
    public void setUp() {
        alarmReceiver = new AlarmReceiver();  // Khởi tạo đối tượng AlarmReceiver
    }
    //TC_298_25
    @Test
    public void testValidDate() {
        String validDate = "2021-03-25";  // Ngày hợp lệ theo định dạng yyyy-MM-dd
        String format = "yyyy-MM-dd";
        // Kiểm tra rằng phương thức trả về false với ngày hợp lệ
        assertFalse("Expected date to be valid", alarmReceiver.isDateInvalid(validDate, format));
    }
    //TC_298_26
    @Test
    public void testInvalidDate() {
        String invalidDate = "2021-02-31";  // Ngày không hợp lệ vì tháng 2 không có 31 ngày
        String format = "yyyy-MM-dd";
        // Phương thức phải trả về true vì ngày không hợp lệ
        boolean result = alarmReceiver.isDateInvalid(invalidDate, format);
        assertTrue("Expected date to be invalid", result);
    }
    //TC_298_27
    @Test
    public void testInvalidMonth() {
        String invalidMonthDate1 = "2021-13-25";  // Tháng 13 không tồn tại
        String invalidMonthDate2 = "2021-00-25";  // Tháng 0 không tồn tại
        String format = "yyyy-MM-dd";

        // Kiểm tra tháng 13 là không hợp lệ
        assertTrue("Expected month 13 to be invalid", alarmReceiver.isDateInvalid(invalidMonthDate1, format));

        // Kiểm tra tháng 0 là không hợp lệ
        assertTrue("Expected month 0 to be invalid", alarmReceiver.isDateInvalid(invalidMonthDate2, format));
    }
    //TC_298_28
    @Test
    public void testEmptyDate() {
        String emptyDate = "";  // Chuỗi ngày rỗng
        String format = "yyyy-MM-dd";
        // Ngày rỗng là không hợp lệ
        boolean result = alarmReceiver.isDateInvalid(emptyDate, format);
        assertTrue("Expected empty date to be invalid", result);
    }
    //TC_298_29
    @Test
    public void testInvalidDateFormat() {
        String wrongFormatDate = "25-03-2021";  // Định dạng không đúng (dd-MM-yyyy)
        String format = "yyyy-MM-dd";
        // Phương thức phải nhận biết định dạng sai và trả về true
        boolean result = alarmReceiver.isDateInvalid(wrongFormatDate, format);
        assertTrue("Expected incorrect date format to be invalid", result);
    }
    //TC_298_30
    // Kiểm tra xem ParseException có được ném ra khi parse ngày không hợp lệ không
    @Test(expected = ParseException.class)
    public void testParseExceptionThrownForInvalidDate() throws ParseException {
        String invalidDate = "2021-02-31";  // Ngày không hợp lệ
        String format = "yyyy-MM-dd";

        // Thiết lập định dạng ngày và ép buộc kiểm tra nghiêm ngặt
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        df.setLenient(false);  // Không cho phép lenient (tự sửa ngày sai)
        df.parse(invalidDate);  // Phải ném ra ParseException
    }
    //TC_298_31
    @Test
    public void testGetNotificationId() {
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        int notificationId = alarmReceiver.getNotificationId();
        // Kiểm tra rằng ID thông báo là một số dương (đảm bảo hợp lệ)
        assertTrue(notificationId > 0);
    }
}
