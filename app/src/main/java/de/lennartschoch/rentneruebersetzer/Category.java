package de.lennartschoch.rentneruebersetzer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Category extends AppCompatActivity {

    String[][] rentner;
    ListView listview;
    Bundle extras;
    ProgressDialog progressDialog;
    String category;
    EditText word_edit, translation_edit;
    AlertDialog.Builder add_dialog;
    AlertDialog add_popup;
    String wordedit_value, translationedit_value;

    class DownloadClass extends AsyncTask<URL, Integer, Long> {

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

            listview.setAdapter(new CustomTwoLineAdapter(Category.this, rentner));
            progressDialog.cancel();

        }

        @Override
        protected Long doInBackground(URL... params) {

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        new URL("http://dustrunner-media.com/projects/rentner/get.php?category="+category).openStream()));

                JSONArray jsonArray = new JSONArray(in.readLine());
                ArrayList<String> wordlist = new ArrayList<String>();
                for (int i=0; i<jsonArray.length(); i++) {
                    wordlist.add(jsonArray.getString(i));
                }

                jsonArray = new JSONArray(in.readLine());
                ArrayList<String> translationlist = new ArrayList<String>();
                for (int i=0; i<jsonArray.length(); i++) {
                    translationlist.add( jsonArray.getString(i) );
                }

                String[] words = wordlist.toArray(new String[wordlist.size()]);
                String[] translations = translationlist.toArray(new String[translationlist.size()]);

                rentner = new String[words.length][2];

                for(int i = 0; i < words.length; i++) {
                    rentner[i][0] = words[i];
                    rentner[i][1] = translations[i];
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    class AddClass extends AsyncTask<URL, Integer, Long> {

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

            new DownloadClass().execute();

        }

        @Override
        protected Long doInBackground(URL... params) {

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        new URL("http://dustrunner-media.com/projects/rentner/add.php?word="+wordedit_value+"&translation="+translationedit_value+"&category="+category).openStream()));
                in.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        category = getIntent().getExtras().getString("category");
        getSupportActionBar().setTitle(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView) findViewById(R.id.listView);
        progressDialog = new ProgressDialog(Category.this);
        progressDialog.setMessage("Rentner werden geladen...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        add_dialog = new AlertDialog.Builder(
                Category.this);

        final LinearLayout add_layout = new LinearLayout(Category.this);
        add_layout.setOrientation(LinearLayout.VERTICAL);
        word_edit = new EditText(Category.this);
        translation_edit = new EditText(Category.this);
        word_edit.setHint("Wort");
        translation_edit.setHint("Übersetzung");
        add_dialog.setCancelable(false);
        add_layout.setPadding(0, (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())), 0, 0);
        add_layout.addView(word_edit);
        add_layout.addView(translation_edit);
        add_dialog.setTitle("Wort hinzufügen");
        add_dialog.setView(add_layout);

        add_dialog.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                wordedit_value = word_edit.getText().toString();
                translationedit_value = translation_edit.getText().toString();
                word_edit.setText("");
                translation_edit.setText("");
                progressDialog.setMessage("Rentner wird hinzugefügt...");
                progressDialog.show();
                new AddClass().execute();
            }
        });
        add_dialog.setNegativeButton("Schließen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                word_edit.setText("");
                translation_edit.setText("");
            }
        });

        add_popup = add_dialog.create();

        new DownloadClass().execute();
    }

    public void addWord(View v) {
        add_popup.show();
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

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
