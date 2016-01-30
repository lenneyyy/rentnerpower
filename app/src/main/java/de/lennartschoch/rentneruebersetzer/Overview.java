package de.lennartschoch.rentneruebersetzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class Overview extends AppCompatActivity {

    RelativeLayout technik, promis, sonstiges;
    Intent category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        technik = (RelativeLayout) findViewById(R.id.tec);
        promis = (RelativeLayout) findViewById(R.id.promis);
        sonstiges = (RelativeLayout) findViewById(R.id.sonstiges);

        category = new Intent(Overview.this, Category.class);

        technik.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                category.putExtra("category","Technik");
                startActivity(category);
            }
        });

        promis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                category.putExtra("category","Promis");
                startActivity(category);
            }
        });

        sonstiges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                category.putExtra("category","Sonstiges");
                startActivity(category);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
