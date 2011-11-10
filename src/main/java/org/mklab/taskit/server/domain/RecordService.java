/**
 * 
 */
package org.mklab.taskit.server.domain;

import org.mklab.taskit.server.auth.Invoker;
import org.mklab.taskit.shared.UserType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * 学生の成績に関するサービスを提供するクラスです。
 * 
 * @author Yuhi Ishikura
 */
public class RecordService {

  private static RecordStatistics recordStatistics = new RecordStatistics();

  static {
    recordStatistics.start();
  }

  /**
   * 成績の統計処理を行うオブジェクトを取得します。
   * 
   * @return 成績の統計処理を行うオブジェクト
   */
  static RecordStatistics getRecordStatistics() {
    return recordStatistics;
  }

  /**
   * 与えられたアカウントIDの生徒の、講義別成績を取得します。
   * 
   * @param accountId 生徒のID
   * @return 講義別成績
   */
  @Invoker({UserType.TA, UserType.TEACHER})
  public static LecturewiseRecords getLecturewiseRecordsByAccountId(String accountId) {
    final User user = User.getUserByAccountId(accountId);
    final List<Lecture> lectures = Lecture.getAllLectures();
    final Iterator<Attendance> attendances = Attendance.getAttendancesByAccountId(accountId).iterator();
    Attendance next = attendances.hasNext() ? attendances.next() : null;

    final List<LecturewiseRecord> records = new ArrayList<LecturewiseRecord>();
    for (final Lecture lecture : lectures) {
      List<Submission> submissions = Submission.getSubmissionByAccountIdAndLectureId(accountId, lecture.getId());
      Attendance attendance = null;
      if (next != null && next.getLecture().getId().equals(lecture.getId())) {
        attendance = next;
        next = attendances.hasNext() ? attendances.next() : null;
      }
      records.add(new LecturewiseRecord(lecture, attendance, submissions));
    }

    return new LecturewiseRecords(user, records);
  }

  /**
   * 自分の講義別成績を取得します。
   * 
   * @return 自分の成績
   */
  @Invoker({UserType.STUDENT})
  public static LecturewiseRecords getMyLecturewiseRecords() {
    final User loginUser = ServiceUtil.getLoginUser();
    return getLecturewiseRecordsByAccountId(loginUser.getAccount().getId());
  }

  /**
   * 現時点での提出物の得点率を取得します。
   * 
   * @return 現時点での提出物の得点率
   */
  @Invoker({UserType.STUDENT})
  public static double getPercentageOfMySubmissionScore() {
    final double p = recordStatistics.getPercentageOfSubmissionScore(ServiceUtil.getLoginUser().getAccount().getId());
    return p;
  }

  /**
   * 与えられたIDの成績を取得します。
   * 
   * @param id ID
   * @return 成績
   */
  @Invoker({UserType.TA, UserType.TEACHER})
  public static StudentwiseRecords getRecordByAccountId(String id) {
    final User user = User.getUserByAccountId(id);
    final List<Lecture> lectures = Lecture.getAllLectures();
    return new StudentwiseRecords(lectures, Arrays.asList(getRecord(user)));
  }

  private static StudentwiseRecord getRecord(User user) {
    final List<Submission> submissions = Submission.getSubmissionsByAccountId(user.getAccount().getId());
    final List<Attendance> attendances = Attendance.getAttendancesByAccountId(user.getAccount().getId());

    final StudentwiseRecord record = new StudentwiseRecord(user, submissions, attendances);
    return record;
  }

  /**
   * 全生徒の成績を取得します。
   * 
   * @return 成績
   */
  @Invoker({UserType.TA, UserType.TEACHER})
  public static StudentwiseRecords getAllRecords() {
    final List<Lecture> lectures = Lecture.getAllLectures();
    final List<User> users = User.getAllStudents();
    final List<StudentwiseRecord> recordList = new ArrayList<StudentwiseRecord>();
    for (final User user : users) {
      recordList.add(getRecord(user));
    }
    return new StudentwiseRecords(lectures, recordList);
  }

}
