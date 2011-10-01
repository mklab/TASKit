/**
 * 
 */
package org.mklab.taskit.client.activity;

import org.mklab.taskit.client.model.StudentScoreModel;
import org.mklab.taskit.client.model.StudentScoreModel.LectureScore;
import org.mklab.taskit.shared.LectureProxy;

import java.util.Date;
import java.util.List;


/**
 * @author ishikura
 */
class Util {

  static LectureProxy getLatestLecture(List<LectureProxy> lectures) {
    return getLatestItem(lectures, new DateExtractor<LectureProxy>() {

      @Override
      public Date getDateOf(LectureProxy item) {
        return item.getDate();
      }
    });
  }

  static StudentScoreModel.LectureScore getLatestScore(StudentScoreModel model) {
    return getLatestItem(model.asList(), new Util.DateExtractor<StudentScoreModel.LectureScore>() {

      @Override
      public Date getDateOf(LectureScore item) {
        return item.getLecture().getDate();
      }

    });
  }

  /**
   * 与えられたアイテムの中から、最も今に近い時刻のアイテムを取得します。
   * 
   * @param items アイテムのリスト
   * @param dateExtractor 単一のアイテムから時刻を取得するためのオブジェクト
   * @return 最も今に近い時刻。アイテムが空だった場合はnull
   */
  static <E> E getLatestItem(List<E> items, DateExtractor<E> dateExtractor) {
    final long now = System.currentTimeMillis();
    long minimumMillisToNow = Long.MAX_VALUE;
    E latestItem = null;
    for (E item : items) {
      final Date date = dateExtractor.getDateOf(item);
      final long millisToNow = Math.abs(now - date.getTime());
      if (millisToNow < minimumMillisToNow) {
        minimumMillisToNow = millisToNow;
        latestItem = item;
      }
    }
    return latestItem;
  }

  interface DateExtractor<E> {

    Date getDateOf(E item);

  }

}
