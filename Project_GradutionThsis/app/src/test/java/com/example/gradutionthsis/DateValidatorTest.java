package com.example.gradutionthsis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateValidatorTest {
    private AlarmReceiver alarmReceiver;

    @Before
    public void setUp() {
        alarmReceiver = new AlarmReceiver();  // Khởi tạo đối tượng AlarmReceiver
    }

    @Test
    public void testValidDate() {
        String validDate = "2021-03-25";  // Ngày hợp lệ
        String format = "yyyy-MM-dd";
        assertFalse("Expected date to be valid", alarmReceiver.isDateInvalid(validDate, format));  // Kỳ vọng trả về false
    }

    @Test
    public void testInvalidDate() {
        String invalidDate = "2021-02-31";  // Ngày không hợp lệ (31 tháng 2)
        String format = "yyyy-MM-dd";
        boolean result = alarmReceiver.isDateInvalid(invalidDate, format);
        assertTrue("Expected date to be invalid", result);  // Kỳ vọng trả về true
    }

    @Test
    public void testInvalidMonth() {
        String invalidMonthDate1 = "2021-13-25";  // Tháng 13 không hợp lệ
        String invalidMonthDate2 = "2021-00-25";  // Tháng 0 không hợp lệ
        String format = "yyyy-MM-dd";

        // Kiểm tra tháng 13
        assertTrue("Expected month 13 to be invalid", alarmReceiver.isDateInvalid(invalidMonthDate1, format));

        // Kiểm tra tháng 0
        assertTrue("Expected month 0 to be invalid", alarmReceiver.isDateInvalid(invalidMonthDate2, format));
    }

    @Test
    public void testEmptyDate() {
        String emptyDate = "";  // Ngày trống
        String format = "yyyy-MM-dd";
        boolean result = alarmReceiver.isDateInvalid(emptyDate, format);
        assertTrue("Expected empty date to be invalid", result);  // Kỳ vọng trả về true
    }

    @Test
    public void testInvalidDateFormat() {
        String wrongFormatDate = "25-03-2021";  // Ngày không đúng định dạng
        String format = "yyyy-MM-dd";
        boolean result = alarmReceiver.isDateInvalid(wrongFormatDate, format);
        assertTrue("Expected incorrect date format to be invalid", result);  // Kỳ vọng trả về true
    }

    // Kiểm tra xử lý ParseException cho ngày tháng không hợp lệ
    @Test(expected = ParseException.class)
    public void testParseExceptionThrownForInvalidDate() throws ParseException {
        String invalidDate = "2021-02-31";  // Ngày không hợp lệ
        String format = "yyyy-MM-dd";

        // Gọi trực tiếp parse để kiểm tra ngoại lệ ParseException
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        df.setLenient(false);
        df.parse(invalidDate);  // Phương thức này phải ném ra ParseException
    }
}
