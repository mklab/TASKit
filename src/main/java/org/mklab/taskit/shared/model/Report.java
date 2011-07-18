/**
 * 
 */
package org.mklab.taskit.shared.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author teshima
 * @version $Revision$, Jan 25, 2011
 */
@Entity(name = "REPORT")
public final class Report implements Serializable {

  /** */
  private static final long serialVersionUID = 28409820481L;

  /** 課題のID */
  @Id
  @GeneratedValue
  private int reportId;
  /** 課題の番号 */
  private int no;
  /** この課題の属する講義のIDです。 */
  private int lectureId;
  /** 課題のタイトル */
  private String title;
  /** 課題の詳細 */
  private String detail;
  /** 課題の配点 */
  private int allotment;

  /**
   * {@link Report}オブジェクトを構築します。
   * 
   * @param no 課題の番号
   * @param title 課題の名前
   * @param detail 課題の詳細
   * @param allotment 課題の配点
   * @param lectureId 講義ID
   */
  public Report(int no, String title, String detail, int allotment, int lectureId) {
    super();
    this.no = no;
    this.title = title;
    this.detail = detail;
    this.allotment = allotment;
    this.lectureId = lectureId;
  }

  /**
   * {@link Report}オブジェクトを構築します。
   */
  Report() {
    // for serialization
  }

  /**
   * 課題の名前を設定します。
   * 
   * @param name 課題の名前
   */
  public void setTitle(String name) {
    this.title = name;
  }

  /**
   * titleを取得します。
   * 
   * @return title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * 課題の詳細説明を設定します。
   * 
   * @param detail 課題の詳細説明
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * 課題の詳細説明を取得します。 　*
   * 
   * @return 課題の詳細説明
   */
  public String getDetail() {
    return this.detail;
  }

  /**
   * 課題の配点を設定します。
   * 
   * @param allotment 課題の配点
   */
  public void setAllotment(int allotment) {
    this.allotment = allotment;
  }

  /**
   * 課題の配点を取得します。 　*
   * 
   * @return 課題の配点
   */
  public int getAllotment() {
    return this.allotment;
  }

  /**
   * reportIdを設定します。
   * 
   * @param reportId reportId
   */
  public void setReportId(int reportId) {
    this.reportId = reportId;
  }

  /**
   * reportIdを取得します。
   * 
   * @return reportId
   */
  public int getReportId() {
    return this.reportId;
  }

  /**
   * lessonIdを設定します。
   * 
   * @param lectureId lessonId
   */
  public void setLectureId(int lectureId) {
    this.lectureId = lectureId;
  }

  /**
   * lessonIdを取得します。
   * 
   * @return lessonId
   */
  public int getLectureId() {
    return this.lectureId;
  }

  /**
   * 課題のレベル（番号）を設定します。
   * 
   * @param no 課題のレベル（番号）
   */
  public void setNo(int no) {
    this.no = no;
  }

  /**
   * noを取得します。
   * 
   * @return no
   */
  public int getNo() {
    return this.no;
  }

}
