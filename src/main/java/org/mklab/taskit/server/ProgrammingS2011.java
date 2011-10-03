/**
 * 
 */
package org.mklab.taskit.server;

import org.mklab.taskit.server.domain.Account;
import org.mklab.taskit.server.domain.EMF;
import org.mklab.taskit.server.domain.Lecture;
import org.mklab.taskit.server.domain.Report;
import org.mklab.taskit.shared.UserType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * @author ishikura
 */
public class ProgrammingS2011 {

  @SuppressWarnings("nls")
  private static final String[] STUDENT_IDS = {"11236001", "11236002", "11236003", "11236004", "11236005", "11236006", "11236007", "11236008", "11236009", "11236010", "11236011", "11236012",
      "11236013", "11236014", "11236015", "11236016", "11236017", "11236018", "11236019", "11236020", "11236021", "11236022", "11236023", "11236024", "11236025", "11236026", "11236027", "11236028",
      "11236029", "11236030", "11236031", "11236032", "11236033", "11236034", "11236035", "11236036", "11236037", "11236038", "11236039", "11236040", "11236041", "11236042", "11236043", "11236044",
      "11236045", "11236046", "11236047", "11236048", "11236049", "11236050", "11236051", "11236052", "11236053", "11236054", "11236055", "11236056", "11236057", "11236058", "11236059", "11236060",
      "11236061", "11236062", "11236063", "11236064", "11236065", "11236066", "11236067", "11236068", "11236069", "11236070", "11236071", "11236072", "11236073", "11236074", "11236075", "11236076",
      "11236077", "11236078", "11236079", "11236080", "11236081", "11236082", "11236083", "11236084", "10236039", "10236047", "10236076", "08236031"};
  @SuppressWarnings("nls")
  private static final String[] TA_IDS = {"ishikura", "suginaga", "teshima", "motoyama", "yamashita", "matsutake"};
  private static final String TEACHER_ID = "koga"; //$NON-NLS-1$
  private static final Map<String, String> idToPass = new TreeMap<String, String>();

  static void registerAccounts() {
    for (final String id : STUDENT_IDS) {
      final String pass = Passwords.generatePassword(8);
      Account.registerNewAccount(id, pass, UserType.STUDENT);
      idToPass.put(id, pass);
    }
    for (final String id : TA_IDS) {
      final String pass = Passwords.generatePassword(8);
      Account.registerNewAccount(id, pass, UserType.TA);
      idToPass.put(id, pass);
    }
    {
      final String pass = Passwords.generatePassword(8);
      Account.registerNewAccount(TEACHER_ID, pass, UserType.TEACHER);
      idToPass.put(TEACHER_ID, pass);
    }
  }

  /**
   * 講義を登録します。
   */
  @SuppressWarnings("nls")
  static void registerLectures() {
    Lecture l1 = createLecture("講義の紹介、タイピング、エディタ、アプレット入門", parse("2011/10/04 13:00"));
    createReport(l1, "自分の名前を表示するアプレット", 1);
    createReport(l1, "Webブラウザでアプレットを実行", 2);
    l1.persist();

    Lecture l2 = createLecture("文字(フォント)とカラー", parse("2011/10/11 13:00"));
    createReport(l2, "フォントとカラーの設定", 1);
    createReport(l2, "影付き文字の表示", 1);
    createReport(l2, "利用可能なすべてのフォントを表示", 2);
    l2.persist();

    Lecture l3 = createLecture("図形描画の基本", parse("2011/10/18 13:00"));
    createReport(l3, "図形の描画", 1);
    createReport(l3, "格子の描画", 2);
    createReport(l3, "グリッド付き座標", 2);
    l3.persist();

    Lecture l4 = createLecture("図形描画の応用", parse("2011/10/25 13:00"));
    createReport(l4, "関数の描画", 1);
    createReport(l4, "カラーテーブル", 2);
    l4.persist();

    Lecture l5 = createLecture("画像描画", parse("2011/11/08 13:00"));
    createReport(l5, "座標変換", 1);
    createReport(l5, "金と銀", 2);
    createReport(l5, "将棋盤", 2);
    l5.persist();

    Lecture l6 = createLecture("基本的なGUI(イベント処理)", parse("2011/11/15 13:00"));
    createReport(l6, "装飾されたGUI部品", 1);
    createReport(l6, "アップダウンカウンタ", 2);
    createReport(l6, "じゃんけんゲーム", 2);
    l6.persist();

    Lecture l7 = createLecture("対話的なGUI, サウンドの再生", parse("2011/11/22 13:00"));
    createReport(l7, "円を描画するプログラム", 1);
    createReport(l7, "蛇プログラム", 2);
    createReport(l7, "○×ゲーム(三目並べ)", 2);
    l7.persist();

    Lecture l8 = createLecture("GUIのレイアウト", parse("2011/11/29 13:00"));
    createReport(l8, "パネルを用いた部品のレイアウト", 1);
    createReport(l8, "一桁電卓", 2);
    l8.persist();

    Lecture l9 = createLecture("GUIの応答(高機能GUI)", parse("2011/12/06 13:00"));
    createReport(l9, "二次間数", 1);
    createReport(l9, "GUI部品の情報利用", 1);
    createReport(l9, "パズルゲーム", 2);
    l9.persist();

    Lecture l10 = createLecture("アニメーションの基本", parse("2011/12/13 13:00"));
    createReport(l10, "斜め移動する図形(画像)のアニメーション", 1);
    createReport(l10, "カウントダウン(100ミリ秒表示機能付)", 2);
    createReport(l10, "将棋の駒の移動", 2);
    l10.persist();

    Lecture l11 = createLecture("アニメーションの利用(ダブルバッファリング)", parse("2011/12/20 13:00"));
    createReport(l11, "ダブルバッファ", 1);
    createReport(l11, "図形の回転", 2);
    createReport(l11, "波形の移動", 2);
    l11.persist();

    Lecture l12 = createLecture("GUIによるアニメーション操作、継承", parse("2012/01/10 13:00"));
    createReport(l12, "GUI部品の情報を利用するアニメーション", 1);
    createReport(l12, "図形のアニメーション", 2);
    createReport(l12, "反射板ゲーム", 2);
    l12.persist();

    Lecture l13 = createLecture("Javaアプリケーション、ダイアログ", parse("2012/01/17 13:00"));
    createReport(l13, "Javaアプリケーション", 1);
    createReport(l13, "確認ダイアログ", 2);
    createReport(l13, "スライダー", 2);
    l13.persist();

    Lecture l14 = createLecture("タイピング試験", parse("2012/01/24 13:00"));
    l14.persist();

    Lecture l15 = createLecture("期末試験", parse("2012/01/31 13:00"));
    l15.persist();
  }

  private static Lecture createLecture(String title, Date date) {
    final Lecture l = new Lecture(date);
    l.setTitle(title);
    return l;
  }

  private static Report createReport(Lecture lecture, String title, int point) {
    Report report = new Report(title, point, lecture);
    lecture.getReports().add(report);
    return report;
  }

  private static Date parse(String date) {
    final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //$NON-NLS-1$
    try {
      return format.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * メインメソッドです。
   * 
   * @param args プログラム引数
   */
  public static void main(String[] args) {
    EMF.setPersistenceUnitName("taskit"); //$NON-NLS-1$
    registerAccounts();
    registerLectures();

    try {
      PrintWriter pw = new PrintWriter(new File("target/accounts.csv")); //$NON-NLS-1$
      for (Entry<String, String> account : idToPass.entrySet()) {
        pw.println(MessageFormat.format("\"{0}\",\"{1}\"", account.getKey(), account.getValue())); //$NON-NLS-1$
      }
      pw.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
