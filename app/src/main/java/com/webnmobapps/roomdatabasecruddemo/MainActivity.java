package com.webnmobapps.roomdatabasecruddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.webnmobapps.roomdatabasecruddemo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainXml;
    private String firstName, lastName, recordId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainXml = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainXml.getRoot());

        // Don't work on main thraid UI room database
        //Create child thread or background thraid and work there related to room database operation

        mainXml.fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new fetchThread().start();
            }
        });

        mainXml.saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = mainXml.firstName.getText().toString();
                lastName = mainXml.lastName.getText().toString();
                recordId = mainXml.recordId.getText().toString();

                Log.e("data",firstName+"ok");
                Log.e("data",lastName+"ok");
                Log.e("data",recordId+"ok");

                // make object of thread
                // call start not run method and start will call run... than only it will work like thread.
                new bgthread().start();

            }
        });
    }

    class fetchThread extends  Thread
    {
        public  void run()
        {
            super.run();

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "sam_db").build();

            // create object of your interface
            UserDao userDao = db.userDao();

            List<User> users = userDao.get_all_user();
            String str = "";

            for(User user : users)
            {
                str = str+"\t   "+user.getUid()+"   "+user.getFirstName()+" "+user.getLastName()+"\n\n";

            }
            Log.e("fetch_room_data", str);
        }
    }

    class bgthread extends  Thread
    {
        // In thread we work in run method....
        public void  run()
        {
           // calling super.run so it initialized it's parent
           super.run();

           // Creating object of our database class

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "sam_db").build();

            // create object of your interface
            UserDao userDao = db.userDao();

            Boolean check = userDao.is_exist(Integer.parseInt(recordId));
            if(check == false)
            {
                userDao.insertRecord(new User(Integer.parseInt(recordId),firstName,lastName));
            }
            // Can not use toast in thread it will crash......

        }
    }
}