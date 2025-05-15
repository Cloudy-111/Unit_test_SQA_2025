
package com.example.gradutionthsis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.presenter.HealthDAO;
import com.example.gradutionthsis.presenter.HealthPresenter;
import com.example.gradutionthsis.DBHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Lớp kiểm thử cho HealthPresenter.
 * Sử dụng Mockito để giả lập DBHelper và HealthDAO để kiểm tra phản hồi của hệ thống
 * trong các trường hợp tạo, cập nhật và xóa dữ liệu sức khỏe.
 */
public class HealthPresenterTest {

    @Mock
    private DBHelper mockDBHelper;  // Giả lập lớp DBHelper - xử lý tương tác với cơ sở dữ liệu

    @Mock
    private HealthDAO mockHealthDAO;  // Giả lập DAO - xử lý phản hồi từ tầng giao diện

    @InjectMocks
    private HealthPresenter healthPresenter;  // Inject các mock vào presenter

    @Before
    public void setUp() {
        // Khởi tạo các mock trước khi chạy test
        MockitoAnnotations.openMocks(this);
        healthPresenter.dbHelper = mockDBHelper;
        healthPresenter.healthDAO = mockHealthDAO;
    }
    //TC_298_13
    // Test khi insertHealth thất bại (trả về -1)
    @Test
    public void testCreateHealth_insertFail() {
        // Giả lập DB trả về -1 để mô phỏng lỗi khi insert
        when(mockDBHelper.insertHealth(any(Health.class))).thenReturn(-1);

        // Gọi phương thức createHealth
        healthPresenter.createHealth(new Health(), 1);

        // Xác nhận rằng phương thức createFail được gọi
        verify(mockHealthDAO, times(1)).createFail();
        // Và createSuccess không được gọi
        verify(mockHealthDAO, never()).createSuccess();
    }
    //TC_298_14
    // Test khi trọng lượng < 0 thì createFail được gọi
    @Test
    public void testCreateHealth_InvalidWeight_Fail() {
        Health invalidHealth = mock(Health.class);  // Giả lập đối tượng Health
        when(invalidHealth.getWeight()).thenReturn(-70.0); // Trọng lượng âm là không hợp lệ

        healthPresenter.createHealth(invalidHealth, 1);

        // Kiểm tra: hệ thống phải gọi createFail
        verify(mockHealthDAO, times(1)).createFail();
        verify(mockHealthDAO, never()).createSuccess();
    }
    //TC_298_15
    // Test khi thời gian là null thì cũng bị từ chối
    @Test
    public void testCreateHealth_InvalidTime_Fail() {
        Health invalidHealth = mock(Health.class);
        when(invalidHealth.getTime()).thenReturn(null);  // Thời gian bị thiếu

        healthPresenter.createHealth(invalidHealth, 1);

        // Hệ thống không chấp nhận thời gian null
        verify(mockHealthDAO, times(1)).createFail();
        verify(mockHealthDAO, never()).createSuccess();
    }
    //TC_298_16
    // Test khi insert thành công
    @Test
    public void testCreateHealth_insertSuccess() {
        // Giả lập trả về ID (insert thành công)
        when(mockDBHelper.insertHealth(any(Health.class))).thenReturn(1);

        healthPresenter.createHealth(new Health(), 1);

        // Hệ thống nên gọi createSuccess
        verify(mockHealthDAO, times(1)).createSuccess();
        verify(mockHealthDAO, never()).createFail();
    }
    //TC_298_17
    // Test khi cập nhật thất bại (trả về -1)
    @Test
    public void testUpdateHealth_updateFail() {
        when(mockDBHelper.updateHealth(any(Health.class))).thenReturn(-1);

        healthPresenter.updateHealth(new Health());

        // Cập nhật thất bại thì gọi updateFail
        verify(mockHealthDAO, times(1)).updateFail();
        verify(mockHealthDAO, never()).updateSuccess();
    }
    //TC_298_18
    // Test khi không tìm thấy bản ghi để update (trả về 0)
    @Test
    public void testUpdateHealth_RecordNotFound_Fail() {
        Health invalidHealth = mock(Health.class);
        when(mockDBHelper.updateHealth(invalidHealth)).thenReturn(0); // Không bản ghi nào được cập nhật

        healthPresenter.updateHealth(invalidHealth);

        // Gọi updateFail vì không có bản ghi phù hợp
        verify(mockHealthDAO, times(1)).updateFail();
        verify(mockHealthDAO, never()).updateSuccess();
    }
    //TC_298_19
    // Test khi không có quyền cập nhật (trả về -2)
    @Test
    public void testUpdateHealth_NoPermission_Fail() {
        when(mockDBHelper.updateHealth(any(Health.class))).thenReturn(-2);  // Mô phỏng lỗi quyền

        healthPresenter.updateHealth(new Health());

        verify(mockHealthDAO, times(1)).updateFail();
        verify(mockHealthDAO, never()).updateSuccess();
    }
    //TC_298_20
    // Test khi cập nhật thành công
    @Test
    public void testUpdateHealth_updateSuccess() {
        when(mockDBHelper.updateHealth(any(Health.class))).thenReturn(1);  // Thành công

        healthPresenter.updateHealth(new Health());

        verify(mockHealthDAO, times(1)).updateSuccess();
        verify(mockHealthDAO, never()).updateFail();
    }
    //TC_298_21
    // Test khi xóa thất bại (trả về false)
    @Test
    public void testDeleteHealth_deleteFail() {
        when(mockDBHelper.deleteHealth(1)).thenReturn(false);  // Mô phỏng lỗi xóa

        healthPresenter.deleteHealth(1);

        verify(mockHealthDAO, times(1)).deleteFail();
        verify(mockHealthDAO, never()).deleteSuccess();
    }
    //TC_298_22
    // Test khi xóa với ID không tồn tại
    @Test
    public void testDeleteHealth_InvalidId_Fail() {
        when(mockDBHelper.deleteHealth(999)).thenReturn(false); // ID không hợp lệ

        healthPresenter.deleteHealth(999);

        verify(mockHealthDAO, times(1)).deleteFail();
        verify(mockHealthDAO, never()).deleteSuccess();
    }
    //TC_298_23
    // Test khi không có quyền xóa
    @Test
    public void testDeleteHealth_NoPermission_Fail() {
        when(mockDBHelper.deleteHealth(1)).thenReturn(false); // Không có quyền

        healthPresenter.deleteHealth(1);

        verify(mockHealthDAO, times(1)).deleteFail();
        verify(mockHealthDAO, never()).deleteSuccess();
    }
    //TC_298_24
    // Test khi xóa thành công
    @Test
    public void testDeleteHealth_deleteSuccess() {
        when(mockDBHelper.deleteHealth(1)).thenReturn(true); // Thành công

        healthPresenter.deleteHealth(1);

        verify(mockHealthDAO, times(1)).deleteSuccess();
        verify(mockHealthDAO, never()).deleteFail();
    }
}
