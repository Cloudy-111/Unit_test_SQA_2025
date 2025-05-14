package com.example.gradutionthsis;

import static org.junit.Assert.assertEquals;

import com.example.gradutionthsis.service.MyService;

import org.junit.Test;
import java.util.Calendar;

public class MyServiceTest {

    //TC32
    // Kiểm tra ngày cộng thêm 1 ngày (dayNotify = 1)
    @Test
    public void testGetCurrentDay_AddOneDay() {
        MyService myService = new MyService();

        // Lấy ngày hiện tại và cộng thêm 1 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Sử dụng add để tự động điều chỉnh tháng/năm
        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        int newMonth = calendar.get(Calendar.MONTH) + 1; // tháng tính từ 0, cộng thêm 1
        int newYear = calendar.get(Calendar.YEAR);

        String expectedDate = newDay + "/" + newMonth + "/" + newYear;

        // Kiểm tra phương thức với dayNotify = 1
        String result = myService.getCurrentDay(1);

        // Đảm bảo ngày trả về đúng với ngày cộng thêm 1
        assertEquals("Ngày phải cộng thêm đúng 1 ngày", expectedDate, result);
    }
    //TC33
    // Kiểm tra ngày cộng thêm 30 ngày và tháng có thể thay đổi
    @Test
    public void testGetCurrentDay_NextMonth() {
        MyService myService = new MyService();

        // Lấy ngày hiện tại và cộng thêm 30 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30); // Sử dụng add để tự động điều chỉnh tháng/năm

        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        int newMonth = calendar.get(Calendar.MONTH) + 1; // tháng tính từ 0, cộng thêm 1
        int newYear = calendar.get(Calendar.YEAR);

        String expectedDate = newDay + "/" + newMonth + "/" + newYear;

        // Kiểm tra với dayNotify = 30
        String result = myService.getCurrentDay(30);

        // Đảm bảo ngày cộng thêm 30 ngày và tháng có thể thay đổi chính xác
        assertEquals("Ngày cộng thêm đúng 30 ngày và tháng phải thay đổi chính xác", expectedDate, result);
    }
    //TC34
    // Kiểm tra ngày cộng thêm 365 ngày và năm phải thay đổi chính xác
    @Test
    public void testGetCurrentDay_NextYear() {
        MyService myService = new MyService();

        // Lấy ngày hiện tại và cộng thêm 365 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 365); // Sử dụng add để tự động điều chỉnh tháng/năm

        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        int newMonth = calendar.get(Calendar.MONTH) + 1;
        int newYear = calendar.get(Calendar.YEAR);

        String expectedDate = newDay + "/" + newMonth + "/" + newYear;

        // Kiểm tra với dayNotify = 365
        String result = myService.getCurrentDay(365);

        // Đảm bảo ngày cộng thêm đúng 365 ngày và năm phải thay đổi chính xác
        assertEquals("Ngày cộng thêm đúng 365 ngày và năm phải thay đổi chính xác", expectedDate, result);
    }
    //TC35
    // Kiểm tra ngày rỗng
    @Test
    public void testSetFormat_EmptyDate() {
        MyService myService = new MyService();

        // Ngày rỗng
        String inputDate = "";
        String expectedDate = "";  // Ngày rỗng không hợp lệ

        // Kiểm tra với chuỗi rỗng
        String result = myService.setFormat(inputDate);

        // Đảm bảo rằng kết quả là chuỗi rỗng
        assertEquals("Ngày rỗng phải trả về chuỗi rỗng", expectedDate, result);
    }
    //TC36
    // Kiểm tra ngày có định dạng không hợp lệ
    @Test
    public void testSetFormat_InvalidDateFormat() {
        MyService myService = new MyService();

        // Ngày không hợp lệ (ví dụ: "31-12-2025")
        String inputDate = "31-12-2025";  // Sai định dạng
        String expectedDate = "2025-12-31";  // Chuyển đổi sang đúng định dạng

        // Kiểm tra với ngày không hợp lệ
        String result = myService.setFormat(inputDate);

        // Đảm bảo rằng phương thức chuyển đúng ngày
        assertEquals("Ngày có định dạng sai phải chuyển đổi đúng", expectedDate, result);
    }
    //TC37
    // Kiểm tra ngày hợp lệ
    @Test
    public void testSetFormat_ValidDate() {
        MyService myService = new MyService();

        // Ngày hợp lệ "dd/MM/yyyy"
        String inputDate = "31/12/2025";
        String expectedDate = "2025-12-31";  // Chuyển đổi đúng

        // Kiểm tra với ngày hợp lệ
        String result = myService.setFormat(inputDate);

        // Đảm bảo rằng ngày được chuyển đổi đúng
        assertEquals("Ngày hợp lệ phải chuyển đổi đúng định dạng", expectedDate, result);
    }
    //TC38
    // Kiểm tra ngày cộng thêm 30 ngày và tháng có thể thay đổi chính xác
    @Test
    public void testGetCurrentDay_NextMonth_Updated() {
        MyService myService = new MyService();

        // Lấy ngày hiện tại và cộng thêm 30 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30); // Sử dụng add để tự động điều chỉnh tháng/năm

        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        int newMonth = calendar.get(Calendar.MONTH) + 1; // tháng tính từ 0, cộng thêm 1
        int newYear = calendar.get(Calendar.YEAR);

        String expectedDate = newDay + "/" + newMonth + "/" + newYear;

        // Kiểm tra với dayNotify = 30
        String result = myService.getCurrentDay(30);

        // Kiểm tra kết quả
        assertEquals("Ngày cộng thêm 30 ngày và tháng có thể thay đổi chính xác", expectedDate, result);
    }
}
