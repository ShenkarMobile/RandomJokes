package mobile.shenkar.com.randomjokes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MainController controller ;
    private TextView jokeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new MainController(this);
        setContentView(R.layout.activity_main);
        jokeTv = (TextView) findViewById(R.id.textViewJoke);

    }

    public void jokeClicked(View v) {

        final ProgressDialog pd = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.downloading_joke));
		controller.getJokeInBackground(new AppCallback<String>() {

            @Override
            public void done(String retObj, Exception e) {
                pd.dismiss();
                if (e == null) {
                    jokeTv.setText(retObj);
                }
            }
        });
    }
}
