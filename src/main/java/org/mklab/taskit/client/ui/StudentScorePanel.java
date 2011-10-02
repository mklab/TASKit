/**
 * 
 */
package org.mklab.taskit.client.ui;

import org.mklab.taskit.client.model.StudentScoreModel;
import org.mklab.taskit.client.model.StudentScoreModel.LectureScore;
import org.mklab.taskit.client.ui.StudentListView.Presenter;
import org.mklab.taskit.shared.AttendanceType;
import org.mklab.taskit.shared.LectureProxy;
import org.mklab.taskit.shared.ReportProxy;
import org.mklab.taskit.shared.SubmissionProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;


/**
 * @author ishikura
 */
public class StudentScorePanel extends Composite {

  private CellTable<LectureScore> table;
  private StudentScoreModel model;
  private Presenter presenter;
  private TableRowElement lastHighlightElement;
  private boolean editable;

  /**
   * {@link StudentScorePanel}オブジェクトを構築します。
   * 
   * @param editable 編集可能かどうか
   */
  public StudentScorePanel(boolean editable) {
    this.table = new CellTable<StudentScoreModel.LectureScore>();
    this.editable = editable;
    initColumns();
    initWidget(this.table);

    this.table.setLoadingIndicator(null);
  }

  /**
   * presenterを設定します。
   * 
   * @param presenter presenter
   */
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  /**
   * presenterを取得します。
   * 
   * @return presenter
   */
  public Presenter getPresenter() {
    return this.presenter;
  }

  /**
   * 成績データをクリアします。
   */
  public void clearScoreData() {
    this.table.setRowData(new ArrayList<LectureScore>());
  }

  /**
   * モデルを設定します。
   * 
   * @param model ユーザーの成績情報
   */
  @SuppressWarnings("hiding")
  public void showUserPage(StudentScoreModel model) {
    this.model = model;
    this.table.setRowData(this.model.asList());
    initColumns();
  }

  /**
   * テーブルデータを更新します。
   */
  public void updateTable() {
    this.table.setRowData(this.model.asList());
    initColumns();
  }

  /**
   * 与えられた行をハイライトします。
   * 
   * @param rowData 行データ
   */
  public void highlightRow(StudentScoreModel.LectureScore rowData) {
    if (this.lastHighlightElement != null) {
      this.lastHighlightElement.getStyle().clearBackgroundColor();
    }

    if (rowData == null) return;

    final TableRowElement rowElement = this.table.getRowElement(rowData.getIndex());
    rowElement.getStyle().setBackgroundColor("#FFFFAA"); //$NON-NLS-1$
    this.lastHighlightElement = rowElement;
  }

  private void initColumns() {
    final int COMMON_COLUMN_COUNT = 2;
    if (this.table.getColumnCount() < COMMON_COLUMN_COUNT) initCommonColumns();

    if (this.model == null) return;

    final int currentReportColumnCount = this.table.getColumnCount() - COMMON_COLUMN_COUNT;
    final int newReportCount = this.model.getMaximumReportCount();

    if (newReportCount > currentReportColumnCount) {
      for (int i = currentReportColumnCount; i < newReportCount; i++) {
        addSubmissionColumn(i - currentReportColumnCount);
      }
    } else {
      for (int i = newReportCount; i < currentReportColumnCount; i++) {
        this.table.removeColumn(this.table.getColumnCount() - 1);
      }
    }
  }

  @SuppressWarnings("nls")
  private void addSubmissionColumn(final int reportIndex) {
    final List<String> options = Arrays.asList("○", "△", "×", "");
    final SelectCell submissionCell = new SelectCell(options);
    submissionCell.setEditable(this.editable);

    final Column<LectureScore, String> submissionColumn = new Column<StudentScoreModel.LectureScore, String>(submissionCell) {

      @Override
      public String getValue(LectureScore object) {
        if (object.getReportCount() <= reportIndex) {// そもそもそんな課題ない場合
          return null;
        }
        final ReportProxy report = object.getReport(reportIndex);
        final SubmissionProxy submission = object.getSubmission(report);
        if (submission == null) return options.get(3); // 未提出

        final int point = submission.getPoint();
        if (point >= 80) {
          return options.get(0);
        } else if (point >= 40) {
          return options.get(1);
        } else {
          return options.get(2);
        }
      }

    };
    submissionColumn.setFieldUpdater(new FieldUpdater<StudentScoreModel.LectureScore, String>() {

      @SuppressWarnings({"synthetic-access", "unqualified-field-access"})
      @Override
      public void update(@SuppressWarnings("unused") int index, LectureScore object, String value) {
        if (object.getReportCount() <= reportIndex) {
          presenter.reloadUserPage();
          return;
        }
        final ReportProxy report = object.getReport(reportIndex);
        int n;
        if (value.equals(options.get(0))) {
          n = 100;
        } else if (value.equals(options.get(1))) {
          n = 50;
        } else if (value.equals(options.get(2))) {
          n = 0;
        } else {
          presenter.reloadUserPage();
          return;
        }

        presenter.submit(report, n);
      }

    });
    this.table.addColumn(submissionColumn, String.valueOf(reportIndex + 1));
  }

  private void initCommonColumns() {
    final Column<LectureScore, Void> lectureNumberColumn = new Column<StudentScoreModel.LectureScore, Void>(new AbstractCell<Void>() {

      @Override
      public void render(com.google.gwt.cell.client.Cell.Context context, @SuppressWarnings("unused") Void value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant(String.valueOf(context.getIndex() + 1));
      }

    }) {

      @Override
      public Void getValue(@SuppressWarnings("unused") LectureScore object) {
        return null;
      }
    };

    final List<String> attendanceTypes = new ArrayList<String>();
    for (AttendanceType type : AttendanceType.values()) {
      attendanceTypes.add(type.name());
    }
    attendanceTypes.add(""); //$NON-NLS-1$
    final SelectCell selectionCell = new SelectCell(attendanceTypes);
    selectionCell.setEditable(this.editable);

    final Column<LectureScore, String> attendanceColumn = new Column<StudentScoreModel.LectureScore, String>(selectionCell) {

      @Override
      public String getValue(LectureScore object) {
        if (object.getAttendance() == null) return ""; //$NON-NLS-1$
        return object.getAttendance().getType().name();
      }

    };
    attendanceColumn.setFieldUpdater(new FieldUpdater<StudentScoreModel.LectureScore, String>() {

      @SuppressWarnings({"unqualified-field-access", "synthetic-access", "unused"})
      @Override
      public void update(int index, LectureScore object, String value) {
        final LectureProxy lecture = object.getLecture();
        final AttendanceType type;
        try {
          type = AttendanceType.valueOf(value);
        } catch (Throwable e) {
          presenter.reloadUserPage();
          return;
        }
        presenter.attend(lecture, type);
      }

    });

    this.table.addColumn(lectureNumberColumn, "No."); //$NON-NLS-1$
    this.table.addColumn(attendanceColumn, "Attendance"); //$NON-NLS-1$
  }
}
