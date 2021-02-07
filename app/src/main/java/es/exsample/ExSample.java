//ExSample3_14.java ヘルパークラスを利用したデータベース利用に関するサンプル
package es.exsample;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Calendar;

public class ExSample extends AppCompatActivity {

    private Button bt1, bt2;
    private EditText et1, et2;
    private TextView tv, tv1, tv2, tv3;
    private SampleDBHelper dbhelper;
    private String str, stret;
    private int num = 0;
    private int a, day, kigen,b;
    private static SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        //setContentView(ll);

        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;

        ll.setLayoutParams(new LinearLayout.LayoutParams(
                matchParent, wrapContent));

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(
                matchParent, wrapContent));

        scrollView.addView(ll);

        setContentView(scrollView);

        bt1 = new Button(this);
        bt1.setText("追加");
        bt1.setOnClickListener(new ExSampleClickListener());
        bt2 = new Button(this);
        bt2.setText("削除");
        bt2.setOnClickListener(new ExSampleClickListener());

        ll.addView(bt1);
        ll.addView(bt2);

        et1 = new EditText(this);
        et2 = new EditText(this);
        tv = new TextView(this);
        tv.setText("課題一覧\n");
        tv1 = new TextView(this);
        tv1.setText("科目\n");
        tv2 = new TextView(this);
        tv2.setText("締め切り日\n");


        ll.addView(tv1);
        ll.addView(et1);
        ll.addView(tv2);
        ll.addView(et2);
        ll.addView(tv);

        dbhelper = new SampleDBHelper(this);  //データベースヘルパークラスの生成
        db = dbhelper.getWritableDatabase();  //データベースのオープン
        db.delete("sampletable", null, null);  //データベースのリセット

        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
    }


    class ExSampleClickListener implements View.OnClickListener {
        public void onClick(View v) {

            ContentValues values = new ContentValues();  //データベースに入力するデータを保存するためのオブジェクトの生成
            if (v == bt1) {
                stret = et2.getText().toString();
                a = Integer.parseInt(stret);
                kigen = a - day;
                if (kigen > 1) {
                    values.put("sampletext", et1.getText().toString() + "      期限 " + kigen + " 日前");  //エディットテキストからデータベースに入力する値を取得
                    db.insert("sampletable", null, values);  //データベースに値を挿入
                    b++;
                } else if (kigen == 1) {
                    et1.setTextColor(Color.YELLOW);
                    values.put("sampletext", et1.getText().toString() + "      期限は " + "明日！");  //エディットテキストからデータベースに入力する値を取得
                    db.insert("sampletable", null, values);  //データベースに値を挿入
                    b++;
                } else if (kigen == 0) {
                    et1.setTextColor(Color.RED);
                    values.put("sampletext", et1.getText().toString() + "      期限は " + "当日...！");  //エディットテキストからデータベースに入力する値を取得
                    db.insert("sampletable", null, values);  //データベースに値を挿入
                    b++;
                }
                else{
                    et1.setTextColor(Color.BLUE);
                    values.put("sampletext", et1.getText().toString() + "      期限は " + "期限切れ");  //エディットテキストからデータベースに入力する値を取得
                    db.insert("sampletable", null, values);  //データベースに値を挿入
                    num = b + 1;
                    b = 0;
                }
            }
            if (v == bt2) {
                    String[] args = {String.valueOf(num)};  //リストの最後の番号を取得
                    db.delete("sampletable", "_id = ?", args);  //リストの最後の値を削除
                    num--;  //リストのナンバーをデクリメント
                }

                Cursor cr = db.query("sampletable", new String[]{"_id", "sampletext"}, null, null, null, null, null);  //クエリ結果をカーソルで取得

                str = "課題一覧\n";
                while (cr.moveToNext()) {  //カーソルを一つづつ動かしてデータ取得
                    str += cr.getString(cr.getColumnIndex("_id")) + ":" +
                            cr.getString(cr.getColumnIndex("sampletext")) + "\n";
                }
                tv.setText(str);
            }
        }
    }
