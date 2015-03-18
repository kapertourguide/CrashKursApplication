package com.example.simonisb.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simonisb.myapplication.db.SQLiteDatabaseHelper;


public class MainActivity extends ActionBarActivity {

    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    TextView tv_null;
    SQLiteDatabaseHelper sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_null = (TextView) findViewById(R.id.start_text);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Text", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, DBListActivity.class));
            }
        });

        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blubb = new Intent(MainActivity.this, ListActivity.class);
                startActivity(blubb);
            }
        });

        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_3.setEnabled(false);
                btn_4.setEnabled(true);
                increaseNumber();

                Toast.makeText(MainActivity.this, "Button 3 geklickt", Toast.LENGTH_SHORT).show();
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_4.setEnabled(false);
                btn_3.setEnabled(true);
                increaseNumber();

                Toast.makeText(MainActivity.this, "Button 4 geklickt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_null.setText("0");
    }

    private void increaseNumber() {
        String buffer = tv_null.getText().toString();
        try {
            int zahl = Integer.parseInt(buffer);
            tv_null.setText(String.valueOf(++zahl));
        } catch (NumberFormatException e){
            Toast.makeText(this, "Kein Zahlenformat", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
