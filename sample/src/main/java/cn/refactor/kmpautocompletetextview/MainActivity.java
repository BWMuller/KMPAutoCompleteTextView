package cn.refactor.kmpautocompletetextview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.kmpautotextview.KMPAdapter;
import cn.refactor.kmpautotextview.KMPAutoComplTextView;


/**
 * 作者 : andy
 * 日期 : 15/10/26 21:01
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 测试界面
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = new ArrayList<String>();
        data.add("Red roses for wedding");
        data.add("Bouquet with red roses");
        data.add("Single red rose flower");
        data.add("Red roses for wedding");
        data.add("Bouquet with red roses");
        data.add("Single red rose flower");

        final KMPAutoComplTextView complTextView = (KMPAutoComplTextView) findViewById(R.id.tvAutoCompl);
        complTextView.setDatas(data);
        complTextView.setShowCurrentTextAsOption(true);
        complTextView.setOnPopupItemClickListener(new KMPAutoComplTextView.OnPopupItemClickListener() {
            @Override
            public void onPopupItemClick(CharSequence charSequence) {
                Toast.makeText(MainActivity.this, charSequence.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = new ArrayList<String>();
                data.add("Black roses for funerals");
                data.add("Bouquet without red roses");
                data.add("Single red rose flower");
                data.add("Single red rose bouquets");
                data.add("Single red rose flower pots");
                data.add("Black roses for funerals");
                data.add("Bouquet without red roses");
                data.add("Single red rose flower");
                data.add("Single red rose bouquets");
                data.add("Single red rose flower pots");
                ((KMPAdapter) complTextView.getAdapter()).addAll(data);
            }
        }, 30000);

    }

}
