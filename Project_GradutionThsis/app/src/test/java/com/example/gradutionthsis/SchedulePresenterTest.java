package com.example.gradutionthsis;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.presenter.DetailScheduleDAO;
import com.example.gradutionthsis.presenter.DetailSchedulePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class SchedulePresenterTest {

    @Mock
    private Context mockContext;

    @Mock
    private DetailScheduleDAO mockDAO;

    private DetailSchedulePresenter presenter;
    private FakeDBHelper fakeDBHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fakeDBHelper = new FakeDBHelper();
        presenter = new DetailSchedulePresenter(mockContext, mockDAO);
        presenter.setDbHelper(fakeDBHelper);
    }

    // TC39: Kiểm tra tạo lịch tiêm thành công (insertDetailSchedule trả về 1)
    @Test
    public void testCreateDetailSchedule_Success() {
        Injection i = new Injection();
        i.setIdInjection(1);
        i.setinjectionMonth(1);
        fakeDBHelper.addInjection(i);

        presenter.createDetailSchedule(1, "01/01/2024");

        // Không nên gọi createFail vì thành công
        verify(mockDAO, never()).createFail();
    }

    // TC40: Tạo lịch tiêm thất bại do ngày sinh rỗng → calTime trả về null → insert fail
    @Test
    public void testCreateDetailSchedule_InsertFail() {
        Injection i = new Injection();
        i.setIdInjection(1);
        i.setinjectionMonth(1);
        fakeDBHelper.addInjection(i);

        presenter.createDetailSchedule(1, ""); // rỗng → calTime fail

        verify(mockDAO, atLeastOnce()).createFail();
    }

    // TC41: Cập nhật lịch thành công (updateDetailSchedule trả về 1)
    @Test
    public void testUpdateDetailSchedule_Success() {
        DetailSchedule s = new DetailSchedule(1, 1, "01/01/2024", 0, 0);
        fakeDBHelper.addSchedule(s); // thêm sẵn lịch

        presenter.updateDetailSchedule(s);

        verify(mockDAO).updateSuccess();
    }

    // TC42: Cập nhật lịch thất bại do không có bản ghi trùng khớp
    @Test
    public void testUpdateDetailSchedule_Fail() {
        DetailSchedule s = new DetailSchedule(99, 99, "01/01/2024", 0, 0);

        presenter.updateDetailSchedule(s);

        verify(mockDAO).updateFail();
    }

    // TC43: Lấy danh sách lịch theo trẻ em có dữ liệu
    @Test
    public void testGetListByIdRelative_WithData() {
        DetailSchedule s = new DetailSchedule(1, 1, "01/01/2024", 0, 0);
        fakeDBHelper.addSchedule(s);

        List<DetailSchedule> result = presenter.getListByIdRelative(1);

        assert result != null && !result.isEmpty();
    }

    // TC44: Lấy danh sách lịch theo trẻ em không có dữ liệu
    @Test
    public void testGetListByIdRelative_Empty() {
        List<DetailSchedule> result = presenter.getListByIdRelative(999); // không có lịch

        assert result == null;
    }

    // TC45: Lấy chi tiết một lịch tiêm thành công
    @Test
    public void testGetDetailSchedule_Found() {
        DetailSchedule s = new DetailSchedule(1, 1, "01/01/2024", 0, 0);
        fakeDBHelper.addSchedule(s);

        DetailSchedule result = presenter.getDetailSchedule(1, 1);

        assert result != null;
    }

    // TC46: Lấy chi tiết một lịch tiêm không tồn tại
    @Test
    public void testGetDetailSchedule_NotFound() {
        DetailSchedule result = presenter.getDetailSchedule(100, 100);

        assert result == null;
    }

    // ========== INNER FAKE DBHELPER ==========
    private static class FakeDBHelper extends DBHelper {
        private final ArrayList<Injection> fakeInjections = new ArrayList<>();
        private final ArrayList<DetailSchedule> fakeSchedules = new ArrayList<>();

        public FakeDBHelper() {
            super(mock(Context.class)); // Dùng Context giả để tránh lỗi khi test
        }

        public void addInjection(Injection injection) {
            fakeInjections.add(injection);
        }

        public void addSchedule(DetailSchedule schedule) {
            fakeSchedules.add(schedule);
        }

        @Override
        public ArrayList<Injection> getAllInjections() {
            return fakeInjections;
        }

        @Override
        public int insertDetailSchedule(DetailSchedule schedule) {
            if (schedule.getInjectionTime() == null || schedule.getInjectionTime().isEmpty()) return -1;
            fakeSchedules.add(schedule);
            return 1;
        }

        @Override
        public int updateDetailSchedule(DetailSchedule schedule) {
            for (DetailSchedule s : fakeSchedules) {
                if (s.getIdRelative() == schedule.getIdRelative() &&
                        s.getIdInjection() == schedule.getIdInjection()) {
                    return 1;
                }
            }
            return 0;
        }

        @Override
        public List<DetailSchedule> getDetailSchedulesById(int idRelative) {
            List<DetailSchedule> result = new ArrayList<>();
            for (DetailSchedule s : fakeSchedules) {
                if (s.getIdRelative() == idRelative) {
                    result.add(s);
                }
            }
            return result;
        }

        @Override
        public DetailSchedule getDetailScheduleById(int idRelative, int idInjection) {
            for (DetailSchedule s : fakeSchedules) {
                if (s.getIdRelative() == idRelative && s.getIdInjection() == idInjection) {
                    return s;
                }
            }
            return null;
        }
    }
}
