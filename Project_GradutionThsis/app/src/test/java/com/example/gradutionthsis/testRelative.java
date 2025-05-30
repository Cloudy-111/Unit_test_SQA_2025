package com.example.gradutionthsis;

import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.presenter.RelativeDAO;
import com.example.gradutionthsis.presenter.RelativePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class testRelative {
    @Mock
    Context mockContext;

    @Mock
    private DBHelper mockDBHelper;
    @Mock
    private RelativeDAO mockRelativeDAO;
    @Mock
    private RelativePresenter relativePresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        relativePresenter = new RelativePresenter(mockContext, mockRelativeDAO);
        relativePresenter.dbHelper = mockDBHelper;
        relativePresenter.relativeDAO = mockRelativeDAO;
    }

    @Test//TC_226_08
    // Test trường hợp tạo Relative thành công
    public void test_create_relative_success(){
        // Đặt DB trả về ID
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(new Relative());  // Gọi hàm tạo trong presenter

        verify(mockRelativeDAO).createSuccess();

        verify(mockRelativeDAO, never()).createFail();
    }

    @Test//TC_226_09
    // Test trường hợp tạo Relative thất bại
    public void test_create_relative_failed(){
        // Đặt DB trả về 0 (thất bại)
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(0);

        relativePresenter.create(new Relative());

        verify(mockRelativeDAO).createFail();

        verify(mockRelativeDAO, never()).createSuccess();
    }


    @Test//TC_226_10
    // Test cập nhật thông tin Relative thành công
    public void test_update_relative_success(){
        // Đặt cập nhật thành công
        when(mockDBHelper.updateRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.update(new Relative());

        // Thành công thì gọi callback
        verify(mockRelativeDAO).updateSuccess();
        verify(mockRelativeDAO, never()).updateFail();
    }

    @Test//TC_226_11
    // Test cập nhật thông tin Relative thất bại
    public void test_update_relative_failed(){
        // Đặt cập nhật thất bại
        when(mockDBHelper.updateRelative(any(Relative.class))).thenReturn(0);

        relativePresenter.update(new Relative());

        // Gọi đến callback lỗi
        verify(mockRelativeDAO).updateFail();
        verify(mockRelativeDAO, never()).updateSuccess();
    }

    @Test//TC_226_12
    // Test xoá Relative thành công
    public void test_delete_relative_success(){
        // Xoá thành công
        when(mockDBHelper.deleteRelative(anyInt())).thenReturn(true);

        relativePresenter.delete(1);

        // callback xóa thành công
        verify(mockRelativeDAO).deleteSuccess();
        verify(mockRelativeDAO, never()).deleteFail();
    }

    @Test//TC_226_13
    // Test xoá Relative thất bại
    public void test_delete_relative_failed(){
        // Xoá thất bại
        when(mockDBHelper.deleteRelative(anyInt())).thenReturn(false);

        relativePresenter.delete(1);

        // callback lỗi khi xóa
        verify(mockRelativeDAO).deleteFail();
        verify(mockRelativeDAO, never()).deleteSuccess();
    }

    @Test//TC_226_14
    // Test lấy Relative cuối cùng khi danh sách có phần tử
    public void test_getFinalRelative_success(){
        List<Relative> fakeList = new ArrayList<>();
        Relative last = new Relative("last_name", "last_nick_name", "last_gender", "last_birthdate");
        fakeList.add(new Relative("fullname", "mick_name", "gender", "birthdate"));
        fakeList.add(last); // Thêm 2 đối tượng Relative
        when(mockDBHelper.getAllRelatives()).thenReturn((ArrayList<Relative>) fakeList);

        relativePresenter.dbHelper = mockDBHelper;
        Relative result = relativePresenter.getRelativeFinal();

        assertNotNull(result);
        assertEquals("last_name", result.getFullName());
    }

    @Test//TC_226_15
    // Test lấy Relative cuối cùng khi không có dữ liệu
    public void test_getFinalRelative_failed(){
        // Danh sách rỗng
        when(mockDBHelper.getAllRelatives()).thenReturn(new ArrayList<>());

        relativePresenter.dbHelper = mockDBHelper;
        Relative result = relativePresenter.getRelativeFinal();

        assertNull(result); // Kết quả phải là null
    }

    @Test//TC_226_16
    public void test_create_relative_with_future_birthdate() {
        Relative relative = new Relative("Name", "Nick", "Male", "2099-01-01");
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(relative);

        verify(mockRelativeDAO).createSuccess(); // => Lỗi vì dữ liệu sai nhưng vẫn chấp nhận
    }


    @Test//TC_226_17
    public void test_create_relative_with_empty_fullname() {
        Relative relative = new Relative("", "Nick", "Male", "2010-01-01");
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(relative);

        verify(mockRelativeDAO).createSuccess(); // => Lỗi
    }

    @Test//TC_226_18
    public void test_create_relative_with_empty_nickname() {
        Relative relative = new Relative("Name", "", "Male", "2010-01-01");
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(relative);

        verify(mockRelativeDAO).createSuccess(); // Nếu nickname bắt buộc → đây là lỗi
    }

    @Test//TC_226_19
    public void test_create_relative_with_empty_gender() {
        Relative relative = new Relative("Name", "Nick", "", "2010-01-01");
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(relative);

        verify(mockRelativeDAO).createSuccess(); // => Lỗi nếu không kiểm tra gender
    }

    @Test//TC_226_20
    public void test_create_relative_with_empty_birthdate() {
        Relative relative = new Relative("Name", "Nick", "Male", "");
        when(mockDBHelper.insertRelative(any(Relative.class))).thenReturn(1);

        relativePresenter.create(relative);

        verify(mockRelativeDAO).createSuccess(); // => Lỗi vì thiếu ngày sinh
    }


}
