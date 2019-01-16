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

        List<Object> data = new ArrayList<Object>();
        data.add(new ItemModel("Red roses for wedding"));
        data.add(new ItemModel("Bouquet with red roses"));
        data.add(new ItemModel("Single red rose flower"));
        data.add(new ItemModel("Red roses for wedding"));
        data.add(new ItemModel("Bouquet with red roses"));
        data.add(new ItemModel("Single red rose flower"));

        final KMPAutoComplTextView complTextView = (KMPAutoComplTextView) findViewById(R.id.tvAutoCompl);
        complTextView.setDatas(data);
        complTextView.setShowCurrentTextAsOption(true);
        complTextView.setOnPopupItemClickListener(new KMPAutoComplTextView.OnPopupItemClickListener() {
            @Override
            public void onPopupItemClick(Object item, CharSequence charSequence) {
                Toast.makeText(MainActivity.this, "Item available: "+ (item != null) + " " + charSequence.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Object> data = new ArrayList<Object>();
                data.add(new ItemModel("Black roses for funerals"));
                data.add(new ItemModel("Bouquet without red roses"));
                data.add(new ItemModel("Single red rose flower"));
                data.add(new ItemModel("Single red rose bouquets"));
                data.add(new ItemModel("Single red rose flower pots"));
                data.add(new ItemModel("Black roses for funerals"));
                data.add(new ItemModel("Bouquet without red roses"));
                data.add(new ItemModel("Single red rose flower"));
                data.add(new ItemModel("Single red rose bouquets"));
                data.add(new ItemModel("Single red rose flower pots"));
                ((KMPAdapter) complTextView.getAdapter()).addAll(data);
            }
        }, 30000);

    }


    class ItemModel {
        private String item;

        public ItemModel(String item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return item;
        }
    }
}
