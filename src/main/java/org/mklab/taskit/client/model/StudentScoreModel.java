/**
 * 
 */
package org.mklab.taskit.client.model;

import org.mklab.taskit.shared.AttendanceProxy;
import org.mklab.taskit.shared.LectureProxy;
import org.mklab.taskit.shared.ReportProxy;
import org.mklab.taskit.shared.SubmissionProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;


/**
 * 単一生徒の成績情報のモデルです。
 * 
 * @author ishikura
 */
public class StudentScoreModel {

  private List<LectureScore> lectureScores;

  /**
   * {@link StudentScoreModel}オブジェクトを構築します。
   * 
   * @param lectures すべての講義
   * @param attendances すべての出席情報
   * @param submissions すべての提出物
   */
  public StudentScoreModel(List<LectureProxy> lectures, List<AttendanceProxy> attendances, List<SubmissionProxy> submissions) {
    final Map<LectureProxy, LectureScore> lectureToSubmissions = new HashMap<LectureProxy, LectureScore>();

    // initialize entries
    int n = 0;
    for (LectureProxy lecture : lectures) {
      lectureToSubmissions.put(lecture, new LectureScore(lecture, n++));
    }

    // inject submissions and attendances
    for (SubmissionProxy submission : submissions) {
      final LectureProxy lecture = submission.getReport().getLecture();

      LectureScore score = lectureToSubmissions.get(lecture);
      if (score == null) throw new IllegalStateException("Unmanaged lecture detected : " + lecture); //$NON-NLS-1$

      score.addSubmission(submission);
    }
    for (AttendanceProxy attendance : attendances) {
      final LectureProxy lecture = attendance.getLecture();
      if (lectureToSubmissions.containsKey(lecture) == false) throw new IllegalStateException("Unmanaged lecture detected : " + lecture); //$NON-NLS-1$
      LectureScore score = lectureToSubmissions.get(lecture);
      score.setAttendance(attendance);
    }

    // sort and set
    final TreeSet<LectureScore> scores = new TreeSet<StudentScoreModel.LectureScore>();
    for (Entry<LectureProxy, LectureScore> entry : lectureToSubmissions.entrySet()) {
      scores.add(entry.getValue());
    }
    this.lectureScores = new ArrayList<StudentScoreModel.LectureScore>(scores);
  }

  /**
   * 講義数を取得します。
   * 
   * @return 講義数
   */
  public int getLectureCount() {
    return this.lectureScores.size();
  }

  /**
   * すべての講義中で最も課題数が多い講義の課題数を取得します。
   * 
   * @return 最大課題数
   */
  public int getMaximumReportCount() {
    int max = 0;
    for (LectureScore score : this.lectureScores) {
      int cnt = score.getReportCount();
      if (cnt > max) {
        max = cnt;
      }
    }
    return max;
  }

  /**
   * 講義情報と成績情報のペアを取得します。
   * 
   * @param lectureIndex 講義のインデックス
   * @return 講義情報と成績情報のペア
   */
  public LectureScore getLectureScore(int lectureIndex) {
    return this.lectureScores.get(lectureIndex);
  }

  /**
   * リストとして成績データを取得します。
   * 
   * @return 成績データ
   */
  public List<LectureScore> asList() {
    return this.lectureScores;
  }

  /**
   * 講義情報と成績情報のペアを表すクラスです。
   * 
   * @author ishikura
   */
  public static class LectureScore implements Comparable<LectureScore> {

    int index;
    LectureProxy lecture;
    AttendanceProxy attendance;
    List<SubmissionProxy> submissions;

    LectureScore(LectureProxy lecture, int index) {
      super();
      this.lecture = lecture;
      this.index = index;
      this.submissions = new ArrayList<SubmissionProxy>();
    }

    /**
     * 講義のインデックスを取得します。
     * 
     * @return 講義のインデックス
     */
    public int getIndex() {
      return this.index;
    }

    void setAttendance(AttendanceProxy attendance) {
      this.attendance = attendance;
    }

    void addSubmission(SubmissionProxy submission) {
      this.submissions.add(submission);
    }

    /**
     * attendanceを取得します。
     * 
     * @return attendance
     */
    public AttendanceProxy getAttendance() {
      return this.attendance;
    }

    /**
     * lectureを取得します。
     * 
     * @return lecture
     */
    public LectureProxy getLecture() {
      return this.lecture;
    }

    /**
     * 課題数を取得します。
     * 
     * @return 課題数
     */
    public int getReportCount() {
      return this.lecture.getReports().size();
    }

    /**
     * 課題を取得します。
     * 
     * @param index 課題のインデックス
     * @return 課題
     */
    public ReportProxy getReport(@SuppressWarnings("hiding") int index) {
      return this.lecture.getReports().get(index);
    }

    /**
     * 提出物を取得します。
     * 
     * @param report 課題
     * @return 提出物
     */
    public SubmissionProxy getSubmission(ReportProxy report) {
      if (report == null) throw new IllegalArgumentException();
      for (int i = 0; i < this.submissions.size(); i++) {
        final SubmissionProxy submission = this.submissions.get(i);
        final ReportProxy r = submission.getReport();
        if (report.equals(r)) {
          return submission;
        }
      }
      return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(LectureScore o) {
      return this.lecture.getDate().compareTo(o.lecture.getDate());
    }
  }

}
