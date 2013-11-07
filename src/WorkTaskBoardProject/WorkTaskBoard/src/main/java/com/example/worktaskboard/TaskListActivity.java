package com.example.worktaskboard;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends Activity {

    final private Activity Outer = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                initinalActivity();
            }
        }).start();
    }

    private void initinalActivity() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://worktaskboard.sinaapp.com/tasks/list/5");
            HttpResponse response = client.execute(request);

            String html = "";
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream stream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    html = buffer.toString();
                }

                Gson gson = new Gson();
                List<Task> tasks = gson.fromJson(html, new TypeToken<List<Task>>(){}.getType());

                Message msg = new Message();
                msg.obj = tasks;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Task> tasks = (List<Task>)msg.obj;
            List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < tasks.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("txtItemText", tasks.get(i).getTaskContent());
                contents.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    Outer,
                    contents,
                    R.layout.layout_tasklist,
                    new String[]{"txtItemText"},
                    new int[]{R.id.txtItemText}
            );

            ListView lsvTasks = (ListView)findViewById(R.id.lsvTasks);
            lsvTasks.setAdapter(adapter);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tasklist, container, false);
            return rootView;
        }
    }

}
