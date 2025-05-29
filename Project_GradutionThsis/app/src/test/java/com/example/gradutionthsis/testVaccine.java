package com.example.gradutionthsis;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.example.gradutionthsis.dto.Vaccine;
import com.example.gradutionthsis.presenter.VaccineDAO;
import com.example.gradutionthsis.presenter.VaccinePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class testVaccine {
    // Mock context Android
    @Mock
    Context mockContext;

    // Mock lớp DBHelper để không tương tác thật với CSDL
    @Mock
    private DBHelper mockDBHelper;

    // Mock interface VaccineDAO để kiểm tra việc gọi các callback
    @Mock
    private VaccineDAO mockVaccineDAO;

    private VaccinePresenter vaccinePresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vaccinePresenter = new VaccinePresenter(mockContext, mockVaccineDAO);
        vaccinePresenter.dbHelper = mockDBHelper;
        vaccinePresenter.vaccineDAO = mockVaccineDAO;
    }

    @Test//TC_226_01
    // Test case kiểm tra xem tạo vaccine lỗi
    public void testCreateVaccine_insertFail() {
        // Đặt DB trả về -1 khi insert
        when(mockDBHelper.insertVaccine(any(Vaccine.class))).thenReturn(-1);

        // Gọi hàm cần test trong presenter
        vaccinePresenter.createVaccine(new Vaccine());

        // Kiểm tra callback createFail() đã được gọi 1 lần vì insert thất bại
        verify(mockVaccineDAO, times(1)).createFail();

        // Kiểm tra callback createSuccess() KHÔNG được gọi vì không thành công
        verify(mockVaccineDAO, never()).createSuccess();
    }

    @Test//TC_226_02
    // Test case kiểm tra xem tạo vaccine thành công
    public void testCreateVaccine_insertSuccess() {
        // Đặt DB trả về 1 khi insert
        when(mockDBHelper.insertVaccine(any(Vaccine.class))).thenReturn(1);

        // Gọi đến hàm cần test trong presenter
        vaccinePresenter.createVaccine(new Vaccine());

        // Kiểm tra callback createSuccess() đã được gọi 1 lần vì insert thành công
        verify(mockVaccineDAO, times(1)).createSuccess();

        // Kiểm tra callback createFail() KHÔNG được gọi
        verify(mockVaccineDAO, never()).createFail();
    }

    @Test//TC_226_03
    // Test case kiểm tra lấy danh sách vaccine khi danh sách rỗng
    public void testGetAllVaccine_emptyList(){
        // Đặt DB trả về danh sách rỗng
        when(mockDBHelper.getAllVaccines()).thenReturn(new ArrayList<>());
        vaccinePresenter.dbHelper = mockDBHelper;

        boolean result = vaccinePresenter.getAllVaccine();

        // Nêú mà danh sách rỗng, trả về True
        assertTrue(result);
    }

    @Test//TC_226_04
    // Test case kiểm tra khi danh sách vaccine trả về có phần tử
    public void testGetAllVaccine_nonEmptyList() {
        // Tạo danh sách giả gồm 2 phần tử Vaccine
        ArrayList<Vaccine> fakeList = new ArrayList<>(Arrays.asList(new Vaccine(), new Vaccine()));

        // Đặt DB trả về danh sách không rỗng
        when(mockDBHelper.getAllVaccines()).thenReturn(fakeList);
        vaccinePresenter.dbHelper = mockDBHelper;

        boolean result = vaccinePresenter.getAllVaccine();

        // Nếu danh sách mà không rỗng, trả về false
        assertFalse(result);
    }

    @Test //TC_226_05
    public void testCreateVaccine_emptyName() {
        Vaccine vaccine = new Vaccine();
        vaccine.setNameVaccine("");  // Trống
        vaccine.setVaccination("Mũi 1");
        vaccine.setDisease("Cúm");

        when(mockDBHelper.insertVaccine(any(Vaccine.class))).thenReturn(1);

        vaccinePresenter.createVaccine(vaccine);

        verify(mockVaccineDAO).createSuccess(); // => Đây là **lỗi** nếu không có kiểm tra đầu vào
    }

    @Test//TC_226_06
    public void testCreateVaccine_emptyVaccination() {
        Vaccine vaccine = new Vaccine();
        vaccine.setNameVaccine("Covid-19");
        vaccine.setVaccination("");  // Trống
        vaccine.setDisease("Covid");

        when(mockDBHelper.insertVaccine(any(Vaccine.class))).thenReturn(1);

        vaccinePresenter.createVaccine(vaccine);

        verify(mockVaccineDAO).createSuccess(); // => **Lỗi nếu không kiểm tra**
    }

    @Test//TC_226_07
    public void testCreateVaccine_emptyDisease() {
        Vaccine vaccine = new Vaccine();
        vaccine.setNameVaccine("Covid-19");
        vaccine.setVaccination("Mũi 1");
        vaccine.setDisease("");  // Trống

        when(mockDBHelper.insertVaccine(any(Vaccine.class))).thenReturn(1);

        vaccinePresenter.createVaccine(vaccine);

        verify(mockVaccineDAO).createSuccess(); // => **Lỗi nếu thiếu validate**
    }

}
