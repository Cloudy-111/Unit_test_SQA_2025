package com.example.gradutionthsis;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.presenter.InjectionDAO;
import com.example.gradutionthsis.presenter.InjectionPresenter;
import com.example.gradutionthsis.DBHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/**
 * Lớp kiểm thử cho InjectionPresenter.
 * Sử dụng Mockito để giả lập DBHelper và InjectionDAO nhằm kiểm tra các tình huống
 * tạo mới, truy xuất và xử lý danh sách các mũi tiêm.
 */
public class InjectionPresenterTest {

    @Mock
    Context mockContext;  // Giả lập context Android

    @Mock
    public DBHelper mockDBHelper;  // Giả lập lớp DBHelper dùng để thao tác dữ liệu

    @Mock
    public InjectionDAO mockInjectionDAO;  // Giả lập lớp DAO phản hồi về giao diện

    private InjectionPresenter injectionPresenter;  // Lớp presenter cần kiểm thử

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Khởi tạo các mock
        injectionPresenter = new InjectionPresenter(mockContext, mockInjectionDAO);
        injectionPresenter.dbHelper = mockDBHelper;  // Gán DBHelper giả vào presenter
    }

    // TC_586_47
    // Test tạo mới mũi tiêm khi insert thành công
    @Test
    public void testCreateInjection_Success() {
        Injection injection = new Injection(6, 30, "Vaccine A", 101);  // Tạo đối tượng Injection hợp lệ

        // Giả lập DB trả về > 0 (insert thành công)
        when(mockDBHelper.insertInjection(injection)).thenReturn(1);

        // Gọi phương thức tạo mới
        injectionPresenter.createInjection(injection);

        // Xác nhận gọi createSuccess()
        verify(mockInjectionDAO, times(1)).createSuccess();
        verify(mockInjectionDAO, never()).createFail();
    }

    // TC_586_48
    // Test tạo mới mũi tiêm khi insert thất bại
    @Test
    public void testCreateInjection_Fail() {
        Injection injection = new Injection(6, 30, "Vaccine A", 101);  // Đối tượng hợp lệ

        // Giả lập DB trả về -1 (insert thất bại)
        when(mockDBHelper.insertInjection(injection)).thenReturn(-1);

        injectionPresenter.createInjection(injection);

        // Xác nhận gọi createFail()
        verify(mockInjectionDAO, times(1)).createFail();
        verify(mockInjectionDAO, never()).createSuccess();
    }

    // TC_586_49
    // Test khi danh sách mũi tiêm trả về rỗng
    @Test
    public void testGetAllInjections_EmptyList() {
        // Giả lập DB trả về danh sách rỗng
        when(mockDBHelper.getAllInjections()).thenReturn(new ArrayList<>());

        boolean result = injectionPresenter.getAllInjections();
// Kỳ vọng trả về true khi danh sách rỗng
        assert(result == true);
    }

    // TC_586_50
    // Test khi danh sách mũi tiêm trả về có phần tử
    @Test
    public void testGetAllInjections_NonEmptyList() {
        // Giả lập DB trả về danh sách có phần tử
        when(mockDBHelper.getAllInjections()).thenReturn(new ArrayList<Injection>() {{
            add(new Injection(6, 30, "Vaccine A", 101));
        }});

        boolean result = injectionPresenter.getAllInjections();

        // Kỳ vọng trả về false khi danh sách không rỗng
        assert(result == false);
    }

    // TC_586_51
    // Test lấy mũi tiêm theo ID thành công
    @Test
    public void testGetInjectionById_Success() {
        int idInjection = 101;

        // Giả lập trả về đối tượng hợp lệ theo ID
        Injection mockInjection = new Injection(6, 30, "Vaccine A", idInjection);
        when(mockDBHelper.getInjectionById(idInjection)).thenReturn(mockInjection);

        Injection result = injectionPresenter.getInjectionById(idInjection);

        // Kỳ vọng trả về đúng đối tượng đã giả lập
        assert(result.getIdInjection() == mockInjection.getIdInjection());
    }

    // TC_586_52
    // Test lấy mũi tiêm theo ID nhưng không tìm thấy
    @Test
    public void testGetInjectionById_Fail() {
        int idInjection = 101;

        // Giả lập không tìm thấy (trả về null)
        when(mockDBHelper.getInjectionById(idInjection)).thenReturn(null);

        Injection result = injectionPresenter.getInjectionById(idInjection);

        // Kỳ vọng kết quả là null
        assert(result == null);
    }
}